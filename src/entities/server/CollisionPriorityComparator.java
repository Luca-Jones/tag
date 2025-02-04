package entities.server;

import java.util.Comparator;

// the collision priority queue needs to be serializable and so this does too
public class CollisionPriorityComparator implements Comparator<ServerEntity> {

    private ServerEntity entity;

    public CollisionPriorityComparator(ServerEntity entity) {
        this.entity = entity;
    }

    @Override
    public int compare(ServerEntity e1, ServerEntity e2) {
        return 
          (int) (entity.hitbox.getCenterX() - e1.hitbox.getCenterX()) * (int) (entity.hitbox.getCenterX() - e1.hitbox.getCenterX())
        + (int) (entity.hitbox.getCenterY() - e1.hitbox.getCenterY()) * (int) (entity.hitbox.getCenterY() - e1.hitbox.getCenterY())
        - (int) (entity.hitbox.getCenterX() - e2.hitbox.getCenterX()) * (int) (entity.hitbox.getCenterX() - e2.hitbox.getCenterX())
        - (int) (entity.hitbox.getCenterY() - e2.hitbox.getCenterY()) * (int) (entity.hitbox.getCenterY() - e2.hitbox.getCenterY());
    }

    

}
