package com.practice.handler;

import com.practice.pojo.Currency;
import com.practice.service.CurrencyService;
import io.muserver.UploadedFile;

import javax.ws.rs.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Path("/currency")
public class CurrencyHandler {

    @GET
    @Path("/query/{currencycode}")
    @Produces("application/json")
    public Currency getAmountByCurrencyCode(@PathParam("currencycode") String currencycode){
        Currency currency = CurrencyService.getCurrencyByCurrencyCode(currencycode.trim().toUpperCase());
        if(currency.getCurrencyCode() == null){
            throw new NotFoundException("No currency amount with currency code" + currencycode);
        }

        return currency;
    }

    @POST
    @Path("/uploadFile")
    public void uploadFile(@FormParam("file") UploadedFile file) throws IOException {
        InputStream inputStream = file.asStream();
        CurrencyService.handUploadFile(inputStream,file.filename());
    }


}
