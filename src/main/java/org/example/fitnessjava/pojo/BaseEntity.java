package org.example.fitnessjava.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
public class BaseEntity {
    // 创建时间
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createTime;

    // 修改时间
    @UpdateTimestamp
    private LocalDateTime updateTime;
}
