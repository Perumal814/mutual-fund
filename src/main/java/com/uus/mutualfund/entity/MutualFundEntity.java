package com.uus.mutualfund.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(name = "t_mutual_funds", uniqueConstraints = @UniqueConstraint(columnNames = { "fund_code", "nav_date" }))
public class MutualFundEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4126443084209519298L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fund_id")
	private Long fundId;

	@Column(name = "fund_code", nullable = false)
	private String fundCode;

	@Column(name = "fund_name")
	private String fundName;

	@Column(name = "fund_type")
	private String fundType;

	@Column(name = "nav_value")
	private double navValue;

	@Column(name = "nav_date")
	private LocalDate navDate;

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
