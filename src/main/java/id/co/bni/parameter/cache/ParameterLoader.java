package id.co.bni.parameter.cache;

import com.hazelcast.core.HazelcastInstance;
import id.co.bni.parameter.dto.request.GatewayParameterRequest;
import id.co.bni.parameter.repository.GatewayParameterChannelRepo;
import id.co.bni.parameter.util.RestConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class ParameterLoader {
    private final HazelcastInstance hazelcastInstance;
    private final GatewayParameterChannelRepo parameterChannelRepository;

    @Async
    void load() {
        loadGatewayParameter();
    }

    private void loadGatewayParameter() {
        ConcurrentHashMap<String, GatewayParameterRequest> hGatewayParameter = new ConcurrentHashMap<>();
        try {
            parameterChannelRepository.findAll()
                    .forEach(gatewayParam -> hGatewayParameter.put(gatewayParam.getTransCode(), gatewayParam.toGatewayParameterResponse()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        clearPackHazelcast(RestConstants.CACHE_NAME.GATEWAY_PARAMETER, hGatewayParameter);
    }

    private void clearPackHazelcast(String cacheName, Map map) {
        hazelcastInstance.getMap(cacheName).evictAll();
        hazelcastInstance.getMap(cacheName).clear();
        hazelcastInstance.getMap(cacheName).putAll(map);
    }

    @Async
    public void clearAndPut(String cacheName, String key, Map map) {
        if (hazelcastInstance.getMap(cacheName).get(key) != null)
            hazelcastInstance.getMap(cacheName).evict(key);
        hazelcastInstance.getMap(cacheName).putAll(map);
    }

    public Collection<GatewayParameterRequest> getAllGatewayParam() {
        Map<String, GatewayParameterRequest> h = (Map<String, GatewayParameterRequest>) checkAndGet(RestConstants.CACHE_NAME.GATEWAY_PARAMETER);
        return h.values();
    }

    public GatewayParameterRequest getGatewayParam(String transCode) {
        Map<String, GatewayParameterRequest> h = (Map<String, GatewayParameterRequest>) checkAndGet(RestConstants.CACHE_NAME.GATEWAY_PARAMETER);
        return h.get(transCode);
    }

    private Map<?, ?> checkAndGet(String cacheName) {
        Map<?, ?> map = hazelcastInstance.getMap(cacheName);
        if (map.isEmpty()) {
            load();
            map = hazelcastInstance.getMap(cacheName);
        }
        return map;
    }
}