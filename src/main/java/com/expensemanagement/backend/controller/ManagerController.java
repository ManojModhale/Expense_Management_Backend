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
import org.springframework.web.bind.annotation.RestController;

import com.expensemanagement.backend.dto.ExpenseDto;
import com.expensemanagement.backend.model.Expense;
import com.expensemanagement.backend.service.ManagerService;

@RestController
@RequestMapping("/api/manager")
@CrossOrigin(origins = "http://localhost:3000")
public class ManagerController {
	
	@Autowired
	private ManagerService managerService;

	@GetMapping("/expensesByUsername/{username}")
    public ResponseEntity<List<Expense>> getExpensesByManager(@PathVariable String username) {
        List<Expense> expenses = managerService.getExpensesByManager(username);
        /*Iterator<Expense> iterator = expenses.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }*/
        return ResponseEntity.ok(expenses);
    }

    @PostMapping("/addexpense/{username}")
    public ResponseEntity<Expense> addExpense(@PathVariable String username, @RequestBody Expense expense) {
        Expense newExpense = managerService.addExpense(username, expense);
        return new ResponseEntity<>(newExpense, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/delete-expense/{username}/{id}")
    public ResponseEntity<Map<String, Object>> deleteExpense(@PathVariable String username, @PathVariable Long id) {
    	boolean isDeleted=managerService.deleteExpense(username, id);
    	Map<String, Object> response = new HashMap<>();
        if (isDeleted) {
            response.put("message", "Expense deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Expense not found for this Manager: " + username + " or expense ID: " + id); 
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // Use 404 for not found
        }
    }
    
    @PutMapping("/update-expense/{username}")
    public ResponseEntity<Expense> updateExpense(@PathVariable String username, @RequestBody Expense updatedExpense) {
    	try {
        	Expense updated=managerService.updateExpense(username, updatedExpense);
        	return ResponseEntity.ok(updated);
    	}catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); //if expense is not found or doesn't belong to the user
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Handle other errors
        }
    }
    
    // Endpoint to get all employee expenses (for manager review)
    @GetMapping("/expenses/allEmployee")
    public ResponseEntity<?> getAllEmployeeExpenses() {
        try {
            List<ExpenseDto> expenses = managerService.getAllEmployeeExpenses();
            return ResponseEntity.ok(expenses);
        } catch (RuntimeException e) {
			// TODO: handle exception
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error fetching all employee expenses: " + e.getMessage());
		} 
        catch (Exception e) {
			// TODO: handle exception
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        			.body("An unexpected error occurred while fetching employee expenses.");
		}
    }

    @PutMapping("/expense/{expenseId}/approve")
    public ResponseEntity<?> approveExpense(@PathVariable Long expenseId) {
        try {
            managerService.approveExpense(expenseId);
            return ResponseEntity.ok("Expense approved successfully.");
        } catch (RuntimeException e) { // Catch specific exceptions like ResourceNotFoundException
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error approving expense: " + e.getMessage());
        }
    }

    @PutMapping("/expense/{expenseId}/reject")
    public ResponseEntity<?> rejectExpense(@PathVariable Long expenseId, @RequestBody Map<String, String> payload) {//, 
        try {
            String reason = payload.get("reason"); // Expect a JSON body like { "reason": "..." }
            managerService.rejectExpense(expenseId);//, reason
            return ResponseEntity.ok("Expense rejected successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) { // Catch specific exceptions like ResourceNotFoundException
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error rejecting expense: " + e.getMessage());
        }
    }

	
}
