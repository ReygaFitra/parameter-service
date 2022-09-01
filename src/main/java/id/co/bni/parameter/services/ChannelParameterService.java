package id.co.bni.parameter.services;

import id.co.bni.parameter.cache.ParameterLoader;
import id.co.bni.parameter.dto.ResponseService;
import id.co.bni.parameter.dto.request.ChannelParameterRequest;
import id.co.bni.parameter.entity.ChannelParameter;
import id.co.bni.parameter.repository.ChannelParameterRepo;
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
public class ChannelParameterService {
    private final ChannelParameterRepo channelParameterRepo;
    private final ParameterLoader parameterLoader;
    private final CacheService cacheService;

    @Transactional
    public ResponseService create(ChannelParameterRequest req) {
        if (channelParameterRepo.findById(req.getChannelId()).isPresent())
            return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_ALREADY_EXIST, null, "");

        ChannelParameter channelParameter = ChannelParameter.builder()
                .channelId(req.getChannelId())
                .branch(req.getBranch())
                .teller(req.getTeller())
                .overrideFlag(req.getOverrideFlag())
                .tandemFlag(req.getTandemFlag())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        channelParameterRepo.save(channelParameter);
        loadCache(channelParameter);
        return ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, channelParameter, "");
    }

    @Transactional
    public ResponseService update(ChannelParameterRequest req) {
        ChannelParameter channel = channelParameterRepo.findByChannelId(req.getChannelId());
        if (channel == null)
            return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, "");

        channel.setBranch(req.getBranch());
        channel.setTeller(req.getTeller());
        channel.setOverrideFlag(req.getOverrideFlag());
        channel.setTandemFlag(req.getTandemFlag());
        channel.setUpdatedAt(new Date());
        channelParameterRepo.saveAndFlush(channel);
        loadCache(channel);
        return ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, channel, "");
    }

    @Transactional
    public ResponseService delete(ChannelParameterRequest req) {
        ChannelParameter channel = channelParameterRepo.findByChannelId(req.getChannelId());
        if (channel == null)
            return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, "");

        channelParameterRepo.delete(channel);
        return cacheService.reloadByKey(RestConstants.CACHE_NAME.GATEWAY_PARAMETER, req.getChannelId());
    }

    public ResponseService findByTransCode(String channelId) {
        ChannelParameterRequest channelParameterRequest = parameterLoader.getChannelParam(channelId);
        if (channelParameterRequest == null) return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, "");
        return ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, channelParameterRequest, "");
    }

    public ResponseService findAll() {
        Collection<ChannelParameterRequest> collection = parameterLoader.getAllChannelParam();
        if (collection.isEmpty()) return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, "");
        return ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, collection, "");
    }

    private void loadCache(ChannelParameter channelParameter) {
        ConcurrentHashMap<String, ChannelParameterRequest> hChannelParameter = new ConcurrentHashMap<>();
        hChannelParameter.put(channelParameter.getChannelId(), channelParameter.toChannelParameterResponse());
        parameterLoader.clearAndPut(RestConstants.CACHE_NAME.CHANNEL_PARAMETER, channelParameter.getChannelId(), hChannelParameter);
    }
}
