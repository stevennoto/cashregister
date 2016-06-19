package com.simplyautomatic.cashregister;

/**
 * Exception for case of having insufficient money to enable a transaction.
 * @author stevennoto
 */
public class InsufficientMoneyException extends Exception {

	public InsufficientMoneyException() {
	}

	public InsufficientMoneyException(String message) {
		super(message);
	}

	public InsufficientMoneyException(String message, Throwable cause) {
		super(message, cause);
	}

	public InsufficientMoneyException(Throwable cause) {
		super(cause);
	}
}
