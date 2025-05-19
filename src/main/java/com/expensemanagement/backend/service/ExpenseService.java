package com.expensemanagement.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expensemanagement.backend.model.Expense;
import com.expensemanagement.backend.model.Status;
import com.expensemanagement.backend.model.User;
import com.expensemanagement.backend.repository.ExpenseRepository;
import com.expensemanagement.backend.repository.UserRepository;

@Service
public class ExpenseService {

	@Autowired
	private ExpenseRepository expenseRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public List<Expense> getExpensesByUser(String username) {
		User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username"));
		
        return expenseRepository.findByUser(user);
    }

    public Expense addExpense(String username, Expense expense) {
    	if (expense.getDate() == null) {
            expense.setDate(LocalDate.now()); // Set the current date as the expense date
        }
    	expense.setStatus(Status.PENDING);
    	
    	Optional<User> userOptional =userRepository.findByUsername(username); 	
    	if(userOptional.isPresent()) {
    		User user=userOptional.get();
    		expense.setUser(user);
    		return expenseRepository.save(expense);
    	}
    	throw new RuntimeException("User not found with Username: " + username);
    }
    
    public boolean deleteExpense(String username, Long id) {
    	Optional<User> userOptional=userRepository.findByUsername(username);
    	if(userOptional.isEmpty()) {
    		return false;
    	}
    	User user=userOptional.get();
    	Expense expense=expenseRepository.findByIdAndUser(id, user);
    	if(expense==null) {
    		return false;
    	}
    	expenseRepository.delete(expense);
    	return true;
    }
    
    public Expense updateExpense(String username, Expense updatedExpense) {
    	User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username"));
    	
    	Expense expense=expenseRepository.findByIdAndUser(updatedExpense.getId(), user);
    	if (expense == null) {
            throw new IllegalArgumentException("Expense not found for this employee: " + updatedExpense.getId());
        }
    	expense.setDescription(updatedExpense.getDescription());
    	expense.setAmount(updatedExpense.getAmount());
    	expense.setCategory(updatedExpense.getCategory());
    	expense.setDate(updatedExpense.getDate());
    	expense.setStatus(Status.PENDING);
    	
    	return expenseRepository.save(expense);
    }
}
