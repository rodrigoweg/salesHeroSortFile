package com.saleshero.sorter.service;

import com.saleshero.sorter.exception.FileManagementException;
import com.saleshero.sorter.utils.Const;
import com.saleshero.sorter.utils.Utils;
import org.codehaus.plexus.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class helps to manage the files operations involved in the process of sort a big file
 */
public class FileManager {

    static Logger log = LoggerFactory.getLogger(FileManager.class);

    /**
     * @param sourceFile Source file to be split
     * @param chunkSize Size of chunds
     * @return List of files with content of the source file
     * @throws FileManagementException Exception with the info about erros
     */
    public static File[] fileSpliter(File sourceFile, int chunkSize) throws FileManagementException {

        if(sourceFile == null){
            throw new FileManagementException("File is null. Not possible to split.");
        }

        String outputPath = Utils.decodeUTF8(sourceFile.getParent()+Const.CHUNK_FOLDER_PATH);
        List<File> result = new ArrayList<>();
        BufferedWriter chunkWriter = null;
        BufferedReader sourceFileReader = null;
        FileInputStream sourceFileInputStream = null;
        try {
            prepareDestinationFolder(outputPath);
            sourceFileInputStream = new FileInputStream(Utils.getFileFromClassPath(sourceFile.getPath()));
            sourceFileReader = new BufferedReader(new InputStreamReader(sourceFileInputStream));

            int fileNumber = 0;
            File chunkFile = Utils.getFileFromClassPath(getFileName(outputPath,fileNumber));
            chunkWriter = getBufferWriterResetFile(chunkFile);
            result.add(chunkFile);

            //Reading the entire source file
            int lineNumber = 0;
            String line = "";
            while ((line = sourceFileReader.readLine()) != null) {
                //When number of lines read equals to chunksize, new file es created
                if (lineNumber != 0 && lineNumber % chunkSize == 0) {
                    close(chunkWriter);
                    fileNumber++;
                    chunkFile = Utils.getFileFromClassPath(getFileName(outputPath,fileNumber));
                    chunkWriter = getBufferWriterResetFile(chunkFile);
                    result.add(chunkFile);

                    chunkWriter.write(line);
                    chunkWriter.newLine();
                    chunkWriter.flush();
                    lineNumber++;
                } else {
                    chunkWriter.write(line);
                    chunkWriter.newLine();
                    chunkWriter.flush();
                    lineNumber++;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new FileManagementException("Source file not found: ",e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileManagementException("Error reading file: ",e);
        } finally {
            close(sourceFileInputStream);
            close(sourceFileReader);
            close(chunkWriter);
        }
        return result.toArray(new File[result.size()]);
    }

    /**
     *
     * @param PATH_CHUNKFILES_FILE Path to files folder
     * @return File with the content of joining all files in the folder
     * @throws FileManagementException Exception with info about error processing the join of files
     */
    public static File fileJoiner(String PATH_CHUNKFILES_FILE) throws FileManagementException {

        if (PATH_CHUNKFILES_FILE == null) {
            throw new FileManagementException("File output is null. Please provide file output for join chunk files");
        }

        BufferedWriter outputBufferWriter = null;
        FileInputStream chunkFileInputStream = null;
        BufferedReader chunkFilebufferReader = null;
        File fileOutput = null;

        try {
            //get bufferWriter from fileOutput. Clean content if file exist or create new one
            fileOutput = Utils.getFileFromClassPath((PATH_CHUNKFILES_FILE+Const.CHUNK_OUTPUT_FILE_NAME));
            outputBufferWriter = getBufferWriterResetFile(fileOutput);

            int fileNumber = 0;
            File chunkfile = new File(getFileName(PATH_CHUNKFILES_FILE,fileNumber));
            if (!chunkfile.exists()) {
                throw new FileManagementException("No file found to join in path: " + chunkfile.getAbsolutePath());
            }
            // This while read all files in the chunk folder until file cant be find due to file number
            while (chunkfile.exists()) {
                chunkFileInputStream = new FileInputStream(getFileName(PATH_CHUNKFILES_FILE,fileNumber));
                chunkFilebufferReader = new BufferedReader(new InputStreamReader(chunkFileInputStream));
                copyFile(outputBufferWriter, chunkFilebufferReader);
                fileNumber++;
                chunkfile = new File(getFileName(PATH_CHUNKFILES_FILE,fileNumber));
                close(chunkFileInputStream);
                close(chunkFilebufferReader);
            }

        } catch (FileNotFoundException e) {
            //this exception should never happen. File already checked if exist.
            e.printStackTrace();
            throw new FileManagementException("Unknown error with chunk files in: " + PATH_CHUNKFILES_FILE+Const.CHUNK_FILE_PATH);
        } finally {
            //close files
            close(outputBufferWriter);
            close(chunkFileInputStream);
            close(chunkFilebufferReader);
        }
        return fileOutput;
    }

    /**
     * @param file File to be converted in array
     * @return Array with content of file
     * @throws FileManagementException
     */
    static public int[] fileToArray(File file) throws FileManagementException {
        List<Integer> data = new ArrayList<>();
        FileInputStream fis = null;
        BufferedReader bufferReader = null;
        try {
            fis = new FileInputStream(Utils.getDecodedPath(file));
            bufferReader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = bufferReader.readLine()) != null) {
                if (line != null && line.length() > 0) {
                    data.add(Integer.parseInt(line));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new FileManagementException("File not found in classpath: ",e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileManagementException("Error reading file: ",e);
        } finally {
            close(fis);
            close(bufferReader);
        }
        return data.stream().mapToInt(i -> i).toArray();
    }

    /**
     * @param data Array with data
     * @param outputFile File where data should be placed
     * @return File with data
     * @throws FileManagementException
     */
    static public File arrayToFile(int[] data, File outputFile) throws FileManagementException {

        FileOutputStream fos = null;
        BufferedWriter bufferedWriter = null;

        if (outputFile.exists()) {
            outputFile.delete();
        }
        try {
            fos = new FileOutputStream(outputFile);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fos));
            for (int i = 0; i < data.length; i++) {
                bufferedWriter.write("" + data[i]);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new FileManagementException("File not found in classpath: ",e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileManagementException("Error writing file: ",e);
        } finally {
            close(fos);
            close(bufferedWriter);
        }
        return outputFile;
    }

    /**
     * @param PATH_CHUNKS Path to chunk folder
     * @throws FileManagementException
     */
    private static void prepareDestinationFolder(String PATH_CHUNKS) throws FileManagementException {
        if (!Utils.getFileFromClassPath(PATH_CHUNKS).exists()) {
            Utils.getFileFromClassPath(PATH_CHUNKS).mkdirs();
        }
        //Directory is cleaned before split the file
        try {
            FileUtils.cleanDirectory(PATH_CHUNKS);
        } catch (IOException e) {
            throw new FileManagementException("Error cleaning folder: "+PATH_CHUNKS,e);
        }
    }

    /**
     *
     * @param outputWriter Writer of output file
     * @param sourceReader Reader of source file
     * @throws FileManagementException Exception with info about errors coping files
     */
    private static void copyFile(BufferedWriter outputWriter, BufferedReader sourceReader) throws FileManagementException {
        try {
            String line = "";
            while ((line = sourceReader.readLine()) != null) {
                outputWriter.write(line);
                outputWriter.newLine();
                outputWriter.flush();
            }
        } catch (IOException e) {
            throw new FileManagementException("Error coping chunk file into output.", e);
        }
    }

    /**
     * @param fileNumber Number of chunk
     * @return File with number of the chunk
     */
    private static String getFileName(String rootPath,int fileNumber) {
        return rootPath+Const.CHUNK_FILE_PATH + fileNumber + ".txt";
    }

    /**
     * @param fileOutput File to be clean
     * @return Writer of new / empty file
     * @throws FileManagementException Exception with info about errors getting the Writer
     */
    private static BufferedWriter getBufferWriterResetFile(File fileOutput) throws FileManagementException {
        try {
            if(fileOutput == null){
                throw new FileManagementException("File is null");
            }
            if(fileOutput.isDirectory()){
                //This method is meant for files only
                throw new FileManagementException("Error: File is directory.");
            }
            if (fileOutput.exists()) {
                // Empty the file if exist
                PrintWriter writer = new PrintWriter(fileOutput);
                writer.print("");
                writer.close();
            } else {
                fileOutput.createNewFile();
            }
            FileOutputStream outputFileStream = new FileOutputStream(Utils.getDecodedPath(fileOutput));
            return new BufferedWriter(new OutputStreamWriter(outputFileStream));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new FileManagementException("OutputFile exist but can not be found: " + fileOutput.getPath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileManagementException("Error creating outputFile: " + fileOutput.getPath());
        }

    }

    /**
     *
     * @param c Object to be closed
     */
    private static void close(Closeable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (IOException e) {
            //log the exception
        }
    }
}
