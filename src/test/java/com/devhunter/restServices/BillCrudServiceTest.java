package com.devhunter.restServices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.devhunter.TestBase;
import com.devhunter.model.Bill;
import com.devhunter.model.Company;
import com.devhunter.restService.BillCrudService;
import com.google.gson.Gson;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@QuarkusTest
class BillCrudServiceTest extends TestBase {

    @Inject
    BillCrudService crudService;

    @Test
    @DisplayName("CRUD Service: Add Bill and Verify Id")
    public void testAddBillId() {
        // Arrange: create bill to add
        String companyName = "Wallace";
        String billAmount = "$12.34";
        Bill newBill = new Bill(new Company(companyName, "UnitTest", "123-456-7890", "https://test.com/"), billAmount,
                LocalDate.now().toString());

        // Act: add a new bill
        Gson gson = new Gson();
        String billString = gson.toJson(newBill);
        Response response = crudService.addBill(billString);

        // Assert: OK
        assertEquals(200, response.getStatus(), "Something is wrong with /add");
        // id of first new bill is 1
        int addedBillId = (Integer) response.getEntity();
        assertNotEquals(0, addedBillId);
    }

    @Test
    @DisplayName("CRUD Service: Add Bill and Verify Details")
    public void testAddBill() {
        // Arrange: create a new bill
        String companyName = "Lily";
        String billAmount = "$43.21";
        Bill newBill = new Bill(new Company(companyName, "UnitTest", "123-456-7890", "https://test.com/"), billAmount,
                LocalDate.now().toString());

        // Act: add a new bill, get the newest added item
        Gson gson = new Gson();
        String billString = gson.toJson(newBill);
        Response response = crudService.addBill(billString);
        assertEquals(200, response.getStatus(), "Something is wrong with /add");
        response = crudService.fetchAllBills();
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
        assertEquals(companyName, newestBill.getCompany().getName());
    }

    @Test
    @DisplayName("CRUD Service: Fetch One Bill")
    public void testFetchOneBill() {
        // Arrange: create & add a bill to make sure it exists
        String companyName = "Apache";
        String billAmount = "$81.66";
        Bill newBill = new Bill(new Company(companyName, "UnitTest", "123-456-7890", "https://test.com/"), billAmount,
                LocalDate.now().toString());

        Gson gson = new Gson();
        String billString = gson.toJson(newBill);
        Response response = crudService.addBill(billString);
        assertEquals(200, response.getStatus(), "Something is wrong with /add");

        // Act: get the added bill
        response = crudService.fetchBill((Integer) response.getEntity());
        Bill fetchedBill = (Bill) response.getEntity();

        // Assert:OK
        assertEquals(200, response.getStatus(), "Something is wrong with /fetch");
        // verify added bill matches "arrange"
        assertEquals(billAmount, fetchedBill.getTotal());
        assertEquals(companyName, fetchedBill.getCompany().getName());
    }

    @Test
    @DisplayName("CRUD Service: Fetch All Bills")
    public void testFetchAllBills() {
        // Arrange

        // Act: get all bills
        Response response = crudService.fetchAllBills();
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
            assertNotNull(each.getValue().getCompany().getName());
            assertInstanceOf(String.class, each.getValue().getCompany().getName());
            assertNotNull(each.getValue().getDate());
            assertInstanceOf(String.class, each.getValue().getDate());
            assertNotNull(each.getValue().getTotal());
            assertInstanceOf(String.class, each.getValue().getTotal());
        }
    }

    @Test
    @DisplayName("CRUD Service: Fetch All Bills with 5 second Blocking Call")
    public void testSlowFetchAllBills() {
        // Arrange

        // Act: get all bills
        Response response = crudService.fetchAllBillsSlowly();
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
            assertNotNull(each.getValue().getCompany().getName());
            assertInstanceOf(String.class, each.getValue().getCompany().getName());
            assertNotNull(each.getValue().getDate());
            assertInstanceOf(String.class, each.getValue().getDate());
            assertNotNull(each.getValue().getTotal());
            assertInstanceOf(String.class, each.getValue().getTotal());
        }
    }

    @Test
    @DisplayName("CRUD Service: Fetch Bill Count")
    public void testFetchBillCount() {
        // Arrange

        // Act: get count
        Response response = crudService.getBillCount();

        // Assert:OK, count could be ANYTHING during continuous testing, just check
        // status
        assertEquals(200, response.getStatus(), "Something is wrong with /count");
    }

    @Test
    @DisplayName("CRUD Service Update Single Bill")
    public void testUpdateBill() {
        // Arrange: create bill & make sure we have a bill to update
        Bill newBill = new Bill(new Company("ReadyToUpdate", "UnitTest", "123-456-7890", "https://test.com/"), "$6.54",
                LocalDate.now().toString());

        Gson gson = new Gson();
        String billString = gson.toJson(newBill);
        Response response = crudService.addBill(billString);
        assertEquals(200, response.getStatus(), "Something is wrong with /add");
        int addedBillId = (Integer) response.getEntity();

        // Act: create an updated bill and update
        Bill updatedBill = new Bill(new Company("Updated", "UnitTest", "123-456-7890", "https://test.com/"),
                "$7.89", LocalDate.now().toString());
        String updatedBillString = gson.toJson(updatedBill);

        response = crudService.updateBill(addedBillId, updatedBillString);
        assertEquals(200, response.getStatus(), "Something is wrong with /update");
        assertNotEquals(0, (Integer) response.getEntity());

        // Act: delete it and update again
        response = crudService.deleteBill(addedBillId);
        assertEquals(200, response.getStatus(), "Something is wrong with /delete");
        Bill deletedBill = (Bill) response.getEntity();
        assertEquals("Updated", deletedBill.getCompany().getName());
        assertEquals("$7.89", deletedBill.getTotal());
        Bill updatedBill2 = new Bill(new Company("ShouldNotUpdate", "UnitTest", "123-456-7890", "https://test.com/"),
                "$6.66", LocalDate.now().toString());
        String updatedBillString2 = gson.toJson(updatedBill2);

        response = crudService.updateBill(addedBillId, updatedBillString2);

        // Assert:OK, but nothing to delete
        assertEquals(0, (Integer) response.getEntity());
    }

    @Test
    @DisplayName("CRUD Service: Delete Single Bill")
    public void testDeleteBill() {
        // Arrange: make sure we have a bill to delete
        Bill newBill = new Bill(new Company("ReadyToDelete", "UnitTest", "123-456-7890", "https://test.com/"), "$1.23",
                LocalDate.now().toString());
        Gson gson = new Gson();
        String billString = gson.toJson(newBill);
        Response response = crudService.addBill(billString);
        assertEquals(200, response.getStatus(), "Something is wrong with /add");
        int addedBillId = (Integer) response.getEntity();

        // Act: delete the added bill
        response = crudService.deleteBill(addedBillId);
        Bill deletedBill = (Bill) response.getEntity();

        // Assert: deleted the righ thing
        assertEquals(200, response.getStatus(), "Something is wrong with /delete");
        assertEquals("ReadyToDelete", deletedBill.getCompany().getName());
        assertEquals("$1.23", deletedBill.getTotal());

        // Act : try to delete it again
        response = crudService.deleteBill(addedBillId);
        assertEquals(200, response.getStatus(), "Something is wrong with /delete");

        // Assert:OK, but nothing to delete
        assertEquals("{}", (String) response.getEntity());
    }
}