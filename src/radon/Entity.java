package radon;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

import radon.Player;
import radon.World;

public class Entity {
    
    public float x, y;
    public float prevX, prevY;
    public Vector2f velocity;
    public Vector2f prevVelocity;
    public Vector2f force;
    public boolean removed;
    Input input;
    public float invMass;
    public float width, height;
    public Color col;
    Random rand = new Random();
    public float restitution = 0.8F;
    public boolean onGround;
    public boolean physics = false;
    /** 0 = not on wall, 1 = left wall, 2 = right wall */
    public byte onWall;
    
    public Entity(float x, float y) {
        this.x = x;
        this.y = y;
        width = 20;
        height = 20;
        invMass = 1F / (width * height);
        //col = new Color(42, 47, 159);
        col = new Color(255, 255, 0); // May have made it yellow :)
        velocity = new Vector2f(0, 0);
        prevVelocity = new Vector2f(0, 0);
        force = new Vector2f(0, 0);
        //Old constructor
    
    }
    
    public Entity(float x, float y, Color colour, float width, float height, boolean physicsactive) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        invMass = 1F / (width * height);
        this.col = colour;
        this.velocity = new Vector2f(0, 0);
        this.physics = physicsactive;
        prevVelocity = new Vector2f(0, 0);
        force = new Vector2f(0, 0);
        
    }
    
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.setColor(col);
        g.pushTransform();
        g.translate(World.partialTicks * (x - prevX), World.partialTicks * (y - prevY));
        g.fillRect(x - width / 2, y - height / 2, width, height);
        g.setColor(Color.black);
        // g.drawLine(x, y, x + velocity.x * 10, y + velocity.y * 10);
        g.popTransform();
    }
    
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        input = container.getInput();
        GameState state = game.getCurrentState();
        if (!(state instanceof World)) return;
        
        prevVelocity.set(velocity);
        
       if (physics == true){
         invMass = 1F / (width * height);
       }
        prevX = x;
        prevY = y;
        
        velocity.add(force.scale(invMass));
        force.set(0, 0);
        
        x += velocity.x;
        y += velocity.y;
        
        if (!(this instanceof Player)) { //Might have broken this, we'll see. - JDOG
            velocity.scale(0.8F);
        }
    }
    
    public void remove() {
        removed = true;
    }
    
    public boolean intersects(Entity e) {
        if (e.x - e.width / 2 > x + width / 2 || x - width / 2 > e.x + e.width / 2) return false;
        if (e.y - e.height / 2 > y + height / 2 || y - height / 2 > e.y + e.height / 2) return false;
        return true;
        
    }
    
    public boolean isCollidable() {
        return true;
    }
    
    public boolean isPhysicsActive() {
    	return physics;
    }
    	
    
    public void init() {}
    
}
