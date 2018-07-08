package com.saleshero.sorter;

import com.saleshero.sorter.exception.FileManagementException;
import com.saleshero.sorter.service.FileManager;
import com.saleshero.sorter.service.Sorter;
import com.saleshero.sorter.utils.Const;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;

@SpringBootApplication
public class SorterApplication {

    static Logger log = LoggerFactory.getLogger(SorterApplication.class);

    public static void main(String[] args) {
        try{
            SpringApplication.run(SorterApplication.class, args);
            log.debug("args[0]: "+args[0]+" args[1]: "+args[1]);
            String fileName = args[0];
            int numberLines = Integer.parseInt(args[1]);
            File file = new File(fileName);
            if(!file.exists()){
                throw new FileNotFoundException();
            }
            Sorter.sortFile(file,numberLines);
        }catch (NumberFormatException e){
            log.error("Please, second parameter is the number of lines for sub-files. This size should be OK for your memory to be able to order an array in memory.");
        } catch (FileNotFoundException e) {
            log.error("Please, provide a valid file with numbers.");
        } catch (FileManagementException e) {
            e.printStackTrace();
        }
    }
}
