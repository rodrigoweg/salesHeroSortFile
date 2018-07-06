package com.salehero.sorter.service;

import com.salehero.sorter.utils.Const;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;

public class Sorter {

    /**
     *
     * @param sourceFile Data to be ordered
     * @param bulkSize Size able to manipulate due memory limitation
     * @return
     */
    static File sortFile(File sourceFile, int bulkSize) throws UnsupportedEncodingException {
        //Divide data in the size that could be processed
        long start = System.currentTimeMillis();
        String PATH_CHUNKS_FOLDER = URLDecoder.decode(Const.CHUNK_FOLDER_PATH, "UTF-8");
        File[] dataFiles = FileManager.fileSpliter(sourceFile,PATH_CHUNKS_FOLDER, bulkSize);
        //Blocks first order subFiles

        for (int i = 0; i < dataFiles.length; i++) {
            File bulkFile = dataFiles[i];
            int[] bulkArray = FileManager.fileToArray(bulkFile);
            //DualPivotQuicksort algorith used
            Arrays.sort(bulkArray);
            FileManager.arrayToFile(bulkArray,bulkFile);
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("END SORTING CHUNKS. Time used: "+time/ 1e3+" seconds.");
        boolean ordered = false;
        int  iteration = 0;
        start = System.currentTimeMillis();
        while(!ordered){
            ordered = true;
            time = System.currentTimeMillis() - start;
            System.out.println("ITERATION NUMBER: "+(iteration++)+". Time used: "+time/ 1e3+" seconds.");
            //This for will iterate until all the blocks are ordered;
            // the condition is that the highest element from lower block is smaller than the smallest from the high block
            for (int i = 0; i < dataFiles.length - 1; i++) {
                //Blocks ar
                int[] low = FileManager.fileToArray(dataFiles[i]);
                int[] high = FileManager.fileToArray(dataFiles[i + 1]);;
                //while will be working until the 2 blocks are ordered
                while (low[low.length - 1] > high[0]) {
                    //It is not possible to guaranty all blocks are sorted after block are reorder and mergerd
                    ordered = false;

                    //Blocks are divided in 2 (round above number)
                    int halfSize = (int)Math.ceil((double)bulkSize/2);
                    int[][] lowHalfSize = splitSubArray(low, halfSize);
                    int[][] highHalfSize = splitSubArray(high, halfSize);


                    // merge and sort the high part of low block and low part of high block
                    int[] medium = ArrayUtils.addAll(lowHalfSize.length > 1 ?lowHalfSize[1]: lowHalfSize[0],highHalfSize[0]);
                    Arrays.sort(medium);

                    //This medium merged bloc is divided 2
                    int mediumHalfSizes = (int)Math.ceil((double)medium.length/2);;
                    int[][] mediumHalfSizeElements = splitSubArray(medium, Math.min(halfSize,mediumHalfSizes));

                    //low block remains with the initial low part and the low part of the medium merged block is added
                    if(lowHalfSize.length > 1){
                        low = mergeOrderedArrays(lowHalfSize[0], mediumHalfSizeElements[0]);
                    }else{
                        low = mediumHalfSizeElements[0];
                    }

                    //high block contains the low part of the medium merged block and the initial high part of the high block
                    high = highHalfSize.length > 1 ? mergeOrderedArrays(mediumHalfSizeElements[1], highHalfSize[1]) : mediumHalfSizeElements[1];

                    //reassign temporary blocks to the general data Matrix
                    dataFiles[i] = FileManager.arrayToFile(low,dataFiles[i]);
                    dataFiles[i + 1] = FileManager.arrayToFile(high,dataFiles[i + 1]);
                }
            }
            //printMatrix(dataMatrix);
        }
        File outPutFile = new File(Const.OUTPUT_FILE_PATH);
        return  FileManager.fileJoiner(outPutFile,Const.CHUNK_FILE_PATH);
    }

    /**
     * Utility to print matrix data
     * @param matrix
     */
    private static void printMatrix(int[][] matrix) {
        for (int[] sub : matrix) {
            System.out.println(Arrays.toString(sub));
        }
    }


    /**
     * This method merges two ordered arrays
     *
     * @param low first array
     * @param high second array
     * @return merged array
     */
    public static int[] mergeOrderedArrays(int[] low, int[] high) {
        int[] result = new int[low.length + high.length];
        int i = low.length - 1, j = high.length - 1, k = result.length;

        //This while iterates from the end of arrays in parallel
        // and set biggest value at the end of merged block
        while (k > 0){
            result[--k] =
                    (j < 0 || (i >= 0 && low[i] >= high[j])) ? low[i--] : high[j--];
        }

        return result;
    }

    /**
     *
     * This method converts matrix into array
     *
     * @param matrix two dimension matrix
     * @return One dimension array
     */
    private static int[] joinMatrix(int[][] matrix) {
        int[] result = {};
        for (int[] sub : matrix) {
            result = ArrayUtils.addAll(result,sub);
        }
        return result;
    }

    /**
     *
     * @param data array to be split
     * @param chunkSize size of the chunks
     *
     * @return Matrix with subarrays of the chunk size
     */
    public static int[][] splitSubArray(final int[] data, final int chunkSize) {
        final int length = data.length;
        final int[][] dest = new int[(length + chunkSize - 1) / chunkSize][];
        int destIndex = 0;
        int stopIndex = 0;

        for (int startIndex = 0; startIndex + chunkSize <= length; startIndex += chunkSize) {
            stopIndex += chunkSize;
            dest[destIndex++] = Arrays.copyOfRange(data, startIndex, stopIndex);
        }

        if (stopIndex < length)
            dest[destIndex] = Arrays.copyOfRange(data, stopIndex, length);

        return dest;
    }

}


