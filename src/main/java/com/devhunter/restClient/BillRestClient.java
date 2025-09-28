package com.devhunter.restClient;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.devhunter.model.Bill;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

/**
 * I think of this as the ACTUAL RestClient (frontend).
 * 
 * It uses the "BillCrudServiceProxy" and the "BillParseServiceProxy" to
 * communicate with the "BillCrudService" (backend) and the "BillParseService"
 * (backend).
 */
@ApplicationScoped
public class BillRestClient {

    @RestClient
    BillCrudServiceProxy crudProxy;

    @RestClient
    BillParseServiceProxy parseProxy;

    /**
     * Create
     * 
     * @param companyName
     * @param total
     * @return
     */
    public int addBill(Bill newBill) {
        Response response = crudProxy.addBill(newBill);
        Integer newId = response.readEntity(Integer.class);
        return newId;
    }

    /**
     * Fetch All with 5 second blocking call
     * 
     * @return
     */
    public Map<Integer, Bill> getAllBillsSlowly() {
        Response allBills = crudProxy.fetchAllBillsSlowly();
        String allBillString = allBills.readEntity(String.class);
        return deserializeBillMap(allBillString);
    }

    /**
     * Delete
     * 
     * @param id
     * @return
     */
    public Bill deleteBill(int id) {
        Response response = crudProxy.deleteBill(id);
        String deletedBillString = response.readEntity(String.class);
        Gson gson = new Gson();
        Bill deletedBill = gson.fromJson(deletedBillString, Bill.class);
        return deletedBill;
    }

    /**
     * Might come in handy later --> Tagging with comment
     */
    private LinkedHashMap<Integer, Bill> deserializeBillMap(String billsString) {
        Gson specialGson = new Gson();
        Type mapType = new TypeToken<Map<Integer, Bill>>() {
        }.getType();
        return specialGson.fromJson(billsString, mapType);
    }

    /**
     * parse a bill
     * 
     * @param billText
     * @return
     */
    public String parseBill(String billText) {
        Response response = parseProxy.parseBill(billText);
        String parsedBillString = response.readEntity(String.class);
        return parsedBillString;
    }
}
