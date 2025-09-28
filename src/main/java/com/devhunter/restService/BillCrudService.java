package com.devhunter.restService;

import org.jboss.resteasy.reactive.RestQuery;

import com.devhunter.model.Bill;
import com.devhunter.restService.database.BillDatabase;
import com.google.gson.Gson;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * This class defines the interfaces, endpoints, and other configurations that
 * the
 * "BillRestServiceProxy" must adhere to.
 */
@ApplicationScoped
@Path("/crud")
public class BillCrudService {

    @Inject
    BillDatabase database;

    /**
     * persist new Bill
     * 
     * @param companyName
     * @param amount
     * @return
     */
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/add")
    public Response addBill(@RestQuery String newBillString) {
        Gson gson = new Gson();
        Bill newBill = gson.fromJson(newBillString, Bill.class);
        return Response.ok(database.persist(newBill)).build();
    }

    /**
     * fetch a single Bill by Id
     * 
     * @return
     */
    @GET
    @Path("/fetch")
    @Produces(MediaType.TEXT_PLAIN)
    public Response fetchBill(@RestQuery int id) {
        Bill fetchedBill = database.findById(id);
        if (fetchedBill != null) {
            return Response.ok(fetchedBill).build();
        }
        // no match, send back empty
        return Response.ok("{}").build();
    }

    /**
     * get all Bills
     * 
     * @return
     */
    @GET
    @Path("/fetchAll")
    @Produces(MediaType.TEXT_PLAIN)
    public Response fetchAllBills() {
        return Response.ok(new Gson().toJson(database.getBillCache())).build();
    }

    /**
     * get all Bills, but with a manually inserted delay
     * 
     * @return
     */
    @GET
    @Path("/slowFetchAll")
    @Produces(MediaType.TEXT_PLAIN)
    public Response fetchAllBillsSlowly() {
        // delay response to simulate a long running task
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return fetchAllBills();
    }

    /**
     * get count of Bills
     * 
     * @return
     */
    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getBillCount() {
        return Response.ok(database.getBillCacheSize()).build();
    }

    /**
     * update a Bill
     * 
     * @param id
     * @param companyName
     * @param amount
     * @return
     */
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/update")
    public Response updateBill(@RestQuery int id, @RestQuery String updatedBillString) {
        Gson gson = new Gson();
        Bill updatedBill = gson.fromJson(updatedBillString, Bill.class);
        return Response.ok(database.persist(id, updatedBill)).build();
    }

    /**
     * Delete Bill
     * 
     * @param newBill
     * @return
     */
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/delete")
    public Response deleteBill(@RestQuery int id) {
        Bill deletedBill = database.delete(id);
        if (deletedBill != null) {
            return Response.ok(deletedBill).build();
        }
        // no match, send back empty
        return Response.ok("{}").build();
    }
}
