package org.frank.common.java.learn;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @Author: frank
 * @Date: 2019-12-14
 * @Description: 操作csv代码
 */
public class LearnCSV {
    private static final Logger logger = LoggerFactory.getLogger(LearnCSV.class);

    public static void main(String[] args) throws Exception {
        String[] strs = {"aa", "bb", "cc"};
        String s = Arrays.toString(strs);
        System.out.println(s);

        String srcPath = "C:\\code_my\\github\\learn-java\\src\\main\\resources\\data\\divice.csv";
        readCSV(srcPath, "utf-8", ':');
    }

    public static void readCSV(String srcPath, String charset, char separator) {
        if (srcPath == null || srcPath.length() == 0) {
            throw new RuntimeException("src path is null");
        }
        if (charset == null || charset.length() == 0) {
            charset = "utf-8";
        }
        try {
            File file = new File(srcPath);
            Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset), 256 * 1024);
            CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(new CSVParserBuilder().withSeparator(separator).build()).build();

            Iterator<String[]> iterator = csvReader.iterator();
            while (iterator.hasNext()) {
                String[] words = iterator.next();
                logger.info(words[0]);
                System.out.println(words[0]);
                System.out.println(words[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
