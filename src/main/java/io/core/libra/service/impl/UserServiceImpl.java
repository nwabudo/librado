package io.core.libra.service.impl;

import io.core.libra.dao.UserRepository;
import io.core.libra.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
}
