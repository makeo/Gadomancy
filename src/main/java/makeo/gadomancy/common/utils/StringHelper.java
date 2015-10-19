package makeo.gadomancy.common.utils;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 27.07.2015 21:23
 */
public class StringHelper {
    private StringHelper() {}

    public static String firstToUpper(String input) {
        if(input.length() > 0)
            return Character.toUpperCase(input.charAt(0)) + input.substring(1);
        return input;
    }
}
