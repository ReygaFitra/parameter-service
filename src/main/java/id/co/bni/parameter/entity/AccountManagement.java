package id.co.bni.parameter.entity;

import id.co.bni.parameter.dto.request.AccountManagementRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "ACCOUNT_MANAGEMENT")
public class AccountManagement {
    @Id
    @Column(name = "COMPANY_ID", nullable = false, length = 50)
    private String companyId;

    @Column(name = "COMPANY_NAME", nullable = false, length = 100)
    private String companyName;

    @Column(name = "DB_ACCOUNT", nullable = false, length = 20)
    private String dbAccount;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    public AccountManagementRequest toAccountManagementResponse() {
        return AccountManagementRequest.builder()
                .companyId(companyId)
                .companyName(companyName)
                .dbAccount(dbAccount)
                .build();
    }
}
