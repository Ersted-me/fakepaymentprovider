package net.ersted.fakepaymentprovider.entity;

import lombok.*;
import net.ersted.fakepaymentprovider.enums.WebhookStatus;
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
@Table("webhook")
public class Webhook implements Persistable<String> {

    @Id
    @Column("id")
    private String id;
    @Column("url")
    private String url;
    @Column("request")
    private String request;
    @Column("response")
    private String response;
    @Column("code")
    private String code;
    @Column("number_retry")
    private Integer numberRetry;
    @Column("last_retry_at")
    private LocalDateTime lastRetryAt;

    @Column("payment_id")
    private String paymentId;

    @Column("created_at")
    private LocalDateTime createdAt;
    @Column("created_by")
    private String createdBy;
    @Column("updated_at")
    private LocalDateTime updatedAt;
    @Column("updated_by")
    private String updatedBy;
    @Column("status")
    private WebhookStatus status;

    @Transient
    private Payment payment;

    @Override
    public boolean isNew() {
        return !StringUtils.hasText(id);
    }
}