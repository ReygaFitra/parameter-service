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
public class GatewayParameterRequest implements Serializable {
    @NotNull
    @NotBlank
    private String transCode;
    @NotNull
    @NotBlank
    private String systemId;
    @NotNull
    @NotBlank
    private String url;
    @NotNull
    private Boolean isUsingProxy;
    private String proxyIp;
    private String proxyPort;
}
