package cz.zcu.kiv.ti.sp.automaton;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Tøída, která zajištuje zpodìné volání vstupu
 * 
 * @author Jakub Vítek (A16B0165P)
 *
 */
public class TimedInput {

	/** Akce, která se provede po dokonèení èasovaèe */
	private TimerTask task;
	/** Vstup, kterı je zavolán po uplynutí èasovaèe */
	private Character input;

	/** Èasovaè vstupu (v milisekundách) */
	private int timing;

	/**
	 * Vytvoøí novou instanci typu TimedInput
	 * 
	 * @param input
	 *            vstup, kterı instance vygeneruje po ubìhnutí èasovaèe
	 * @param timing
	 *            èas v milisekundách
	 */
	public TimedInput(Character input, int timing) {
		this.input = input;
		this.timing = timing;
	}

	public void run() {
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				Automaton.getInstance().tick(input);

			}
		}, timing);
	}

	/**
	 * Metoda, která zajišuje volání vstupu se zpodìním. Pouze jednou
	 * 
	 * @param ch
	 *            vstup, kterı má bıt vyvolán
	 * @param timing
	 *            poèet milisekund po kterıch má bıt vstup vyvolán
	 */
	public static void runDelayedInput(Character ch, int timing) {
		new TimedInput(ch, timing).run();
	}

}
