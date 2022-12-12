package id.co.bni.parameter.services;

import id.co.bni.parameter.cache.ParameterLoader;
import id.co.bni.parameter.dto.ResponseService;
import id.co.bni.parameter.dto.request.AccountManagementRequest;
import id.co.bni.parameter.entity.AccountManagement;
import id.co.bni.parameter.repository.AccountManageRepo;
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
public class AccountManageService {
    private final AccountManageRepo accountManageRepo;
    private final ParameterLoader parameterLoader;
    private final CacheService cacheService;

    @Transactional
    public ResponseEntity<ResponseService> create(AccountManagementRequest req) {
        if (accountManageRepo.findById(req.getCompanyId()).isPresent())
            return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_ALREADY_EXIST, null, ""), HttpStatus.FOUND);

        AccountManagement accountManagement = AccountManagement.builder()
                .companyId(req.getCompanyId())
                .companyName(req.getCompanyName())
                .dbAccount(req.getDbAccount())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        accountManageRepo.save(accountManagement);
        loadCache(accountManagement);
        return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, accountManagement, ""), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ResponseService> update(AccountManagementRequest req) {
        AccountManagement management = accountManageRepo.findByCompanyId(req.getCompanyId());
        if (management == null)
            return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, ""), HttpStatus.NOT_FOUND);

        management.setCompanyName(req.getCompanyName());
        management.setDbAccount(req.getDbAccount());
        management.setUpdatedAt(new Date());
        accountManageRepo.saveAndFlush(management);
        loadCache(management);
        return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, management, ""), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ResponseService> delete(AccountManagementRequest req) {
        AccountManagement management = accountManageRepo.findByCompanyId(req.getCompanyId());
        if (management == null)
            return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, ""), HttpStatus.NOT_FOUND);

        accountManageRepo.delete(management);
        return new ResponseEntity<>(cacheService.reloadByKey(RestConstants.CACHE_NAME.ACCOUNT_MANAGEMENT, req.getCompanyId()), HttpStatus.OK);
    }

    public ResponseEntity<ResponseService> findByCompanyId(String companyId) {
        AccountManagementRequest accountManagementRequest = parameterLoader.getAccountParam(companyId);
        if (accountManagementRequest == null) return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, ""), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, accountManagementRequest, ""), HttpStatus.OK);
    }

    public ResponseEntity<ResponseService> findAll() {
        Collection<AccountManagementRequest> collection = parameterLoader.getAllAccountParam();
        if (collection.isEmpty()) return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, ""), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, collection, ""), HttpStatus.OK);
    }

    private void loadCache(AccountManagement accountManagement) {
        ConcurrentHashMap<String, AccountManagementRequest> hAccountManagement = new ConcurrentHashMap<>();
        hAccountManagement.put(accountManagement.getCompanyId(), accountManagement.toAccountManagementResponse());
        parameterLoader.clearAndPut(RestConstants.CACHE_NAME.ACCOUNT_MANAGEMENT, accountManagement.getCompanyId(), hAccountManagement);
    }
}
