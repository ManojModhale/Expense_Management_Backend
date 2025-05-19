package com.expensemanagement.backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;
//import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "expenses")
public class Expense {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String description;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Category category;
	
	@Column(nullable = false)
	private BigDecimal amount;
	
	@Column(nullable = false)
	private LocalDate date;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)  //status will be null first when employee adds an expense
	private Status status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private User user;

	public Expense() {
		super();
		// TODO Auto-generated constructor stub
		this.status=Status.PENDING;
		this.date=LocalDate.now();
	}

	public Expense(Long id, String description, Category category, BigDecimal amount, LocalDate date, Status status,
			User user) {
		super();
		this.id = id;
		this.description = description;
		this.category = category;
		this.amount = amount;
		this.date = date;
		this.status = status;
		this.user = user;
	}

	public Expense(String description, Category category, BigDecimal amount, LocalDate date) {
		super();
		this.description = description;
		this.category = category;
		this.amount = amount;
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
	    return "Expense [id=" + id + ", description=" + description + ", category=" + category + ", amount=" + amount
	            + ", date=" + date + ", status=" + status + ", userId=" + (user != null ? user.getId() : null) + "]";
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    Expense expense = (Expense) o; // Corrected cast to Expense
	    return id != null && id.equals(expense.id); // Corrected to use expense.id
	}

	@Override
	public int hashCode() {
	    return id != null ? id.hashCode() : 0; // Better hashCode based on ID
	    // Or, for JPA entities, you might use:
	    // return Objects.hash(id); // Requires java.util.Objects import
	    // If you don't use the ID in hashCode for transient entities, use getClass().hashCode()
	    // return getClass().hashCode();
	}

}
