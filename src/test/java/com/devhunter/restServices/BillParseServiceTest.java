package com.devhunter.restServices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.devhunter.TestBase;
import com.devhunter.model.Bill;
import com.devhunter.model.Company;
import com.devhunter.restService.BillParseService;
import com.google.gson.Gson;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@QuarkusTest
public class BillParseServiceTest extends TestBase {

    @Inject
    BillParseService parseService;

    @Test
    @DisplayName("Parse Service: Parse Water Bill Company")
    public void testParseWaterCompany() {
        // Arrange: create a string with a total in it
        String billString = "City of WylieThisisateststing$45.67withabillamountmixedintoit";

        // Act: parse the total
        Company parsedCompany = parseService.getCompany(billString);

        // Assert: total matches expected
        assertEquals("City of Wylie", parsedCompany.getName());
        assertEquals("Water", parsedCompany.getService());
        assertEquals("https://www.WylieTexas.gov/WaterSmart", parsedCompany.getWebsite());
        assertEquals("972-516-6100", parsedCompany.getPhoneNumber());
    }

    @Test
    @DisplayName("Parse Service: Parse Electric Bill Company")
    public void testParseElectricCompany() {
        // Arrange: create a string with a total in it
        String billString = "TXUThisisateststing$45.67withabillamountmixedintoit";

        // Act: parse the total
        Company parsedCompany = parseService.getCompany(billString);

        // Assert: total matches expected
        assertEquals("TXU Energy", parsedCompany.getName());
        assertEquals("Electric", parsedCompany.getService());
        assertEquals("https://www.txu.com/", parsedCompany.getWebsite());
        assertEquals("800-242-9113", parsedCompany.getPhoneNumber());
    }

    @Test
    @DisplayName("Parse Service: Parse Gas Bill Company")
    public void testParseGasCompany() {
        // Arrange: create a string with a total in it
        String billString = "AtMosThisisateststing$45.67withabillamountmixedintoit";

        // Act: parse the total
        Company parsedCompany = parseService.getCompany(billString);

        // Assert: total matches expected
        assertEquals("Atmos Energy", parsedCompany.getName());
        assertEquals("Gas", parsedCompany.getService());
        assertEquals("https://www.atmosenergy.com/", parsedCompany.getWebsite());
        assertEquals("1-888-286-6700", parsedCompany.getPhoneNumber());
    }

    @Test
    @DisplayName("Parse Service: Parse Internet Bill Company")
    public void testParseInternetCompany() {
        // Arrange: create a string with a total in it
        String billString = "frontierThisisateststing$45.67withabillamountmixedintoit";

        // Act: parse the total
        Company parsedCompany = parseService.getCompany(billString);

        // Assert: total matches expected
        assertEquals("Frontier", parsedCompany.getName());
        assertEquals("Internet", parsedCompany.getService());
        assertEquals("https://frontier.com/", parsedCompany.getWebsite());
        assertEquals("UNKNOWN", parsedCompany.getPhoneNumber());
    }

    @Test
    @DisplayName("Parse Service: Parse Bill Total")
    public void testParseBillTotal() {
        // Arrange: create a string with a total in it
        String billString = "Thisisateststing$45.67withabillamountmixedintoit";

        // Act: parse the total
        String parsedTotal = parseService.getTotal(billString);

        // Assert: total matches expected
        assertEquals("$45.67", parsedTotal);
    }

    @Test
    @DisplayName("Parse Service: Parse Bill Date")
    public void testParseBillDate() {
        // Arrange: create a string with a total in it
        String billString = "City of Wylie Thisisateststing$45.67withabillamountmixedintoit";

        // Act: parse the total
        String parsedDate = parseService.getDate(billString);

        // Assert: total matches expected
        assertNotNull(parsedDate);
    }

    @Test
    @DisplayName("Parse Service: Parse Bill example 1")
    public void testparseBill1() {
        // Arrange: create bill to add
        String billString = "City of Wylie Thisisateststing$45.67withabillamountmixedintoit";
        Response response = parseService.parseBill(billString);

        // Assert: OK
        assertEquals(200, response.getStatus(), "Something is wrong with /add");
        // id of first new bill is 1
        String parsedBillString = (String) response.getEntity();

        Gson gson = new Gson();
        Bill parsedBill = gson.fromJson(parsedBillString, Bill.class);
        assertEquals("City of Wylie", parsedBill.getCompany().getName());
        assertNotNull(parsedBill.getDate());
        assertEquals("$45.67", parsedBill.getTotal());
    }

        @Test
    @DisplayName("Parse Service: Parse Bill example 2")
    public void testParseBill2() {
        // Arrange: create bill to add
        String billString = "Atmossdfasdfadsfs0-das0f02304imdlkfma;dfm kasdm;//.dafsdeadf:)$666.66`//\\\\;'sdfaoq";
        Response response = parseService.parseBill(billString);

        // Assert: OK
        assertEquals(200, response.getStatus(), "Something is wrong with /add");
        // id of first new bill is 1
        String parsedBillString = (String) response.getEntity();

        Gson gson = new Gson();
        Bill parsedBill = gson.fromJson(parsedBillString, Bill.class);
        assertEquals("Atmos Energy", parsedBill.getCompany().getName());
        assertNotNull(parsedBill.getDate());
        assertEquals("$666.66", parsedBill.getTotal());
    }
}
