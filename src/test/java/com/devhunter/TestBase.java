package com.devhunter;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.devhunter.model.Bill;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Utilities that all the test classes may use
 */
public class TestBase {

    /**
     * get the last item from a ordered map
     * 
     * @param <K>
     * @param <V>
     * @param map
     * @return
     */
    protected <K, V> Map.Entry<K, V> getLastEntryInMap(LinkedHashMap<K, V> map) {
        Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
        Map.Entry<K, V> result = null;
        while (iterator.hasNext()) {
            result = iterator.next();
        }
        return result;
    }

    /**
     * allows gson to deserialize a the specific structure used by the
     * "BillRestService" from a string
     */
    protected LinkedHashMap<Integer, Bill> deserializeBillMap(String billsString) {
        Gson specialGson = new Gson();
        Type mapType = new TypeToken<Map<Integer, Bill>>() {
        }.getType();
        return specialGson.fromJson(billsString, mapType);
    }
}
