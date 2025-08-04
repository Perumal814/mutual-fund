package com.uus.mutualfund.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.uus.mutualfund.entity.MutualFundEntity;
import com.uus.mutualfund.entity.MFUserHoldingsEntity;
import com.uus.mutualfund.entity.MFTransactionHistoryEntity;
import com.uus.mutualfund.entity.UserEntity;
import com.uus.mutualfund.enums.TransactionType;
import com.uus.mutualfund.exception.BusinessException;
import com.uus.mutualfund.model.BuyFund;
import com.uus.mutualfund.model.RedeemFund;
import com.uus.mutualfund.repository.MutualFundRepository;
import com.uus.mutualfund.repository.TransactionHistoryRepository;
import com.uus.mutualfund.repository.TransactionRepository;
import com.uus.mutualfund.service.MutualFundService;
import com.uus.mutualfund.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@AllArgsConstructor
public class MutualFundServiceImpl implements MutualFundService {

	private final MutualFundRepository mutualFundRepo;
	private final TransactionRepository transactionRepo;
	private final TransactionHistoryRepository transactionHistoryRepo;
	private final UserService userService;

	public MutualFundEntity addFund(MutualFundEntity fund) {
		fund.setNavDate(LocalDate.now());
		return mutualFundRepo.save(fund);
	}

	public Optional<MutualFundEntity> findByFundCodeAndNavDateAndStatus(String fundId, LocalDate navDate,
			boolean status) {
		return mutualFundRepo.findByFundCodeAndNavDateAndStatus(fundId, navDate, status);
	}

	@Override
	public void buyUnits(BuyFund buyFund) {

		Optional<UserEntity> user = userService.findUserById(buyFund.getUserId());
		UserEntity userEnity = null;
		if (user.isPresent()) {
			userEnity = user.get();
			log.info("user role "+userEnity.getRole());
		} else {
			throw new BusinessException("4001");
		}
		MutualFundEntity todayNav = mutualFundRepo
				.findByFundIdAndNavDateAndStatus(buyFund.getFundId(), LocalDate.now(), true)
				.orElseThrow(() -> new BusinessException("4003"));

		// new IllegalStateException("NAV for today not available"));

		//double units = buyFund.getAmount() / todayNav.getNavValue();

		MFTransactionHistoryEntity history = new MFTransactionHistoryEntity();
		history.setUserId(buyFund.getUserId());
		history.setFundId(buyFund.getFundId());
		history.setTransactionType(TransactionType.BUY);
		history.setNavValue(todayNav.getNavValue());
		history.setUnits(buyFund.getUnits());
		history.setAmount(buyFund.getUnits() * todayNav.getNavValue());
		history.setNavDate(LocalDate.now());
		history.setTransactionDate(LocalDateTime.now());
		history.setCreatedDate(LocalDateTime.now());
		history.setModifiedDate(LocalDateTime.now());
		transactionHistoryRepo.save(history);
		Optional<MFUserHoldingsEntity> transaction = transactionRepo.findByUserIdAndFundId(buyFund.getUserId(),
				buyFund.getFundId());
		MFUserHoldingsEntity txn = null;
		if (transaction.isPresent()) {
			txn = transaction.get();
			txn.setTotalUnits(buyFund.getUnits()  + txn.getTotalUnits());
			txn.setTotalUnitsRedeemed(txn.getTotalUnitsRedeemed());
			txn.setTotalUnitsAvailable(txn.getTotalUnits() - txn.getTotalUnitsRedeemed());
		} else {
			txn = new MFUserHoldingsEntity();
			txn.setTotalUnits(buyFund.getUnits());
			txn.setTotalUnitsRedeemed(0.00);
			txn.setTotalUnitsAvailable(txn.getTotalUnits() - txn.getTotalUnitsRedeemed());
		}
		txn.setFundId(buyFund.getFundId());
		txn.setTotalUnitsAvailable(txn.getTotalUnits() - txn.getTotalUnitsRedeemed());
		txn.setUserId(buyFund.getUserId());
		transactionRepo.save(txn);

	}

	@Override
	public void redeemUnits(RedeemFund redeemFund) {

		Optional<UserEntity> user = userService.findUserById(redeemFund.getUserId());
		UserEntity userEnity = null;
		if (user.isPresent()) {
			userEnity = user.get();
			log.info("user role "+userEnity.getRole());
		} else {
			throw new BusinessException("4001");
		}
		MutualFundEntity todayNav = mutualFundRepo
				.findByFundIdAndNavDateAndStatus(redeemFund.getFundId(), LocalDate.now(), true)
				.orElseThrow(() -> new BusinessException("4003"));
		Optional<MFUserHoldingsEntity> transaction = transactionRepo
				.findByUserIdAndFundId(redeemFund.getUserId(), redeemFund.getFundId());
		MFUserHoldingsEntity txn = null;
		if (transaction.isPresent()) {
			txn = transaction.get();
			txn.setTotalUnitsRedeemed(txn.getTotalUnitsRedeemed() + redeemFund.getUnits());
			txn.setTotalUnitsAvailable(txn.getTotalUnits() - txn.getTotalUnitsRedeemed());
		} else {
			throw new BusinessException("4004");
		}
		if (redeemFund.getUnits() > txn.getTotalUnitsAvailable()) {
			throw new BusinessException("4005");
		} else if (redeemFund.getUnits() <= txn.getTotalUnitsAvailable()) {
			double amount = redeemFund.getUnits() * todayNav.getNavValue();
			MFTransactionHistoryEntity history = new MFTransactionHistoryEntity();
			history.setUserId(redeemFund.getUserId());
			history.setFundId(redeemFund.getFundId());
			history.setTransactionType(TransactionType.REDEEM);
			history.setNavValue(todayNav.getNavValue());
			history.setUnits(redeemFund.getUnits());
			history.setAmount(amount);
			history.setNavDate(LocalDate.now());
			history.setTransactionDate(LocalDateTime.now());
			history.setCreatedDate(LocalDateTime.now());
			history.setModifiedDate(LocalDateTime.now());
			transactionHistoryRepo.save(history);

			txn.setTotalUnitsRedeemed(txn.getTotalUnitsRedeemed() + redeemFund.getUnits());
			txn.setTotalUnitsAvailable(txn.getTotalUnits() - txn.getTotalUnitsRedeemed());

			txn.setFundId(redeemFund.getFundId());
			txn.setUserId(redeemFund.getUserId());
			transactionRepo.save(txn);
		}
	}

	
}
