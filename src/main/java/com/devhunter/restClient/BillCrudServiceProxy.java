package com.devhunter.restClient;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestQuery;

import com.devhunter.model.Bill;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * The methods in this class adheres to the interface defined by the
 * "BillRestService" (backend).
 * 
 * The "BillRestClient" (frontend) injects this proxy and uses it to communicate
 * with the "BillRestService" (backend).
 */
@Path("/bill")
@RegisterRestClient(configKey = "bill-crud-service-proxy")
public interface BillCrudServiceProxy {

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/add")
    Response addBill(@RestQuery Bill newBill);

    @GET
    @Path("/fetch")
    @Produces(MediaType.TEXT_PLAIN)
    public Response fetchBill(@RestQuery int id);

    @GET
    @Path("/fetchAll")
    @Produces(MediaType.TEXT_PLAIN)
    public Response fetchAllBills();

    @GET
    @Path("/slowFetchAll")
    @Produces(MediaType.TEXT_PLAIN)
    public Response fetchAllBillsSlowly();

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getBillCount();

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/update")
    public Response updateBill(@RestQuery int id, @RestQuery Bill updatedBill);

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/delete")
    public Response deleteBill(@RestQuery int id);

}
