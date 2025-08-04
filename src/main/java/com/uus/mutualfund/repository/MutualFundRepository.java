package com.uus.mutualfund.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uus.mutualfund.entity.MutualFundEntity;

@Repository
public interface MutualFundRepository extends JpaRepository<MutualFundEntity, Long> {

	Optional<MutualFundEntity> findByFundCodeAndNavDateAndStatus(String fundCode, LocalDate navDate, boolean status);
	
	Optional<MutualFundEntity> findByFundIdAndNavDateAndStatus(Long fundId, LocalDate navDate, boolean status);

}
