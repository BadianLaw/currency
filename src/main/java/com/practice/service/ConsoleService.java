package com.practice.service;

import com.practice.util.CurrencyParserUtil;
import com.practice.util.MuServerUtil;
import com.practice.util.ThreadPoolUtil;
import io.muserver.MuServer;
import io.muserver.MuServerBuilder;
import javafx.util.Pair;
import sun.rmi.runtime.Log;

import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

public class ConsoleService {
    private static final long sleep = 60_000;

    public static void handInputOfTerminal() {
        Scanner scanner = new Scanner(System.in);
        String scanLine = null;
        while ((scanLine = scanner.nextLine()) != null) {
            scanLine = scanLine.trim();
            if ("quit".equals(scanLine)) {
                ThreadPoolUtil.shutdownExecutor();
                System.exit(-1);
                break;
            }
            Pair<String, Long> pair = CurrencyParserUtil.parseInputLine(scanLine);
            if (Objects.isNull(pair)) {
                System.out.println("terminal input wrongs line:" + scanLine);
                continue;
            }
            Long payment = CurrencyService.mergeInput(pair.getKey(), pair.getValue());
            System.out.println(pair.getKey() + " " + payment);
        }
    }

    public static void consoleOutput() {
        ThreadPoolExecutor executor = ThreadPoolUtil.getExecutor();
        executor.execute(() -> {
            boolean isTerminated = ThreadPoolUtil.isTerminated;
            while (!isTerminated) {
                List<String> consoleList = new ArrayList<>();
                Map<String, AtomicLong> currencyMap = CurrencyService.getCurrencyMap();
                currencyMap.forEach((currency, value) -> {
                    if (value.get() != 0L) {
                        consoleList.add(currency + " " + value);
                    }
                });
                System.out.println("console display once per one minute:");
                consoleList.forEach(str -> System.out.println(str));
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
