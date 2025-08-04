package com.uus.mutualfund.service;

import java.util.List;
import java.util.Optional;

import com.uus.mutualfund.entity.UserEntity;

public interface UserService {

	public UserEntity addUser(UserEntity userEntity);

	public List<UserEntity> getAllUsers();

	public Optional<UserEntity> findUserByUsername(String username);

	public Optional<UserEntity> findUserById(Long userId);

	public boolean deleteUser(Long userId);

}
