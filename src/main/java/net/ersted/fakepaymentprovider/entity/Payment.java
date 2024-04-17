package net.ersted.fakepaymentprovider.entity;

import lombok.*;
import net.ersted.fakepaymentprovider.enums.PaymentStatus;
import net.ersted.fakepaymentprovider.enums.PaymentType;
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
@Table("payment")
public class Payment implements Persistable<String>  {

    @Id
    @Column("transaction_id")
    private String transactionId;
    @Column("language")
    private String language;
    @Column("notification_url")
    private String notificationUrl;
    @Column("message")
    private String message;
    @Column("type")
    private PaymentType type;
    @Column("payment_method")
    private String paymentMethod;
    @Column("currency")
    private String currency;
    @Column("amount")
    private BigDecimal amount;

    @Column("card_id")
    private String cardId;
    @Column("account_id")
    private String accountId;

    @Column("created_at")
    private LocalDateTime createdAt;
    @Column("created_by")
    private String createdBy;
    @Column("updated_at")
    private LocalDateTime updatedAt;
    @Column("updated_by")
    private String updatedBy;
    @Column("status")
    private PaymentStatus status;

    @Transient
    private Card card;
    @Transient
    private Account account;
    @Transient
    @ToString.Exclude
    private Set<Webhook> webhooks;

    @Override
    public String getId() {
        return transactionId;
    }

    @Override
    public boolean isNew() {
        return !StringUtils.hasText(transactionId);
    }
}