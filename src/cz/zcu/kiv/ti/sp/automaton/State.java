package cz.zcu.kiv.ti.sp.automaton;

/**
 * T��da, jej� instance reprezentuj� jeden stav v kone�n�m automatu
 * 
 * @author Jakub V�tek (A16B0165P)
 *
 */
public class State {

	/** Identifik�tor stavu */
	private final String ID;

	/**
	 * Konstruktor stavu v KA
	 * 
	 * @param id
	 *            identifik�tor stavu
	 */
	public State(String id) {
		this.ID = id;
	}

	/**
	 * Vr�t� identifik�tor stavu
	 * 
	 * @return identifik�tor stavu v KA
	 */
	public String getID() {
		return ID;
	}

	/**
	 * Vr�t� reprezentaci instance jako �et�zec
	 * 
	 * @return reprezentace instance jako �et�zec
	 */
	@Override
	public String toString() {
		return String.format("[stav: %s]", this.ID);
	}

}
