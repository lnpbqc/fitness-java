package org.example.fitnessjava.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "SystemConfig")
@Table(name = "system_config")
@EqualsAndHashCode(callSuper = true)
public class SystemConfig extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "config_key", nullable = false, unique = true, length = 128)
    private String configKey;

    @Column(name = "config_value", nullable = false, length = 256)
    private String configValue;

    @Column(name = "updated_by", length = 128)
    private String updatedBy;

    @Column(name = "description", length = 512)
    private String description;
}
