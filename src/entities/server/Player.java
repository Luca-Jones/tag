package entities.server;

import java.awt.Dimension;
import java.awt.Graphics;

import entities.tiles.DangerousObstacle;
import entities.tiles.End;
import entities.tiles.Obstacle;
import entities.tiles.Platform;
import entities.tiles.Wall;
import graphics.Camera;
import graphics.Depth;
import graphics.SpriteStore;
import graphics.TileMap;

public class Player extends MoveableEntity {

    protected static final double FRICTION_COEFFICIENT = 2.5;
    protected static final double DRAG_COEFFICIENT = 2;
    protected static final Depth DEPTH = Depth.MIDDLE;
    protected static final int GRAVITY = 150;
    protected static final int COYOTE_TIMEOUT = 30;

    private static final Dimension REGULAR_HITBOX = new Dimension(20, 20);

    public final int SPEED_STAT = 150;
    public final int JUMP_STAT = 190;

    protected Direction wall;
    protected Direction direction;
    protected long wallJumpBuffer;
    protected long dashBuffer;
    protected int jumpCounter;
    protected int coyoteTimer;
    protected int spawnX;
    protected int spawnY;

    protected boolean isGrounded;
    protected boolean isJumping;
    protected boolean isDashing; // higher animation priority
    protected boolean isRunning;
    protected boolean isClimbing;
    protected boolean isDoubleJumping;

    public boolean it;
    public int immunityTimer;
    private static final int IMMUNITY_TIMEOUT = 50;

    private String username;

    public boolean checkRep() {
        boolean check = true;
        if (isGrounded) {
            check = !isJumping && !isClimbing && !isDoubleJumping;
        }
        if (isJumping) {
            check = !isGrounded && !isRunning && !isClimbing && !isDoubleJumping;
        }
        if (isDashing) {
            check = !isRunning && !isClimbing;
        }
        if (isRunning) {
            check = isGrounded && !isJumping && !isDashing && !isClimbing && !isDoubleJumping;
        }
        if (isClimbing) {
            check = !isGrounded && !isJumping && !isDashing && !isRunning && !isDoubleJumping;
        }
        if (isDoubleJumping) {
            check = !isGrounded && !isJumping && !isRunning && !isClimbing;
        }
        return check;
    }

    public Player(int x, int y, String username) {
        super(
            x, y, REGULAR_HITBOX.width, REGULAR_HITBOX.height, 
            SpriteStore.getSpriteStore().getSprite("IDLE_LEFT"), 
            DEPTH, FRICTION_COEFFICIENT, GRAVITY
        );
        this.username = username;
        spawnX = x;
        spawnY = y;
        isGrounded = false;
        isJumping = false;
        isDashing = false;
        isRunning = false;
        isClimbing = false;
        isDoubleJumping = false;
        wallJumpBuffer = 0;
        dashBuffer = 0;
        jumpCounter = 0;
        coyoteTimer = 0;
        direction = Direction.LEFT;
        it = false;
        immunityTimer = 0;
    }

    public void moveLeft() {
        if (!isDashing && wallJumpBuffer <= 0) {
            direction = Direction.LEFT;
            velocityX = -SPEED_STAT;
            isRunning = isGrounded;
            if (isClimbing) {
                isClimbing = wall == Direction.LEFT;
            }
        }
    }

    public void moveRight() {
        if (!isDashing && wallJumpBuffer <= 0) {
            direction = Direction.RIGHT;
            velocityX = SPEED_STAT;
            isRunning = isGrounded;
            if(isClimbing){
                isClimbing = wall == Direction.RIGHT;
            }
        }
    }

    public void jump() {

        // first jump
        if (isGrounded && !isJumping) {
            isGrounded = false;
            isJumping = true;
            isDashing = false;
            isRunning = false;
            isClimbing = false;
            isDoubleJumping = false;

            velocityY = -JUMP_STAT;
            jumpCounter = 0;
        }

        if (isJumping && jumpCounter <= 10) {
            jumpCounter ++;
            if (jumpCounter >= 10) {
                velocityY = -JUMP_STAT;
            }
        }

        // wall jump
        if (isClimbing) {
            isGrounded = false;
            isJumping = true;
            isDashing = false;
            isRunning = false;
            isClimbing = false;
            isDoubleJumping = false;
            wallJumpBuffer = 30;
            
            velocityY = -JUMP_STAT;
            direction = (wall == Direction.LEFT) ? Direction.RIGHT : Direction.LEFT;
            velocityX = SPEED_STAT * ((direction == Direction.RIGHT) ? 2 : -2);
        }
        
    }

    public void doubleJump() {
        if (!isDoubleJumping && !isGrounded && wallJumpBuffer <= 0) {
            isGrounded = false;
            isJumping = false;
            isDashing = false;
            isRunning = false;
            isClimbing = false;
            isDoubleJumping = true;
            velocityY = -JUMP_STAT;
        }
    }

    public void groundPound() {
        if (!isGrounded) {
            isGrounded = false;
            isJumping = false;
            isDashing = false;
            isRunning = false;
            isClimbing = false;
            isDoubleJumping = false;

            velocityY = 2 * JUMP_STAT;
            jumpCounter = 0;
        }
    }

    public void dash() {
        if (!isDashing && !isClimbing && wallJumpBuffer <= 0 && dashBuffer <= 0) {
            isDashing = true;
            isRunning = false;
            isClimbing = false;
            velocityX = SPEED_STAT * ((direction == Direction.RIGHT) ? 5 : -5);
            dashBuffer = 60;
        }
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);

        if (wallJumpBuffer > 0) wallJumpBuffer --;
        if (dashBuffer > 0) dashBuffer --;
        if (coyoteTimer > 0) coyoteTimer --;
        if (immunityTimer > 0) immunityTimer --;

        if (velocityY != 0 && isGrounded) {
            if (coyoteTimer <= 0) {
                coyoteTimer = COYOTE_TIMEOUT;
            }
            if (coyoteTimer <= 1) {
                isGrounded = false;
                isRunning = false;
            }
        }

        if (velocityY >= 0 && hitbox.y + hitbox.height >= TileMap.GROUND) {
            isGrounded = true;
            isJumping = false;
            isRunning = false;
            isClimbing = false;
            isDoubleJumping = false;
            velocityY = 0;
            hitbox.y = TileMap.GROUND - hitbox.height;
        }

        if (isDashing && velocityX == 0) {
            isDashing = false;
        }

        accelerationY = (isClimbing) ? 0 : GRAVITY;
        velocityY = (isClimbing) ? 30 : velocityY;
        frictionCoefficient = (isGrounded) ? FRICTION_COEFFICIENT : DRAG_COEFFICIENT;

        isClimbing = false;

        if (it) {
            sprite = SpriteStore.getSpriteStore().getSprite("IT");
        } else {
            sprite = SpriteStore.getSpriteStore().getSprite("IDLE_LEFT");
        }

    }

    @Override
    public void handleCollision(ServerEntity entity) {
        super.handleCollision(entity);

        if (entity instanceof Player) {
            Player otherPlayer = (Player) entity;
            if (this.it && otherPlayer.immunityTimer <= 0) {
                otherPlayer.it = true;
                it = false;
                immunityTimer = IMMUNITY_TIMEOUT;
            }
        }

        if (entity instanceof DangerousObstacle || entity instanceof End) {
            respawn();
        }
        
        if (entity instanceof Wall) {
            
            // this above entity
            if (previousHitbox.y + previousHitbox.height <= entity.hitbox.y) {
                isGrounded = true;
                isJumping = false;
                isRunning = false; // unnecessary
                isClimbing = false; // unnecessary
                isDoubleJumping = false;
            }
    
            // this below entity
            else if (previousHitbox.y >= entity.hitbox.y + entity.hitbox.height) {
                
            }

            // this left of entity
            else if (previousHitbox.x + previousHitbox.width <= entity.hitbox.x) {
                isGrounded = false;
                isJumping = false;
                isDashing = false;
                isRunning = false;
                isClimbing = true;
                isDoubleJumping = false;
                wall = Direction.RIGHT;
            }

            // this right of entity
            else if (previousHitbox.x >= entity.hitbox.x + entity.hitbox.width) {
                isGrounded = false;
                isJumping = false;
                isDashing = false;
                isRunning = false;
                isClimbing = true;
                isDoubleJumping = false;
                wall = Direction.LEFT;
            }

        } else if (entity instanceof Platform || entity instanceof Obstacle) {
            
            // this above entity
            if (previousHitbox.y + previousHitbox.height <= entity.hitbox.y) {
                isGrounded = true; // this is causing issues with clipping
                isJumping = false;
                isRunning = false; // unnecessary
                isClimbing = false; // unnecessary
                isDoubleJumping = false;
            }
            
            // this below entity
            else if (previousHitbox.y >= entity.hitbox.y + entity.hitbox.height) {
                
            }
            
            // this left of entity
            else if (previousHitbox.x + previousHitbox.width <= entity.hitbox.x) {
                
            }

            // this right of entity
            else if (previousHitbox.x >= entity.hitbox.x + entity.hitbox.width) {
                
            }

        }
    }
    
    @Override
    public void draw(Graphics g, Camera camera) {
        super.draw(g, camera);
        g.drawString(username, hitbox.x - camera.getX(), hitbox.y - camera.getY() - 10);
        if (it) {
            g.drawString("IT", hitbox.x - camera.getX(), hitbox.y - camera.getY() - 20);
        }
    }

    public void respawn() {
        hitbox.x = spawnX;
        hitbox.y = spawnY;
        velocityX = 0;
        velocityY = 0;
    }
    
    public String getUsername() {
        return username;
    }

    public void setSprite(String spriteName) {
        this.sprite = SpriteStore.getSpriteStore().getSprite(spriteName);
    }

}
