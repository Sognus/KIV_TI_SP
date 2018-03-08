package cz.zcu.kiv.ti.sp.automaton;

/**
 * Tøída, jejíž instance reprezentují jeden stav v koneèném automatu. Pøi tvorbì
 * instance lze pøetížit metodu macro a vytvoøit tak makro stav
 * 
 * @author Jakub Vítek (A16B0165P)
 *
 */
public class State {

	/** Identifikátor stavu */
	private final String ID;

	/**
	 * Konstruktor stavu v KA
	 * 
	 * @param id
	 *            identifikátor stavu
	 */
	public State(String id) {
		this.ID = id;
	}

	/**
	 * Vrátí identifikátor stavu
	 * 
	 * @return identifikátor stavu v KA
	 */
	public String getID() {
		return ID;
	}

	/**
	 * Vrátí reprezentaci instance jako øetìzec
	 * 
	 * @return reprezentace instance jako øetìzec
	 */
	@Override
	public String toString() {
		return String.format("[stav: %s]", this.ID);
	}

	/**
	 * Generická definice metody, kterou lze pøi vytváøení instance pøepsat a
	 * vytvoøit tak tvz. makro stav
	 * 
	 */
	public void macro() {

	}

}
