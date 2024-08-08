package io.github.mmm.orm.memory.example;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class to {@link #normalize(String) normalize} {@link String}s.
 *
 * @see #normalize(String)
 */
public final class StringNormalizer implements Function<String, String> {

  private static final StringNormalizer INSTANCE = new StringNormalizer();

  private static final Map<Character, String> MAPPINGS = new HashMap<>();

  static {
    //
    map('_', " ");
    map('-', " ");
    // German umlauts
    map('ß', "ss");
    map('ä', "ae");
    map('ö', "oe");
    map('ü', "ue");
  }

  private static void map(char c, String mapping) {

    MAPPINGS.put(Character.valueOf(c), mapping);
  }

  /**
   * @param value the value to normalize.
   * @return the normalized value. Will be transliterated to only contain Latin {@link Character#isLowerCase(char) lower
   *         case} letters and regular digits (0-9). Also includes stemming of the words "a" and "the" so "The Beatles"
   *         becomes "beatles" and "A Hard Day's Night" becomes "hard days night".
   */
  public static String normalize(String value) {

    return normalize(value, false);
  }

  /**
   * @param value the value to normalize.
   * @param removeSpaces - {@code true} to also remove whitespaces, {@code false} otherwise.
   * @return the normalized value. Will be transliterated to only contain Latin {@link Character#isLowerCase(char) lower
   *         case} letters and regular digits (0-9). Also includes stemming of the words "a" and "the" so "The Beatles"
   *         becomes "beatles" and "A Hard Day's Night" becomes "hard days night".
   */
  public static String normalize(String value, boolean removeSpaces) {

    if (value == null) {
      return null;
    }
    String key = value.toLowerCase(Locale.ROOT);
    int length = key.length();
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      char c = key.charAt(i);
      if (((c >= 'a') && (c <= 'z')) || ((c >= '0') && (c <= '9')) || (c == ' ')) {
        sb.append(c);
      } else {
        String mapping = MAPPINGS.get(Character.valueOf(c));
        if (mapping != null) {
          sb.append(mapping);
        }
      }
    }
    key = sb.toString();
    key = key.replaceAll("(^| )(a|the) ", "$1");
    if (removeSpaces) {
      key = key.replace(" ", "");
    } else {
      key = key.replaceAll(" +", " ");
    }
    return key;
  }

  @Override
  public String apply(String t) {

    return normalize(t);
  }

  /**
   * @return the singleton instance of {@link StringNormalizer}.
   */
  public static StringNormalizer get() {

    return INSTANCE;
  }
}
