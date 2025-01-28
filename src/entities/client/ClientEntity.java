package entities.client;

import java.awt.Rectangle;

import entities.server.Entity;
import graphics.Depth;

/**
 * From the view of the client, all entities are the same. This class strips the
 * Entity class of any of its actual functionality and is only meant to be drawn
 * on the client side.
 * The player is unique in certain properties, but this is accounted for in the
 * ClientPlayer class.
 */
public class ClientEntity extends Entity {

    public ClientEntity(Rectangle hitbox, String spriteName, Depth depth) {
        super(hitbox, spriteName, depth);
    }

}
