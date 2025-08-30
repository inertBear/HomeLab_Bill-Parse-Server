package com.devhunter.restService.database;

import java.util.LinkedHashMap;
import java.util.Map;

import com.devhunter.model.Bill;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * This class acts as a database for the "BillRestService" (Backend).
 */
@ApplicationScoped
public class BillDatabase {

    private int mId = 0;
    private Map<Integer, Bill> billCache = new LinkedHashMap<>();

    /**
     * add resource
     * 
     * @param bill
     * @return
     */
    public int persist(Bill bill) {
        mId++;
        billCache.put(mId, bill);
        return mId;
    }

    /**
     * fetch all resources
     * 
     * @return
     */
    public Map<Integer, Bill> getBillCache() {
        return billCache;
    }

    /**
     * get resource count
     * 
     * @return
     */
    public int getBillCacheSize() {
        return billCache.size();
    }

    /**
     * fetch one resource
     * 
     * @param id
     * @return
     */
    public Bill findById(int id) {
        for (Map.Entry<Integer, Bill> each : billCache.entrySet()) {
            if (each.getKey().equals(id)) {
                return each.getValue();
            }
        }
        return null;
    }

    /**
     * update a resource
     * 
     * @param id
     * @param bill
     * @return
     */
    public int persist(int id, Bill bill) {
        if (billCache.containsKey(id)) {
            billCache.put(id, bill);
            return id;
        }
        return 0;
    }

    /**
     * delete a resource
     * 
     * @param id
     * @return
     */
    public Bill delete(int id) {
        return billCache.remove(id);
    }
}
