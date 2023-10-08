package app;

import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

// getList C
// convert x (wrong input handling)
// user fav list X

public class CurrencyConverter {

    public static void main(String[] args) throws IOException {

        boolean loop = true;
        Scanner scanner = new Scanner(System.in);
        CurrencyConverter currencyConverter = new CurrencyConverter();

        ArrayList<String> customerCurrencyList = new ArrayList<>();

        while (loop){
            System.out.println("\n\n");
            System.out.println("|===============================================|");
            System.out.println("|\t\t\t Currency Converter \t\t\t\t|");
            System.out.println("|===============================================|\n\n");

            System.out.println("A. Display All Currencies\tB. Display User Currency List\t" +
                    "C. Add Currency in the User Currency List\nD. Delete Currency From User Currency List\t" +
                    "E. Convert Currency\nF. Exit");
            String option = scanner.next();
            option = option.toUpperCase();

            if (option.equals("A")){
                currencyConverter.getList();

            }else if (option.equals("B")) {
                currencyConverter.getCustomerCurrencyList(customerCurrencyList);

            }else if(option.equals("C")){
                customerCurrencyList = currencyConverter.addCurrenciesInCustomerList(customerCurrencyList);
                System.out.println("===============================================");
                System.out.println("\t\tCustomer Currency List Updated !");
                System.out.println("===============================================\n");

            }else if(option.equals("D")){
                int size = customerCurrencyList.size();
                customerCurrencyList = currencyConverter.deleteCurrency(customerCurrencyList);
                if (customerCurrencyList.size() != size) {
                    System.out.println("===============================================");
                    System.out.println("\t\tCustomer Currency List Updated !");
                    System.out.println("===============================================\n");
                }

            }else if(option.equals("E")){
                currencyConverter.convertCurrencies();

            }else if(option.equals("F")){
                loop = false;

            }else {
                System.out.println("===============================================");
                System.out.println("\t\tInvalid Input !!!");
                System.out.println("===============================================\n");
            }
        }
    }


    // Method for fetching the list of currencies
    public void getList() throws IOException{

        // API url
        String url = "http://api.exchangerate.host/list?access_key=17a320ad339c9e30b6deb27e06c95da8&format=1";

        // Sending request and retrieving list of currencies from API
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();

        String respBody = response.body().string();
        JSONObject jsonObject = new JSONObject(respBody);
        JSONObject curJsonObj = jsonObject.getJSONObject("currencies");

        Iterator<String> keys = curJsonObj.keys();
        while (keys.hasNext()){
            String key = keys.next();
            Object value = curJsonObj.get(key);
            System.out.println(key+ " : " + value);
        }
    }


    // Method for currency conversion
    public void convertCurrencies() throws  IOException{

        // Input collection
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter first currency : ");
        String cur1 = scanner.nextLine();
        System.out.println("Enter second currency : ");
        String cur2 = scanner.nextLine();
        System.out.println("Enter the amount for conversion :");
        int amount = scanner.nextInt();

        // API url
        String url = "http://api.exchangerate.host/convert?access_key=17a320ad339c9e30b6deb27e06c95da8&from="
                + cur1.toUpperCase() +"&to="+ cur2.toUpperCase() + "&amount="+ amount +"&format=1";

        // Sending request and retrieving results from API
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();
        String respBody = response.body().string();
        JSONObject jsonObject = new JSONObject(respBody);
        Object result = jsonObject.get("result");

        // Displaying Results
        System.out.println("The value of "+ amount + " "+ cur1.toUpperCase() + " in "+ cur2.toUpperCase() +" is " +result);
    }

    public ArrayList<String> addCurrenciesInCustomerList(ArrayList<String> list) throws IOException{

        Scanner scanner = new Scanner(System.in);
        System.out.println("(If there are multiple currencies, separate them with comma. Example: EUR,INR,USD...)\n" +
                                                            "Enter the currencies : ");
        String names = scanner.nextLine();
        String[] currencies = names.split(",");
        for (String x : currencies) {
            list.add(x.toUpperCase());
        }
        return list;
    }

    public void getCustomerCurrencyList(ArrayList<String> list) throws IOException{

        // url
        String url = "http://api.exchangerate.host/list?access_key=17a320ad339c9e30b6deb27e06c95da8&format=1";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();
        String respBody = response.body().string();
        JSONObject jsonObject = new JSONObject(respBody);
        JSONObject curJsonObj = jsonObject.getJSONObject("currencies");

        if (list.size()<1){
            System.out.println("===============================================");
            System.out.println("\t\t\tList is empty");
            System.out.println("===============================================\n");
        }else {
            System.out.println("User Currency List :");
            for (int i = 0; i < list.size(); i++) {
                Object x = curJsonObj.get(list.get(i));
                System.out.println("\t" + list.get(i) + " : " + x);
            }
        }

    }

    public ArrayList<String> deleteCurrency(ArrayList<String> currencyList){

        boolean isEmpty = true;
        Scanner scanner = new Scanner(System.in);
        System.out.println("(If there are multiple currencies, separate them with comma. Example: EUR,INR,USD...)\n" +
                "Enter the currencies : ");
        String names = scanner.nextLine();
        String[] currencies = names.split(",");

        for (String x : currencyList){
            for(int i=0; i<currencies.length; i++){
                if(x.equals(currencies[i])){
                    currencyList.remove(x);
                    isEmpty = false;
                }else {
                    isEmpty = true;
                }
            }
        }
        if(isEmpty){
            System.out.println("===============================================");
            System.out.println("\t\t\tNo Match Found !!!");
            System.out.println("===============================================");
        }
        return currencyList;
    }
}
