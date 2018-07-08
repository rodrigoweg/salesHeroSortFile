package com.saleshero.sorter.service;

import com.saleshero.sorter.exception.FileManagementException;
import com.saleshero.sorter.service.Utils.ConstTest;
import com.saleshero.sorter.service.Utils.UtilsTest;
import com.saleshero.sorter.utils.FileGenerator;
import com.saleshero.sorter.utils.Utils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.Arrays;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

@RunWith(SpringRunner.class)
public class SortTest {

    File sourceFileTmp = null;

    @Before
    public void setup() throws FileManagementException {
        sourceFileTmp = UtilsTest.getFileFromClassPath(ConstTest.TEST_NUMBERS_TMP_FILE_PATH);
    }


    @Test
    public void basic() throws FileManagementException {
        int[] elements = {0,1,3,2,5,1,2,2};
        int NUMBER_OF_LINES = 4;
        File sourceFile = FileManager.arrayToFile(elements, sourceFileTmp);
        File result = Sorter.sortFile(sourceFile, NUMBER_OF_LINES);
        Assert.assertEquals("[0, 1, 1, 2, 2, 2, 3, 5]",Arrays.toString(FileManager.fileToArray(result)));
    }

    @Test
    public void basic2() throws FileManagementException {
        int[] elements = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 2, 4, 2, 3, 5, 21, 4, 5, 23, 23, 5, 23, 5,2,0,999999};
        int NUMBER_OF_LINES = 3;
        File result = Sorter.sortFile(FileManager.arrayToFile(elements, sourceFileTmp), NUMBER_OF_LINES);
        Assert.assertEquals("[0, 1, 2, 2, 2, 2, 3, 3, 4, 4, 4, 5, 5, 5, 5, 5, 6, 7, 8, 9, 10, 11, 12, 21, 23, 23, 23, 999999]",Arrays.toString(FileManager.fileToArray(result)));
    }

    @Test
    public void basic3() throws FileManagementException {
        int[] elements = {952596251, 684964922, 83205282, 879659927, 548469615, 56156459, 113425171, 595470910, 534426352, 864431367};
        int NUMBER_OF_LINES = 3;
        File result = Sorter.sortFile(FileManager.arrayToFile(elements, sourceFileTmp), NUMBER_OF_LINES);
        Assert.assertEquals("[56156459, 83205282, 113425171, 534426352, 548469615, 595470910, 684964922, 864431367, 879659927, 952596251]",Arrays.toString(FileManager.fileToArray(result)));
    }



    @Test
    public void dynamic() throws FileManagementException {
        TestCoupleValue values = SortTest.TestCoupleValue.getRandomArray(10);
        File elements = values.randomFile;
        int NUMBER_OF_LINES = 3;
        File result = Sorter.sortFile(elements, NUMBER_OF_LINES);
        Assert.assertEquals(values.orderedAsString,Arrays.toString(FileManager.fileToArray(result)));
    }

    @Test
    public void dynamicRandom() throws FileManagementException {
        Random rand = new Random();
        int randomNumberElements = rand.nextInt(10000);
        TestCoupleValue values = SortTest.TestCoupleValue.getRandomArray(randomNumberElements);
        File elements = values.randomFile;
        int NUMBER_OF_LINES = 1000;
        File result = Sorter.sortFile(elements, NUMBER_OF_LINES);
        Assert.assertEquals(values.orderedAsString,Arrays.toString(FileManager.fileToArray(result)));
    }

    @Test
    public void dynamicSmallFile() throws FileManagementException {
        //APROX 100.000 lines 1MB
        int NUMBER_OF_LINES = 100000;
        File elements = FileGenerator.getFile(ConstTest.TEST_FILES,1);
        File result = Sorter.sortFile(elements, NUMBER_OF_LINES);
    }

    //@Test
    public void dynamicBigFile() throws FileManagementException {
        //APROX 100.000 lines 1MB
        int NUMBER_OF_LINES = 1000000;
        File elements = FileGenerator.getFile(ConstTest.TEST_FILES,100);
        File result = Sorter.sortFile(elements, NUMBER_OF_LINES);
    }

    private static class TestCoupleValue {
        String orderedAsString;
        File randomFile;

        TestCoupleValue(String orderedAsString, File randomFile) {
            this.orderedAsString = orderedAsString;
            this.randomFile = randomFile;
        }

        static TestCoupleValue getRandomArray(int size) throws FileManagementException {

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
            File randomFile = FileManager.arrayToFile(randomArray,UtilsTest.getFileFromClassPath(ConstTest.TEST_NUMBERS_TMP_FILE_PATH));
            return new TestCoupleValue(orderedAsString, FileManager.arrayToFile(randomArray,randomFile));
        }
    }
}

