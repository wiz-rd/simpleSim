package main;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class SimWindow extends JPanel implements Runnable {
	
	/**
	 * auto generated ID
	 */
	private static final long serialVersionUID = 316527361262252587L;
	
	Graphics2D g;
	Random r = new Random();
	ArrayList<Entity> field = new ArrayList<Entity>();
	
	//screen settings
	final int screenH = 1000;
	final int screenW = 1000;
	
	Thread simThread;
	
	//SIM settings
	static int scale;
	static int fps;
	
	//default scale is 1
	public SimWindow() {
		this(1, 30);
	}
	
	public SimWindow(int s, int f) {
		scale = s;
		fps = f;
		this.setPreferredSize(new Dimension(screenW, screenH));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		
		createField(15);
	}
	
	public void startSimThread() {
		simThread = new Thread(this);
		simThread.start();
	}
	
	public void stopSimThread() {
		return;
	}
	
	public static void setScale(int s) {
		scale = s;
	}

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
			if (self.target != null) {
				self.movex(self.x, self.target.x);
				self.movey(self.y, self.target.y);
			}
		}
		
		for (int i = 0; i < field.size(); i++) {
			Entity self = field.get(i);
			if (self.target != null) {
				self.fight(self.target);
				if (self.target.health <= 0) {
					self.kill();
					field.remove(self.target);
					self.target = null;
				} else {}
				
			} else {continue;}
			
			if (self.health <= 0) {
				self = null;
			}
		}
		
		if (field.size() == 1) {
			stopSimThread();//does nothing currently. Hopefully will stop the current animation
		}
		
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		
		g = (Graphics2D) graphics;
		for (Entity i : field) {
			renderEntity(i);
		}
		if (field.size() == 1) {
			g.drawString(field.get(0).print(), 250, 250);
		} else if (field.get(0).friends.size() + 1 == field.size()) {
			int xey = 250;
			int yey = 250;
			for (Entity i : field) {
				g.drawString(i.print(), xey, yey);
				yey += 30;
			}
		}
		g.dispose();
	}
	
	public void createField(int size) {
		for (int i = 0; i < size; i++) {
			Entity e = new Entity();
			field.add(e);
		}
	}
	
	public void renderEntity(Entity e) {
		g.setColor(Color.white);
		//g.drawString(e.name, e.x, e.y); //"x:" + e.x + "y:" + e.y, e.x, e.y);
		g.setColor(e.color);
		g.fillOval(e.x, e.y, e.size, e.size);
	}
	
	public void targetAcquired(int i) {
		Entity self = field.get(i);
		int deltax = 0;
		int deltay = 0;
		int proximity = 2000;
		
		for (int j = 0; j < field.size(); j++) {
			Entity closest = field.get(j);
			deltax = closest.x;
			deltay = closest.y;
			
			if ((self == closest)) { continue; }
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
