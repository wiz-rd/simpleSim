package main;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

/**
 * This creates the window (rather, the portion of the window - the JPanel) for the field and entities to be placed on
 * @author wiz-rd
 * 
 */
public class SimWindow extends JPanel implements Runnable {
	
	/**
	 * auto generated ID
	 */
	private static final long serialVersionUID = 316527361262252587L;
	
	//creating a global graphics and random object so they can be referenced in every method that needs them
	Graphics2D g;
	Random r = new Random();
	//initializing an ArrayList to hold each entity
	ArrayList<Entity> field = new ArrayList<Entity>();
	
	//screen settings
	final int screenH = 1000;
	final int screenW = 1000;
	
	//creating simThread
	Thread simThread;
	
	//SIM settings
	static int scale;
	static int fps;
	
	/**
	 * A default constructor for creating a window. Calls {@link #SimWindow(int, int)} with 1 and 30
	 */
	public SimWindow() {
		this(1, 30);
	}
	
	/**
	 * Creates and prepares the window, as well as calls {@link #createField(int)} to initialize the entities
	 * @param s the scale of the window (not really used)
	 * @param f the fps of the window. Defaults to 30 if {@link #SimWindow()} is called
	 * 
	 */
	public SimWindow(int s, int f) {
		//preparing the window
		scale = s;
		fps = f;
		this.setPreferredSize(new Dimension(screenW, screenH));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		
		createField(15);
	}
	
	/**
	 * Starts the simulation thread
	 */
	public void startSimThread() {
		simThread = new Thread(this);
		simThread.start();
	}
	
	/**
	 * Does nothing :(
	 */
	public void stopSimThread() {
		return;
	}
	
	/**
	 * Somewhat of a placeholder method that allows Main.java to change the scale of the program. Likely with the use of a config or settings file
	 * @param s the scale to be set
	 */
	public static void setScale(int s) {
		scale = s;
	}

	/**
	 * Determines the rate at which the program should run
	 */
	@Override
	public void run() {
		
		double drawInterval = 1000000000/fps;
		double delta = 0;
		long lastTime = System.nanoTime();
		long curTime;
		
		//simulation activity; updating and drawing to screen
		while(simThread != null) {
			
			curTime = System.nanoTime();
			
			delta += (curTime - lastTime) / drawInterval;
			
			lastTime = curTime;
			
			if (delta >= 1) {
				//updating
				update();
				//redrawing
				repaint();
				delta--;
			}
		}
		
	}
	
	/**
	 * How often the behind-the-scenes code is updated. See also {@link #paintComponent(Graphics)}
	 */
	public void update() {
		
		//setting the target for each entity
		for (int i = 0; i < field.size(); i++) {
			targetAcquired(i);
		}
		
		//moving towards each other, and stopping when close enough
		for (int i = 0; i < field.size(); i++) {
			Entity self = field.get(i);
			//because pacifists are friends with everyone :)
			if (self.pacifist && self.target != null) {
				self.addFriend(self.target);
			}
			//moves the entities towards each other
			if (self.target != null) {
				self.movex(self.x, self.target.x);
				self.movey(self.y, self.target.y);
			}
		}
		
		for (int i = 0; i < field.size(); i++) {
			Entity self = field.get(i);
			if (self.target != null) {
				self.fight(self.target); //initiates a fight with the target entity
				if (self.target.health <= 0) { //if the entity dies...
					self.kill(); //does *NOT* kill the current entity; it adds a kill to their score
					field.remove(self.target); //removes the current entity from the game
					self.target = null; //no longer targeting the entity since they are neither visible nor alive
				} else {}
				
			} else {continue;}
			
			//this checks to ensure their health isn't below zero
			if (self.health <= 0) {
				self = null;
			}
		}
		
		if (field.size() == 1) {
			stopSimThread();//does nothing currently. Hopefully will stop the current animation someday
		}
		
	}
	
	/**
	 * How often the gui is updated. Uses super.paintComponent()
	 */
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		
		//renders each entity
		g = (Graphics2D) graphics;
		for (Entity i : field) {
			renderEntity(i);
		}
		
		if (field.size() == 1) {
			g.drawString(field.get(0).print(), 250, 250);
		} else if (field.get(0).friends.size() + 1 == field.size()) {
			//creates temporary x and y variables for displaying entity statistics
			int xey = 250;
			int yey = 250;
			for (Entity i : field) {
				g.drawString(i.print(), xey, yey);
				yey += 30;
			}
		}
		
		g.dispose();
	}
	
	/**
	 * Creates all of the entities on the field and adds them to ArrayList {@code field}.
	 * Does *not* render the entities. See {@link #renderEntity(Entity)}
	 * @param size the amount of entities to be added
	 */
	public void createField(int size) {
		for (int i = 0; i < size; i++) {
			Entity e = new Entity();
			field.add(e);
		}
	}
	
	/**
	 * Renders the given entities onto Graphics2D {@code g}
	 * @param e the entity to be rendered
	 */
	public void renderEntity(Entity e) {
		g.setColor(Color.white);
		//g.drawString(e.name, e.x, e.y); //"x:" + e.x + "y:" + e.y, e.x, e.y);
		g.setColor(e.color);
		g.fillOval(e.x, e.y, e.size, e.size);
	}
	
	/**
	 * Selects an entity from the ArrayList containing all entities - {@code field} - and chooses the closest entity to them and sets them as their target
	 * @param i the index in {@code field} for the entity
	 */
	public void targetAcquired(int i) {
		Entity self = field.get(i);
		int deltax = 0;
		int deltay = 0;
		int proximity = 2000;
		
		//finds the closes  entity and targets them
		for (int j = 0; j < field.size(); j++) {
			Entity closest = field.get(j);
			deltax = closest.x;
			deltay = closest.y;
			
			if ((self == closest)) { continue; } //skip if the closest entity is itself
			else if (self.friends.contains(closest) && !self.pacifist) {continue;}
			else if (Math.abs(self.y-deltay) + Math.abs(self.x-deltax) <= proximity) {
				proximity = Math.abs(self.y-deltay) + Math.abs(self.x-deltax);
				self.setTarget(closest);
				self.proximity = proximity;
			}
			else {
				continue;
			}
		}
	}
	
}
