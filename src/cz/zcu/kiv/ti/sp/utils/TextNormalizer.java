package cz.zcu.kiv.ti.sp.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class TextNormalizer {

	public static String replaceDiacritics(String input) {
		if (input == null) {
			return "";
		}

		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

		String temp = input.trim();
		temp = Normalizer.normalize(temp, Normalizer.Form.NFD);
		temp = pattern.matcher(temp).replaceAll("");
		temp = temp.replaceAll("[\uFEFF-\uFFFF]", ""); // remove 'ZERO WIDTH NO-BREAK SPACE' chars (U+FEFF)

		return temp;
	}

}
