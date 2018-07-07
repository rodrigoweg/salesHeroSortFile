package com.saleshero.sorter.service;

import com.saleshero.sorter.exception.FileManagementException;
import com.saleshero.sorter.service.Utils.ConstTest;
import com.saleshero.sorter.utils.Const;
import com.saleshero.sorter.utils.FileGenerator;
import com.saleshero.sorter.utils.Utils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@RunWith(SpringRunner.class)
public class FileManagerTest {

    File sourceFile = null;
    File sourceFileTmp = null;


    @Before
    public void setupFileManagerTest() throws IOException, FileManagementException {
        sourceFile = Utils.getFileFromClassPath(ConstTest.TEST_NUMBERS_FILE_PATH);
        sourceFileTmp = Utils.getFileFromClassPath(ConstTest.TEST_NUMBERS_TMP_FILE_PATH);
    }

    @Test
    public void split() throws FileManagementException {
        FileManager.fileSpliter(sourceFile,5);
        File chunkFolder = Utils.getFileFromClassPath(
                ConstTest.TEST_FILES +Const.CHUNK_FOLDER_PATH);
        Assert.assertTrue(chunkFolder!=null && chunkFolder.listFiles()!=null &&
                chunkFolder.listFiles().length > 0);
    }

    @Test
    public void join() throws FileManagementException {
        FileManager.fileSpliter(sourceFile,5);
        String filesPath = Utils.decodeUTF8(ConstTest.TEST_FILES +Const.CHUNK_FOLDER_PATH);
        FileManager.fileJoiner(filesPath);
        Assert.assertTrue(sourceFileTmp.exists());
    }

    @Test
    public void fileToArray() throws FileManagementException {
        int[] resutl = FileManager.fileToArray(sourceFile);
        Assert.assertTrue(resutl!=null && resutl.length > 0);
    }

    @Test
    public void arrayToFile() throws FileManagementException {
        int[] data = FileManager.fileToArray(sourceFile);
        File file = FileManager.arrayToFile(data, sourceFileTmp);
        int[] dataResponse = FileManager.fileToArray(file);
        System.out.println(Arrays.toString(data));
        System.out.println(Arrays.toString(dataResponse));
        Assert.assertTrue(Arrays.equals(data,dataResponse));
    }

    @Test
    public void creteBigFile() throws FileManagementException {
        FileGenerator.getFile(ConstTest.TEST_FILES,0.1);
    }


}
