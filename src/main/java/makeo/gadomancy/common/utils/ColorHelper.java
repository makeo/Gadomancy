package makeo.gadomancy.common.utils;

import java.awt.*;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 13.07.2015 03:50
 */
public class ColorHelper {
    public static final char COLOR_CHAR = '\u00a7';

    private ColorHelper() {}

    public static int toHex(int red, int green, int blue) {
        return (red << 16) | (green << 8) | (blue);
    }

    public static int toHex(Color color) {
        return toHex(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static String extractColors(String input) {
        StringBuilder output = new StringBuilder();

        boolean isColor = false;
        for(char c : input.toCharArray()) {
            if(c == COLOR_CHAR) {
                isColor = true;
            } else if(isColor) {
                isColor = false;
                output.append(COLOR_CHAR).append(c);
            }
        }
        return output.toString();
    }
}
