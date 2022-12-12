package id.co.bni.parameter.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "ACCOUNT_MANAGEMENT_DETAIL")
@IdClass(AccountManagementDetailId.class)
public class AccountManagementDetail {
    @Id
    @Column(name = "COMPANY_ID", nullable = false, length = 50)
    private String companyId;
    @Id
    @Column(name = "DEBIT_ACCOUNT", nullable = false, length = 20)
    private String dbAccount;
    @Column(name = "DEBIT_NAME", nullable = false, length = 100)
    private String dbAccountName;
}
