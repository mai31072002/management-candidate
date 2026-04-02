package com.candidate.candidate_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
@Getter
@Setter
public abstract class EntityBase {

    @Version
    @Column(columnDefinition = "integer default 0")
    private int version;

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

}
