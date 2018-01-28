package cz.zcu.kiv.ti.sp.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * T��da Logger, kter� zaji�tuje logov�n� DEBUG zpr�v
 * 
 * a) Vypisuje logy na consoli dle nastaven�
 * 
 * b) Vypisuje logy do struktury, ze kter� lze n�sledn� exportovat data do
 * souboru
 * 
 * TODO: printFile() - vyp�e v�echny kategorie do souboru
 * 
 * TODO: printFile(String cat) - vyp�e zadanou kategorii do souboru
 * 
 * 
 * @author Jakub V�tek (A16B0165P)
 *
 */
public class Logger {

	/** Ulo�en� logy */
	private static HashMap<String, ArrayList<String>> data = new HashMap<String, ArrayList<String>>();

	/** Ur�uje, zda se bude logovan� text okam�it� vypisovat na obrazovku */
	public static boolean console = true;

	/** Ur�uje, zda se p�i vypisu do console bude zobrazovat kategorie */
	public static boolean consolePrintCategory = true;

	/** Ur�uje, zda se u logu v consoli bude vypisovat �as zalogov�n� */
	public static boolean consolePrintTime = true;

	/**
	 * Na n�kter�ch syst�mech je probl�m s diakritikou, tato hodnota ur�uje zda se
	 * bude diakritika nahrazovat
	 */
	public static boolean autoNormalize = true;

	/**
	 * Ur�uje, zda se bude logovan� text ukl�dat do struktury, kterou je mo�n�
	 * ulo�it do souboru
	 */
	public static boolean save = false;

	/** Ur�uje zda se v ulo�en� struktu�e bude vypisovat �as logov�n� */
	public static boolean savePrintTime = false;

	/**
	 * Zaloguje text s obecnou kategori�
	 * 
	 * @param text
	 *            �etezec k logov�n�
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
	 * Ulo�� do struktury text s kategori�
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
	 * Ulo�� do struktury text s kategori� a �asem
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
	 * Vyp�e logovan� text s kategori�
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
	 * Vyp�e logovan� text s kategori� a �asem
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
