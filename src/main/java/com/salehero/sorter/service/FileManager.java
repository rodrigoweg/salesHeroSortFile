package com.salehero.sorter.service;

import com.salehero.sorter.utils.Utils;
import org.codehaus.plexus.util.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {


    public static File[] fileSpliter(File sourceFile,int chunkSize) {

        List<File> result = new ArrayList<>();
        String PATH_CHUNKS = sourceFile.getParent()+Utils.CHUNK_FOLDER_NAME;
        String line = "";
        try {
            if(!new File(PATH_CHUNKS).exists()){
                new File(PATH_CHUNKS).mkdirs();
            }
            FileUtils.cleanDirectory(PATH_CHUNKS);
            FileInputStream fs = new FileInputStream(sourceFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fs));

            int count = 0;
            int lineNumber = 0;
            FileOutputStream fos = new FileOutputStream(PATH_CHUNKS + Utils.TMP_CHUNK_FILE_NAME + count + ".txt");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            result.add(new File(PATH_CHUNKS + Utils.TMP_CHUNK_FILE_NAME + count + ".txt"));
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if (lineNumber!= 0 && lineNumber % chunkSize == 0) {
                    fos.close();
                    count++;
                    fos = new FileOutputStream(PATH_CHUNKS + Utils.TMP_CHUNK_FILE_NAME + count + ".txt");
                    bw = new BufferedWriter(new OutputStreamWriter(fos));
                    result.add(new File(PATH_CHUNKS + Utils.TMP_CHUNK_FILE_NAME + count + ".txt"));

                    bw.write(line);
                    bw.newLine();
                    bw.flush();
                    lineNumber++;
                } else {
                    bw.write(line);
                    bw.newLine();
                    bw.flush();
                    lineNumber++;
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toArray(new File[result.size()]);
    }

    public static File fileJoiner(String PATH_JOIN, String PATH_CHUNKS) {

        File fileOutput = null;
        try {
            int count = 0;

            fileOutput = new File(PATH_JOIN);
            if (fileOutput.exists()) {
                fileOutput.delete();
            }
            FileOutputStream fos = new FileOutputStream(PATH_JOIN);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            File fileInput = new File(PATH_CHUNKS + Utils.TMP_CHUNK_FILE_NAME + count + ".txt");
            while (fileInput.exists()) {
                FileInputStream fs = new FileInputStream(PATH_CHUNKS + Utils.TMP_CHUNK_FILE_NAME + count + ".txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(fs));
                String line = "";
                while ((line = br.readLine()) != null) {
                    bw.write(line);
                    bw.newLine();
                    bw.flush();
                }

                count++;
                fileInput = new File(PATH_CHUNKS + Utils.TMP_CHUNK_FILE_NAME + count + ".txt");
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileOutput;
    }

    static public int[] fileToArray(File file){
        List<Integer> data = new ArrayList<>();

        try {
            FileInputStream fs = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fs));
            String line;
            while ((line = br.readLine()) != null) {
                data.add(Integer.parseInt(line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return data.stream().mapToInt(i -> i).toArray();
    }


    static public File arrayToFile(int[] data, String PATH){
        File file = new File(PATH);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fos = new FileOutputStream(PATH);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            for(int i = 0; i < data.length;i++) {
                bw.write(""+data[i]);
                bw.newLine();
                bw.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

}
