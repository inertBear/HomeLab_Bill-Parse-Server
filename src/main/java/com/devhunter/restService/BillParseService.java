package com.devhunter.restService;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.devhunter.model.Bill;
import com.devhunter.model.Company;
import com.google.gson.Gson;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("/bill")
public class BillParseService {

    @Inject
    BillCrudService crudService;

    /**
     * Workflow for parsing a bill
     * 
     * @param billText
     * @return
     */
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/parse")
    public Response parseBill(String billText) {
        // parse
        Bill bill = new Bill(getCompany(billText), getTotal(billText), getDate(billText));
        // deserialize
        Gson gson = new Gson();
        String billString = gson.toJson(bill);
        // save
        crudService.addBill(billString);
        // respond
        return Response.ok(bill.toString()).build();
    }

    /**
     * create a bill if the text includes a specific company name
     * 
     * @param billText
     * @return
     */
    public Company getCompany(String billText) {
        Company company;

        if (billText.toLowerCase().contains("city of wylie")) {
            company = new Company("City of Wylie", "Water", "972-516-6100",
                    "https://www.WylieTexas.gov/WaterSmart");
        } else if (billText.toLowerCase().contains("atmos")) {
            company = new Company("Atmos Energy", "Gas", "1-888-286-6700",
                    "https://www.atmosenergy.com/");
        } else if (billText.toLowerCase().contains("txu")) {
            company = new Company("TXU Energy", "Electric", "800-242-9113",
                    "https://www.txu.com/");
        } else if (billText.toLowerCase().contains("frontier")) {
            company = new Company("Frontier", "Internet", "UNKNOWN",
                    "https://frontier.com/");
        } else {
            company = new Company("UNKNOWN", "UNKNOWN", "UNKNOWN",
                    "UNKNOWN");
        }
        return company;
    }

    /**
     * extract the total amount due
     * works for bills under $1000
     * 
     * @param billText
     * @return
     */
    public String getTotal(String billText) {
        final Pattern pattern = Pattern.compile("\\$[0-9]{1,3}.[0-9]{2}", Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(billText);
        if (matcher.find()) {
            return matcher.group();
        } else
            return null;
    }

    /**
     * create a timestamp of NOW
     * 
     * @param billText
     * @return
     */
    public String getDate(String billText) {
        return LocalDate.now().toString();
    }
}
