package com.devhunter.restClients;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.devhunter.model.Bill;
import com.devhunter.model.Company;
import com.devhunter.restClient.BillRestClient;
import com.google.gson.Gson;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

/**
 * Tests the BillClients use of the "BillCrudServiceProxy" against the
 * "BillCrudService" and the "BillParseServiceProxy" against the
 * "BillParseService"
 */
@QuarkusTest
class BillRestClientTest {

    @Inject
    BillRestClient client;

    @Test
    @Disabled("Disabled for Docker build only")
    @DisplayName("Proxy: Add one Bill then Delete it")
    public void testAddAndDelete() {
        // Arrange: create & add a bill from rest client
        Bill newBill = new Bill(new Company("TEST1", "UnitTest", "123-456-7890", "https://test.com/"), "$12,345.67",
                LocalDate.now().toString());
        int billId = client.addBill(newBill);

        // Act: delete the one we just created
        Bill deletedBill = client.deleteBill(billId);

        // Assert: verify data
        assertEquals("TEST1", deletedBill.getCompany().getName());
        assertEquals("$12345.67", deletedBill.getTotal());
    }

    @Test
    @Disabled("Disabled for Docker build only")
    @DisplayName("Proxy: Fetch All Bills with 5 second Blocking Call")
    public void testFetchAllSlowly() {
        // Arrange: create & add two Bills from Rest Client
        Bill newBill1 = new Bill(new Company("TEST1", "UnitTest", "123-456-7890", "https://test.com/"), "$12,345.67",
                LocalDate.now().toString());
        Bill newBill2 = new Bill(new Company("TEST2", "UnitTest", "123-456-7890", "https://test.com/"), "$76,543.21",
                LocalDate.now().toString());
        int firstId = client.addBill(newBill1);
        int secondId = client.addBill(newBill2);

        // Act: fetch all items (Blocking)
        Map<Integer, Bill> firstAllBills = client.getAllBillsSlowly();
        // Assert: Verify there are two items
        assertEquals(2, firstAllBills.size());

        // Arrange: delete the two Bills we just created
        client.deleteBill(firstId);
        client.deleteBill(secondId);

        // Act: fetch all items (Blocking)
        Map<Integer, Bill> secondAllBills = client.getAllBillsSlowly();
        // Assert: Verify there are no itemsKs
        assertEquals(0, secondAllBills.size());
    }

    @Test
    @Disabled("Disabled for Docker build only")
    @DisplayName("Proxy: Parse a test bill")
    public void testParse() {
        // Arrange: create & add a bill from rest client
        String billString = "asdkfjakdsjfkjskdfkjFrontierasdkfj 23o478098uokasdfl n $75.64adsfkj asdfi238iIJNIJIJ";

        // Act: parse bill text
        String parsedBillString = client.parseBill(billString);
        Gson gson = new Gson();
        Bill parsedBill = gson.fromJson(parsedBillString, Bill.class);

        // Assert: verify bill data
        assertEquals("Frontier", parsedBill.getCompany().getName());
        assertEquals("Internet", parsedBill.getCompany().getService());
        assertEquals("$75.64", parsedBill.getTotal());
    }
}