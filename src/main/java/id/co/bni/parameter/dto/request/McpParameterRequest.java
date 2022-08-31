package id.co.bni.parameter.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class McpParameterRequest implements Serializable {
    private String mcpId;
    private String billerCode;
    private String regionCode;
    private String billerName;
}
