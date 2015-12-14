package makeo.gadomancy.common.utils;

import io.netty.buffer.ByteBuf;

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

    public static void writeToBuffer(ByteBuf buf, String toWrite) {
        byte[] str = toWrite.getBytes();
        buf.writeInt(str.length);
        buf.writeBytes(str);
    }

    public static String readFromBuffer(ByteBuf buf) {
        int length = buf.readInt();
        byte[] strBytes = new byte[length];
        buf.readBytes(strBytes, 0, length);
        return new String(strBytes);
    }

}
