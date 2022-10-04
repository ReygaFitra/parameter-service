package id.co.bni.parameter.dto.request;

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
public class GatewayParameterRequest implements Serializable {
    @NotNull
    @NotBlank
    @Size(max = 20)
    private String transCode;
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String systemIdOrmcpId;
    @NotNull
    @NotBlank
    @Size(max = 200)
    private String url;
    @NotNull
    private Boolean isUsingProxy;
    private String proxyIp;
    private String proxyPort;
}
