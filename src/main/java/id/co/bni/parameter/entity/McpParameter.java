package id.co.bni.parameter.entity;

import id.co.bni.parameter.dto.request.McpParameterRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "MCP_PARAMETER")
public class McpParameter {
    @Id
    @Column(name = "MCP_ID", nullable = false, length = 50)
    private String mcpId;
    @Column(name = "BILLER_CODE", nullable = false, length = 10)
    private String billerCode;
    @Column(name = "REGION_CODE", nullable = false, length = 10)
    private String regionCode;
    @Column(name = "BILLER_NAME", nullable = false, length = 100)
    private String billerName;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    public McpParameterRequest toMcpParameterResponse() {
        return McpParameterRequest.builder()
                .mcpId(mcpId)
                .regionCode(regionCode)
                .billerCode(billerCode)
                .billerName(billerName)
                .build();
    }
}