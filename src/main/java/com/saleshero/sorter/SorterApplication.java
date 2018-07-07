package com.saleshero.sorter;

import com.saleshero.sorter.exception.FileManagementException;
import com.saleshero.sorter.service.FileManager;
import com.saleshero.sorter.service.Sorter;
import com.saleshero.sorter.utils.Const;
import org.junit.Assert;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Arrays;
import java.util.Random;

@SpringBootApplication
public class SorterApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(SorterApplication.class, args);
            Random rand = new Random();
            int BULKSIZE = 1000;
            //Sorter.sortFile(new File(Const.NUMBERS_FILE_PATH), BULKSIZE);
            Sorter.sortFile(new File(""), BULKSIZE);
        } catch (FileManagementException e) {
            e.printStackTrace();
        }
    }
}
