package id.co.bni.parameter.dto.request;

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
public class KeyParameterRequest implements Serializable {
    @NotNull
    @NotBlank
    private String key;
    @NotNull
    @NotBlank
    private String value;
    @NotNull
    @NotBlank
    private String desc;
}
