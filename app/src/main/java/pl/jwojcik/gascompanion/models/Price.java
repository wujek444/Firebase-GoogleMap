package pl.jwojcik.gascompanion.models;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.jwojcik.gascompanion.enumerated.GasType;

@Data
@NoArgsConstructor
public class Price {

    private String userEmail;
    private Double value;
    private Date insertDt;
    private GasType gasType;

    public Price(Double value, GasType gasType) {
        this.userEmail = CurrentUserService.getLoggedUser().getEmail();
        this.value = value;
        this.insertDt = new Date();
        this.gasType = gasType;
    }
}
