package cz.zcu.kiv.ti.sp.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Tøída Logger, která zajištuje logování DEBUG zpráv
 * 
 * a) Vypisuje logy na consoli dle nastavení
 * 
 * b) Vypisuje logy do struktury, ze které lze následnì exportovat data do
 * souboru
 * 
 * TODO: printFile() - vypíše všechny kategorie do souboru
 * 
 * TODO: printFile(String cat) - vypíše zadanou kategorii do souboru
 * 
 * 
 * @author Jakub Vítek (A16B0165P)
 *
 */
public class Logger {

	/** Uložené logy */
	private static HashMap<String, ArrayList<String>> data = new HashMap<String, ArrayList<String>>();

	/** Urèuje, zda se bude logovaný text okamžitì vypisovat na obrazovku */
	public static boolean console = true;

	/** Urèuje, zda se pøi vypisu do console bude zobrazovat kategorie */
	public static boolean consolePrintCategory = true;

	/** Urèuje, zda se u logu v consoli bude vypisovat èas zalogování */
	public static boolean consolePrintTime = true;

	/**
	 * Na nìkterých systémech je problém s diakritikou, tato hodnota urèuje zda se
	 * bude diakritika nahrazovat
	 */
	public static boolean autoNormalize = true;

	/**
	 * Urèuje, zda se bude logovaný text ukládat do struktury, kterou je možné
	 * uložit do souboru
	 */
	public static boolean save = false;

	/** Urèuje zda se v uložené struktuøe bude vypisovat èas logování */
	public static boolean savePrintTime = false;

	/**
	 * Zaloguje text s obecnou kategorií
	 * 
	 * @param text
	 *            øetezec k logování
	 */
	public static void log(String text) {
		if (Logger.save) {
			if (Logger.savePrintTime) {
				Logger.save("General", text, new Date());
			} else {
				Logger.save("General", text);
			}
		}

		if (Logger.console) {
			if (Logger.consolePrintCategory && Logger.consolePrintTime) {
				Logger.print("General", text, new Date());
			}

			if (Logger.consolePrintCategory && !Logger.consolePrintTime) {
				Logger.print("General", text);
			}

			if (!Logger.consolePrintCategory && !Logger.consolePrintTime) {
				Logger.print(text);
			}

		}
	}

	public static void log(String category, String text) {
		if (Logger.save) {
			if (Logger.savePrintTime) {
				Logger.save(category, text, new Date());
			} else {
				Logger.save(category, text);
			}
		}

		if (Logger.console) {
			if (Logger.consolePrintCategory && Logger.consolePrintTime) {
				Logger.print(category, text, new Date());
			}

			if (Logger.consolePrintCategory && !Logger.consolePrintTime) {
				Logger.print(category, text);
			}

			if (!Logger.consolePrintCategory && !Logger.consolePrintTime) {
				Logger.print(text);
			}

		}
	}

	/**
	 * Uloží do struktury text s kategorií
	 * 
	 * @param category
	 * @param text
	 */
	private static void save(String category, String text) {
		if (data.get(category) == null) {
			data.put(category, new ArrayList<String>());
		}
		data.get(category).add(text);
	}

	/**
	 * Uloží do struktury text s kategorií a èasem
	 * 
	 * @param category
	 * @param text
	 * @param now
	 */
	private static void save(String category, String text, Date now) {
		SimpleDateFormat dt = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
		String txt = String.format("[%s][%s]: %s", dt.format(now), category, text);

		if (data.get(category) == null) {
			data.put(category, new ArrayList<String>());
		}
		data.get(category).add(txt);
	}

	/**
	 * Vypíše logovaný text s kategorií
	 * 
	 * @param category
	 * @param text
	 */
	private static void print(String category, String text) {
		if (autoNormalize) {
			text = TextNormalizer.replaceDiacritics(text);
		}

		System.out.println(String.format("[%s] %s", category, text));
	}

	/**
	 * Vypíše logovaný text s kategorií a èasem
	 * 
	 * @param category
	 * @param text
	 * @param now
	 */
	private static void print(String category, String text, Date now) {
		if (autoNormalize) {
			text = TextNormalizer.replaceDiacritics(text);
		}

		SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		System.out.println(String.format("[%s][%s]: %s", dt.format(now), category, text));
	}

	private static void print(String text) {
		if (autoNormalize) {
			text = TextNormalizer.replaceDiacritics(text);
		}

		System.out.println(String.format("%s", text));
	}

}
