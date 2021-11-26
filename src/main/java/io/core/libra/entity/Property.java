package io.core.libra.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_property")
@Data
public class Property extends AuditModel {
    private String propertyCode;
    private String propertyValue;
}
