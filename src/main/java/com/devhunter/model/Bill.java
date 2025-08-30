package com.devhunter.model;

import com.google.gson.Gson;

public class Bill {

    private Double total;
    private String date;
    private String companyName;

    public Bill(Double total, String date, String companyName) {
        this.total = total;
        this.date = date;
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

    public Double getTotal() {
        return total;
    }

    public String getDate() {
        return date;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
