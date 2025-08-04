package com.uus.mutualfund.service;

import java.time.LocalDate;
import java.util.Optional;

import com.uus.mutualfund.entity.MutualFundEntity;
import com.uus.mutualfund.model.BuyFund;
import com.uus.mutualfund.model.RedeemFund;

public interface MutualFundService {

	public MutualFundEntity addFund(MutualFundEntity fund);

	public Optional<MutualFundEntity> findByFundCodeAndNavDateAndStatus(String fundCode, LocalDate navDate,
			boolean status);

	void buyUnits(BuyFund buyFund);

	void redeemUnits(RedeemFund redeemFund);

}
