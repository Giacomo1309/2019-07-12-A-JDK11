package it.polito.tdp.food.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import it.polito.tdp.food.model.Event.EventType;

public class Simulator {
	// PARAMETRI DI SIMULAZIONE
	Model model  ;
	private int K = 7;
	private Food foodIniziale = new Food(0, null);

	private final LocalTime oraInizio = LocalTime.of(8, 0);
	// private final LocalTime oraFine = LocalTime.of(20, 0);

	// private final Duration TICK_TIME = Duration.ofMinutes(5);

	// OUTPUT DA CALCOLARE
	private int cibiTot;
	private LocalTime oraFine;

	// STATO DEL SISTEMA
	private Map<Integer, Boolean> cucine; // codice cucina true se e piena false se vuota
//	private PriorityQueue<Paziente> attesa ; // post-triage prima di essere chiamati
	private int cucineLibere;
	private Map<Integer, Food> cibiGiaPreparati;
	private Map<Integer, Food> cibiInPreparazione;
	private Map<Integer, Supporto> cibiDaPreparare;

	// CODA DEGLI EVENTI
	private PriorityQueue<Event> queue;

	// GET E SET

	public void setK(int k) {
		K = k;
	}

	public int getCibiTot() {
		return cibiTot;
	}

	public LocalTime getOraFine() {
		return oraFine;
	}

	// INIZIALIZZAZIONE
	public void init(int nCucine, Food food, Model model) {
		this.model = model;
		this.foodIniziale.setFood_code(food.getFood_code());
		this.foodIniziale.setDisplay_name(food.getDisplay_name());
		this.K = nCucine;
		this.queue = new PriorityQueue<>();
		this.cibiGiaPreparati = new HashMap<Integer, Food>();
		this.cibiInPreparazione = new HashMap<Integer, Food>();
		this.cibiDaPreparare = new HashMap<Integer, Supporto>();
		this.cucineLibere = K;
		this.cibiTot = 0;

		// this.pazienti = new ArrayList<>();
		// this.attesa = new PriorityQueue<>();

		// generiamo eventi iniziali
		this.queue = new PriorityQueue<>();
		List<Supporto> cibiIniziali = new ArrayList(model.calorieCongiunte(this.foodIniziale));
		for (Supporto s : cibiIniziali) {
			System.out.println(s);
			this.cibiDaPreparare.put(s.getFood().getFood_code(), s);
		}

		// LocalTime oraArrivo = this.oraInizio;
		for(Supporto s : this.cibiDaPreparare.values()) {
			
			Event e = new Event(oraInizio, EventType.ENTRA_IN_CUCINA, s.getFood());
			queue.add(e);
		}
	}

	// ESECUZIONE
	public void run() {
		while (!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			 System.out.println(e + " Proviamo "+this.cucineLibere);
			processEvent(e);
		}
	}

	private void processEvent(Event e) {

		switch (e.getType()) {
		case ENTRA_IN_CUCINA:
			int cibo = e.getFood().getFood_code();
			if (this.cucineLibere > 0 && !this.cibiGiaPreparati.containsKey(cibo)
					&& !this.cibiInPreparazione.containsKey(cibo)) {
				Event e2 = new Event(e.getTime(), EventType.IN_PREPARAZIONE, e.getFood());
				queue.add(e2);
				this.cibiDaPreparare.remove(cibo);

			} else if (!this.cibiGiaPreparati.containsKey(cibo) && !this.cibiInPreparazione.containsKey(cibo)) {
				Duration d = Duration.ofSeconds(60);
				Event e2 = new Event(e.getTime().plus(d), EventType.ENTRA_IN_CUCINA, e.getFood());
				queue.add(e2);

			}
			break;

		case IN_PREPARAZIONE:
			int cibo2 = e.getFood().getFood_code();
			System.out.println(e + " qui "+this.cibiDaPreparare.get(cibo2));
			int tempo = (int) this.cibiDaPreparare.get(cibo2).getCalorie();
			this.cibiInPreparazione.put(e.getFood().getFood_code(), e.getFood());
			this.cucineLibere--;
			Duration t = Duration.ofMinutes(tempo);
			Event e2 = new Event((e.getTime().plus(t)), EventType.PRONTO, e.getFood());

			break;

		case PRONTO:
			int cibo3 = e.getFood().getFood_code();
			this.cibiInPreparazione.remove(cibo3);
			this.cibiGiaPreparati.put(cibo3, e.getFood());
			this.cibiTot++;
			this.cucineLibere++;
			Supporto massimoTraAdiacenti = model.getMassimo(e.getFood());
			Duration d = Duration.ofSeconds(1);
			Event e3 = new Event(e.getTime().plus(d), EventType.ENTRA_IN_CUCINA, massimoTraAdiacenti.getFood());
			break;

		}
		// 1. aggiorna modello del mondo

		// 2. aggiorna i risultati

		// 3. genera nuovi eventi

	}
}
