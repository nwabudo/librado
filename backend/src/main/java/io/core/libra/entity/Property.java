package io.core.libra.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_property")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Property extends AuditModel {

    @Column(unique = true, length = 50)
    private String propertyCode;

    private String propertyValue;
}
