package id.co.bni.parameter.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

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
    @Valid
    private List<AccountDetailRequest> listAccount;
}
