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
        return (entity.hitbox.x - e1.hitbox.x) * (entity.hitbox.x - e1.hitbox.x)
        + (entity.hitbox.y - e1.hitbox.y) * (entity.hitbox.y - e1.hitbox.y)
        - (entity.hitbox.x - e2.hitbox.x) * (entity.hitbox.x - e2.hitbox.x)
        - (entity.hitbox.y - e2.hitbox.y) * (entity.hitbox.y - e2.hitbox.y);
    }

    

}
