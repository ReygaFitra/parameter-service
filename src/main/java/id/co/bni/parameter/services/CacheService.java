package id.co.bni.parameter.services;

import id.co.bni.parameter.dto.ResponseService;
import id.co.bni.parameter.util.ResponseUtil;
import id.co.bni.parameter.util.RestConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class CacheService {

    private final CacheManager cacheManager;

    public ResponseService reload(String name) {
        if ("".equals(name)) {
            cacheManager.getCacheNames()
                    .forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
        } else {
            Objects.requireNonNull(cacheManager.getCache(name)).clear();
        }
        return ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, true, "");
    }

    public ResponseService reloadByKey(String name, String key) {
        Objects.requireNonNull(cacheManager.getCache(name)).evict(key);
        return ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, true, "");
    }

}
