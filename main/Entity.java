package main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

/**
 * An entity to participate in the simulation. See {@link #Entity()}
 * @author wiz-rd
 */
public class Entity {
	
	Random stats = new Random();
	int health = (stats.nextInt(201)+100); //base health of 100
	int startingHealth = health;
	int damage = stats.nextInt(101);
	int speed = 1;//stats.nextInt(11);
	int size = health/10; //size is a number from 1-30 based on their health
	int x = stats.nextInt(751); //locations
	int y = stats.nextInt(751);
	int proximity = 751; //temporary proximity value
	boolean pacifist = (stats.nextInt(101) > 90); //a 10% chance of shooting only "helpful" bullets
	int kills; //amount of killed enemies
	ArrayList<Entity> friends = new ArrayList<Entity>();
	Entity leader = null; // does nothing at the moment, I may add later but I feel like that's far too much of a headache right now
	Color color = new Color(stats.nextInt());
	String name = ("ID: " + stats.nextInt(10001));
	Entity target = null;
	
	/**
	 * Constructs the Entity and prints their information out to console for debugging
	 */
	public Entity() {
		if (pacifist) {
			this.color = Color.white;
		}
		System.out.println(name + " health : " + health + " damage : " + damage + " size : " + size + " pacifist: " + pacifist);
	}
	
	/**
	 * Intended to heal the entity by the given amount. I am unsure if it is used, though
	 * @param amount the amount to heal them by
	 * @return amount, for debugging and for running through {@link #healed(int, Entity)}
	 */
	public int heal(int amount) {
		amount = damage;
		return amount;
	}
	
	/**
	 * Damages the entity by the given amount, and shows who it was that damaged them. This allows the current entity to remove them from their friends list should them be on it
	 * @param amount the amount to damage the entity
	 * @param e the entity that damaged the current entity
	 */
	public void damaged(int amount, Entity e) {
		this.health -= amount;
		if (friends.contains(e)) {
			friends.remove(e);
			damage -= 2;
		}
	}
	
	/**
	 * A method for moving along the x axis
	 * @param loc current location
	 * @param destx destination location
	 */
	public void movex(int loc, int destx) {
		if (proximity < 50) { return; }
		
		if (destx - x > 0) {
			x += speed;
			return;
		} else if (destx - x < 0) {
			x -= speed;
		} else {return;}
	}
	
	/**
	 * A method for moving along the y axis
	 * @param loc current location
	 * @param desty destination location
	 */
	public void movey(int loc, int desty) {
		if (proximity < 50) { return; }
		
		if (desty - y > 0) {
			y += speed;
			return;
		} else if (desty - y < 0) {
			y -= speed;
		} else {return;}
	}
	
	/**
	 * Not to be confused with {@link #heal(int)}, this is ran when the entity is the subject of being healed. There's a chance the entity performing the healing will be added to the current entity's friends
	 * @param amount the amount to be healed by
	 * @param actor the healer
	 */
	public void healed(int amount, Entity actor) {
		this.health += amount;
		
		if (stats.nextInt(101) > 90 && !(friends.contains(actor))) {
			addFriend(actor);
		}
		
	}
	
	/**
	 * Somewhat self-explanatory; adds the entity passed through to the current entity's friends. However, it also marginally increases their collective damage output. 
	 * @param e the entity to add
	 */
	public void addFriend(Entity e) {
		if (!friends.contains(e)) {
			friends.add(e);
			damage += 2;
		}
	}
	
	/**
	 * Sets the entity t as the current entity's target
	 * @param t the entity to target
	 */
	public void setTarget(Entity t) {
		target = t;
	}
	
	/**
	 * "Fights" the target entity, ensuring they are within close enough range to do so, and if the current entity is a pacifist, to heal them
	 * @param t the entity to target
	 */
	public void fight(Entity t) {
		if (proximity <= 50 && t.health > 0 && !pacifist) {
			t.damaged(damage, this);
			return;
		} else if (proximity <= 50 && pacifist && t.health < 500) {
			t.healed(damage, this);
			return;
		} else { return; }
	}
	
	/**
	 * A method for if the entity was killed. Used to remove them from the board
	 * @return self
	 */
	public Entity killed() {
		return this;
	}
	
	/**
	 * Iterates the current entity's kill count by one
	 */
	public void kill() {
		kills++;
	}
	
	/**
	 * Prints and returns the entity's total kills
	 * @return kills
	 */
	public int killCount() {
		System.out.println(kills);
		return kills;
	}
	
	/**
	 * Used to print the statistics about the current entity
	 * @return a string of the statistics
	 */
	public String print() {
		return name + " current health : " + health + " starting health : " + startingHealth + " damage : " + damage + " size : " + size + " pacifist : " + pacifist + " kills : " + kills;
	}
	
}
