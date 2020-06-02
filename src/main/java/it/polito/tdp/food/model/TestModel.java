package it.polito.tdp.food.model;

import it.polito.tdp.food.model.Model;

public class TestModel {

	public static void main(String[] args) {
		Model model = new Model();
		model.creaGrafo(6);
	//	System.out.println(model.getVertici());
	//	System.out.println(model.getArchi());
		Food f = new Food(21501000,"Ground beef (75% lean, regular)");
	//	System.out.println(model.calorieCongiunte(f));
		Simulator s = new Simulator();
		s.init(5, f,model);
		s.run();
		
	}

}
