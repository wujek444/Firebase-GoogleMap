package pl.jwojcik.gascompanion.models;

import java.util.Date;

public class Price {

    private String userEmail;
    private Double value;
    private Date insertDt;
    private GasType gasType;

    public Price() {
    }

    public Price(Double value, GasType gasType) {
        this.userEmail = CurrentUserService.getLoggedUser().getEmail();
        this.value = value;
        this.insertDt = new Date();
        this.gasType = gasType;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Date getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(Date insertDt) {
        this.insertDt = insertDt;
    }

    public GasType getGasType() {
        return gasType;
    }

    public void setGasType(GasType gasType) {
        this.gasType = gasType;
    }
}
