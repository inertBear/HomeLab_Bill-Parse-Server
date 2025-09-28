package com.devhunter.model;

import com.google.gson.Gson;

public class Bill {

    private String total;
    private String date;
    private Company company;

    public Bill(Company company, String total, String date) {
        this.total = total;
        this.date = date;
        this.company = company;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

    public String getTotal() {
        return total;
    }

    public String getDate() {
        return date;
    }

    public Company getCompany() {
        return company;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCompanyName(Company company) {
        this.company = company;
    }

}
