package id.co.bni.parameter.services;

import id.co.bni.parameter.cache.ParameterLoader;
import id.co.bni.parameter.dto.ResponseService;
import id.co.bni.parameter.dto.request.McpParameterRequest;
import id.co.bni.parameter.dto.response.McpParameterDetailResponse;
import id.co.bni.parameter.dto.response.McpParameterFeeResponse;
import id.co.bni.parameter.entity.McpParameter;
import id.co.bni.parameter.entity.McpParameterDetail;
import id.co.bni.parameter.entity.McpParameterFee;
import id.co.bni.parameter.repository.McpParameterDetailRepo;
import id.co.bni.parameter.repository.McpParameterFeeRepo;
import id.co.bni.parameter.repository.McpParameterRepo;
import id.co.bni.parameter.util.ResponseUtil;
import id.co.bni.parameter.util.RestConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class McpParameterService {
    private final McpParameterRepo mcpParameterRepo;
    private final ParameterLoader parameterLoader;
    private final CacheService cacheService;
    private final McpParameterFeeRepo mcpParameterFeeRepo;
    private final McpParameterDetailRepo mcpParameterDetailRepo;

    @Transactional
    public ResponseService create(McpParameterRequest req) {
        if (mcpParameterRepo.findById(req.getMcpId()).isPresent())
            return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_ALREADY_EXIST, null, "");

        McpParameter mcpParameter = McpParameter.builder()
                .mcpId(req.getMcpId())
                .isMatch(req.getIsMatch())
                .billerName(req.getBillerName())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        mcpParameterRepo.save(mcpParameter);

        req.getDetail().forEach(detail -> mcpParameterDetailRepo.save(McpParameterDetail.builder()
                .mcpId(mcpParameter.getMcpId())
                        .trxField(detail.getTrxField())
                        .startWith(detail.getStartWith())
                        .billerCode(detail.getBillerCode())
                        .regionCode(detail.getRegionCode())
                .build()));

        req.getDataFee().forEach(fee -> mcpParameterFeeRepo.save(McpParameterFee.builder()
                .mcpId(mcpParameter.getMcpId())
                .currency(fee.getCurrency())
                .fee(BigDecimal.valueOf(Double.parseDouble(fee.getFee())))
                .build()));
        loadCache(mcpParameter, req.getDataFee(), req.getDetail());
        return ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, mcpParameter, "");
    }

    @Transactional
    public ResponseService update(McpParameterRequest req) {
        McpParameter mcpParameter = mcpParameterRepo.findByMcpId(req.getMcpId());
        if (mcpParameter == null)
            return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, "");

        mcpParameter.setBillerName(req.getBillerName());
        mcpParameter.setIsMatch(req.getIsMatch());
        mcpParameter.setUpdatedAt(new Date());
        mcpParameterRepo.saveAndFlush(mcpParameter);

        List<McpParameterDetail> listDet = mcpParameterDetailRepo.findByMcpId(req.getMcpId());
        if (listDet != null && !listDet.isEmpty()) mcpParameterDetailRepo.deleteAll(listDet);

        List<McpParameterFee> listFee = mcpParameterFeeRepo.findByMcpId(req.getMcpId());
        if (listFee != null && !listFee.isEmpty()) mcpParameterFeeRepo.deleteAll(listFee);

        req.getDataFee().forEach(fee -> mcpParameterFeeRepo.save(McpParameterFee.builder()
                .mcpId(mcpParameter.getMcpId())
                .currency(fee.getCurrency())
                .fee(BigDecimal.valueOf(Double.parseDouble(fee.getFee())))
                .build()));

        req.getDetail().forEach(detail -> mcpParameterDetailRepo.save(McpParameterDetail.builder()
                .mcpId(mcpParameter.getMcpId())
                .trxField(detail.getTrxField())
                .startWith(detail.getStartWith())
                .billerCode(detail.getBillerCode())
                .regionCode(detail.getRegionCode())
                .build()));

        loadCache(mcpParameter, req.getDataFee(), req.getDetail());
        return ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, mcpParameter, "");
    }

    @Transactional
    public ResponseService delete(McpParameterRequest req) {
        McpParameter mcpParameter = mcpParameterRepo.findByMcpId(req.getMcpId());
        if (mcpParameter == null)
            return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, "");

        mcpParameterRepo.delete(mcpParameter);

        List<McpParameterFee> listFee = mcpParameterFeeRepo.findByMcpId(req.getMcpId());
        if (listFee != null && !listFee.isEmpty()) mcpParameterFeeRepo.deleteAll(listFee);

        List<McpParameterDetail> listDet = mcpParameterDetailRepo.findByMcpId(req.getMcpId());
        if (listDet != null && !listDet.isEmpty()) mcpParameterDetailRepo.deleteAll(listDet);

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

    private void loadCache(McpParameter mcpParameter, List<McpParameterFeeResponse> listFee, List<McpParameterDetailResponse> listDet) {
        ConcurrentHashMap<String, McpParameterRequest> hMcpParameter = new ConcurrentHashMap<>();
        hMcpParameter.put(mcpParameter.getMcpId(), mcpParameter.toMcpParameterResponse(listFee, listDet));
        parameterLoader.clearAndPut(RestConstants.CACHE_NAME.MCP_PARAMETER, mcpParameter.getMcpId(), hMcpParameter);
    }
}
