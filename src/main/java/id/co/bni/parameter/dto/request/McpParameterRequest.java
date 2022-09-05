package id.co.bni.parameter.dto.request;

import id.co.bni.parameter.dto.response.McpParameterDetailResponse;
import id.co.bni.parameter.dto.response.McpParameterFeeResponse;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class McpParameterRequest implements Serializable {
    @NotNull
    @NotBlank
    private String mcpId;
    @NotNull
    @NotBlank
    private String billerName;
    @NotNull
    private Boolean isMatch;
    @Valid
    private List<McpParameterDetailResponse> detail;
    @Valid
    private List<McpParameterFeeResponse> dataFee;
}