package com.uus.mutualfund.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.uus.mutualfund.enums.Role;

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
@Table(name = "t_users")
public class UserEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6021362727968035585L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;

	/*
	 * @Column(name = "first_name") private String firstName;
	 * 
	 * @Column(name = "last_name") private String lastName;
	 * 
	 * @Column(name = "date_of_Birth") private LocalDate dateOfBirth;
	 * 
	 * @Column(name = "gender") private String gender;
	 * 
	 * @Column(name = "mobile_number") private String mobileNumber;
	 * 
	 * @Column(name = "email_address") private String emailAddress;
	 * 
	 * @Column(name = "pan_number") private String panNumber;
	 * 
	 * @Column(name = "aadhaar_number") private String aadhaarNumber;
	 */

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
