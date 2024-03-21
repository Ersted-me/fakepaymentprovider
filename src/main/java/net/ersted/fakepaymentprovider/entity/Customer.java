package net.ersted.fakepaymentprovider.entity;

import lombok.*;
import net.ersted.fakepaymentprovider.enums.CustomerStatus;
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
@Table("customer")
public class Customer implements Persistable<String> {
    @Id
    private String id;
    @Column("first_name")
    private String firstName;
    @Column("last_name")
    private String lastName;
    @Column("country")
    private String country;
    @Column("created_at")
    private LocalDateTime createdAt;
    @Column("created_by")
    private String createdBy;
    @Column("updated_at")
    private LocalDateTime updatedAt;
    @Column("updated_by")
    private String updatedBy;
    @Column("status")
    private CustomerStatus status;

    @Transient
    @ToString.Exclude
    private Set<Card> cards;

    @Override
    public boolean isNew() {
        return !StringUtils.hasText(id);
    }
}
