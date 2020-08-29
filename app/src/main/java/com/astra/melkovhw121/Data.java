package com.astra.melkovhw121;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Data {
    /**
     * Write content to file in private external storage
     * @param context context (Activity)
     * @param fileName file name
     * @param content content
     * @param overwrite flag: overwrite or append
     */
    public static void writeToPrivateExternalStorage(Context context, String fileName, String content, boolean overwrite) {
        if (isExternalStorageWritable()) {
            File file = new File(context.getExternalFilesDir(null), fileName);
            // init only in first time
            if(!overwrite && file.exists()) {
                return;
            }

            FileWriter writer = null;
            try {
                writer = new FileWriter(file, !overwrite);
                writer.write(content);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Read data from file in private external storage
     * @param context content (Activity)
     * @param fileName file name
     * @return file content
     */
    public static String readFromPrivateExternalStorage(Context context, String fileName) {
        StringBuilder result = new StringBuilder();

        if (isExternalStorageReadable()) {
            FileReader reader = null;
            try {
                File file = new File(context.getExternalFilesDir(null), fileName);
                reader = new FileReader(file);
                char[] charArray = new char[1024];
                reader.read(charArray);

                for(char character: charArray){
                    result.append(character);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result.toString();
    }

    /**
     * Checks if external storage is available for read and write
     * @return flag: storage ready to write
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if external storage is available to at least read
     * @return flag: storage ready to read
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
