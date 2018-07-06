package com.salehero.sorter.service;

import com.salehero.sorter.utils.Const;
import com.salehero.sorter.utils.FileGenerator;
import com.salehero.sorter.utils.Utils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

@RunWith(SpringRunner.class)
public class SortTest {

    File sourceFileTmp = Utils.getFileClassPath(Const.NUMBERS_TMP_FILE_PATH);

    public SortTest() throws IOException {
    }

    @Test
    public void basic() throws UnsupportedEncodingException {
        int[] elements = {0,1,3,2,5,1,2,2};
        int BULKSIZE = 4;
        File sourceFile = FileManager.arrayToFile(elements, sourceFileTmp);
        File result = Sorter.sortFile(sourceFile, BULKSIZE);
        Assert.assertEquals("[0, 1, 1, 2, 2, 2, 3, 5]",Arrays.toString(FileManager.fileToArray(result)));
    }

    @Test
    public void basic2() throws UnsupportedEncodingException {
        int[] elements = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 2, 4, 2, 3, 5, 21, 4, 5, 23, 23, 5, 23, 5,2,0,999999};
        int BULKSIZE = 3;
        File result = Sorter.sortFile(FileManager.arrayToFile(elements, sourceFileTmp), BULKSIZE);
        Assert.assertEquals("[0, 1, 2, 2, 2, 2, 3, 3, 4, 4, 4, 5, 5, 5, 5, 5, 6, 7, 8, 9, 10, 11, 12, 21, 23, 23, 23, 999999]",Arrays.toString(FileManager.fileToArray(result)));
    }

    @Test
    public void basic3() throws UnsupportedEncodingException {
        int[] elements = {952596251, 684964922, 83205282, 879659927, 548469615, 56156459, 113425171, 595470910, 534426352, 864431367};
        int BULKSIZE = 3;
        File result = Sorter.sortFile(FileManager.arrayToFile(elements, sourceFileTmp), BULKSIZE);
        Assert.assertEquals("[56156459, 83205282, 113425171, 534426352, 548469615, 595470910, 684964922, 864431367, 879659927, 952596251]",Arrays.toString(FileManager.fileToArray(result)));
    }



    @Test
    public void dynamic() throws IOException {
        TestCoupleValue values = SortTest.TestCoupleValue.getRandomArray(10);
        File elements = values.randomFile;
        int BULKSIZE = 3;
        File result = Sorter.sortFile(elements, BULKSIZE);
        Assert.assertEquals(values.orderedAsString,Arrays.toString(FileManager.fileToArray(result)));
    }

    @Test
    public void dynamicRandom() throws IOException {
        Random rand = new Random();
        int randomNumberElements = rand.nextInt(10000);
        TestCoupleValue values = SortTest.TestCoupleValue.getRandomArray(randomNumberElements);
        File elements = values.randomFile;
        int BULKSIZE = 1000;
        File result = Sorter.sortFile(elements, BULKSIZE);
        Assert.assertEquals(values.orderedAsString,Arrays.toString(FileManager.fileToArray(result)));
    }

    @Test
    public void dynamicBigFile() throws IOException {
        int BULKSIZE = 999999;
        File elements = FileGenerator.getFile(0.1);
        File result = Sorter.sortFile(elements, BULKSIZE);
    }

    private static class TestCoupleValue {
        String orderedAsString;
        File randomFile;

        TestCoupleValue(String orderedAsString, File randomFile) {
            this.orderedAsString = orderedAsString;
            this.randomFile = randomFile;
        }

        static TestCoupleValue getRandomArray(int size) throws IOException {

            SortedSet set = new TreeSet();
            int[] randomArray = new int[size];
            for (int i = 0; i < size; i++) {
                Random rand = new Random();
                int value = rand.nextInt(999999999);
                randomArray[i] = value;
                set.add(value);
            }
            int[] orderedArray = Arrays.stream(set.toArray()).mapToInt(o -> (int)o).toArray();
            String orderedAsString = Arrays.toString(orderedArray);
            File randomFile = FileManager.arrayToFile(randomArray,Utils.getFileClassPath(Const.NUMBERS_TMP_FILE_PATH));
            return new TestCoupleValue(orderedAsString, FileManager.arrayToFile(randomArray,randomFile));
        }
    }
}

