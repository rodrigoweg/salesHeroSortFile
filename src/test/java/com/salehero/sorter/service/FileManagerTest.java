package com.salehero.sorter.service;

import com.salehero.sorter.utils.Utils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RunWith(SpringRunner.class)
public class FileManagerTest {

    @Test
    public void split() {
        final String PATH = "files/numbers.txt";
        final String PATH_CHUNKS = "files/chunks";
        FileManager.fileSpliter(new File(Utils.getAbsolutPath(PATH)),5);
        Assert.assertTrue(new File(Utils.getAbsolutPath(PATH_CHUNKS)).listFiles()!=null &&
                new File(Utils.getAbsolutPath(PATH_CHUNKS)).listFiles().length > 0);
    }

    @Test
    public void join() {
        final String PATH = "files";
        final String PATH_CHUNKS = "files/chunks";
        FileManager.fileJoiner(Utils.getAbsolutPath(PATH),Utils.getAbsolutPath(PATH_CHUNKS));
        Assert.assertTrue(new File(Utils.getAbsolutPath(PATH)+ "/numbers.txt").exists());
    }

    @Test
    public void fileToArray() {
        final String PATH = "numbers.txt";
        int[] resutl = FileManager.fileToArray(new File(Utils.getAbsolutPath(PATH)));
        Assert.assertTrue(resutl!=null && resutl.length > 0);
    }

    @Test
    public void arrayToTest() throws IOException {
        final String PATH_RESULT = "files\\numbersTest.txt";
        final String PATH_ORIGIN = "numbers.txt";
        int[] data = FileManager.fileToArray(new File(Utils.getAbsolutPath(PATH_ORIGIN)));
        File file = FileManager.arrayToFile(data, Utils.getAbsolutPath(PATH_RESULT));
        Assert.assertTrue(file!=null && Files.size(file.toPath()) > 0);
    }


}
