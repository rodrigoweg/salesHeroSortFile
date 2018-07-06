package com.salehero.sorter.service;

import com.salehero.sorter.utils.Const;
import com.salehero.sorter.utils.Utils;
import org.codehaus.plexus.util.FileUtils;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class FileManager {


    public static File[] fileSpliter(File sourceFile, String PATH_CHUNKS, int chunkSize) {

        List<File> result = new ArrayList<>();
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            if(!new File(PATH_CHUNKS).exists()){
                new File(PATH_CHUNKS).mkdirs();
            }
            FileUtils.cleanDirectory(PATH_CHUNKS);
            FileInputStream fs = new FileInputStream(URLDecoder.decode(sourceFile.getPath(), "UTF-8"));
            BufferedReader br = new BufferedReader(new InputStreamReader(fs));

            int count = 0;
            int lineNumber = 0;
            fos = new FileOutputStream(Const.CHUNK_FILE_PATH + count + ".txt");
            bw = new BufferedWriter(new OutputStreamWriter(fos));
            result.add(new File(Const.CHUNK_FILE_PATH + count + ".txt"));
            String line = "";
            while ((line = br.readLine()) != null) {
                if (lineNumber!= 0 && lineNumber % chunkSize == 0) {
                    fos.close();
                    count++;
                    fos = new FileOutputStream(Const.CHUNK_FILE_PATH + count + ".txt");
                    bw = new BufferedWriter(new OutputStreamWriter(fos));
                    result.add(new File(Const.CHUNK_FILE_PATH + count + ".txt"));

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

        finally {
            close(fos);
            close(bw);
        }
        return result.toArray(new File[result.size()]);
    }

    public static File fileJoiner(File fileOutput, String PATH_CHUNKS) {

        FileOutputStream fos = null;
        BufferedWriter bufferWriter = null;
        FileInputStream fis = null;
        BufferedReader bufferReader = null;
        try {
            int count = 0;

            if (fileOutput.exists()) {
                PrintWriter writer = new PrintWriter(fileOutput);
                writer.print("");
                writer.close();
            }else{
                fileOutput.createNewFile();
            }

            fos = new FileOutputStream(Utils.getDecodedPath(fileOutput));
            bufferWriter = new BufferedWriter(new OutputStreamWriter(fos));

            File fileInput = new File(Const.CHUNK_FILE_PATH + count + ".txt");
            while (fileInput.exists()) {
                fis = new FileInputStream(Const.CHUNK_FILE_PATH + count + ".txt");
                bufferReader = new BufferedReader(new InputStreamReader(fis));
                String line = "";
                while ((line = bufferReader.readLine()) != null) {
                    bufferWriter.write(line);
                    bufferWriter.newLine();
                    bufferWriter.flush();
                }

                count++;
                fileInput = new File(Const.CHUNK_FILE_PATH + count + ".txt");
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        finally {
            close(fos);
            close(bufferWriter);
            close(fis);
            close(bufferReader);
        }

        return fileOutput;
    }

    static public int[] fileToArray(File file){
        List<Integer> data = new ArrayList<>();
        FileInputStream fis = null;
        BufferedReader bufferReader = null;
        try {
            fis = new FileInputStream(Utils.getDecodedPath(file));
            bufferReader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = bufferReader.readLine()) != null) {
                if(line!= null && line.length() > 0){
                    data.add(Integer.parseInt(line));
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            close(fis);
            close(bufferReader);
        }

        return data.stream().mapToInt(i -> i).toArray();
    }


    static public File arrayToFile(int[] data, File outputFile){

        FileOutputStream fos = null;
        BufferedWriter bufferedWriter = null;

        if (outputFile.exists()) {
            outputFile.delete();
        }
        try {
            fos = new FileOutputStream(outputFile);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fos));
            for(int i = 0; i < data.length;i++) {
                bufferedWriter.write(""+data[i]);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        finally {
            close(fos);
            close(bufferedWriter);
        }
        return outputFile;
    }

    public static void close(Closeable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (IOException e) {
            //log the exception
        }
    }
}
