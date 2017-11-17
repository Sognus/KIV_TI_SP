package cz.zcu.kiv.ti.sp;

import cz.zcu.kiv.ti.sp.automaton.Automaton;
import cz.zcu.kiv.ti.sp.automaton.Edge;
import cz.zcu.kiv.ti.sp.automaton.State;

/**
 * Hlavní tøída aplikace
 * 
 * @author Jakub Vítek (A16B0165P)
 *
 */
public class Main {

	/** Koneèný automat */
	private static Automaton automat;

	/**
	 * Hlavní metoda aplikace
	 * 
	 * @param args
	 *            vstupní parametry
	 */
	public static void main(String[] args) {
		// Nastavení
		Main.setup();

		automat.run();
		automat.tick("aababbaa");

		for (String s : automat.log) {
			System.out.println(s);
		}

		System.out.println("Akceptuje: " + automat.accepting());

	}

	/**
	 * Nastavení automatu
	 */
	private static void setup() {
		// Nastavení struktury
		automat = new Automaton();

		// Stavy automatu
		State s1 = new State(String.format("%d", 1));
		State s2 = new State(String.format("%d", 2));
		State s3 = new State(String.format("%d", 3));
		State s4 = new State(String.format("%d", 4));

		// Vstupy automatu
		automat.addInput('a');
		automat.addInput('b');

		// Vstupní stav
		automat.addState(s1);
		automat.setStart(s1);

		// Stavy
		automat.addState(s2);
		automat.addState(s3);

		// Výstupní stav
		automat.addState(s4);
		automat.addOutputState(s4);

		// Hrany automatu
		Edge s1a = new Edge(s1, s2, 'a');
		Edge s1b = new Edge(s1, s3, 'b');

		Edge s2a = new Edge(s2, s1, 'a');
		Edge s2b = new Edge(s2, s4, 'b');

		Edge s3a = new Edge(s3, s4, 'a');
		Edge s3b = new Edge(s3, s1, 'b');

		Edge s4a = new Edge(s4, s3, 'a');
		Edge s4b = new Edge(s4, s2, 'b');

		automat.addEdge(s1a);
		automat.addEdge(s1b);
		automat.addEdge(s2a);
		automat.addEdge(s2b);
		automat.addEdge(s3a);
		automat.addEdge(s3b);
		automat.addEdge(s4a);
		automat.addEdge(s4b);

	}

}
