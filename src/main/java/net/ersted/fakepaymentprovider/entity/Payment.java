package net.ersted.fakepaymentprovider.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ersted.fakepaymentprovider.enums.MerchantStatus;
import net.ersted.fakepaymentprovider.enums.PaymentStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table("payment")
public class Payment implements Persistable<String>  {
    @Id
    @Column("id")
    private String id;
    @Column("transaction_id")
    private String transactionId;
    @Column("language")
    private String language;
    @Column("notification_url")
    private String notificationUrl;
    @Column("message")
    private String message;
    @Column("type")
    private String type;
    @Column("currency")
    private String currency;

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
    private Webhook webhook;

    @Override
    public boolean isNew() {
        return !StringUtils.hasText(id);
    }
}