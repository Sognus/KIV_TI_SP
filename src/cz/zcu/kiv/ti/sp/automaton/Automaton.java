package cz.zcu.kiv.ti.sp.automaton;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Tøída reprezentující jeden koneèný automat
 * 
 * @author Jakub Vítek
 *
 */
public class Automaton {

	// Nastavení funkènosti

	/** Logování stavu */
	public ArrayList<String> log = new ArrayList<String>();

	// Struktura automatu

	/** Množina stavù */
	private ArrayList<State> states;
	/** Množina vstupù */
	private ArrayList<Character> inputs;
	/** Množina pøechodových funkcí = hrany */
	private HashMap<State, ArrayList<Edge>> edges;
	/** Poèáteèní stav automatu */
	private State start;
	/** Množina koncových stavù automatu */
	private ArrayList<State> end;

	// Aktuální prùchod automatu

	/** Aktuální stav pøi prùchodu automatem (zmìna metodou tick) */
	private State currentState;
	/** Idikátor, zda se automat dostal do chybového stavu */
	private boolean errorState;
	/** Indikátor, zda automat v souèasné dobì bìží */
	private boolean running;

	/**
	 * Konstruktor koneèného automatu
	 */
	public Automaton() {
		// Initializace struktury automatu
		this.states = new ArrayList<State>();
		this.inputs = new ArrayList<Character>();
		this.end = new ArrayList<State>();
		this.start = null;
		this.edges = new HashMap<State, ArrayList<Edge>>();

		// Initializace prùchodu automatu
		this.currentState = null;
		this.errorState = false;
		this.running = false;

	}

	/**
	 * Vrátí poèáteèní stav
	 * 
	 * @return aktuální poèáteèní stav
	 */
	public State getStart() {
		return start;
	}

	/**
	 * Nastaví poèáteèní stav
	 * 
	 * @param start
	 *            nový poèáteèní stav
	 */
	public boolean setStart(State start) {
		if (this.running) {
			// Pøi bìhu automatu nelze mìnit jeho strukturu
			log.add("setStart: V režimu akce nelze upravit strukturu automatu");
			return false;
		}

		if (!this.states.contains(start)) {
			// Poèáteèní stav nemùže být takový, že není v množinì stavù automatu
			log.add(String.format("setStart: Stav nemùže být vstupní - není v množinì stavù [stav: %s]",
					start.getID()));
			return false;
		}

		log.add(String.format("setStart: Nový vstupní stav => %s ", start.getID()));
		this.start = start;
		return true;
	}

	/**
	 * Vloží nový stav do koneèného automatu
	 * 
	 * @param state
	 *            nový stav
	 * @return informace o ne/úspìchu operace
	 */
	public boolean addState(State state) {
		if (this.running) {
			// Pøi bìhu automatu nelze mìnit jeho strukturu
			log.add("addState: V režimu akce nelze upravit strukturu automatu");
			return false;
		}

		if (this.states.contains(state)) {
			// Nelze vložit stav, který již existuje
			log.add(String.format("addState: Nelze vložit stav, který již existuje [stav: %s]", state.getID()));
			return false;
		}

		log.add(String.format("addState: Pøidán nový stav [stav: %s]", state.getID()));
		return this.states.add(state);
	}

	/**
	 * Vloží nový koncový stav do koneèného automatu
	 * 
	 * @param state
	 *            nový stav
	 * @return informace o ne/úspìchu operace
	 */
	public boolean addOutputState(State state) {
		if (this.running) {
			// Pøi bìhu automatu nelze mìnit jeho strukturu
			log.add("addOutputState: V režimu akce nelze upravit strukturu automatu");
			return false;
		}

		if (!this.states.contains(state)) {
			log.add(String.format("addOutputState: Výstupní stav nemùže být mimo množinu stavù [stav: %s]",
					state.getID()));
			// Koneèný stav nemùže být mimo množinu všech stavù (tøeba: F c Q)
			return false;
		}

		if (this.end.contains(state)) {
			// Nelze vložit stav, který již existuje
			log.add(String.format("addOutputState: Nelze vložit výstupní stav, který již existuje [stav: %s]",
					state.getID()));
			return false;
		}

		log.add(String.format("addOutputState: Pøidán nový výstupní stav [stav: %s]", state.getID()));
		return this.end.add(state);

	}

	/**
	 * Pøidá vstup tak, aby bylo možné jej využívat v koneèném automatu
	 * 
	 * @param ch
	 *            nový vstup
	 * @return informace o ne/úspìchu operace
	 */
	public boolean addInput(Character ch) {
		if (this.running) {
			// Pøi bìhu automatu nelze mìnit jeho strukturu
			log.add("addInput: V režimu akce nelze upravit strukturu automatu");
			return false;
		}

		if (this.inputs.contains(ch)) {
			log.add(String.format("Nelze pøidat vstup, který již existuje [vstup: %s]", ch.toString()));
			// Nelze pøidat vstup, který již existuje
			return false;
		}

		log.add(String.format("addInput: Pøidán nový vstup => %s", ch.toString()));
		return this.inputs.add(ch);
	}

	/**
	 * Pøidá vstup tak, aby bylo možné jej využívat v koneèném automatu
	 * 
	 * @param ch
	 *            nový vstup
	 * @return informace o ne/úspìchu operace
	 */
	public boolean addInput(char ch) {
		return this.addInput(new Character(ch));
	}

	/**
	 * Pøiøadí k výchozímu stavu hrany hranu samotnou
	 * 
	 * @param edge
	 *            hrana pro pøidání
	 * @return informace o ne/úspìchu operace
	 */
	public boolean addEdge(Edge edge) {

		if (this.running) {
			// Pøi bìhu automatu nelze mìnit jeho strukturu
			log.add("addEdge: V režimu akce nelze upravit strukturu automatu");
			return false;
		}

		State start = edge.getStart();

		if (!this.states.contains(start) || !this.states.contains(edge.getEnd())) {
			// V automatu není poèáteèní èi koncový stav hrany
			log.add("addEdge: V automatu není poèáteèní èi koncový stav hrany " + edge.toString());
			return false;
		}

		if (!this.inputs.contains(edge.getInput())) {
			// Reakce na tento znak není povolena
			log.add("addEdge: Reakce na tento znak není povolena " + edge.toString());
			return false;
		}

		if (!this.edges.containsKey(start)) {
			// Pokud neexistuje úložištì pro hrany, vytvoøí se
			log.add("addEdge: Vytváøím uložištì pro hrany pro stav " + start.toString());
			this.edges.put(start, new ArrayList<Edge>());
		}

		// Hrana již existuje
		if (this.edges.get(start).contains(edge)) {
			log.add("addEdge: Nelze pøidat hranu, která již existuje " + edge.toString());
			// Nelze pøidat hranu, která již existuje
			return false;
		}

		// Pridání hrany
		log.add("addEdge: Pøidávám hranu ke koneènému automatu " + edge.toString());
		return this.edges.get(start).add(edge);
	}

	/**
	 * Pokud automat "bìží", vznikne pokus o pøesun ze souèasného stavu za pomocí
	 * vstupu do následujícího stavu.
	 * 
	 * Metoda pracující s celým øetìzcem
	 * 
	 * @param ch
	 *            vstup ke zpracování
	 * @return ne/úspìšnost operace/chyby
	 */
	public boolean tick(String s) {
		char[] arr = s.toCharArray();

		for (char ch : arr) {
			if (!this.inputs.contains(ch)) {
				// Špatný vstup
				log.add("tick(String): Øetezec obsahuje nepovolený znak -> " + ch);
				return false;
			}
		}

		Boolean state = null;
		for (int i = 0; i < arr.length; i++) {
			if (this.errorState) {
				// Automat narazil na chybový absorbèní stav, není nutné pokraèovat
				log.add("tick(String): Automat se dostal do absorbèního chybové stavu");
				break;
			}
			state = new Boolean(this.tick(arr[i]));
		}

		state = state == null ? false : state.booleanValue();
		return state;
	}

	/**
	 * Pokud automat "bìží", vznikne pokus o pøesun ze souèasného stavu za pomocí
	 * vstupu do následujícího stavu
	 * 
	 * @param ch
	 *            vstup ke zpracování
	 * @return ne/úspìšnost operace
	 */
	public boolean tick(Character ch)

	{
		if (!running) {
			// Automat ve ve stavu úpravy, nelze jím procházet
			log.add("tick(char): Nelze provádìt tick, automat je ve stavu úpravy");
			return false;
		}

		if (this.errorState) {
			// Automat se nachází v absorbèním chybovém stavu
			log.add("tick(char): Pøeskakuji tick, automat se nachází v chybovém stavu");
			return true;
		}

		if (!this.inputs.contains(ch)) {
			// Automat ignoruje nepovolené stavy
			log.add("tick(char): Ignoruji tick, vstupem je nepovolený znak");
			return false;
		}

		if (this.edges.get(currentState) == null) {
			// Souèasný stav nemá uložištì pro hrany - vytváøím
			log.add("tick(char): Souèasný stav nemá uložištì pro hrany " + currentState.toString());
			this.edges.put(currentState, new ArrayList<Edge>());
		}

		// Ovìøení zda v souèasném stavu je hrana øešící daný vstup
		ArrayList<Edge> edgez = this.edges.get(this.currentState);
		Edge desired = null;
		for (Edge ed : edgez) {
			if (ed.getInput() == ch) {
				desired = ed;
				break;
			}
		}

		if (desired == null) {
			// Hrana neexistuje -> absorbèní chybový stav
			this.currentState = new State("ERROR");
			this.errorState = true;
			log.add(String.format(
					"tick(char): Automat se dostal do chybového absorbèního stavu. [aktuální stav: %s, vstup: %s]",
					currentState.getID(), ch.toString()));
			return true;
		}

		// Pøesun daným vstupem do koncového stavu
		log.add(String.format("tick(char): Provádím akci => výchozí: %s + vstup: %s => následující: %s",
				desired.getStart().getID(), desired.getInput().toString(), desired.getEnd().getID()));

		currentState = desired.getEnd();
		return true;

	}

	/**
	 * Zmìní stav automatu na "bìží"
	 * 
	 */
	public void run() {
		this.currentState = this.start;
		this.errorState = false;

		log.add("");
		log.add("");
		log.add("Resetuji automat - pøepínám do režimu akce ");

		this.running = true;
	}

	/**
	 * Zmìní mód automatu na "Nebìží"
	 * 
	 */
	public void stop() {
		log.add("Ukonèuji automat - pøepínám do režimu úpravy");
		this.running = false;
	}

	/**
	 * Vrátí informaci o tom, zda se KA nachází v jednom z výstupních stavù
	 * 
	 * @return informace zda je KA ve výstupním stavu
	 */
	public boolean accepting() {

		if (!this.running) {
			log.add("Automat nebìží!!");

			return false;
		}

		if (this.errorState) {
			log.add("Automat chyboval!!");
			return false;
		}

		if (!this.end.contains(currentState)) {
			log.add("Automat není v koneèném stavu (current: " + this.currentState + ")");
			return false;
		}

		return true;
	}

}
