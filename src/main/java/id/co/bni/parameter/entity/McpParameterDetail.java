package id.co.bni.parameter.entity;

import id.co.bni.parameter.dto.response.McpParameterDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "MCP_PARAMETER_DETAIL")
@IdClass(McpParameterDetailId.class)
public class McpParameterDetail {
//    @Id
//    @GeneratedValue(generator = "system-uuid2")
//    @GenericGenerator(name = "system-uuid2", strategy = "uuid2")
//    private String id;
    @Id
    @Column(name = "MCP_ID", nullable = false, length = 50)
    private String mcpId;
    @Id
    @Column(name = "TRX_FIELD", nullable = false, length = 30)
    private String trxField;
    @Id
    @Column(name = "START_WITH", nullable = false, length = 20)
    private String startWith;
    @Column(name = "BILLER_CODE", nullable = false, length = 10)
    private String billerCode;
    @Column(name = "REGION_CODE", nullable = false, length = 10)
    private String regionCode;

    public McpParameterDetailResponse toMcpDetailResponse() {
        return McpParameterDetailResponse.builder()
                .trxField(trxField)
                .startWith(startWith)
                .billerCode(billerCode)
                .regionCode(regionCode)
                .build();
    }
}
