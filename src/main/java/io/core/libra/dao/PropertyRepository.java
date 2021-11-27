package io.core.libra.dao;

import io.core.libra.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository  extends JpaRepository<Property, Long> {

    Property findByPropertyCode(String propertyCode);
}
