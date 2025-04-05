package com.yipintsoi.authservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "roles", schema = "auth")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String name;
    
    private String description;
    
    @Column(nullable = false)
    private Boolean active;
    
    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;
    
    @CreatedDate
    @Column(name = "created_date")
    private Instant createdDate;
    
    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;
    
    @LastModifiedDate
    @Column(name = "updated_date")
    private Instant updatedDate;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
      name = "role_permissions",
      schema = "auth",
      joinColumns = @JoinColumn(name = "role_id"),
      inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;
}
