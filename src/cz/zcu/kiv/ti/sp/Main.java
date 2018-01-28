package cz.zcu.kiv.ti.sp;

import cz.zcu.kiv.ti.sp.automaton.Automaton;
import cz.zcu.kiv.ti.sp.automaton.Edge;
import cz.zcu.kiv.ti.sp.automaton.State;
import cz.zcu.kiv.ti.sp.gui.GUI;
import cz.zcu.kiv.ti.sp.gui.MainView;
import cz.zcu.kiv.ti.sp.gui.img.ImageReference;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Hlavní tøída aplikace
 * 
 * @author Jakub Vítek (A16B0165P)
 *
 */
public class Main {

	/** Koneèný automat */
	private static Automaton automat;

	private static final int slowTime = 8000;
	private static final int fastTime = 4000;

	/**
	 * Hlavní metoda aplikace
	 * 
	 * @param args
	 *            vstupní parametry
	 */
	public static void main(String[] args) {
		// Nastavení
		Main.setup();

		// Vytvoøení GUI okna
		GUI.start();

	}

	/**
	 * Nastavení automatu
	 */
	private static void setupDemo() {
		// Nastavení struktury
		automat = Automaton.getInstance();

		// Stavy automatu

		// ---- Zmìní automatický tick na A
		State s1 = new State(String.format("%d", 1)) {
			@Override
			public void macro() {

				Automaton.getInstance().setTimedInput('a', 5000);
			}
		};

		// ---- Zmìní automatický tick na B
		State s2 = new State(String.format("%d", 2)) {
			@Override
			public void macro() {
				Image image = new Image(
						ImageReference.getInstance().getClass().getResource("S1-2.png").toExternalForm());
				Automaton.getInstance().setTimedInput('b', 5000);
			}
		};

		// ---- Zmìní automatický tick na B
		State s3 = new State(String.format("%d", 3)) {
			@Override
			public void macro() {
				Image image = new Image(ImageReference.getInstance().getClass().getResource("S2.png").toExternalForm());

				MainView viewInstance = MainView.getInstance();
				viewInstance.view = new ImageView(image);

				Automaton.getInstance().setTimedInput('b', 5000);
			}
		};

		// ---- Zmìní automatický tick na A
		State s4 = new State(String.format("%d", 4)) {
			@Override
			public void macro() {
				Automaton.getInstance().setTimedInput('a', 5000);
			}
		};

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

		// Èasovaný vstup
		automat.setTimedInput('a', 5000);

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

	private static void setup() {
		automat = Automaton.getInstance();

		/*
		 * 
		 * Èas pro definici stavù
		 * 
		 * 
		 */

		// ---- S0 - START
		State s0 = new State(String.format("%d", 0)) {
			@Override
			public void macro() {
				Image image = new Image(ImageReference.getInstance().getClass().getResource("S0.png").toExternalForm());

				MainView viewInstance = MainView.getInstance();
				viewInstance.view.setImage(image);

				Automaton.getInstance().removeTimedInput();
			}
		};

		// ---- S1
		State s1 = new State(String.format("%d", 1)) {
			@Override
			public void macro() {
				Image image = new Image(ImageReference.getInstance().getClass().getResource("S1.png").toExternalForm());

				MainView viewInstance = MainView.getInstance();
				viewInstance.view.setImage(image);

				Automaton.getInstance().setTimedInput('c', slowTime);
			}
		};

		// ---- S1P - okruh pro pøechody
		State s1p = new State(String.format("%d", 7)) {
			@Override
			public void macro() {
				Image image = new Image(
						ImageReference.getInstance().getClass().getResource("S1-Button.png").toExternalForm());

				MainView viewInstance = MainView.getInstance();
				viewInstance.view.setImage(image);

				Automaton.getInstance().setTimedInput('c', fastTime);
			}
		};

		// ---- S1-2
		State s2 = new State(String.format("%d", 2)) {
			@Override
			public void macro() {
				Image image = new Image(
						ImageReference.getInstance().getClass().getResource("S1-2.png").toExternalForm());

				MainView viewInstance = MainView.getInstance();
				viewInstance.view.setImage(image);

				Automaton.getInstance().setTimedInput('c', slowTime);
			}
		};

		// ---- S1-2P - okruh pro pøechody
		State s2p = new State(String.format("%d", 8)) {
			@Override
			public void macro() {
				Image image = new Image(
						ImageReference.getInstance().getClass().getResource("S1-2-Button.png").toExternalForm());

				MainView viewInstance = MainView.getInstance();
				viewInstance.view.setImage(image);

				Automaton.getInstance().setTimedInput('c', fastTime);
			}
		};

		// ---- S2
		State s3 = new State(String.format("%d", 3)) {
			@Override
			public void macro() {
				Image image = new Image(ImageReference.getInstance().getClass().getResource("S2.png").toExternalForm());

				MainView viewInstance = MainView.getInstance();
				viewInstance.view.setImage(image);

				Automaton.getInstance().setTimedInput('c', slowTime);
			}
		};

		// ---- S2-3
		State s4 = new State(String.format("%d", 4)) {
			@Override
			public void macro() {
				Image image = new Image(
						ImageReference.getInstance().getClass().getResource("S2-3.png").toExternalForm());

				MainView viewInstance = MainView.getInstance();
				viewInstance.view.setImage(image);

				Automaton.getInstance().setTimedInput('c', slowTime);
			}
		};

		// ---- S2-3P - okruh pro pøechody
		State s4p = new State(String.format("%d", 9)) {
			@Override
			public void macro() {
				Image image = new Image(
						ImageReference.getInstance().getClass().getResource("S2-3-Button.png").toExternalForm());

				MainView viewInstance = MainView.getInstance();
				viewInstance.view.setImage(image);

				Automaton.getInstance().setTimedInput('c', fastTime);
			}
		};

		// ---- S3
		State s5 = new State(String.format("%d", 5)) {
			@Override
			public void macro() {
				Image image = new Image(ImageReference.getInstance().getClass().getResource("S3.png").toExternalForm());

				MainView viewInstance = MainView.getInstance();
				viewInstance.view.setImage(image);

				Automaton.getInstance().setTimedInput('c', slowTime);
			}
		};

		// ---- S1-2P - okruh pro pøechody
		State s5p = new State(String.format("%d", 10)) {
			@Override
			public void macro() {
				Image image = new Image(
						ImageReference.getInstance().getClass().getResource("S3-Button.png").toExternalForm());

				MainView viewInstance = MainView.getInstance();
				viewInstance.view.setImage(image);

				Automaton.getInstance().setTimedInput('c', fastTime);
			}
		};

		// ---- S3-1
		State s6 = new State(String.format("%d", 6)) {
			@Override
			public void macro() {
				Image image = new Image(
						ImageReference.getInstance().getClass().getResource("S3-1.png").toExternalForm());

				MainView viewInstance = MainView.getInstance();
				viewInstance.view.setImage(image);

				Automaton.getInstance().setTimedInput('c', slowTime);
			}
		};

		// ---- S1-2P - okruh pro pøechody
		State s6p = new State(String.format("%d", 11)) {
			@Override
			public void macro() {
				Image image = new Image(
						ImageReference.getInstance().getClass().getResource("S3-1-Button.png").toExternalForm());

				MainView viewInstance = MainView.getInstance();
				viewInstance.view.setImage(image);

				Automaton.getInstance().setTimedInput('c', fastTime);
			}
		};

		// Èasovaný vstup - c
		automat.addInput('c');
		// Tlaèítkový vstup - t
		automat.addInput('t');
		// Zapínací signál - p
		automat.addInput('p');
		// Vypínací signál - k
		automat.addInput('k');

		// Poèáteèní stav
		automat.addState(s0);
		automat.setStart(s0);

		// Pøidání stavù - hlavní okruh
		automat.addState(s1);
		automat.addState(s2);
		automat.addState(s3);
		automat.addState(s4);
		automat.addState(s5);
		automat.addState(s6);

		// Pøidání stavù - vedlejší okruh
		automat.addState(s1p);
		automat.addState(s2p);
		automat.addState(s4p);
		automat.addState(s5p);
		automat.addState(s6p);

		/*
		 * 
		 * Èást pro definici hran
		 * 
		 * 
		 */

		// S1 to S1-2
		Edge s1c = new Edge(s1, s2, 'c');

		// S1-2 to S2
		Edge s12c = new Edge(s2, s3, 'c');

		// S2 to S2-3
		Edge s2c = new Edge(s3, s4, 'c');

		// S2-3 to S3
		Edge s23c = new Edge(s4, s5, 'c');

		// S3 to S3-1
		Edge s3c = new Edge(s5, s6, 'c');

		// S3-1 to S1
		Edge s31c = new Edge(s6, s1, 'c');

		// S1 to S1P
		Edge s1t = new Edge(s1, s1p, 't');

		// S1P to S12P
		Edge s1pc = new Edge(s1p, s2p, 'c');

		// S1P to S1P
		Edge s1pt = new Edge(s1p, s1p, 't');

		// S12 to S12P
		Edge s2t = new Edge(s2, s2p, 't');

		// S12P to S2
		Edge s2pc = new Edge(s2p, s3, 'c');

		// S3 to S3
		Edge s3t = new Edge(s3, s3, 't');

		// S4 to S4p
		Edge s4t = new Edge(s4, s4p, 't');

		// S4p to S4p
		Edge s4pt = new Edge(s4p, s4p, 't');

		// S4p to S5p
		Edge s4pc = new Edge(s4p, s5p, 'c');

		// S5P to S5p
		Edge s5pt = new Edge(s5p, s5p, 't');

		// S5p to S6P
		Edge s5pc = new Edge(s5p, s6p, 'c');

		// S6P to S6P
		Edge s6pt = new Edge(s6p, s6p, 't');

		// S6P to S1P
		Edge s6pc = new Edge(s6p, s1p, 'c');

		// S5 to S5P
		Edge s5t = new Edge(s5, s5p, 't');

		// S6 to S6P
		Edge s6t = new Edge(s6, s6p, 't');

		// S0 to S1
		Edge s0p = new Edge(s0, s1, 'p');
		Edge s0c = new Edge(s0, s0, 'c');
		Edge s0t = new Edge(s0, s0, 't');
		Edge s0k = new Edge(s0, s0, 'k');

		// Hrany konce
		Edge s1k = new Edge(s1, s0, 'k');
		Edge s2k = new Edge(s2, s0, 'k');
		Edge s3k = new Edge(s3, s0, 'k');
		Edge s4k = new Edge(s4, s0, 'k');
		Edge s5k = new Edge(s5, s0, 'k');
		Edge s6k = new Edge(s6, s0, 'k');
		Edge s1pk = new Edge(s1p, s0, 'k');
		Edge s2pk = new Edge(s2p, s0, 'k');
		Edge s4pk = new Edge(s4p, s0, 'k');
		Edge s5pk = new Edge(s5p, s0, 'k');
		Edge s6pk = new Edge(s6p, s0, 'k');

		// Hrany poèátku
		Edge s1ph = new Edge(s1, s1, 'p');
		Edge s2ph = new Edge(s2, s2, 'p');
		Edge s3ph = new Edge(s3, s3, 'p');
		Edge s4ph = new Edge(s4, s4, 'p');
		Edge s5ph = new Edge(s5, s5, 'p');
		Edge s6ph = new Edge(s6, s6, 'p');
		Edge s1pp = new Edge(s1p, s1p, 'p');
		Edge s2pp = new Edge(s2p, s2p, 'p');
		Edge s4pp = new Edge(s4p, s4p, 'p');
		Edge s5pp = new Edge(s5p, s5p, 'p');
		Edge s6pp = new Edge(s6p, s6p, 'p');

		// Hrany vnitøního cyklu
		automat.addEdge(s1c);
		automat.addEdge(s12c);
		automat.addEdge(s2c);
		automat.addEdge(s23c);
		automat.addEdge(s3c);
		automat.addEdge(s31c);

		// Hrany vnìjšího cyklu
		automat.addEdge(s1t);
		automat.addEdge(s1pc);
		automat.addEdge(s1pt);
		automat.addEdge(s2t);
		automat.addEdge(s2pc);
		automat.addEdge(s3t);
		automat.addEdge(s4t);
		automat.addEdge(s4pt);
		automat.addEdge(s4pc);
		automat.addEdge(s5pt);
		automat.addEdge(s5pc);
		automat.addEdge(s6pt);
		automat.addEdge(s6pc);
		automat.addEdge(s5t);
		automat.addEdge(s6t);

		// Hrany poèátku
		automat.addEdge(s0p);
		automat.addEdge(s0c);
		automat.addEdge(s0t);
		automat.addEdge(s0k);

		// Hrany pro end signal
		automat.addEdge(s1k);
		automat.addEdge(s2k);
		automat.addEdge(s3k);
		automat.addEdge(s4k);
		automat.addEdge(s5k);
		automat.addEdge(s6k);
		automat.addEdge(s1pk);
		automat.addEdge(s2pk);
		automat.addEdge(s4pk);
		automat.addEdge(s5pk);
		automat.addEdge(s6pk);

		// Hrany po start signal
		automat.addEdge(s1ph);
		automat.addEdge(s2ph);
		automat.addEdge(s3ph);
		automat.addEdge(s4ph);
		automat.addEdge(s5ph);
		automat.addEdge(s6ph);
		automat.addEdge(s1pp);
		automat.addEdge(s2pp);
		automat.addEdge(s4pp);
		automat.addEdge(s5pp);
		automat.addEdge(s6pp);

		/*
		 * 
		 * Nastavení poèáteèního èasovaného vstupu
		 * 
		 * 
		 */

		Automaton.getInstance().setTimedInput('c', 5000);

	}

}
