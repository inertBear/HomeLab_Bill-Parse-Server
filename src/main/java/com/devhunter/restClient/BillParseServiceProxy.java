package com.devhunter.restClient;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestQuery;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * The methods in this class adheres to the interface defined by the
 * "BillParseService" (backend).
 * 
 * The "BillParseClient" (frontend) injects this proxy and uses it to communicate
 * with the "BillParseService" (backend).
 */
@Path("/bill")
@RegisterRestClient(configKey = "bill-parse-service-proxy")
public interface BillParseServiceProxy {

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/parse")
    Response parseBill(@RestQuery String billText);

}
