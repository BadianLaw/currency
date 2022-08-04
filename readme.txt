1.how to run the program
starting up the program by run the main method in class MuServerApplication.

2.function that the program provides
1.get the currency amount which froms the program in the page:localhost:8080/currency.html
    1.1 input the currency code,such as cny,you will get the amount from the program.
    1.2 some input will be verify,if click query button without any input,the page will give you notice.
        Blank or empty string will not allowed.
    1.3 the amount will be changed when payment is updated,which refresh the data on the page by SSE(Server send Event).

2.function in console
    2.1 when the program starting up,you will see the currency data from the console as output once per minute.
        the initial data will be loaded from the file.the format of the file will be one or more lines:
        CNY 2000
        USD 1000
        HKD 100
        USD -100
        HKD 200
    2.2 besides the above,you can input the payment with the format like CNY 100,then the amount of the payment will
        be updated.if you browse the page:localhost:8080/currency.html,you will see the change of the amount;
    2.3 and if you input the string "quit",the program will be shutdown right away.

3.two endpoint that provided by the program
    3.1 Request/Response - use currency Code as input then payment number as output.the url of this endpoint is
        localhost:8080/currency/query/{currencycode},and the {currencycode} will be replaced just like cny,etc.
    3.2 Server Sent Event - use currency code as input then stream out the payment (when payment is updated)
        the url of this endpoint is localhost:8080/sse/currency
    3.3 furthermore,a file upload endpoint is provided too,the url of this endpoint is localhost:8080/currency/uploadFile.
        Once you upload the file in the correct format,just described in 2.1 details,you will see the data updated,both updated
        in the page : localhost:8080/currency.html and the console output also if the amount is not zero

4.how to test
the test code is in test package and  the test unit will be used in junit.the more test method will be updated in the next version.
for example,you can run the CurrencyServiceTest method like {testHandUploadFile}by click debug button in the idea ide.

5.how to concat with me
just in the github,submit the issues or give the pull request will be nice.
