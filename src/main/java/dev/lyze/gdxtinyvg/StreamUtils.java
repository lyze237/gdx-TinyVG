package dev.lyze.gdxtinyvg;

import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.LittleEndianInputStream;
import java.io.IOException;
import lombok.var;

public class StreamUtils {
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

    public static int[] readNBytes(LittleEndianInputStream stream, int amount) throws IOException {
        var bytes = new int[amount];

        for (int i = 0; i < amount; i++)
            bytes[i] = stream.readUnsignedByte();

        return bytes;
    }

    public static void printRestOfBytes(LittleEndianInputStream stream) {
        IntArray arr = new IntArray();
        while (true) {
            try {
                arr.add(stream.readUnsignedByte());
            } catch (Exception e) {
                break;
            }
        }

        for (int i = 0; i < arr.size; i++) {
            String s = Integer.toBinaryString(arr.get(i));
            System.out.print("00000000".substring(s.length()) + s + " ");
        }

        System.out.println();
        for (int i = 0; i < arr.size; i++) {
            String s = Integer.toHexString(arr.get(i));
            if (s.length() == 1)
                System.out.print("0" + s + " ");
            else
                System.out.print(s + " ");
        }
    }
}
