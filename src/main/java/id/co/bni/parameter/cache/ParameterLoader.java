package id.co.bni.parameter.cache;

import com.hazelcast.core.HazelcastInstance;
import id.co.bni.parameter.dto.request.*;
import id.co.bni.parameter.dto.response.McpParameterDetailResponse;
import id.co.bni.parameter.dto.response.McpParameterFeeResponse;
import id.co.bni.parameter.entity.AccountManagementDetail;
import id.co.bni.parameter.entity.McpParameterDetail;
import id.co.bni.parameter.entity.McpParameterFee;
import id.co.bni.parameter.repository.*;
import id.co.bni.parameter.util.RestConstants;
import id.co.bni.parameter.util.RestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class ParameterLoader {
    private final HazelcastInstance hazelcastInstance;
    private final GatewayParameterChannelRepo parameterChannelRepository;
    private final McpParameterRepo mcpParameterRepo;
    private final McpParameterFeeRepo mcpParameterFeeRepo;
    private final ChannelParameterRepo channelParameterRepo;
    private final McpParameterDetailRepo mcpParameterDetailRepo;
    private final KeyParameterRepo keyParameterRepo;
    private final AccountManageRepo accountManageRepo;
    private final AccountManageDetailRepo accountManageDetailRepo;

    @Async
    void load() {
        loadGatewayParameter();
        loadMcpParameter();
        loadChannelParameter();
        loadKeyParameter();
        loadAccountParameter();
    }

    private void loadGatewayParameter() {
        ConcurrentHashMap<String, GatewayParameterRequest> hGatewayParameter = new ConcurrentHashMap<>();
        try {
            // id = transCode + systemIdOrMcpId
            parameterChannelRepository.findAll()
                    .forEach(gatewayParam -> {
                        String id = gatewayParam.getTransCode()+gatewayParam.getSystemIdOrMcpId();
                        hGatewayParameter.put(id, gatewayParam.toGatewayParameterResponse());
                    });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        clearPackHazelcast(RestConstants.CACHE_NAME.GATEWAY_PARAMETER, hGatewayParameter);
    }

    private void loadMcpParameter() {
        ConcurrentHashMap<String, McpParameterRequest> hMcpParameter = new ConcurrentHashMap<>();
        try {
            mcpParameterRepo.findAll()
                    .forEach(mcpParameter ->
                            {
                                List<McpParameterDetailResponse> listDet = null;
                                List<McpParameterDetail> listDataDet = mcpParameterDetailRepo.findByMcpId(mcpParameter.getMcpId());
                                if (listDataDet != null && !listDataDet.isEmpty()) {
                                    listDet = new ArrayList<>();
                                    for (McpParameterDetail a : listDataDet) {
                                        listDet.add(McpParameterDetailResponse.builder()
                                                        .trxField(a.getTrxField())
                                                        .match(a.getMatchRegex())
                                                        .position(a.getPosition())
                                                        .billerCode(a.getBillerCode())
                                                        .billerName(a.getBillerName())
                                                        .regionCode(a.getRegionCode())
                                                .build());
                                    }
                                }

                                List<McpParameterFeeResponse> listFee = null;
                                List<McpParameterFee> listDataFee = mcpParameterFeeRepo.findByMcpId(mcpParameter.getMcpId());
                                if (listDataFee != null && !listDataFee.isEmpty()) {
                                    listFee = new ArrayList<>();
                                    for (McpParameterFee a : listDataFee) {
                                        listFee.add(McpParameterFeeResponse.builder()
                                                        .currency(a.getCurrency())
                                                        .fee(RestUtil.df.format(a.getFee().doubleValue()))
                                                .build());
                                    }
                                }
                                hMcpParameter.put(mcpParameter.getMcpId(), mcpParameter.toMcpParameterResponse(listFee, listDet));
                            }
                    );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        clearPackHazelcast(RestConstants.CACHE_NAME.MCP_PARAMETER, hMcpParameter);
    }

    private void loadChannelParameter() {
        ConcurrentHashMap<String, ChannelParameterRequest> hChannelParameter = new ConcurrentHashMap<>();
        try {
            // id = channelId + systemId
            channelParameterRepo.findAll()
                    .forEach(channelParameter -> hChannelParameter.put(channelParameter.getChannelId()+channelParameter.getSystemId(), channelParameter.toChannelParameterResponse()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        clearPackHazelcast(RestConstants.CACHE_NAME.CHANNEL_PARAMETER, hChannelParameter);
    }

    private void loadKeyParameter() {
        ConcurrentHashMap<String, KeyParameterRequest> hKeyParameter = new ConcurrentHashMap<>();
        try {
            keyParameterRepo.findAll()
                    .forEach(keyParameter -> hKeyParameter.put(keyParameter.getKey(), keyParameter.toKeyParameterResponse()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        clearPackHazelcast(RestConstants.CACHE_NAME.KEY_PARAMETER, hKeyParameter);
    }

    private void loadAccountParameter() {
        ConcurrentHashMap<String, AccountManagementRequest> hAccountParameter = new ConcurrentHashMap<>();
        try {
            accountManageRepo.findAll()
                    .forEach(account ->
                            {
                                List<AccountDetailRequest> listDet = null;
                                List<AccountManagementDetail> listAccountDet = accountManageDetailRepo.findByCompanyId(account.getCompanyId());
                                if (listAccountDet != null && !listAccountDet.isEmpty()) {
                                    listDet = new ArrayList<>();
                                    for (AccountManagementDetail a : listAccountDet) {
                                        listDet.add(AccountDetailRequest.builder()
                                                        .dbAccount(a.getDbAccount())
                                                        .dbAccountName(a.getDbAccountName())
                                                .build());
                                    }
                                }
                                hAccountParameter.put(account.getCompanyId(), account.toAccountManagementResponse(listDet));
                            }
                    );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        clearPackHazelcast(RestConstants.CACHE_NAME.ACCOUNT_MANAGEMENT, hAccountParameter);
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


    public Collection<McpParameterRequest> getAllMcpParam() {
        Map<String, McpParameterRequest> h = (Map<String, McpParameterRequest>) checkAndGet(RestConstants.CACHE_NAME.MCP_PARAMETER);
        return h.values();
    }

    public McpParameterRequest getMcpParam(String mcpId) {
        Map<String, McpParameterRequest> h = (Map<String, McpParameterRequest>) checkAndGet(RestConstants.CACHE_NAME.MCP_PARAMETER);
        return h.get(mcpId);
    }

    public Collection<ChannelParameterRequest> getAllChannelParam() {
        Map<String, ChannelParameterRequest> h = (Map<String, ChannelParameterRequest>) checkAndGet(RestConstants.CACHE_NAME.CHANNEL_PARAMETER);
        return h.values();
    }

    public ChannelParameterRequest getChannelParam(String channelIdAndSystemId) {
        Map<String, ChannelParameterRequest> h = (Map<String, ChannelParameterRequest>) checkAndGet(RestConstants.CACHE_NAME.CHANNEL_PARAMETER);
        return h.get(channelIdAndSystemId);
    }

    public Collection<KeyParameterRequest> getAllKeyParam() {
        Map<String, KeyParameterRequest> h = (Map<String, KeyParameterRequest>) checkAndGet(RestConstants.CACHE_NAME.KEY_PARAMETER);
        return h.values();
    }

    public KeyParameterRequest getKeyParam(String key) {
        Map<String, KeyParameterRequest> h = (Map<String, KeyParameterRequest>) checkAndGet(RestConstants.CACHE_NAME.KEY_PARAMETER);
        return h.get(key);
    }

    public Collection<AccountManagementRequest> getAllAccountParam() {
        Map<String, AccountManagementRequest> h = (Map<String, AccountManagementRequest>) checkAndGet(RestConstants.CACHE_NAME.ACCOUNT_MANAGEMENT);
        return h.values();
    }

    public AccountManagementRequest getAccountParam(String key) {
        Map<String, AccountManagementRequest> h = (Map<String, AccountManagementRequest>) checkAndGet(RestConstants.CACHE_NAME.ACCOUNT_MANAGEMENT);
        return h.get(key);
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
