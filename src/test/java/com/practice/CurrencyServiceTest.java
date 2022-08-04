package com.practice;

import com.practice.service.CurrencyService;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class CurrencyServiceTest {

    public static void main(String[] args) {

    }

    @Test
    public void testHandUploadFile(){
        String filePath = ".\\src\\test_data.txt";

        File file = new File(filePath);
        try {
            InputStream inputStream = new FileInputStream(file);
            CurrencyService.handUploadFile(inputStream, file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Map<String, AtomicLong> actualMap = CurrencyService.getCurrencyMap();
        // check result

        for(Map.Entry<String,AtomicLong> entry:actualMap.entrySet()){
            System.out.println("CurrencyCode: " + entry.getKey() + "=====" + "amount: " + entry.getValue().get());
        }
    }
}
