package id.co.bni.parameter.services;

import id.co.bni.parameter.cache.ParameterLoader;
import id.co.bni.parameter.dto.ResponseService;
import id.co.bni.parameter.dto.request.GatewayParameterRequest;
import id.co.bni.parameter.dto.request.McpParameterRequest;
import id.co.bni.parameter.entity.GatewayParameterChannel;
import id.co.bni.parameter.entity.McpParameter;
import id.co.bni.parameter.repository.McpParameterRepo;
import id.co.bni.parameter.util.ResponseUtil;
import id.co.bni.parameter.util.RestConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class McpParameterService {
    private final McpParameterRepo mcpParameterRepo;
    private final ParameterLoader parameterLoader;
    private final CacheService cacheService;

    @Transactional
    public ResponseService create(McpParameterRequest req) {
        if (mcpParameterRepo.findById(req.getMcpId()).isPresent())
            return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_ALREADY_EXIST, null, "");

        McpParameter mcpParameter = McpParameter.builder()
                .mcpId(req.getMcpId())
                .billerCode(req.getBillerCode())
                .regionCode(req.getRegionCode())
                .billerName(req.getBillerName())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        mcpParameterRepo.save(mcpParameter);
        loadCache(mcpParameter);
        return ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, mcpParameter, "");
    }

    @Transactional
    public ResponseService update(McpParameterRequest req) {
        McpParameter mcpParameter = mcpParameterRepo.findByMcpId(req.getMcpId());
        if (mcpParameter == null)
            return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, "");

        mcpParameter.setBillerCode(req.getBillerCode());
        mcpParameter.setRegionCode(req.getRegionCode());
        mcpParameter.setBillerName(req.getBillerName());
        mcpParameter.setUpdatedAt(new Date());
        mcpParameterRepo.saveAndFlush(mcpParameter);
        loadCache(mcpParameter);
        return ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, mcpParameter, "");
    }

    @Transactional
    public ResponseService delete(McpParameterRequest req) {
        McpParameter mcpParameter = mcpParameterRepo.findByMcpId(req.getMcpId());
        if (mcpParameter == null)
            return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, "");

        mcpParameterRepo.delete(mcpParameter);
        return cacheService.reloadByKey(RestConstants.CACHE_NAME.MCP_PARAMETER, req.getMcpId());
    }

    public ResponseService findByMcpId(String mcpId) {
        McpParameterRequest mcpParameterRequest = parameterLoader.getMcpParam(mcpId);
        if (mcpParameterRequest == null) return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, "");
        return ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, mcpParameterRequest, "");
    }

    public ResponseService findAll() {
        Collection<McpParameterRequest> collection = parameterLoader.getAllMcpParam();
        if (collection.isEmpty()) return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, "");
        return ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, collection, "");
    }

    private void loadCache(McpParameter mcpParameter) {
        ConcurrentHashMap<String, McpParameterRequest> hMcpParameter = new ConcurrentHashMap<>();
        hMcpParameter.put(mcpParameter.getMcpId(), mcpParameter.toMcpParameterResponse());
        parameterLoader.clearAndPut(RestConstants.CACHE_NAME.MCP_PARAMETER, mcpParameter.getMcpId(), hMcpParameter);
    }
}
