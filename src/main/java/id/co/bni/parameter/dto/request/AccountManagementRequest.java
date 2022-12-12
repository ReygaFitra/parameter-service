package id.co.bni.parameter.dto.request;

import id.co.bni.parameter.validation.annotation.NumberOnly;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AccountManagementRequest implements Serializable {
    @NotNull
    @NotBlank
    private String companyId;
    @NotNull
    @NotBlank
    private String companyName;
    @NotNull
    @NotBlank
    @NumberOnly
    private String dbAccount;
}
