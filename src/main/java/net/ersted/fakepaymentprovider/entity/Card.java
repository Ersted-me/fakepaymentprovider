package net.ersted.fakepaymentprovider.entity;

import lombok.*;
import net.ersted.fakepaymentprovider.enums.CardStatus;
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
@Table("customer")
public class Card implements Persistable<String> {
    @Id
    private String id;
    @Column("card_number")
    private String cardNumber;
    @Column("exp_date")
    private LocalDateTime expDate;
    @Column("cvv")
    private String cvv;
    @Column("balance")
    private BigDecimal balance;
    @Column("customer_id")
    private String customerId;
    @Column("created_at")
    private LocalDateTime createdAt;
    @Column("created_by")
    private String createdBy;
    @Column("updated_at")
    private LocalDateTime updatedAt;
    @Column("updated_by")
    private String updatedBy;
    @Column("status")
    private CardStatus status;

    @Transient
    private Customer customer;
    @Transient
    @ToString.Exclude
    private Set<Payment> payments;

    @Override
    public boolean isNew() {
        return !StringUtils.hasText(id);
    }
}
