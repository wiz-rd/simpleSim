package main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class Entity {
	
	Random stats = new Random();
	int health = (stats.nextInt(201)+100); //base health of 100
	int startingHealth = health;
	int damage = stats.nextInt(101);
	int speed = 1;//stats.nextInt(11);
	int size = health/10; //size is a number from 1-30 based on their health
	int x = stats.nextInt(751); //locations
	int y = stats.nextInt(751);
	int proximity = 751;
	boolean pacifist = (stats.nextInt(101) > 90); //a 10% chance of shooting only "helpful" bullets
	int kills; //amount of killed enemies
	ArrayList<Entity> friends = new ArrayList<Entity>();
	Entity leader = null; // does nothing at the moment, I may add later but I feel like that's far too much of a headache right now
	Color color = new Color(stats.nextInt());
	String name = ("ID: " + stats.nextInt(10001));
	Entity target = null;
	
	public Entity() {
		if (pacifist) {
			this.color = Color.white;
		}
		System.out.println(name + " health : " + health + " damage : " + damage + " size : " + size + " pacifist: " + pacifist);
	}
	
	public int heal(int amount) {
		amount = damage;
		return amount;
	}
	
	public void damaged(int amount, Entity e) {
		this.health -= amount;
		if (friends.contains(e)) {
			friends.remove(e);
			damage -= 2;
		}
	}
	
	public void movex(int loc, int destx) {
		if (proximity < 50) { return; }
		
		if (destx - x > 0) {
			x += speed;
			return;
		} else if (destx - x < 0) {
			x -= speed;
		} else {return;}
	}
	
	public void movey(int loc, int desty) {
		if (proximity < 50) { return; }
		
		if (desty - y > 0) {
			y += speed;
			return;
		} else if (desty - y < 0) {
			y -= speed;
		} else {return;}
	}
	
	public void healed(int amount, Entity actor) {
		this.health += amount;
		
		if (stats.nextInt(101) > 90 && !(friends.contains(actor))) {
			addFriend(actor);
		}
		
	}
	
	public void addFriend(Entity e) {
		if (!friends.contains(e)) {
			friends.add(e);
			damage += 2;
		}
	}
	
	public void setTarget(Entity t) {
		target = t;
	}
	
	public void fight(Entity t) {
		if (proximity <= 50 && t.health > 0 && !pacifist) {
			t.damaged(damage, this);
			return;
		} else if (proximity <= 50 && pacifist && t.health < 500) {
			t.healed(damage, this);
			return;
		} else { return; }
	}
	
	public Entity killed() {
		return this;
	}
	
	public void kill() {
		kills++;
	}
	
	public int killCount() {
		System.out.println(kills);
		return kills;
	}
	
	public String print() {
		return name + " current health : " + health + " starting health : " + startingHealth + " damage : " + damage + " size : " + size + " pacifist : " + pacifist + " kills : " + kills;
	}
	
}
