package io.core.libra.service.impl;

import io.core.libra.dao.PropertyRepository;
import io.core.libra.service.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
}
