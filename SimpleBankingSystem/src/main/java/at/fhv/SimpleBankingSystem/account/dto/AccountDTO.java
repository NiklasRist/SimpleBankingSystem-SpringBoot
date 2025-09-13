package at.fhv.SimpleBankingSystem.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AccountDTO {
    @NotBlank(message = "Name darf nicht leer sein")
    public String name;
    @NotNull
    public BigDecimal balance;
}
