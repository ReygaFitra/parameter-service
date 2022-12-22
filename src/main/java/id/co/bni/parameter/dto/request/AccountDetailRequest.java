package id.co.bni.parameter.dto.request;

import id.co.bni.parameter.validation.annotation.NumberOnly;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailRequest implements Serializable {
    @NotNull
    @NotBlank
    @NumberOnly
    @Size(max = 20)
    private String dbAccount;
    @NotNull
    @NotBlank
    @Size(max = 100)
    private String dbAccountName;
}
