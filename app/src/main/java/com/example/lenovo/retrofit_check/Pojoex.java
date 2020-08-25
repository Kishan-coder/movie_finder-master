
package com.example.lenovo.retrofit_check;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pojoex {

    @SerializedName("Search")
    @Expose
    private List<Search> search = null;
    public String searchName;
    @SerializedName("Response")
    @Expose
    private String response;

    public List<Search> getSearch() {
        return search;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}
