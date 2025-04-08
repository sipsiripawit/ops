package com.yipintsoi.authservice.service.impl;

import com.yipintsoi.authservice.common.Constants;
import com.yipintsoi.authservice.domain.dto.UserDTO;
import com.yipintsoi.authservice.domain.entity.User;
import com.yipintsoi.authservice.domain.mapper.UserMapper;
import com.yipintsoi.authservice.exception.ResourceNotFoundException;
import com.yipintsoi.authservice.repository.UserRepository;
import com.yipintsoi.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation ของ UserService
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO getUserById(Long id) {
        log.debug("Getting user by id: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
                
        return userMapper.userToUserDTO(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO getUserByUsername(String username) {
        log.debug("Getting user by username: {}", username);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND));
                
        return userMapper.userToUserDTO(user);
    }
}