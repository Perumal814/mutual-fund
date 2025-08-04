package com.uus.mutualfund.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.uus.mutualfund.enums.TransactionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "t_mf_transaction_history")
public class MFTransactionHistoryEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5728339692121550063L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transactions_id")
	private Long transactionsId;

	// @Column(name = "user_id", nullable = false)
	// @ManyToOne
	// @JoinColumn(name = "user_id")
	// private UserEntity userId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "fund_id", nullable = false)
	private Long fundId;

	@Column(name = "transaction_type")
	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;

	@Column(name = "nav_value")
	private double navValue;

	@Column(name = "units")
	private double units;

	@Column(name = "amount")
	private double amount;

	@Column(name = "navDate")
	private LocalDate navDate;

	@Column(name = "transaction_date")
	private LocalDateTime transactionDate;

	@Column(name = "created_by", updatable = false)
	private String createdBy;

	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime createdDate = getCurrentDateTime();

	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime modifiedDate = getCurrentDateTime();

	@Column(name = "active")
	private boolean active = true;

	public LocalDateTime getCurrentDateTime() {
		return LocalDateTime.now();
	}

}
