package com.practice.service;

import com.practice.pojo.Currency;
import com.practice.util.CurrencyParserUtil;
import com.practice.util.ThreadPoolUtil;
import javafx.util.Pair;

import javax.ws.rs.NotFoundException;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

public class CurrencyService {
    private static final Map<String, AtomicLong> currencyMap = new HashMap<>();

    public static Map<String,AtomicLong> getCurrencyMap(){
        return currencyMap;
    }

    public static Long mergeInput(String currencyCode, Long value) {
        AtomicLong aLong = getAtomicLongValue(currencyCode);
        return aLong.addAndGet(value);
    }

    public static void loadFromFile(String filePath){
        File file = new File(filePath);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            handUploadFile(fileInputStream,file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Currency getCurrencyByCurrencyCode(String currencyCode){
        Currency currency = new Currency();
        AtomicLong atomicLong = currencyMap.get(currencyCode);
        if(atomicLong == null){
            return currency;
        }

        currency.setCurrencyCode(currencyCode);
        currency.setAmount(atomicLong.get());

        return currency;
    }

    public static void handUploadFile(InputStream inputStream, String fileName) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String readLine = null;
            List<Future<?>> list = new ArrayList<>();
            while ((readLine = bufferedReader.readLine()) != null) {
                String finalReadLine = readLine;
                ThreadPoolExecutor executor = ThreadPoolUtil.getExecutor();
                Future<?> future = executor.submit(() -> {
                    // handle finalReadLine
                    Pair<String, Long> pair = CurrencyParserUtil.parseInputLine(finalReadLine);
                    if (Objects.isNull(pair)) {
                        System.out.println("filename=" + fileName + " contains wrong line=" + finalReadLine);
                    } else {
                        mergeInput(pair.getKey(), pair.getValue());
                    }
                    return;
                });
                list.add(future);
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            // await task finish
            awaitTaskFinish(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static AtomicLong getAtomicLongValue(String currencyCode) {
        AtomicLong aLong = currencyMap.get(currencyCode);
        // double check lock
        if (Objects.isNull(aLong)) {
            synchronized (currencyMap) {
                aLong = currencyMap.get(currencyCode);
                if (Objects.isNull(aLong)) {
                    aLong = new AtomicLong();
                    currencyMap.put(currencyCode, aLong);
                }
            }
        }
        return aLong;
    }

    private static void awaitTaskFinish(List<Future<?>> list) {
        list.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
    }
}
