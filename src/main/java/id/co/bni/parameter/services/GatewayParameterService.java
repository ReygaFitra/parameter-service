package id.co.bni.parameter.services;

import id.co.bni.parameter.dto.ResponseService;
import id.co.bni.parameter.dto.request.GatewayParameterRequest;
import id.co.bni.parameter.entity.GatewayParameterChannel;
import id.co.bni.parameter.repository.GatewayParameterChannelRepository;
import id.co.bni.parameter.util.ResponseUtil;
import id.co.bni.parameter.util.RestConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GatewayParameterService {

    private final GatewayParameterChannelRepository gatewayParameterChannelRepository;

    @Transactional
    public ResponseService create(GatewayParameterRequest req) {
        if (!validateNewDataById(req.getTransCode())) {
            return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_ALREADY_EXIST, null, "");
        }
        if (req.getIsUsingProxy() && (req.getProxyIp() == null || "".equals(req.getProxyIp()))) {
            return ResponseUtil.setResponseSuccessCustom(RestConstants.RESPONSE.WRONG_FORMAT_DATA, null, "proxyIp - must not be blank or null", "");
        }
        if (req.getIsUsingProxy() && (req.getProxyPort() == null || "".equals(req.getProxyPort()))) {
            return ResponseUtil.setResponseSuccessCustom(RestConstants.RESPONSE.WRONG_FORMAT_DATA, null, "proxyPort - must not be blank or null", "");
        }
        GatewayParameterChannel gatewayParameterChannel = new GatewayParameterChannel();
        gatewayParameterChannel.setTransCode(req.getTransCode());
        gatewayParameterChannel.setSystemId(req.getSystemId());
        gatewayParameterChannel.setUrl(req.getUrl());
        gatewayParameterChannel.setIsUsingProxy(req.getIsUsingProxy());
        gatewayParameterChannel.setProxyIp(req.getProxyIp());
        gatewayParameterChannel.setProxyPort(req.getProxyPort());
        gatewayParameterChannelRepository.save(gatewayParameterChannel);
        return ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, gatewayParameterChannel, "");
    }

    @Transactional
    public ResponseService findByTransCode(String transCode) {
        GatewayParameterChannel gatewayParameterChannel = gatewayParameterChannelRepository.findByTransCode(transCode);
        if (gatewayParameterChannel == null) return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, "");
        return ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, gatewayParameterChannel, "");
    }

    private boolean validateNewDataById(String transCode) {
        boolean isValid = true;
        GatewayParameterChannel channel = gatewayParameterChannelRepository.findByTransCode(transCode);
        if (channel != null) isValid = false;
        return isValid;
    }
}
