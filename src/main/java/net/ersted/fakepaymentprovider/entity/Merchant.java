package net.ersted.fakepaymentprovider.entity;

import lombok.*;
import net.ersted.fakepaymentprovider.enums.MerchantStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table("merchant")
public class Merchant implements Persistable<String> {
    @Id
    @Column("merchant_id")
    private String merchantId;
    @Column("secret_key")
    private String secretKey;

    @Column("created_at")
    private LocalDateTime createdAt;
    @Column("created_by")
    private String createdBy;
    @Column("updated_at")
    private LocalDateTime updatedAt;
    @Column("updated_by")
    private String updatedBy;
    @Column("status")
    private MerchantStatus status;

    @Transient
    @ToString.Exclude
    private Set<Account> accounts;

    @ToString.Include(name = "secretKey")
    private String maskSecretKey() {
        return "********";
    }

    @Override
    public String getId() {
        return merchantId;
    }

    @Override
    public boolean isNew() {
        return !StringUtils.hasText(merchantId);
    }
}
