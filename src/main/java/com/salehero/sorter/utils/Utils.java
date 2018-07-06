package com.salehero.sorter.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;

public class Utils {

    static public File getFileClassPath(String path) throws IOException {
        ClassLoader classLoader = Utils.class.getClassLoader();
        URL resource = classLoader.getResource(path);
        File file = null;
        if(resource==null){
            file = new File(path);
            file.createNewFile();
        }else{
            file = new File(URLDecoder.decode(resource.getFile(), "UTF-8"));
        }



        return file;
    }

    static public String getDecodedPath(File file) throws UnsupportedEncodingException {
        return URLDecoder.decode(file.getPath(), "UTF-8");
    }
}
