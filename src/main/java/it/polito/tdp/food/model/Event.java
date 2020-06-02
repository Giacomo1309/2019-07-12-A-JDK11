package it.polito.tdp.food.model;

import java.time.LocalTime;

public class Event implements Comparable<Event> {
	public enum EventType {
		ENTRA_IN_CUCINA, IN_PREPARAZIONE, PRONTO,
	}

	private LocalTime time;
	private EventType type;
	private Food food;

	// FORSE CI METTO LA CUCINA
	public Event(LocalTime time, EventType type, Food food) {
		super();
		this.time = time;
		this.type = type;
		this.food = food;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}

	@Override
	public String toString() {
		return "Event [time=" + time + ", type=" + type + ", food=" + food + "]";
	}

	@Override
	public int compareTo(Event o) {

		return this.time.compareTo(o.time);
	}

}
