package id.co.bni.parameter.dto.response;

import id.co.bni.parameter.validation.annotation.NumberOnly;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class McpParameterFeeResponse implements Serializable {
    @NotNull
    @NotBlank
    private String currency;
    @NotNull
    @NotBlank
    @NumberOnly
    private String fee;
}
