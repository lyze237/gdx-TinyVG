package dev.lyze.gdxtinyvg.utils;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import java.io.IOException;
import lombok.var;

public class StreamUtils {
    /**
     * This type is used to encode 32 bit unsigned integers while keeping the number
     * of bytes low. It is encoded as a variable-sized integer that uses 7 bit per
     * byte for integer bits and the 7th bit to encode that there is "more bits
     * available".
     *
     * @param stream The appropriately positioned input stream.
     * @return The actual int value of the VarUInt.
     */
    public static int readVarUInt(LittleEndianInputStream stream) throws IOException {
        var count = 0;
        var result = 0;

        while (true) {
            var b = stream.readUnsignedByte();
            var val = (b & 0x7F) << (7 * count);
            result |= val;

            if ((b & 0x80) == 0)
                break;

            count++;
        }

        return result;
    }

    /**
     * Reads multiple consecutive bytes.
     * 
     * @param stream The appropriately positioned input stream.
     * @param amount How many bytes to read.
     * @return A byte array of all the read bytes.
     */
    public static int[] readNBytes(LittleEndianInputStream stream, int amount) throws IOException {
        var bytes = new int[amount];

        for (int i = 0; i < amount; i++)
            bytes[i] = stream.readUnsignedByte();

        return bytes;
    }
}
