package moe.gensokyoradio.liberty.mymind;
/*
 *     This file is part of MyMind.
 * 
 *     MyMind is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     MyMind is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with MyMind. If not, see <http://www.gnu.org/licenses/>.
 */

import android.content.Context;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
    private Util() {
        // Instance not allowed
    }

    public static String readAll(Context context, String path) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStreamReader reader = new InputStreamReader(context.openFileInput(path));
            int c = 0;
            while ((c = reader.read()) != -1) {
                stringBuilder.append((char) c);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return stringBuilder.toString();
    }

    public static void writeAll(Context context, String path, String string) throws IOException {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(context.openFileOutput(path, Context.MODE_PRIVATE)));
            writer.write(string);
            writer.close();
        } catch (FileNotFoundException e) {
            // TODO: Think about the case when a directory doesn't exist.
            e.printStackTrace();
            throw new RuntimeException("Shouldn't the openFileOutput() create one when file not found?", e);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static byte[] createChecksum(byte[] bytes, String encryption) throws NoSuchAlgorithmException {
        MessageDigest complete = MessageDigest.getInstance(encryption);
        complete.update(bytes);
        return complete.digest();
    }

    public static String getMD5Checksum(String string) {
        byte[] bytes;
        try {
            bytes = createChecksum(string.getBytes(), "MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("I believe there will an MD5 algorithm.");
        }
        StringBuilder result = new StringBuilder();

        for (byte b : bytes) {
            result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }
}
