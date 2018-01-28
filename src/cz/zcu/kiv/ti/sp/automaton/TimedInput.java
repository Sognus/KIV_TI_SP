package cz.zcu.kiv.ti.sp.automaton;

import java.util.Timer;
import java.util.TimerTask;

/**
 * T��da, kter� zaji�tuje zpo�d�n� vol�n� vstupu
 * 
 * @author Jakub V�tek (A16B0165P)
 *
 */
public class TimedInput {

	/** Akce, kter� se provede po dokon�en� �asova�e */
	private TimerTask task;
	/** Vstup, kter� je zavol�n po uplynut� �asova�e */
	private Character input;

	private Timer timer;

	/** �asova� vstupu (v milisekund�ch) */
	private int timing;

	/**
	 * Vytvo�� novou instanci typu TimedInput
	 * 
	 * @param input
	 *            vstup, kter� instance vygeneruje po ub�hnut� �asova�e
	 * @param timing
	 *            �as v milisekund�ch
	 */
	public TimedInput(Character input, int timing) {
		this.input = input;
		this.timing = timing;
	}

	public void run() {
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				Automaton.getInstance().tick(input);

			}
		}, timing);
	}

	public void stop() {
		if (timer == null) {
			return;
		}
		timer.cancel();
	}

	/**
	 * Metoda, kter� zaji��uje vol�n� vstupu se zpo�d�n�m. Pouze jednou
	 * 
	 * @param ch
	 *            vstup, kter� m� b�t vyvol�n
	 * @param timing
	 *            po�et milisekund po kter�ch m� b�t vstup vyvol�n
	 */
	public static void runDelayedInput(Character ch, int timing) {
		new TimedInput(ch, timing).run();
	}

}
