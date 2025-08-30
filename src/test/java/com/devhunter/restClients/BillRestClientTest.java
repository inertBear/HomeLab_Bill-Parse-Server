package com.devhunter.restClients;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.devhunter.model.Bill;
import com.devhunter.restClient.BillRestClient;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

/**
 * Tests the BillRestClient's use of the "BillRestServiceProxy" against the
 * "BillRestService"
 */
@QuarkusTest
class BillRestClientTest {

    @Inject
    BillRestClient client;

    @Test
    @Disabled("Disabled for Docker build only")
    @DisplayName("Proxy: Add one Bill then Delete it")
    public void testAddAndDelete() {
        // add a bill from rest client
        int billId = client.addBill("TEST1", 12345.67);

        // delete the one we just created
        Bill deletedBill = client.deleteBill(billId);
        assertEquals("TEST1", deletedBill.getCompanyName());
        assertEquals(12345.67, deletedBill.getTotal());
    }

    @Test
    @Disabled("Disabled for Docker build only")
    @DisplayName("Proxy: Fetch All Bills with 5 second Blocking Call")
    public void testFetchAllSlowly() {
        // Arrange: add two Bills from Rest Client
        int firstId = client.addBill("TEST1", 12345.67);
        int secondId = client.addBill("TEST2", 76543.21);

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
}