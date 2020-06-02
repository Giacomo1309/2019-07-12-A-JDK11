package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {

	private Graph<Food, DefaultWeightedEdge> grafo;
	// private List<Adiacenza> adiacenze;
	private FoodDao dao = new FoodDao();
	private Map<Integer, Food> idMap;

	public Model() {
	}

	public void creaGrafo(int porzioni) {
		this.idMap = new HashMap();
		this.grafo = new SimpleWeightedGraph<Food, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.dao.getCibi(idMap, porzioni); // riempio l'idMap
		Graphs.addAllVertices(this.grafo, idMap.values());
		for (Adiacenza a : dao.getAdiacenze(idMap)) {
			// System.out.println(a +"\n");
			Graphs.addEdge(this.grafo, a.getF1(), a.getF2(), a.getPeso());
		}
	}

	public Set<Food> getVertici() {
		return this.grafo.vertexSet();

	}

	public Set<DefaultWeightedEdge> getArchi() {
		return this.grafo.edgeSet();
	}

	public List<Supporto> calorieCongiunte(Food c) {

		List<Supporto> lista = new ArrayList<Supporto>();
		if (Graphs.neighborListOf(this.grafo, c)!= null ) {
			for (Food f : Graphs.neighborListOf(this.grafo, c)) {

				Double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(f, c));
				// System.out.println(peso + "\n");
				Supporto s = new Supporto(f, peso);
				lista.add(s);
			}
			// System.out.println(lista);
			Collections.sort(lista);
			List<Supporto> listaOrdinata = new ArrayList<Supporto>();
			for (int i = 0; i < lista.size(); i++) {
				if (i < 5)
					listaOrdinata.add(lista.get(i));
			}
			return listaOrdinata;
		} else {
			return null;
		}
	}

	public Supporto getMassimo(Food food) {
		Double massimo = 0.0;
		Supporto s = new Supporto(null, 0.0);
		for (Food f : Graphs.neighborListOf(this.grafo, food)) {

			Double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(f, food));
			if (peso > massimo) {
				s.setCalorie(peso);
				s.setFood(f);
				massimo = peso;
			}
			return s;
		}
		return null;
	}
}
