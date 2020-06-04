package com.lee.content.service.impl;

import com.lee.content.entity.UserDO;
import com.lee.content.repository.UserRepository;
import com.lee.content.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


@Service
@Primary
@Slf4j
public class BaseUserService implements UserService {

    private final UserRepository userRepository;

    public BaseUserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void insert(UserDO userDO) {
        userRepository.save(userDO);
    }

    @Override
    @Cacheable(value = "UserDO", key="'username=' + #username")
    public UserDO getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Cacheable(value = "UserDO", key="'id=' + #id", unless = "#result==null")
    public UserDO findById(Long id) {
        UserDO userDO = userRepository.findById(id).orElse(null);
        return userDO;
    }

    @Override
    @CachePut(value = "UserDO", key = "'id=' + #userDO.id", condition = "#userDO.id != null")
    public UserDO save(UserDO userDO) {
        UserDO do2 = userRepository.save(userDO);
        return do2;
    }

    @Override
    @CacheEvict(value = "UserDO", key = "'id=' + #id")
    public void delteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @CacheEvict(value = "UserDO", allEntries = true)
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    @Cacheable(value = "UserDO", key="'all'")
    public Iterable<UserDO> findAll() {
        return userRepository.findAll();
    }

}
