
package com.risahabhapps.zomatoapiinternshala.POJO.RestautantPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HasMenuStatus {

    @SerializedName("delivery")
    @Expose
    private String delivery;
    @SerializedName("takeaway")
    @Expose
    private String takeaway;

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getTakeaway() {
        return takeaway;
    }

    public void setTakeaway(String takeaway) {
        this.takeaway = takeaway;
    }

}
