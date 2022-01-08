package io.core.libra.dao;

import io.core.libra.entity.Property;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class PropertyRepositoryTest {

    @Autowired
    PropertyRepository propertyRepository;

    @Test
    void findByPropertyCode() {
        String propertyCode = "user.book.limit";
        Property property = propertyRepository.findByPropertyCode(propertyCode);
        assertThat(property).hasFieldOrPropertyWithValue("propertyCode", propertyCode);
    }

    @Test
    void findByPropertyCode_Fail() {
        String propertyCode = "invalid.property.code";
        Property property = propertyRepository.findByPropertyCode(propertyCode);
        assertThat(property).isEqualTo(null);
    }
}