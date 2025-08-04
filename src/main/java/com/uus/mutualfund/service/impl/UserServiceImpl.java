package com.uus.mutualfund.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.uus.mutualfund.entity.UserEntity;
import com.uus.mutualfund.repository.UserRepository;
import com.uus.mutualfund.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@AllArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepo;

	@Override
	public UserEntity addUser(UserEntity userEntity) {
		return userRepo.save(userEntity);
	}

	@Override
	public List<UserEntity> getAllUsers() {
		return userRepo.findAll();
	}

	@Override
	public Optional<UserEntity> findUserByUsername(String username) {
		return userRepo.findUserByUsername(username);
	}

	@Override
	public Optional<UserEntity> findUserById(Long userId) {
		return userRepo.findById(userId);
	}

	@Override
	public boolean deleteUser(Long userId) {
		log.info("deleteUser");
		return userRepo.findById(userId).map(user -> {
			user.setStatus(false);
			userRepo.save(user);
			return true;
		}).orElse(false);
	}

}
