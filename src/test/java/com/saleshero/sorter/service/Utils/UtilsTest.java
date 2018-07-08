package com.saleshero.sorter.service.Utils;

import com.saleshero.sorter.exception.FileManagementException;
import com.saleshero.sorter.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class UtilsTest {

    /**
     * @param path Path to the file
     * @return File in the class path
     * @throws FileManagementException Exception with info about the erros
     */
    static public File getFileFromClassPath(String path) throws FileManagementException {
        ClassLoader classLoader = Utils.class.getClassLoader();
        URL resource = classLoader.getResource(path);
        File file = null;

        if (path == null) {
            throw new FileManagementException("Path is null. Not possible to get file.");
        }

        try {
            if (resource == null) {
                file = new File(path);
                file.createNewFile();
            } else {
                file = new File(Utils.decodeUTF8(resource.getFile()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileManagementException("Error creating file.");
        }


        return file;
    }
}
