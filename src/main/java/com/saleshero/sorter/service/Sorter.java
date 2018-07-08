package com.saleshero.sorter.service;

import com.saleshero.sorter.exception.FileManagementException;
import com.saleshero.sorter.utils.Const;
import com.saleshero.sorter.utils.Utils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.Arrays;

/**
 * This class provide the methods to order a file regardless its size.
 */
public class Sorter {

    static Logger log = LoggerFactory.getLogger(Sorter.class);

    /**
     * @param sourceFile Data to be ordered
     * @param chunkSizeLines  Number of lines able to manipulate due memory limitation
     * @return file with the sorted data
     */
    public static File sortFile(File sourceFile, int chunkSizeLines) throws FileManagementException {
        //Divide data in the size that could be processed (bulk size)
        long start = System.currentTimeMillis();
        log.debug("Chunk folder will be created in: "+Utils.getParentFolder(sourceFile));
        File[] chunkFiles = FileManager.fileSpliter(sourceFile, chunkSizeLines);

        //first order chunkFiles
        for (File chunkFile : chunkFiles) {
            int[] bulkArray = FileManager.fileToArray(chunkFile);
            //DualPivotQuicksort algorith used
            Arrays.sort(bulkArray);
            FileManager.arrayToFile(bulkArray, chunkFile);
        }
        long time = System.currentTimeMillis() - start;
        log.info("END SORTING CHUNKS. Time used: " + time / 1e3 + " seconds.");
        start = System.currentTimeMillis();

        boolean fullSortedFiles = false;
        int iteration = 0;

        while (!fullSortedFiles) {
            fullSortedFiles = true;

            //This for will iterate until all the blocks are ordered;
            // the condition is that the highest element from lower block is smaller than the smallest from the high block
            for (int i = 0; i < chunkFiles.length - 1; i++) {
                //Compared last element of low block with first of adjacent block (high)
                int[] lowBlock = FileManager.fileToArray(chunkFiles[i]);
                int[] highBlock = FileManager.fileToArray(chunkFiles[i + 1]);
                ;
                //while will be working until the 2 blocks are ordered
                while (lowBlock[lowBlock.length - 1] > highBlock[0]) {
                    //It is not possible to guaranty all blocks are sorted after block are reorder and mergerd
                    fullSortedFiles = false;

                    //Blocks are divided in subbloks of half size
                    int halfSize = (int) Math.ceil((double) chunkSizeLines / 2);
                    int[][] lowHalfSize = splitSubArray(lowBlock, halfSize);
                    int[][] highHalfSize = splitSubArray(highBlock, halfSize);

                    // merge and sort the high part of low block and low part of high block
                    // check if the last block only has one subblock
                    int[] temporaryMergedBlock = ArrayUtils.addAll(lowHalfSize.length > 1 ? lowHalfSize[1] : lowHalfSize[0], highHalfSize[0]);
                    Arrays.sort(temporaryMergedBlock);

                    //temporaryMergedBlock is divided 2
                    int mediumHalfSizes = (int) Math.ceil((double) temporaryMergedBlock.length / 2);
                    int[][] mediumHalfSizeElements = splitSubArray(temporaryMergedBlock, Math.min(halfSize, mediumHalfSizes));

                    //low block remains with the initial low part and the low part of the temporaryMergedBlock
                    if (lowHalfSize.length > 1) {
                        lowBlock = mergeOrderedArrays(lowHalfSize[0], mediumHalfSizeElements[0]);
                    } else {
                        lowBlock = mediumHalfSizeElements[0];
                    }

                    //high block contains the low part of the temporaryMergedBlock
                    // and the initial high part of the high block
                    highBlock = highHalfSize.length > 1 ? mergeOrderedArrays(mediumHalfSizeElements[1], highHalfSize[1]) : mediumHalfSizeElements[1];

                    //reassign temporary blocks to the general data Matrix
                    chunkFiles[i] = FileManager.arrayToFile(lowBlock, chunkFiles[i]);
                    chunkFiles[i + 1] = FileManager.arrayToFile(highBlock, chunkFiles[i + 1]);
                }
            }
            time = System.currentTimeMillis() - start;
            log.debug("ITERATION NUMBER: " + (iteration++) + ". Time used: " + time / 1e3 + " seconds.");
            //printMatrix(dataMatrix);
        }
        return FileManager.fileJoiner(Utils.decodeUTF8(Utils.getParentFolder(sourceFile) + File.separator + Const.CHUNK_FOLDER_PATH));
    }

    /**
     * Utility to print matrix data
     *
     * @param matrix
     */
    private static void printMatrix(int[][] matrix) {
        for (int[] sub : matrix) {
            log.info(Arrays.toString(sub));
        }
    }

    /**
     * This method merges two ordered arrays
     *
     * @param low  first array
     * @param high second array
     * @return merged array
     */
    public static int[] mergeOrderedArrays(int[] low, int[] high) {
        int[] result = new int[low.length + high.length];
        int i = low.length - 1, j = high.length - 1, k = result.length;

        //This while iterates from the end of arrays in parallel
        // and set biggest value at the end of merged block
        while (k > 0) {
            result[--k] =
                    (j < 0 || (i >= 0 && low[i] >= high[j])) ? low[i--] : high[j--];
        }
        return result;
    }

    /**
     * This method converts matrix into array
     *
     * @param matrix two dimension matrix
     * @return One dimension array
     */
    private static int[] joinMatrix(int[][] matrix) {
        int[] result = {};
        for (int[] sub : matrix) {
            result = ArrayUtils.addAll(result, sub);
        }
        return result;
    }

    /**
     * @param data      array to be split
     * @param chunkSize size of the chunks
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
