package cz.zcu.kiv.ti.sp.automaton;

/**
 * T��da, jej� instance reprezentuj� hranu v kone�n�m automatu
 * 
 * @author Jakub V�tek (A16B0165P)
 * 
 */
public class Edge {

	/** V�choz� stav p�ed zpracov�n�m */
	private final State start;
	/** Stav po zpracov�n� hrany */
	private final State end;

	/** Rozpozn�van� vstup */
	private final Character input;

	/**
	 * Konstruktor hrany v KA
	 * 
	 * @param start
	 *            v�choz� stav
	 * @param end
	 *            kone�n� stav
	 * @param input
	 *            rozpozn�van� vstup
	 */
	public Edge(State start, State end, Character input) {
		super();
		this.start = start;
		this.end = end;
		this.input = input;
	}

	/**
	 * Vr�t� v�choz� stav p�ed zpracov�n�m hrany
	 * 
	 * @return
	 */
	public State getStart() {
		return start;
	}

	/** Vr�t� stav po zpracov�n� hrany */
	public State getEnd() {
		return end;
	}

	/** Vr�t� rozpozn�van� vstup */
	public Character getInput() {
		return input;
	}

	/**
	 * Vr�t� reprezentace instance jako �et�zec
	 * 
	 * @return reprezentace instance jako �et�zec
	 */
	public String toString() {
		return String.format("[start: %s, end: %s, vstup: %s]", this.getStart().getID(), this.getEnd().getID(),
				this.getInput().toString());
	}

}
