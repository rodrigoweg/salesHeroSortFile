package com.saleshero.sorter.utils;

import com.saleshero.sorter.exception.FileManagementException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.FileAlreadyExistsException;

public class Utils {
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

    static public String getParentFolder(File file) throws FileManagementException {
        if(file == null){
            throw new FileManagementException("File is null. Not possible to get parent folder");
        }

        if(file.getParent()!=null){
            return Utils.decodeUTF8(file.getParent());
        }

        String absolutePath = file.getAbsolutePath();
        String parentPath = absolutePath.substring(0,absolutePath.lastIndexOf(File.separatorChar));

        if(!new File(parentPath).exists()){
            throw new FileManagementException("Unknown error getting parent folder: "+absolutePath
                    +". Path obtained: "+parentPath);
        }

        return parentPath;
    }
}
