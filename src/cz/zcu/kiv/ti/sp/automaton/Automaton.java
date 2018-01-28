package cz.zcu.kiv.ti.sp.automaton;

import java.util.ArrayList;
import java.util.HashMap;

import cz.zcu.kiv.ti.sp.utils.Logger;

/**
 * T��da reprezentuj�c� jeden kone�n� automat
 * 
 * @author Jakub V�tek
 *
 */
public class Automaton {

	/** Jedin��ek */
	private static Automaton singleton;

	// Nastaven� funk�nosti

	/** Logov�n� stavu */
	public static ArrayList<String> log = new ArrayList<String>();

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

	// Aktu�ln� �asovan� vstup

	/**
	 * Vstup kter� se periodicky bude volat po n�jak�m �ase - NULL = nic se nevol�
	 */
	private TimedInput timedInput;

	/**
	 * Konstruktor kone�n�ho automatu
	 */
	private Automaton() {
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

		// Initializace �asovan�ho vstupu
		this.timedInput = null;

	}

	/**
	 * P�enastav� �asovan� vstup
	 * 
	 * @param ch
	 *            znak, kter� bude periodicky vys�l�n
	 * @param timings
	 *            po�et milisekund za kter� se automaticky provede vstup
	 */
	public void setTimedInput(Character ch, int timings) {
		// Zastavit �asova� pokud existuje
		if (this.timedInput != null) {
			this.timedInput.stop();
		}

		this.timedInput = new TimedInput(ch, timings);
	}

	/**
	 * Vyma�e aktu�ln� �asovan� vstup
	 */
	public void removeTimedInput() {
		// Zastavit �asova� pokud existuje
		if (this.timedInput != null) {
			this.timedInput.stop();
		}

		this.timedInput = null;
	}

	/**
	 * Vr�t� jedinou instanci t��dy Automaton
	 * 
	 * @return instance t��dy Automaton
	 */
	public static Automaton getInstance() {
		if (Automaton.singleton == null) {
			Automaton.singleton = new Automaton();
		}
		return Automaton.singleton;
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
			Logger.log("setStart: V re�imu akce nelze upravit strukturu automatu");
			return false;
		}

		if (!this.states.contains(start)) {
			// Po��te�n� stav nem��e b�t takov�, �e nen� v mno�in� stav� automatu
			Logger.log(String.format("setStart: Stav nem��e b�t vstupn� - nen� v mno�in� stav� [stav: %s]",
					start.getID()));
			return false;
		}

		Logger.log(String.format("setStart: Nov� vstupn� stav => %s ", start.getID()));
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
			Logger.log("addState: V re�imu akce nelze upravit strukturu automatu");
			return false;
		}

		if (this.states.contains(state)) {
			// Nelze vlo�it stav, kter� ji� existuje
			Logger.log(String.format("addState: Nelze vlo�it stav, kter� ji� existuje [stav: %s]", state.getID()));
			return false;
		}

		Logger.log(String.format("addState: P�id�n nov� stav [stav: %s]", state.getID()));
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
			Logger.log("addOutputState: V re�imu akce nelze upravit strukturu automatu");
			return false;
		}

		if (!this.states.contains(state)) {
			Logger.log(String.format("addOutputState: V�stupn� stav nem��e b�t mimo mno�inu stav� [stav: %s]",
					state.getID()));
			// Kone�n� stav nem��e b�t mimo mno�inu v�ech stav� (t�eba: F c Q)
			return false;
		}

		if (this.end.contains(state)) {
			// Nelze vlo�it stav, kter� ji� existuje
			Logger.log(String.format("addOutputState: Nelze vlo�it v�stupn� stav, kter� ji� existuje [stav: %s]",
					state.getID()));
			return false;
		}

		Logger.log(String.format("addOutputState: P�id�n nov� v�stupn� stav [stav: %s]", state.getID()));
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
			Logger.log("addInput: V re�imu akce nelze upravit strukturu automatu");
			return false;
		}

		if (this.inputs.contains(ch)) {
			Logger.log(String.format("Nelze p�idat vstup, kter� ji� existuje [vstup: %s]", ch.toString()));
			// Nelze p�idat vstup, kter� ji� existuje
			return false;
		}

		Logger.log(String.format("addInput: P�id�n nov� vstup => %s", ch.toString()));
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
			Logger.log("addEdge: V re�imu akce nelze upravit strukturu automatu");
			return false;
		}

		State start = edge.getStart();

		if (!this.states.contains(start) || !this.states.contains(edge.getEnd())) {
			// V automatu nen� po��te�n� �i koncov� stav hrany
			Logger.log("addEdge: V automatu nen� po��te�n� �i koncov� stav hrany " + edge.toString());
			return false;
		}

		if (!this.inputs.contains(edge.getInput())) {
			// Reakce na tento znak nen� povolena
			Logger.log("addEdge: Reakce na tento znak nen� povolena " + edge.toString());
			return false;
		}

		if (!this.edges.containsKey(start)) {
			// Pokud neexistuje �lo�i�t� pro hrany, vytvo�� se
			Logger.log("addEdge: Vytv���m ulo�i�t� pro hrany pro stav " + start.toString());
			this.edges.put(start, new ArrayList<Edge>());
		}

		// Hrana ji� existuje
		if (this.edges.get(start).contains(edge)) {
			Logger.log("addEdge: Nelze p�idat hranu, kter� ji� existuje " + edge.toString());
			// Nelze p�idat hranu, kter� ji� existuje
			return false;
		}

		// Prid�n� hrany
		Logger.log("addEdge: P�id�v�m hranu ke kone�n�mu automatu " + edge.toString());
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
				Logger.log("tick(String): �etezec obsahuje nepovolen� znak -> " + ch);
				return false;
			}
		}

		Boolean state = null;
		for (int i = 0; i < arr.length; i++) {
			if (this.errorState) {
				// Automat narazil na chybov� absorb�n� stav, nen� nutn� pokra�ovat
				Logger.log("tick(String): Automat se dostal do absorb�n�ho chybov� stavu");
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
			Logger.log("tick(char): Nelze prov�d�t tick, automat je ve stavu �pravy");
			return false;
		}

		if (this.errorState) {
			// Automat se nach�z� v absorb�n�m chybov�m stavu
			Logger.log("tick(char): P�eskakuji tick, automat se nach�z� v chybov�m stavu");
			return true;
		}

		if (!this.inputs.contains(ch)) {
			// Automat ignoruje nepovolen� stavy
			Logger.log("tick(char): Ignoruji tick, vstupem je nepovolen� znak");
			return false;
		}

		if (this.edges.get(currentState) == null) {
			// Sou�asn� stav nem� ulo�i�t� pro hrany - vytv���m
			Logger.log("tick(char): Sou�asn� stav nem� ulo�i�t� pro hrany " + currentState.toString());
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
			Logger.log(String.format(
					"tick(char): Automat se dostal do chybov�ho absorb�n�ho stavu. [aktu�ln� stav: %s, vstup: %s]",
					currentState.getID(), ch.toString()));
			return true;
		}

		// P�esun dan�m vstupem do koncov�ho stavu
		Logger.log(String.format("tick(char): Prov�d�m akci => v�choz�: %s + vstup: %s => n�sleduj�c�: %s",
				desired.getStart().getID(), desired.getInput().toString(), desired.getEnd().getID()));

		currentState = desired.getEnd();

		// Zavol� metodu macro - Pokud se jedn� o makro stav, provede se makro
		currentState.macro();

		// Pokud m�me nastaven �asovan� vstup, za ur�it� �as ho zavol�me
		if (this.timedInput != null) {
			this.timedInput.run();
		}

		return true;

	}

	/**
	 * Zm�n� stav automatu na "b��"
	 * 
	 */
	public void run() {
		this.currentState = this.start;
		this.errorState = false;

		Logger.log("");
		Logger.log("");
		Logger.log("Resetuji automat - p�ep�n�m do re�imu akce ");

		this.running = true;

		// Provede prvn� �asovan� vstup automatu
		if (this.timedInput != null) {
			this.timedInput.run();
		}
	}

	/**
	 * Zm�n� m�d automatu na "Neb��"
	 * 
	 */
	public void stop() {
		Logger.log("Ukon�uji automat - p�ep�n�m do re�imu �pravy");
		this.running = false;
	}

	/**
	 * Vr�t� informaci o tom, zda se KA nach�z� v jednom z v�stupn�ch stav�
	 * 
	 * @return informace zda je KA ve v�stupn�m stavu
	 */
	public boolean accepting() {

		if (!this.running) {
			Logger.log("Automat neb��!!");

			return false;
		}

		if (this.errorState) {
			Logger.log("Automat chyboval!!");
			return false;
		}

		if (!this.end.contains(currentState)) {
			Logger.log("Automat nen� v kone�n�m stavu (current: " + this.currentState + ")");
			return false;
		}

		return true;
	}

}
