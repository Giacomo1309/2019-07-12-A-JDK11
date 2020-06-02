package it.polito.tdp.food.model;

public class Supporto implements Comparable<Supporto> {
	private Food food;
	private double calorie;

	public Supporto(Food food, double calorie) {
		super();
		this.food = food;
		this.calorie = calorie;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}

	public double getCalorie() {
		return calorie;
	}

	public void setCalorie(double calorie) {
		this.calorie = calorie;
	}

	@Override
	public int compareTo(Supporto o) {
		if (this.calorie > o.getCalorie())
			return -1;
		else if (this.calorie < o.getCalorie())
			return 1;
		else
			return 0;
	}

	@Override
	public String toString() {
		return "Supporto [food=" + food + ", calorie=" + calorie + "]";
	}

}
