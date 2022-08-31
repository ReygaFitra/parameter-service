package id.co.bni.parameter.services;

import id.co.bni.parameter.cache.ParameterLoader;
import id.co.bni.parameter.dto.ResponseService;
import id.co.bni.parameter.dto.request.GatewayParameterRequest;
import id.co.bni.parameter.entity.GatewayParameterChannel;
import id.co.bni.parameter.repository.GatewayParameterChannelRepo;
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
public class GatewayParameterService {

    private final GatewayParameterChannelRepo gatewayParameterChannelRepo;
    private final ParameterLoader parameterLoader;
    private final CacheService cacheService;

    @Transactional
    public ResponseService create(GatewayParameterRequest req) {
        if (gatewayParameterChannelRepo.findById(req.getTransCode()).isPresent())
            return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_ALREADY_EXIST, null, "");

        if (req.getIsUsingProxy() && (req.getProxyIp() == null || "".equals(req.getProxyIp())))
            return ResponseUtil.setResponseSuccessCustom(RestConstants.RESPONSE.WRONG_FORMAT_DATA, null, "proxyIp - must not be blank or null", "");

        if (req.getIsUsingProxy() && (req.getProxyPort() == null || "".equals(req.getProxyPort())))
            return ResponseUtil.setResponseSuccessCustom(RestConstants.RESPONSE.WRONG_FORMAT_DATA, null, "proxyPort - must not be blank or null", "");

        GatewayParameterChannel gatewayParameterChannel = GatewayParameterChannel.builder()
                .transCode(req.getTransCode())
                .systemId(req.getSystemId())
                .url(req.getUrl())
                .isUsingProxy(req.getIsUsingProxy())
                .proxyIp(req.getProxyIp())
                .proxyPort(req.getProxyPort())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        gatewayParameterChannelRepo.save(gatewayParameterChannel);
        loadCache(gatewayParameterChannel);
        return ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, gatewayParameterChannel, "");
    }

    @Transactional
    public ResponseService update(GatewayParameterRequest req) {
        GatewayParameterChannel channel = gatewayParameterChannelRepo.findByTransCode(req.getTransCode());
        if (channel == null)
            return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, "");

        channel.setSystemId(req.getSystemId());
        channel.setUrl(req.getUrl());
        channel.setIsUsingProxy(req.getIsUsingProxy());
        channel.setProxyIp(req.getProxyIp());
        channel.setProxyPort(req.getProxyPort());
        channel.setUpdatedAt(new Date());
        gatewayParameterChannelRepo.saveAndFlush(channel);
        loadCache(channel);
        return ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, channel, "");
    }

    @Transactional
    public ResponseService delete(GatewayParameterRequest req) {
        GatewayParameterChannel channel = gatewayParameterChannelRepo.findByTransCode(req.getTransCode());
        if (channel == null)
            return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, "");

        gatewayParameterChannelRepo.delete(channel);
        return cacheService.reloadByKey(RestConstants.CACHE_NAME.GATEWAY_PARAMETER, req.getTransCode());
    }

    public ResponseService findByTransCode(String transCode) {
        GatewayParameterRequest gatewayParameterChannel = parameterLoader.getGatewayParam(transCode);
        if (gatewayParameterChannel == null) return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, "");
        return ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, gatewayParameterChannel, "");
    }

    public ResponseService findAll() {
        Collection<GatewayParameterRequest> collection = parameterLoader.getAllGatewayParam();
        if (collection.isEmpty()) return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, "");
        return ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, collection, "");
    }

    private void loadCache(GatewayParameterChannel gatewayParameterChannel) {
        ConcurrentHashMap<String, GatewayParameterRequest> hGatewayParameter = new ConcurrentHashMap<>();
        hGatewayParameter.put(gatewayParameterChannel.getTransCode(), gatewayParameterChannel.toGatewayParameterResponse());
        parameterLoader.clearAndPut(RestConstants.CACHE_NAME.GATEWAY_PARAMETER, gatewayParameterChannel.getTransCode(), hGatewayParameter);
    }
}
