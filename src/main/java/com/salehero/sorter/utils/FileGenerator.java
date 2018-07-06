package com.salehero.sorter.utils;

import java.io.*;
import java.util.Random;

public class FileGenerator {

    public static File getFile(double sizeGB) throws IOException {
        Random random = new Random();
        File file = Utils.getFileClassPath(Const.BIG_FILE_PATH);
        file.createNewFile();
        long start = System.currentTimeMillis();
        PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8")), false);
        int counter = 0;
        while (true) {
            String sep = "";
            for (int i = 0; i < 100; i++) {
                int number = random.nextInt(9999999) + 1;
                writer.print(sep);
                writer.print(number+"\n");
            }
            writer.println();
            //Check to see if the current size is what we want it to be
            if (++counter == 20000) {
                System.out.printf("Size: %.3f GB%n", file.length() / 1e9);
                if (file.length() >= sizeGB * 1e9) {
                    writer.close();
                    break;
                } else {
                    counter = 0;
                }
            }
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("Took "+time/ 1e3+" seconds to create a file of "+file.length() / 1e9 +" GBs");

       return file;
    }
}
