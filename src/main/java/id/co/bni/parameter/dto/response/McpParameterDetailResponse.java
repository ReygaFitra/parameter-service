package id.co.bni.parameter.dto.response;

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
public class McpParameterDetailResponse implements Serializable {
    @NotNull
    @NotBlank
    private String trxField;
    @NotNull
    @NotBlank
    private String match;
    @NotNull
    @NotBlank
    private String position;
    @NotNull
    @NotBlank
    private String billerCode;
    @NotNull
    @NotBlank
    private String billerName;
    @NotNull
    @NotBlank
    private String regionCode;
}
