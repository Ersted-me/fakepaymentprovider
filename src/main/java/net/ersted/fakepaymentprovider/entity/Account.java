package net.ersted.fakepaymentprovider.entity;

import lombok.*;
import net.ersted.fakepaymentprovider.enums.AccountStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table("account")
public class Account implements Persistable<String> {
    @Id
    @Column("id")
    private String id;
    @Column("currency")
    private String currency;
    @Column("balance")
    private BigDecimal balance;
    @Column("merchant_id")
    private String merchantId;

    @Column("created_at")
    private LocalDateTime createdAt;
    @Column("created_by")
    private String createdBy;
    @Column("updated_at")
    private LocalDateTime updatedAt;
    @Column("updated_by")
    private String updatedBy;
    @Column("status")
    private AccountStatus status;

    @Transient
    private Merchant merchant;
    @Transient
    @ToString.Exclude
    private Set<Payment> payments;

    @Override
    public boolean isNew() {
        return !StringUtils.hasText(id);
    }
}