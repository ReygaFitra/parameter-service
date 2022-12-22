package id.co.bni.parameter.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AccountManagementRequest implements Serializable {
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String companyId;
    @NotNull
    @NotBlank
    @Size(max = 100)
    private String companyName;
    @Valid
    private List<AccountDetailRequest> listAccount;
}
