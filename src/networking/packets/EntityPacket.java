package networking.packets;

import java.awt.Rectangle;

import entities.server.EntityType;
import entities.server.Player;
import entities.server.ServerEntity;
import entities.tiles.DangerousObstacle;
import entities.tiles.End;
import entities.tiles.Obstacle;
import entities.tiles.Platform;
import entities.tiles.Wall;
import graphics.Depth;

public class EntityPacket extends Packet {

    public EntityType entityType;
    public Rectangle hitbox;
    public String spriteName; // in the future, this will be a frame of an animation
    public Depth depth;
    public String username; // empty if not a player belonging to a client
    public boolean it; // false unless the entity is a player that is "it"
    
    public EntityPacket(ServerEntity entity) {
        super();
        hitbox = entity.getHitbox();
        spriteName = entity.getSprite().getName();
        depth = entity.getDepth();
        username = "";
        it = false;
        if (entity instanceof Player) {
            Player player = (Player) entity;
            username = player.getUsername();
            it = player.it;
            entityType = EntityType.PLAYER;
        } else if (entity instanceof DangerousObstacle) {
            entityType = EntityType.DANGEROUS_OBSTACLE;
        } else if (entity instanceof Obstacle) {
            entityType = EntityType.OBSTACLE;
        } else if (entity instanceof Platform) {
            entityType = EntityType.PLATFORM;
        } else if (entity instanceof Wall) {
            entityType = EntityType.WALL;
        } else if(entity instanceof End) {
            entityType = EntityType.END;
        }
    }
}
