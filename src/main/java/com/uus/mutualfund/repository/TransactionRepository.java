package com.uus.mutualfund.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uus.mutualfund.entity.MFUserHoldingsEntity;

@Repository
public interface TransactionRepository extends JpaRepository<MFUserHoldingsEntity, Long> {

	Optional<MFUserHoldingsEntity> findByUserIdAndFundId(Long userId, Long fundId);

}
