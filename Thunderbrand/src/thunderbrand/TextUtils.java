package thunderbrand;

public class TextUtils {
	
	public static String aOrAn(String word) {
		word = word.trim();
		if (word.isEmpty() || !isVowel(word.charAt(0))) {
			return "a";
		}
		return "an";
	}
	
	public static boolean isVowel(char letter) {
		if (letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u' ||
				letter == 'A' || letter == 'E' || letter == 'I' || letter == 'O' || letter == 'U') {
			return true;
		}
		return false;
	}
	
}
