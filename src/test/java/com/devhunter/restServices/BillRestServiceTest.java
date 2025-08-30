package com.devhunter.restServices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.devhunter.TestBase;
import com.devhunter.model.Bill;
import com.devhunter.restService.BillRestService;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@QuarkusTest
class BillRestServiceTest extends TestBase {

    @Inject
    BillRestService restService;

    @Test
    @DisplayName("Service: Add Bill and Verify Id")
    public void testAddBillId() {
        // Arrange
        String companyName = "Wallace";
        Double billAmount = 12.34;

        // Act: add a new bill
        Response response = restService.addBill(companyName, billAmount);

        // Assert: OK
        assertEquals(200, response.getStatus(), "Something is wrong with /add");
        // id of first new bill is 1
        int addedBillId = (Integer) response.getEntity();
        assertNotEquals(0, addedBillId);
    }

    @Test
    @DisplayName("Service: Add Bill and Verify Details")
    public void testAddBill() {
        // Arrange
        String companyName = "Lily";
        Double billAmount = 43.21;

        // Act: add a new bill, get the newest added item
        Response response = restService.addBill(companyName, billAmount);
        assertEquals(200, response.getStatus(), "Something is wrong with /add");
        response = restService.fetchAllBills();
        String billsString = (String) response.getEntity();
        // deserialize (with custom Gson)
        LinkedHashMap<Integer, Bill> billMap = deserializeBillMap(billsString);
        // get last item in the map
        Entry<Integer, Bill> lastBill = getLastEntryInMap(billMap);
        int lastKey = lastBill.getKey();
        Bill newestBill = billMap.get(lastKey);

        // Assert: OK
        assertEquals(200, response.getStatus(), "Something is wrong with /add");
        // verify added bill matches "arrange"
        assertEquals(billAmount, newestBill.getTotal());
        assertEquals(companyName, newestBill.getCompanyName());
    }

    @Test
    @DisplayName("Service: Fetch One Bill")
    public void testFetchOneBill() {
        // Arrange: add a bill to make sure it exists
        String companyName = "Apache";
        Double billAmount = 81.66;
        Response response = restService.addBill(companyName, billAmount);
        assertEquals(200, response.getStatus(), "Something is wrong with /add");

        // Act: get the added bill
        response = restService.fetchBill((Integer) response.getEntity());
        Bill fetchedBill = (Bill) response.getEntity();

        // Assert:OK
        assertEquals(200, response.getStatus(), "Something is wrong with /fetch");
        // verify added bill matches "arrange"
        assertEquals(billAmount, fetchedBill.getTotal());
        assertEquals(companyName, fetchedBill.getCompanyName());
    }

    @Test
    @DisplayName("Service: Fetch All Bills")
    public void testFetchAllBills() {
        // Arrange

        // Act: get all bills
        Response response = restService.fetchAllBills();
        String billsString = (String) response.getEntity();
        // deserialize (with custom Gson)
        LinkedHashMap<Integer, Bill> billMap = deserializeBillMap(billsString);

        // Assert:OK
        assertEquals(200, response.getStatus(), "Something is wrong with /fetchAll");
        // with Quarkus "Continuous testing", we don't know how many each will have, so
        // test them all each time
        for (Map.Entry<Integer, Bill> each : billMap.entrySet()) {
            // structure of results
            assertNotNull(each.getKey());
            assertNotNull(each.getValue());
            // structure of Bill
            assertNotNull(each.getValue().getCompanyName());
            assertInstanceOf(String.class, each.getValue().getCompanyName());
            assertNotNull(each.getValue().getDate());
            assertInstanceOf(String.class, each.getValue().getDate());
            assertNotNull(each.getValue().getTotal());
            assertInstanceOf(Double.class, each.getValue().getTotal());
        }
    }

    @Test
    @DisplayName("Service: Fetch All Bills with 5 second Blocking Call")
    public void testSlowFetchAllBills() {
        // Arrange

        // Act: get all bills
        Response response = restService.fetchAllBillsSlowly();
        String billsString = (String) response.getEntity();
        // deserialize (with custom Gson)
        LinkedHashMap<Integer, Bill> billMap = deserializeBillMap(billsString);

        // Assert:OK
        assertEquals(200, response.getStatus(), "Something is wrong with /slowFetchAll");
        // with Quarkus "Continuous testing", we don't know how many each will have, so
        // test them all each time
        for (Map.Entry<Integer, Bill> each : billMap.entrySet()) {
            // structure of results
            assertNotNull(each.getKey());
            assertNotNull(each.getValue());
            // structure of Bill
            assertNotNull(each.getValue().getCompanyName());
            assertInstanceOf(String.class, each.getValue().getCompanyName());
            assertNotNull(each.getValue().getDate());
            assertInstanceOf(String.class, each.getValue().getDate());
            assertNotNull(each.getValue().getTotal());
            assertInstanceOf(Double.class, each.getValue().getTotal());
        }
    }

    @Test
    @DisplayName("Service: Fetch Bill Count")
    public void testFetchBillCount() {
        // Arrange

        // Act: get count
        Response response = restService.getBillCount();

        // Assert:OK, count could be ANYTHING during continuous testing, just check
        // status
        assertEquals(200, response.getStatus(), "Something is wrong with /count");
    }

    @Test
    @DisplayName("Service Update Single Bill")
    public void testUpdateBill() {
        // Arrange: make sure we have a bill to update
        Response response = restService.addBill("ReadyToUpdate", 6.45);
        assertEquals(200, response.getStatus(), "Something is wrong with /add");
        int addedBillId = (Integer) response.getEntity();

        // Act: update the added bill
        response = restService.updateBill(addedBillId, "Updated", 7.89);
        assertEquals(200, response.getStatus(), "Something is wrong with /update");
        assertNotEquals(0, (Integer) response.getEntity());

        // Act: delete it and update again
        response = restService.deleteBill(addedBillId);
        assertEquals(200, response.getStatus(), "Something is wrong with /delete");
        Bill deletedBill = (Bill) response.getEntity();
        assertEquals("Updated", deletedBill.getCompanyName());
        assertEquals(7.89, deletedBill.getTotal());
        response = restService.updateBill(addedBillId, "ShouldNotUpdate", 6.66);

        // Assert:OK, but nothing to delete
        assertEquals(0, (Integer) response.getEntity());
    }

    @Test
    @DisplayName("Service: Delete Single Bill")
    public void testDeleteBill() {
        // Arrange: make sure we have a bill to delete
        Response response = restService.addBill("ReadyToDelete", 1.23);
        assertEquals(200, response.getStatus(), "Something is wrong with /add");
        int addedBillId = (Integer) response.getEntity();

        // Act: delete the added bill
        response = restService.deleteBill(addedBillId);
        Bill deletedBill = (Bill) response.getEntity();

        // Assert: deleted the righ thing
        assertEquals(200, response.getStatus(), "Something is wrong with /delete");
        assertEquals("ReadyToDelete", deletedBill.getCompanyName());
        assertEquals(1.23, deletedBill.getTotal());

        // Act : try to delete it again
        response = restService.deleteBill(addedBillId);
        assertEquals(200, response.getStatus(), "Something is wrong with /delete");

        // Assert:OK, but nothing to delete
        assertEquals("{}", (String) response.getEntity());
    }
}