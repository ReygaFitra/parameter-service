package id.co.bni.parameter.services;

import id.co.bni.parameter.cache.ParameterLoader;
import id.co.bni.parameter.dto.ResponseService;
import id.co.bni.parameter.dto.request.GatewayParameterRequest;
import id.co.bni.parameter.entity.GatewayParameterChannel;
import id.co.bni.parameter.entity.GatewayParameterChannelId;
import id.co.bni.parameter.repository.GatewayParameterChannelRepo;
import id.co.bni.parameter.util.ResponseUtil;
import id.co.bni.parameter.util.RestConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseService> create(GatewayParameterRequest req) {
        if (gatewayParameterChannelRepo.findById(GatewayParameterChannelId.builder()
                        .transCode(req.getTransCode())
                        .systemIdOrMcpId(req.getSystemIdOrMcpId())
                .build()).isPresent())
            return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_ALREADY_EXIST, null, ""), HttpStatus.FOUND);

        if (req.getIsUsingProxy() && (req.getProxyIp() == null || "".equals(req.getProxyIp())))
            return new ResponseEntity<>(ResponseUtil.setResponseSuccessCustom(RestConstants.RESPONSE.WRONG_FORMAT_DATA, null, "proxyIp - must not be blank or null", ""), HttpStatus.BAD_REQUEST);

        if (req.getIsUsingProxy() && (req.getProxyPort() == null || "".equals(req.getProxyPort())))
            return new ResponseEntity<>(ResponseUtil.setResponseSuccessCustom(RestConstants.RESPONSE.WRONG_FORMAT_DATA, null, "proxyPort - must not be blank or null", ""), HttpStatus.BAD_REQUEST);

        GatewayParameterChannel gatewayParameterChannel = GatewayParameterChannel.builder()
                .transCode(req.getTransCode())
                .systemIdOrMcpId(req.getSystemIdOrMcpId())
                .url(req.getUrl())
                .isUsingProxy(req.getIsUsingProxy())
                .proxyIp(req.getProxyIp())
                .proxyPort(req.getProxyPort())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        gatewayParameterChannelRepo.save(gatewayParameterChannel);
        loadCache(gatewayParameterChannel);
        return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, gatewayParameterChannel, ""), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ResponseService> update(GatewayParameterRequest req) {
        GatewayParameterChannel channel = gatewayParameterChannelRepo.findByTransCodeAndSystemIdOrMcpId(req.getTransCode(), req.getSystemIdOrMcpId());
        if (channel == null)
            return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, ""), HttpStatus.NOT_FOUND);

        channel.setUrl(req.getUrl());
        channel.setIsUsingProxy(req.getIsUsingProxy());
        channel.setProxyIp(req.getProxyIp());
        channel.setProxyPort(req.getProxyPort());
        channel.setUpdatedAt(new Date());
        gatewayParameterChannelRepo.saveAndFlush(channel);
        loadCache(channel);
        return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, channel, ""), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ResponseService> delete(String transCode, String systemIdOrmcpId) {
        GatewayParameterChannel channel = gatewayParameterChannelRepo.findByTransCodeAndSystemIdOrMcpId(transCode, systemIdOrmcpId);
        if (channel == null)
            return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, ""), HttpStatus.NOT_FOUND);

        gatewayParameterChannelRepo.delete(channel);
        return new ResponseEntity<>(cacheService.reloadByKey(RestConstants.CACHE_NAME.GATEWAY_PARAMETER, transCode+systemIdOrmcpId), HttpStatus.OK);
    }

    public ResponseEntity<ResponseService> findByTransCodeAndSystemIdOrmcpId(String transCode, String systemIdOrmcpId) {
        GatewayParameterRequest gatewayParameterChannel = parameterLoader.getGatewayParam(transCode+systemIdOrmcpId);
        if (gatewayParameterChannel == null) return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, ""), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, gatewayParameterChannel, ""), HttpStatus.OK);
    }

    public ResponseEntity<ResponseService> findAll() {
        Collection<GatewayParameterRequest> collection = parameterLoader.getAllGatewayParam();
        if (collection.isEmpty()) return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, ""), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, collection, ""), HttpStatus.OK);
    }

    private void loadCache(GatewayParameterChannel gatewayParameterChannel) {
        ConcurrentHashMap<String, GatewayParameterRequest> hGatewayParameter = new ConcurrentHashMap<>();
        hGatewayParameter.put(gatewayParameterChannel.getTransCode()+gatewayParameterChannel.getSystemIdOrMcpId(), gatewayParameterChannel.toGatewayParameterResponse());
        parameterLoader.clearAndPut(RestConstants.CACHE_NAME.GATEWAY_PARAMETER, gatewayParameterChannel.getTransCode()+gatewayParameterChannel.getSystemIdOrMcpId(), hGatewayParameter);
    }
}
