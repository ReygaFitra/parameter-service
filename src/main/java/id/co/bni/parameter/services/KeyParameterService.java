package id.co.bni.parameter.services;

import id.co.bni.parameter.cache.ParameterLoader;
import id.co.bni.parameter.dto.ResponseService;
import id.co.bni.parameter.dto.request.KeyParameterRequest;
import id.co.bni.parameter.entity.KeyParameter;
import id.co.bni.parameter.repository.KeyParameterRepo;
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
public class KeyParameterService {
    private final KeyParameterRepo keyParameterRepo;
    private final ParameterLoader parameterLoader;
    private final CacheService cacheService;

    @Transactional
    public ResponseService create(KeyParameterRequest req) {
        if (keyParameterRepo.findById(req.getKey()).isPresent())
            return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_ALREADY_EXIST, null, "");

        KeyParameter keyParameter = KeyParameter.builder()
                .key(req.getKey())
                .value(req.getValue())
                .desc(req.getDesc())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        keyParameterRepo.save(keyParameter);
        loadCache(keyParameter);
        return ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, keyParameter, "");
    }

    @Transactional
    public ResponseService update(KeyParameterRequest req) {
        KeyParameter keyParameter = keyParameterRepo.findByKey(req.getKey());
        if (keyParameter == null)
            return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, "");

        keyParameter.setValue(req.getValue());
        keyParameter.setDesc(req.getDesc());
        keyParameter.setUpdatedAt(new Date());
        keyParameterRepo.saveAndFlush(keyParameter);
        loadCache(keyParameter);
        return ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, keyParameter, "");
    }

    @Transactional
    public ResponseService delete(KeyParameterRequest req) {
        KeyParameter keyParameter = keyParameterRepo.findByKey(req.getKey());
        if (keyParameter == null)
            return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, "");

        keyParameterRepo.delete(keyParameter);
        return cacheService.reloadByKey(RestConstants.CACHE_NAME.GATEWAY_PARAMETER, req.getKey());
    }

    public ResponseService findByKey(String key) {
        KeyParameterRequest keyParam = parameterLoader.getKeyParam(key);
        if (keyParam == null) return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, "");
        return ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, keyParam, "");
    }

    public ResponseService findAll() {
        Collection<KeyParameterRequest> collection = parameterLoader.getAllKeyParam();
        if (collection.isEmpty()) return ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, "");
        return ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, collection, "");
    }

    private void loadCache(KeyParameter keyParameter) {
        ConcurrentHashMap<String, KeyParameterRequest> hKeyParameter = new ConcurrentHashMap<>();
        hKeyParameter.put(keyParameter.getKey(), keyParameter.toKeyParameterResponse());
        parameterLoader.clearAndPut(RestConstants.CACHE_NAME.KEY_PARAMETER, keyParameter.getKey(), hKeyParameter);
    }
}
