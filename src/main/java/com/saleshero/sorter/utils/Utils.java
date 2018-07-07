package com.saleshero.sorter.utils;

import com.saleshero.sorter.exception.FileManagementException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

public class Utils {

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
                file = new File(decodeUTF8(resource.getFile()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileManagementException("Error creating file.");
        }


        return file;
    }

    /**
     * @param file File in the classpath
     * @return String with path to the file
     * @throws FileManagementException
     */
    static public String getDecodedPath(File file) throws FileManagementException {
        if (file == null) {
            throw new FileManagementException("File is null. Not posible to decode path.");
        }
        return decodeUTF8(file.getPath());
    }

    /**
     * @param path path to be decoded
     * @return Decoded path
     * @throws FileManagementException
     */
    static public String decodeUTF8(String path) throws FileManagementException {
        try {
            return URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new FileManagementException("Error decoding path in UTF-8: " + path, e);
        }
    }
}
