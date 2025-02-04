package graphics;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class SpriteStore {
    
    private static SpriteStore spriteStore;

    private static final Map<String, Sprite> SPRITE_MAP = new HashMap<>();

    private SpriteStore() {
        SPRITE_MAP.put("IDLE_LEFT", new Sprite("IDLE_LEFT", Color.BLUE, 20, 20));
        SPRITE_MAP.put("IT", new Sprite("IT", Color.RED, 20, 20));
        SPRITE_MAP.put("OBSTACLE", new Sprite("OBSTACLE", Color.GRAY, 50, 50));
        SPRITE_MAP.put("DANGEROUS_OBSTACLE", new Sprite("DANGEROUS_OBSTACLE", Color.RED, 50, 50));
        SPRITE_MAP.put("WALL", new Sprite("WALL", Color.DARK_GRAY, 50, 50));
        SPRITE_MAP.put("PLATFORM", new Sprite("PLATFORM", Color.LIGHT_GRAY, 50, 20));
        SPRITE_MAP.put("END", new Sprite("END", Color.GREEN, 25, 50));
    }

    public static SpriteStore getSpriteStore() {
        if (spriteStore == null) {
            spriteStore = new SpriteStore();
        }
        return spriteStore;
    }

    public Sprite getSprite(String name) {
        return SPRITE_MAP.get(name);
    }


}
