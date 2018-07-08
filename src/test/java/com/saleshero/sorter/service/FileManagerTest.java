package com.saleshero.sorter.service;

import com.saleshero.sorter.exception.FileManagementException;
import com.saleshero.sorter.service.Utils.ConstTest;
import com.saleshero.sorter.service.Utils.UtilsTest;
import com.saleshero.sorter.utils.Const;
import com.saleshero.sorter.utils.FileGenerator;
import com.saleshero.sorter.utils.Utils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@RunWith(SpringRunner.class)
public class FileManagerTest {

    static Logger log = LoggerFactory.getLogger(FileManagerTest.class);

    File sourceFile = null;
    File sourceFileTmp = null;

    @Before
    public void setupFileManagerTest() throws IOException, FileManagementException {
        sourceFile = UtilsTest.getFileFromClassPath(ConstTest.TEST_NUMBERS_FILE_PATH);
        sourceFileTmp = UtilsTest.getFileFromClassPath(ConstTest.TEST_NUMBERS_TMP_FILE_PATH);
    }

    @Test
    public void split() throws FileManagementException {
        FileManager.fileSpliter(sourceFile,5);
        File chunkFolder = UtilsTest.getFileFromClassPath(
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
        FileGenerator.getFile(ConstTest.TEST_FILES,1);
        File file = new File(ConstTest.TEST_FILES+Const.BIG_FILE_PATH);
        Assert.assertTrue(file.exists() && file.length() > 0);
    }

    @Test
    public void parentPathFile() throws IOException, FileManagementException {
        File file = new File("pom.xml");
        String parent = (Utils.getParentFolder(file));
        File parentFile = new File(parent);
        Assert.assertTrue(parentFile.exists() );
    }


}
