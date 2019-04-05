package com.capgemini.bankapp.exception;

public class LowBalanceException extends Exception {

	public LowBalanceException() {
	}
	
	
	public LowBalanceException(String message) {
		super(message);
	}

	
	
}
