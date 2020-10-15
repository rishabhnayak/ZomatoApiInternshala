
package com.risahabhapps.zomatoapiinternshala.POJO.GeoPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class R {

    @SerializedName("has_menu_status")
    @Expose
    private HasMenuStatus hasMenuStatus;
    @SerializedName("res_id")
    @Expose
    private Integer resId;
    @SerializedName("is_grocery_store")
    @Expose
    private Boolean isGroceryStore;

    public HasMenuStatus getHasMenuStatus() {
        return hasMenuStatus;
    }

    public void setHasMenuStatus(HasMenuStatus hasMenuStatus) {
        this.hasMenuStatus = hasMenuStatus;
    }

    public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    public Boolean getIsGroceryStore() {
        return isGroceryStore;
    }

    public void setIsGroceryStore(Boolean isGroceryStore) {
        this.isGroceryStore = isGroceryStore;
    }

}
