package com.ePocket.ws.user;
import java.util.HashMap;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ePocket.ws.error.ApiError;
import com.ePocket.ws.shared.ResponseGeneric;



@RestController
public class UserController {
	
	
	
	@Autowired
	UserService userService;
	
	
	@PostMapping("/api/1.0/users")
	public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
		ApiError error = new ApiError(400, "Validation error", "/api/1.0/users");
		Map<String, String> validationErrors = new HashMap<>();

		
		String userName = user.getUserName();
		String mail = user.getMail();
		String password = user.getPassword();
		
		if(userName == null  || userName.isEmpty()) {
			validationErrors.put("userName", "userName cannot be null");
			
			
			
		}
		
		if(mail == null  || mail.isEmpty()) {
			validationErrors.put("mail", "Mail Cannot Be Null");
			
			
			
		}
		if(password == null  || password.isEmpty()) {
			validationErrors.put("password", "Password Cannot Be Null");
			
			
			
		}
		if(validationErrors.size() > 0) {
			error.setValidationErrors(validationErrors);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
			
		}
		userService.save(user);
		return ResponseEntity.ok(new ResponseGeneric("User Created"));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiError solveValidatonProblem(MethodArgumentNotValidException exception) {
		ApiError error = new ApiError(400, "Validation error", "/api/1.0/users");
		Map<String, String> validationErrors = new HashMap<>();
		for(FieldError fieldError:exception.getBindingResult().getFieldErrors()) {
			
			validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
		
		}
		error.setValidationErrors(validationErrors);
		return error;
	}
	
	
}
