package com.expensemanagement.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.expensemanagement.backend.model.Expense;
import com.expensemanagement.backend.service.EmployeeService;
import com.expensemanagement.backend.service.ExpenseService;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin(origins = "http://localhost:3000")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private ExpenseService expenseService;
	
	@GetMapping("/expensesByUsername/{username}")
    public ResponseEntity<List<Expense>> getExpensesByEmployee(@PathVariable String username) {
        List<Expense> expenses = employeeService.getExpensesByEmployee(username);
        /*Iterator<Expense> iterator = expenses.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }*/
        return ResponseEntity.ok(expenses);
    }

    @PostMapping("/addexpense/{username}")
    public ResponseEntity<Expense> addExpense(@PathVariable String username, @RequestBody Expense expense) {
        Expense newExpense = employeeService.addExpense(username, expense);
        return new ResponseEntity<>(newExpense, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/delete-expense/{username}/{id}")
    public ResponseEntity<Map<String, Object>> deleteExpense(@PathVariable String username, @PathVariable Long id) {
    	boolean isDeleted=employeeService.deleteExpense(username, id);
    	Map<String, Object> response = new HashMap<>();
        if (isDeleted) {
            response.put("message", "Expense deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Expense not found for this Employee: " + username + " or expense ID: " + id); 
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // Use 404 for not found
        }
    }
    
    @PutMapping("/update-expense/{username}")
    public ResponseEntity<Expense> updateExpense(@PathVariable String username, @RequestBody Expense updatedExpense) {
    	try {
        	Expense updated=employeeService.updateExpense(username, updatedExpense);
        	return ResponseEntity.ok(updated);
    	}catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); //if expense is not found or doesn't belong to the user
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Handle other errors
        }
    }
    
    @PutMapping("/update-expense-name")
    public boolean updateExpenseName(@RequestParam Long id, @RequestParam String name) {
    	return expenseService.updateExpenseName(id, name);
    }
}
