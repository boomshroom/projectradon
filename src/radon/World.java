package radon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class World extends BasicGameState {
    
    float accumulator = 0.0F;
    public static long tickCount = 0;
    static float partialTicks;
    public static final float timesetp = 50 / 3; // 1/60 second
    
    public static List<Entity> entities = new ArrayList<Entity>();
    
    public static Random rand = new Random();
    
    public World(int play) {}
    
    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        accumulator += delta;
        
        while (accumulator >= timesetp) {
            tick(container, game, delta);
            accumulator -= timesetp;
        }
        
        partialTicks = accumulator / timesetp;
    }
    
    public void tick(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        tickCount++;
        
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            
            for (int j = 0; j < entities.size(); j++) {
                Entity e2 = entities.get(j);
                if (e2 != e && e.intersects(e2) && e.isCollidable() && e2.isCollidable()) {
                    Collision.doCollision(e, e2);
                }
            }
            
            e.update(container, game, delta);
            Collision.doEdgeCollision(e);
            
            if (e.removed) {
                entities.remove(i--);
            }
        }
    }
    
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        Player p = new Player(100, 100);
        add(p);
        
        for (int i = 0; i < 10; i++) {
            Entity e = new Entity(rand.nextFloat() * Game.width, rand.nextFloat() * Game.height);
            e.width = rand.nextFloat() * 50;
            e.height = rand.nextFloat() * 50;
            add(e);
        }
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, Game.width, Game.height);
        
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            
            e.render(container, game, g);
        }
    }
    
    public static void add(Entity entity) {
        entity.removed = false;
        entities.add(entity);
        entity.init();
    }
    
    public static List<Entity> getEntities() {
        return entities;
    }
    
    @Override
    public int getID() {
        return Game.WORLD;
    }
    
}