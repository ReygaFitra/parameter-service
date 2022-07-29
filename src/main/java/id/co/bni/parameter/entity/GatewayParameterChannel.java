package id.co.bni.parameter.entity;

import id.co.bni.parameter.dto.request.GatewayParameterRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "GATEWAY_PARAMETER_CHANNEL")
public class GatewayParameterChannel {
    @Id
    @Column(name = "TRANS_CODE", nullable = false, length = 20)
    private String transCode;
    @Column(name = "SYSTEM_ID", length = 50)
    private String systemId;
    @Column(name = "URL", length = 150)
    private String url;
    @Column(nullable = false)
    private Boolean isUsingProxy;
    @Column(name = "PROXY_IP", length = 20)
    private String proxyIp;
    @Column(name = "PROXY_PORT", length = 10)
    private String proxyPort;

    public GatewayParameterRequest toGatewayParameterResponse() {
        return GatewayParameterRequest.builder()
                .transCode(transCode)
                .systemId(systemId)
                .url(url)
                .isUsingProxy(isUsingProxy)
                .proxyIp(proxyIp)
                .proxyPort(proxyPort)
                .build();
    }
}
