package cz.zcu.kiv.ti.sp.automaton;

/**
 * Tøída, jejíž instance reprezentují hranu v koneèném automatu
 * 
 * @author Jakub Vítek (A16B0165P)
 * 
 */
public class Edge {

	/** Výchozí stav pøed zpracováním */
	private final State start;
	/** Stav po zpracování hrany */
	private final State end;

	/** Rozpoznávaný vstup */
	private final Character input;

	/**
	 * Konstruktor hrany v KA
	 * 
	 * @param start
	 *            výchozí stav
	 * @param end
	 *            koneèný stav
	 * @param input
	 *            rozpoznávaný vstup
	 */
	public Edge(State start, State end, Character input) {
		super();
		this.start = start;
		this.end = end;
		this.input = input;
	}

	/**
	 * Vrátí výchozí stav pøed zpracováním hrany
	 * 
	 * @return
	 */
	public State getStart() {
		return start;
	}

	/** Vrátí stav po zpracování hrany */
	public State getEnd() {
		return end;
	}

	/** Vrátí rozpoznávaný vstup */
	public Character getInput() {
		return input;
	}

	/**
	 * Vrátí reprezentace instance jako øetìzec
	 * 
	 * @return reprezentace instance jako øetìzec
	 */
	public String toString() {
		return String.format("[start: %s, end: %s, vstup: %s]", this.getStart().getID(), this.getEnd().getID(),
				this.getInput().toString());
	}

}
