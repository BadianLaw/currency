package com.practice;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.practice.handler.CurrencyHandler;
import com.practice.service.ConsoleService;
import com.practice.service.CurrencyService;
import com.practice.util.MuServerUtil;
import io.muserver.*;
import io.muserver.handlers.ResourceHandlerBuilder;
import io.muserver.rest.RestHandlerBuilder;

import java.io.IOException;
import java.util.Currency;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class MuServerApplication {

    public static void main(String[] args) {
        CurrencyHandler currencyHandler = new CurrencyHandler();

        MuServer server = MuServerBuilder.httpServer()
                .addHandler(Method.GET, "/", (request, response, pathParams) -> {
                    response.write("Hello, world");
                }).addHandler(ResourceHandlerBuilder.fileOrClasspath("src/main/resources/web", "/web"))
                .addHandler(Method.GET, "/sse/currency", (request, response, pathParams) -> {

                    SsePublisher publisher = SsePublisher.start(request, response);
                    new Thread(() -> currency(publisher,request)).start();
                }).addHandler(RestHandlerBuilder.restHandler(currencyHandler)
                    .addCustomWriter(new JacksonJaxbJsonProvider())
                    .addCustomReader(new JacksonJaxbJsonProvider())
                )
                .withHttpPort(8080)
                .start();

        System.out.println("Started server at " + server.uri());

        //load the currency from local file when starting up
        String file = ".\\src\\currency.txt";
        CurrencyService.loadFromFile(file);

        //start the console output
        ConsoleService.consoleOutput();

        //start the scanner to get the input
        ConsoleService.handInputOfTerminal();
    }

    public static void currency(SsePublisher publisher, MuRequest request) {
        String currencycode = request.query().get("currencycode");
        if(currencycode == null || currencycode.isEmpty()){
            try {
                publisher.send("Currency Code Input Error");
            } catch (IOException e) {
                e.printStackTrace();
            }
//            publisher.close();
            return;
        }

        long netAmount;

        Map<String, AtomicLong> currencyMap = CurrencyService.getCurrencyMap();
        if(!currencyMap.containsKey(currencycode)){
            try {
                publisher.send("Currency Code NOT FOUND：" + currencycode);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long temp = 0;
        while (true){
            AtomicLong atomicLong = currencyMap.get(currencycode);
            if(Objects.isNull(atomicLong)){
                try {
                    publisher.send("There is no net amount for this currency");
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            netAmount = atomicLong.get();
            try {
                if(netAmount != temp) {
                    publisher.send("Net Amount：" + currencycode + ": " + netAmount);
                    temp = netAmount;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        publisher.close();
    }
}
