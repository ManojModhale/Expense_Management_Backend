package com.expensemanagement.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expensemanagement.backend.model.Expense;


@Service
public class EmployeeService {
	
	@Autowired
	private ExpenseService expenseService;
	
	public List<Expense> getExpensesByEmployee(String username) {
		
        return expenseService.getExpensesByUser(username);
    }

    public Expense addExpense(String username, Expense expense) {
    	return expenseService.addExpense(username, expense);
    }
    
    public boolean deleteExpense(String username, Long id) {
    	return expenseService.deleteExpense(username, id);
    }
    
    public Expense updateExpense(String username, Expense updatedExpense) {
    	return expenseService.updateExpense(username, updatedExpense);
    }
}
