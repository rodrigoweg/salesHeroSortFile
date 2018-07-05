package com.salehero.sorter.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

@RunWith(SpringRunner.class)
public class SortTest {


    @Test
    public void basic() {
        int[] elements = {0,1,3,2,5,1,2,2};
        int BULKSIZE = 4;
        int[] result = Sorter.sort(elements, BULKSIZE);
        System.out.println(Arrays.toString(result));
        Assert.assertEquals("[0, 1, 1, 2, 2, 2, 3, 5]",Arrays.toString(result));
    }

    @Test
    public void basic2() {
        int[] elements = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 2, 4, 2, 3, 5, 21, 4, 5, 23, 23, 5, 23, 5,2,0,999999};
        int BULKSIZE = 3;
        int[] result = Sorter.sort(elements, BULKSIZE);
        System.out.println(Arrays.toString(result));
        Assert.assertEquals("[0, 1, 2, 2, 2, 2, 3, 3, 4, 4, 4, 5, 5, 5, 5, 5, 6, 7, 8, 9, 10, 11, 12, 21, 23, 23, 23, 999999]",Arrays.toString(result));
    }

    @Test
    public void dinamic() {
        TestCoupleValue values = SortTest.TestCoupleValue.getRandomArray(10);
        int[] elements = values.randomArray;
        System.out.println("Original: "+Arrays.toString(elements));
        int BULKSIZE = 3;
        int[] result = Sorter.sort(elements, BULKSIZE);
        System.out.println("Result: "+Arrays.toString(result));
        Assert.assertEquals(values.orderedAsString,Arrays.toString(result));
    }


    @Test
    public void dinamicBig() {
        TestCoupleValue values = SortTest.TestCoupleValue.getRandomArray(10000);
        int[] elements = values.randomArray;
        System.out.println("Original: "+Arrays.toString(elements));
        int BULKSIZE = 1000;
        int[] result = Sorter.sort(elements, BULKSIZE);
        System.out.println("Result: "+Arrays.toString(result));
        Assert.assertEquals(values.orderedAsString,Arrays.toString(result));
    }

    @Test
    public void dinamicVeryBig() {
        TestCoupleValue values = SortTest.TestCoupleValue.getRandomArray(99999999);
        int[] elements = values.randomArray;
        System.out.println("Original: "+Arrays.toString(elements));
        int BULKSIZE = 9999999;
        int[] result = Sorter.sort(elements, BULKSIZE);
        System.out.println("Result: "+Arrays.toString(result));
        Assert.assertEquals(values.orderedAsString,Arrays.toString(result));
    }

    private static class TestCoupleValue {
        String orderedAsString;
        int[] randomArray;

        TestCoupleValue(String orderedAsString, int[] randomArray) {
            this.orderedAsString = orderedAsString;
            this.randomArray = randomArray;
        }

        static TestCoupleValue getRandomArray(int size) {

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
            System.out.println("Size ordered: "+orderedArray.length+" size random: "+randomArray.length);
            return new TestCoupleValue(orderedAsString, (int[])randomArray);
        }
    }
}

