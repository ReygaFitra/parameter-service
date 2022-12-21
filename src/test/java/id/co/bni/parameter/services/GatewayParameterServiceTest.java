package id.co.bni.parameter.services;

import id.co.bni.parameter.cache.ParameterLoader;
import id.co.bni.parameter.dto.ResponseService;
import id.co.bni.parameter.dto.request.GatewayParameterRequest;
import id.co.bni.parameter.entity.GatewayParameterChannel;
import id.co.bni.parameter.entity.GatewayParameterChannelId;
import id.co.bni.parameter.repository.GatewayParameterChannelRepo;
import id.co.bni.parameter.util.ResponseUtil;
import id.co.bni.parameter.util.RestConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

class GatewayParameterServiceTest {

    @InjectMocks
    GatewayParameterService gatewayParameterService;

    @Mock
    GatewayParameterChannelRepo gatewayParameterChannelRepo;

    @Mock
    ParameterLoader parameterLoader;

    @Mock
    CacheService cacheService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create() {
        Mockito.when(gatewayParameterChannelRepo.findById(ArgumentMatchers.any(GatewayParameterChannelId.class))).thenReturn(Optional.empty());
        GatewayParameterChannel gatewayParameterChannel = GatewayParameterChannel.builder().transCode("95477").systemIdOrMcpId("PIHC_PKC").proxyIp("-").proxyPort("-").isUsingProxy(false).url("http://mhp-pihc-pkc-inquiry.mhp.svc.cluster.local:8080/mhp/pihc-pkc-inquiry").createdAt(new Date()).updatedAt(new Date()).build();
        Mockito.when(gatewayParameterChannelRepo.save(ArgumentMatchers.any(GatewayParameterChannel.class))).thenReturn(gatewayParameterChannel);

        GatewayParameterRequest request = GatewayParameterRequest.builder().build();
        request.setTransCode("95477");
        request.setSystemIdOrMcpId("PIHC_PKC");
        request.setIsUsingProxy(false);
        request.setProxyIp("-");
        request.setProxyPort("-");
        request.setUrl("http://mhp-pihc-pkc-inquiry.mhp.svc.cluster.local:8080/mhp/pihc-pkc-inquiry");

        ResponseEntity<ResponseService> response = gatewayParameterService.create(request);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("00", response.getBody().getStatusCode());
        GatewayParameterChannel resp = (GatewayParameterChannel) response.getBody().getData();
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(gatewayParameterChannel.getTransCode(), resp.getTransCode());
        Assertions.assertEquals(gatewayParameterChannel.getSystemIdOrMcpId(), resp.getSystemIdOrMcpId());
    }

    @Test
    void createDuplicate() {
        GatewayParameterChannel gatewayParameterChannel = GatewayParameterChannel.builder().transCode("95477").systemIdOrMcpId("PIHC_PKC").proxyIp("-").proxyPort("-").isUsingProxy(false).url("http://mhp-pihc-pkc-inquiry.mhp.svc.cluster.local:8080/mhp/pihc-pkc-inquiry").createdAt(new Date()).updatedAt(new Date()).build();
        Mockito.when(gatewayParameterChannelRepo.findById(ArgumentMatchers.any(GatewayParameterChannelId.class))).thenReturn(Optional.of(gatewayParameterChannel));

        GatewayParameterRequest request = GatewayParameterRequest.builder().build();
        request.setTransCode("95477");
        request.setSystemIdOrMcpId("PIHC_PKC");
        request.setIsUsingProxy(false);
        request.setProxyIp("-");
        request.setProxyPort("-");
        request.setUrl("http://mhp-pihc-pkc-inquiry.mhp.svc.cluster.local:8080/mhp/pihc-pkc-inquiry");
        ResponseEntity<ResponseService> response = gatewayParameterService.create(request);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.FOUND, response.getStatusCode());
        Assertions.assertEquals(RestConstants.RESPONSE.DATA_ALREADY_EXIST.getCode(), response.getBody().getStatusCode());
        Assertions.assertNull(response.getBody().getData());
    }

    @Test
    void update() {
        Mockito.when(gatewayParameterChannelRepo.findByTransCodeAndSystemIdOrMcpId(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(GatewayParameterChannel.builder().transCode("95477").systemIdOrMcpId("PIHC_PKC").proxyIp("-").proxyPort("-").isUsingProxy(false).url("http://mhp-pihc-pkc-inquiry.mhp.svc.cluster.local:8080/mhp/pihc-pkc-inquiry").createdAt(new Date()).updatedAt(new Date()).build());
        Mockito.when(gatewayParameterChannelRepo.saveAndFlush(ArgumentMatchers.any(GatewayParameterChannel.class))).thenReturn(GatewayParameterChannel.builder().transCode("95477").systemIdOrMcpId("PIHC_PKC_UPDATE").proxyIp("-").proxyPort("-").isUsingProxy(false).url("http://mhp-pihc-pkc-inquiry.mhp.svc.cluster.local:8080/mhp/pihc-pkc-inquiry").createdAt(new Date()).updatedAt(new Date()).build());

        GatewayParameterRequest request = GatewayParameterRequest.builder().build();
        request.setTransCode("95477");
        request.setSystemIdOrMcpId("PIHC_PKC_UPDATE");
        request.setIsUsingProxy(false);
        request.setProxyIp("-");
        request.setProxyPort("-");
        request.setUrl("http://mhp-pihc-pkc-inquiry.mhp.svc.cluster.local:8080/mhp/pihc-pkc-inquiry");
        ResponseEntity<ResponseService> response = gatewayParameterService.update(request);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("00", response.getBody().getStatusCode());
        GatewayParameterChannel resp = (GatewayParameterChannel) response.getBody().getData();
        Assertions.assertNotNull(resp);
        Assertions.assertNotEquals("PIHC_PKC_UPDATE", resp.getSystemIdOrMcpId());
    }

    @Test
    void updateNotFound() {
        Mockito.when(gatewayParameterChannelRepo.findByTransCodeAndSystemIdOrMcpId(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(null);

        ResponseEntity<ResponseService> response = gatewayParameterService.update(GatewayParameterRequest.builder().transCode("95477").systemIdOrMcpId("PIHC_PKC_UPDATE").isUsingProxy(false).proxyIp("-").proxyPort("-").url("http://mhp-pihc-pkc-inquiry.mhp.svc.cluster.local:8080/mhp/pihc-pkc-inquiry").build());
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(RestConstants.RESPONSE.DATA_NOT_FOUND.getCode(), response.getBody().getStatusCode());
        Assertions.assertNull(response.getBody().getData());
    }

    @Test
    void delete() {
        Mockito.when(gatewayParameterChannelRepo.findByTransCodeAndSystemIdOrMcpId(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(GatewayParameterChannel.builder().transCode("95477").systemIdOrMcpId("PIHC_PKC").proxyIp("-").proxyPort("-").isUsingProxy(false).url("http://mhp-pihc-pkc-inquiry.mhp.svc.cluster.local:8080/mhp/pihc-pkc-inquiry").createdAt(new Date()).updatedAt(new Date()).build());

        Mockito.when(cacheService.reloadByKey(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, true, ""));

        ResponseEntity<ResponseService> response = gatewayParameterService.delete("95477", "PIHC_PKC");
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("00", response.getBody().getStatusCode());
        Assertions.assertTrue((Boolean) response.getBody().getData());
    }

    @Test
    void deleteNotFound() {
        Mockito.when(gatewayParameterChannelRepo.findByTransCodeAndSystemIdOrMcpId(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(null);

        ResponseEntity<ResponseService> response = gatewayParameterService.delete("95477", "PIHC_PKC");
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(RestConstants.RESPONSE.DATA_NOT_FOUND.getCode(), response.getBody().getStatusCode());
        Assertions.assertNull(response.getBody().getData());
    }

    @Test
    void findByTransCodeAndSystemIdOrmcpId() {
        GatewayParameterRequest gatewayParameterRequest = new GatewayParameterRequest();
        gatewayParameterRequest.setTransCode("95477");
        gatewayParameterRequest.setSystemIdOrMcpId("PIHC_PKC");
        gatewayParameterRequest.setUrl("http://mhp-pihc-pkc-inquiry.mhp.svc.cluster.local:8080/mhp/pihc-pkc-inquiry");
        Mockito.when(parameterLoader.getGatewayParam(ArgumentMatchers.anyString())).thenReturn(gatewayParameterRequest);

        ResponseEntity<ResponseService> response = gatewayParameterService.findByTransCodeAndSystemIdOrmcpId("95477", "PIHC_PKC");
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("00", response.getBody().getStatusCode());
        GatewayParameterRequest resp = (GatewayParameterRequest) response.getBody().getData();
        Assertions.assertNotNull(resp);
        Assertions.assertEquals("95477", resp.getTransCode());
        Assertions.assertEquals("PIHC_PKC", resp.getSystemIdOrMcpId());
    }

    @Test
    void findByTransCodeAndSystemIdOrmcpIdNotFound() {
        Mockito.when(parameterLoader.getGatewayParam(ArgumentMatchers.anyString())).thenReturn(null);

        ResponseEntity<ResponseService> response = gatewayParameterService.findByTransCodeAndSystemIdOrmcpId("95477", "PIHC_PKC");
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(RestConstants.RESPONSE.DATA_NOT_FOUND.getCode(), response.getBody().getStatusCode());
        Assertions.assertNull(response.getBody().getData());
    }

    @Test
    void findAll() {
        Collection<GatewayParameterRequest> collection = new ArrayList<>();
        collection.add(GatewayParameterRequest.builder().transCode("95477").systemIdOrMcpId("PIHC_PKC_UPDATE").isUsingProxy(false).proxyIp("-").proxyPort("-").url("http://mhp-pihc-pkc-inquiry.mhp.svc.cluster.local:8080/mhp/pihc-pkc-inquiry").build());
        collection.add(GatewayParameterRequest.builder().transCode("9547723").systemIdOrMcpId("PIHC_PKG").isUsingProxy(false).proxyIp("-").proxyPort("-").url("http://mhp-pihc-pkc-inquiry.mhp.svc.cluster.local:8080/mhp/pihc-pkc-inquiry").build());
        Mockito.when(parameterLoader.getAllGatewayParam()).thenReturn(collection);

        ResponseEntity<ResponseService> response = gatewayParameterService.findAll();
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("00", response.getBody().getStatusCode());

        Collection<GatewayParameterRequest> resp = (Collection<GatewayParameterRequest>) response.getBody().getData();
        Assertions.assertNotNull(resp);
        Assertions.assertFalse(resp.isEmpty());
        Assertions.assertEquals(2, resp.size());
    }

    @Test
    void findAllNotFound() {
        Collection<GatewayParameterRequest> collection = new ArrayList<>();
        Mockito.when(parameterLoader.getAllGatewayParam()).thenReturn(collection);
        ResponseEntity<ResponseService> response = gatewayParameterService.findAll();
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(RestConstants.RESPONSE.DATA_NOT_FOUND.getCode(), response.getBody().getStatusCode());
        Assertions.assertNull(response.getBody().getData());
    }
}