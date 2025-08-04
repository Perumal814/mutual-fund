package com.uus.mutualfund.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uus.mutualfund.entity.MFTransactionHistoryEntity;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<MFTransactionHistoryEntity, Long>{

}
