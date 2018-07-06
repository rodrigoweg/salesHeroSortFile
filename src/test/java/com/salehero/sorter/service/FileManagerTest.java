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
import java.net.URLDecoder;
import java.util.Arrays;

@RunWith(SpringRunner.class)
public class FileManagerTest {

    File sourceFile = Utils.getFileClassPath(Const.NUMBERS_FILE_PATH);
    File sourceFileTmp = Utils.getFileClassPath(Const.NUMBERS_TMP_FILE_PATH);
    String PATH_CHUNKS = Const.CHUNK_FOLDER_PATH;

    public FileManagerTest() throws IOException {
    }

    @Test
    public void split() throws UnsupportedEncodingException {
        FileManager.fileSpliter(sourceFile,PATH_CHUNKS,5);
        File chunkFolder = new File(URLDecoder.decode(PATH_CHUNKS, "UTF-8"));
        Assert.assertTrue(chunkFolder!=null && chunkFolder.listFiles()!=null &&
                chunkFolder.listFiles().length > 0);
    }

    @Test
    public void join() throws UnsupportedEncodingException {
        FileManager.fileSpliter(sourceFile,PATH_CHUNKS,5);
        FileManager.fileJoiner(sourceFileTmp,PATH_CHUNKS);
        Assert.assertTrue(sourceFileTmp.exists());
    }

    @Test
    public void fileToArray() throws UnsupportedEncodingException {
        int[] resutl = FileManager.fileToArray(sourceFile);
        Assert.assertTrue(resutl!=null && resutl.length > 0);
    }

    @Test
    public void arrayToFile() throws IOException {
        int[] data = FileManager.fileToArray(sourceFile);
        File file = FileManager.arrayToFile(data, sourceFileTmp);
        int[] dataResponse = FileManager.fileToArray(file);
        System.out.println(Arrays.toString(data));
        System.out.println(Arrays.toString(dataResponse));
        Assert.assertTrue(Arrays.equals(data,dataResponse));
    }

    @Test
    public void creteBigFile() throws IOException {
        FileGenerator.getFile(0.1);
    }


}
