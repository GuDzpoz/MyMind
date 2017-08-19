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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
    private Util() {
        // Instance not allowed
    }

    public static void directorySaveCopy(File src, File dst) throws IOException {
        dst.getParentFile().mkdirs();
        copy(src, dst);
    }

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    public static String readLocalAll(Context context, String fileName) throws IOException {
        try {
            return read(context.openFileInput(fileName));
        } catch (FileNotFoundException e) {
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void writeLocalAll(Context context, String fileName, String string) throws IOException {
        write(context.openFileOutput(fileName, Context.MODE_PRIVATE), string);
    }

    public static String readAll(String path) throws IOException {
        try {
            return read(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void writeAll(String path, String string) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            try {
                write(new FileOutputStream(path), string);
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        } else {
            file.getParentFile().mkdirs();
            try {
                write(new FileOutputStream(path), string);
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    public static String read(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(inputStream);
        int c = 0;
        try {
            while ((c = reader.read()) != -1) {
                stringBuilder.append((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return stringBuilder.toString();
    }

    public static void write(OutputStream outputStream, String string) throws FileNotFoundException, IOException {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(string);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
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
