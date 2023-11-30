package id.co.bni.parameter.services;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Service
public class ReadinessService {
    @PersistenceContext
    private EntityManager entityManager;

    public boolean isDatabaseConnected() {
        String sqlQuery = "SELECT 1 FROM DUAL";
        Query query = entityManager.createNativeQuery(sqlQuery);
        List<?> resultList = query.getResultList();
        return !resultList.isEmpty();
    }
}
