package cz.zcu.kiv.ti.sp.automaton;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * T��da reprezentuj�c� jeden kone�n� automat
 * 
 * @author Jakub V�tek
 *
 */
public class Automaton {

	// Nastaven� funk�nosti

	/** Logov�n� stavu */
	public ArrayList<String> log = new ArrayList<String>();

	// Struktura automatu

	/** Mno�ina stav� */
	private ArrayList<State> states;
	/** Mno�ina vstup� */
	private ArrayList<Character> inputs;
	/** Mno�ina p�echodov�ch funkc� = hrany */
	private HashMap<State, ArrayList<Edge>> edges;
	/** Po��te�n� stav automatu */
	private State start;
	/** Mno�ina koncov�ch stav� automatu */
	private ArrayList<State> end;

	// Aktu�ln� pr�chod automatu

	/** Aktu�ln� stav p�i pr�chodu automatem (zm�na metodou tick) */
	private State currentState;
	/** Idik�tor, zda se automat dostal do chybov�ho stavu */
	private boolean errorState;
	/** Indik�tor, zda automat v sou�asn� dob� b�� */
	private boolean running;

	/**
	 * Konstruktor kone�n�ho automatu
	 */
	public Automaton() {
		// Initializace struktury automatu
		this.states = new ArrayList<State>();
		this.inputs = new ArrayList<Character>();
		this.end = new ArrayList<State>();
		this.start = null;
		this.edges = new HashMap<State, ArrayList<Edge>>();

		// Initializace pr�chodu automatu
		this.currentState = null;
		this.errorState = false;
		this.running = false;

	}

	/**
	 * Vr�t� po��te�n� stav
	 * 
	 * @return aktu�ln� po��te�n� stav
	 */
	public State getStart() {
		return start;
	}

	/**
	 * Nastav� po��te�n� stav
	 * 
	 * @param start
	 *            nov� po��te�n� stav
	 */
	public boolean setStart(State start) {
		if (this.running) {
			// P�i b�hu automatu nelze m�nit jeho strukturu
			log.add("setStart: V re�imu akce nelze upravit strukturu automatu");
			return false;
		}

		if (!this.states.contains(start)) {
			// Po��te�n� stav nem��e b�t takov�, �e nen� v mno�in� stav� automatu
			log.add(String.format("setStart: Stav nem��e b�t vstupn� - nen� v mno�in� stav� [stav: %s]",
					start.getID()));
			return false;
		}

		log.add(String.format("setStart: Nov� vstupn� stav => %s ", start.getID()));
		this.start = start;
		return true;
	}

	/**
	 * Vlo�� nov� stav do kone�n�ho automatu
	 * 
	 * @param state
	 *            nov� stav
	 * @return informace o ne/�sp�chu operace
	 */
	public boolean addState(State state) {
		if (this.running) {
			// P�i b�hu automatu nelze m�nit jeho strukturu
			log.add("addState: V re�imu akce nelze upravit strukturu automatu");
			return false;
		}

		if (this.states.contains(state)) {
			// Nelze vlo�it stav, kter� ji� existuje
			log.add(String.format("addState: Nelze vlo�it stav, kter� ji� existuje [stav: %s]", state.getID()));
			return false;
		}

		log.add(String.format("addState: P�id�n nov� stav [stav: %s]", state.getID()));
		return this.states.add(state);
	}

	/**
	 * Vlo�� nov� koncov� stav do kone�n�ho automatu
	 * 
	 * @param state
	 *            nov� stav
	 * @return informace o ne/�sp�chu operace
	 */
	public boolean addOutputState(State state) {
		if (this.running) {
			// P�i b�hu automatu nelze m�nit jeho strukturu
			log.add("addOutputState: V re�imu akce nelze upravit strukturu automatu");
			return false;
		}

		if (!this.states.contains(state)) {
			log.add(String.format("addOutputState: V�stupn� stav nem��e b�t mimo mno�inu stav� [stav: %s]",
					state.getID()));
			// Kone�n� stav nem��e b�t mimo mno�inu v�ech stav� (t�eba: F c Q)
			return false;
		}

		if (this.end.contains(state)) {
			// Nelze vlo�it stav, kter� ji� existuje
			log.add(String.format("addOutputState: Nelze vlo�it v�stupn� stav, kter� ji� existuje [stav: %s]",
					state.getID()));
			return false;
		}

		log.add(String.format("addOutputState: P�id�n nov� v�stupn� stav [stav: %s]", state.getID()));
		return this.end.add(state);

	}

	/**
	 * P�id� vstup tak, aby bylo mo�n� jej vyu��vat v kone�n�m automatu
	 * 
	 * @param ch
	 *            nov� vstup
	 * @return informace o ne/�sp�chu operace
	 */
	public boolean addInput(Character ch) {
		if (this.running) {
			// P�i b�hu automatu nelze m�nit jeho strukturu
			log.add("addInput: V re�imu akce nelze upravit strukturu automatu");
			return false;
		}

		if (this.inputs.contains(ch)) {
			log.add(String.format("Nelze p�idat vstup, kter� ji� existuje [vstup: %s]", ch.toString()));
			// Nelze p�idat vstup, kter� ji� existuje
			return false;
		}

		log.add(String.format("addInput: P�id�n nov� vstup => %s", ch.toString()));
		return this.inputs.add(ch);
	}

	/**
	 * P�id� vstup tak, aby bylo mo�n� jej vyu��vat v kone�n�m automatu
	 * 
	 * @param ch
	 *            nov� vstup
	 * @return informace o ne/�sp�chu operace
	 */
	public boolean addInput(char ch) {
		return this.addInput(new Character(ch));
	}

	/**
	 * P�i�ad� k v�choz�mu stavu hrany hranu samotnou
	 * 
	 * @param edge
	 *            hrana pro p�id�n�
	 * @return informace o ne/�sp�chu operace
	 */
	public boolean addEdge(Edge edge) {

		if (this.running) {
			// P�i b�hu automatu nelze m�nit jeho strukturu
			log.add("addEdge: V re�imu akce nelze upravit strukturu automatu");
			return false;
		}

		State start = edge.getStart();

		if (!this.states.contains(start) || !this.states.contains(edge.getEnd())) {
			// V automatu nen� po��te�n� �i koncov� stav hrany
			log.add("addEdge: V automatu nen� po��te�n� �i koncov� stav hrany " + edge.toString());
			return false;
		}

		if (!this.inputs.contains(edge.getInput())) {
			// Reakce na tento znak nen� povolena
			log.add("addEdge: Reakce na tento znak nen� povolena " + edge.toString());
			return false;
		}

		if (!this.edges.containsKey(start)) {
			// Pokud neexistuje �lo�i�t� pro hrany, vytvo�� se
			log.add("addEdge: Vytv���m ulo�i�t� pro hrany pro stav " + start.toString());
			this.edges.put(start, new ArrayList<Edge>());
		}

		// Hrana ji� existuje
		if (this.edges.get(start).contains(edge)) {
			log.add("addEdge: Nelze p�idat hranu, kter� ji� existuje " + edge.toString());
			// Nelze p�idat hranu, kter� ji� existuje
			return false;
		}

		// Prid�n� hrany
		log.add("addEdge: P�id�v�m hranu ke kone�n�mu automatu " + edge.toString());
		return this.edges.get(start).add(edge);
	}

	/**
	 * Pokud automat "b��", vznikne pokus o p�esun ze sou�asn�ho stavu za pomoc�
	 * vstupu do n�sleduj�c�ho stavu.
	 * 
	 * Metoda pracuj�c� s cel�m �et�zcem
	 * 
	 * @param ch
	 *            vstup ke zpracov�n�
	 * @return ne/�sp�nost operace/chyby
	 */
	public boolean tick(String s) {
		char[] arr = s.toCharArray();

		for (char ch : arr) {
			if (!this.inputs.contains(ch)) {
				// �patn� vstup
				log.add("tick(String): �etezec obsahuje nepovolen� znak -> " + ch);
				return false;
			}
		}

		Boolean state = null;
		for (int i = 0; i < arr.length; i++) {
			if (this.errorState) {
				// Automat narazil na chybov� absorb�n� stav, nen� nutn� pokra�ovat
				log.add("tick(String): Automat se dostal do absorb�n�ho chybov� stavu");
				break;
			}
			state = new Boolean(this.tick(arr[i]));
		}

		state = state == null ? false : state.booleanValue();
		return state;
	}

	/**
	 * Pokud automat "b��", vznikne pokus o p�esun ze sou�asn�ho stavu za pomoc�
	 * vstupu do n�sleduj�c�ho stavu
	 * 
	 * @param ch
	 *            vstup ke zpracov�n�
	 * @return ne/�sp�nost operace
	 */
	public boolean tick(Character ch)

	{
		if (!running) {
			// Automat ve ve stavu �pravy, nelze j�m proch�zet
			log.add("tick(char): Nelze prov�d�t tick, automat je ve stavu �pravy");
			return false;
		}

		if (this.errorState) {
			// Automat se nach�z� v absorb�n�m chybov�m stavu
			log.add("tick(char): P�eskakuji tick, automat se nach�z� v chybov�m stavu");
			return true;
		}

		if (!this.inputs.contains(ch)) {
			// Automat ignoruje nepovolen� stavy
			log.add("tick(char): Ignoruji tick, vstupem je nepovolen� znak");
			return false;
		}

		if (this.edges.get(currentState) == null) {
			// Sou�asn� stav nem� ulo�i�t� pro hrany - vytv���m
			log.add("tick(char): Sou�asn� stav nem� ulo�i�t� pro hrany " + currentState.toString());
			this.edges.put(currentState, new ArrayList<Edge>());
		}

		// Ov��en� zda v sou�asn�m stavu je hrana �e��c� dan� vstup
		ArrayList<Edge> edgez = this.edges.get(this.currentState);
		Edge desired = null;
		for (Edge ed : edgez) {
			if (ed.getInput() == ch) {
				desired = ed;
				break;
			}
		}

		if (desired == null) {
			// Hrana neexistuje -> absorb�n� chybov� stav
			this.currentState = new State("ERROR");
			this.errorState = true;
			log.add(String.format(
					"tick(char): Automat se dostal do chybov�ho absorb�n�ho stavu. [aktu�ln� stav: %s, vstup: %s]",
					currentState.getID(), ch.toString()));
			return true;
		}

		// P�esun dan�m vstupem do koncov�ho stavu
		log.add(String.format("tick(char): Prov�d�m akci => v�choz�: %s + vstup: %s => n�sleduj�c�: %s",
				desired.getStart().getID(), desired.getInput().toString(), desired.getEnd().getID()));

		currentState = desired.getEnd();
		return true;

	}

	/**
	 * Zm�n� stav automatu na "b��"
	 * 
	 */
	public void run() {
		this.currentState = this.start;
		this.errorState = false;

		log.add("");
		log.add("");
		log.add("Resetuji automat - p�ep�n�m do re�imu akce ");

		this.running = true;
	}

	/**
	 * Zm�n� m�d automatu na "Neb��"
	 * 
	 */
	public void stop() {
		log.add("Ukon�uji automat - p�ep�n�m do re�imu �pravy");
		this.running = false;
	}

	/**
	 * Vr�t� informaci o tom, zda se KA nach�z� v jednom z v�stupn�ch stav�
	 * 
	 * @return informace zda je KA ve v�stupn�m stavu
	 */
	public boolean accepting() {

		if (!this.running) {
			log.add("Automat neb��!!");

			return false;
		}

		if (this.errorState) {
			log.add("Automat chyboval!!");
			return false;
		}

		if (!this.end.contains(currentState)) {
			log.add("Automat nen� v kone�n�m stavu (current: " + this.currentState + ")");
			return false;
		}

		return true;
	}

}
