package com.uus.mutualfund.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "t_mf_user_holdings")
public class MFUserHoldingsEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5029936448428312486L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transactions_id")
	private Long transactionsId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "fund_id", nullable = false)
	private Long fundId;

	@Column(name = "total_units")
	private double totalUnits;

	@Column(name = "total_units_redeemed")
	private double totalUnitsRedeemed;

	@Column(name = "total_units_available")
	private double totalUnitsAvailable;

	@Column(name = "created_by", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private String createdBy;

	@Column(name = "created_date", updatable = false)
	private LocalDateTime createdDate = getCurrentDateTime();

	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime modifiedDate = getCurrentDateTime();

	@Column(name = "status")
	private boolean status = true;

	public LocalDateTime getCurrentDateTime() {
		return LocalDateTime.now();
	}

}
