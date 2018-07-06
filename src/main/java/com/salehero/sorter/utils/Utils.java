package com.salehero.sorter.utils;

import java.io.File;

public class Utils {
    public static final String TMP_CHUNK_FILE_NAME = "/chunkFile_";
    public static final String CHUNK_FOLDER_NAME = "/chunks";

    static public String getAbsolutPath(String path){
        ClassLoader classLoader = Utils.class.getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());
        return file.getAbsolutePath();
    }
}
