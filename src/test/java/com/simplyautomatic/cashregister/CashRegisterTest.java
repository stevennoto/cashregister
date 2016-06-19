package com.simplyautomatic.cashregister;

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CashRegister
 * @author stevennoto
 */
public class CashRegisterTest {
	private CashRegister getCashRegister() {
		CashRegister cashRegister = new CashRegister();
		return cashRegister;
	}
	
	private CashRegister getCashRegisterAltDenominations() {
		CashRegister cashRegister = new CashRegister(
				Arrays.asList(new Integer[]{20, 5, 2}));
		return cashRegister;
	}
	
	// Test construction validation
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorDuplicate() {
		CashRegister cashRegister = new CashRegister(Arrays.asList(new Integer[]{20, 5, 20}));
	}
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNegative() {
		CashRegister cashRegister = new CashRegister(Arrays.asList(new Integer[]{20, 5, -1}));
	}
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorZero() {
		CashRegister cashRegister = new CashRegister(Arrays.asList(new Integer[]{20, 0, 1}));
	}
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNone() {
		CashRegister cashRegister = new CashRegister(Arrays.asList(new Integer[]{}));
	}
	
	// Test getting total from register
	@Test
	public void testGetTotal() {
		CashRegister register = getCashRegister();
		assertEquals(0, register.getTotal());
		
		register = getCashRegisterAltDenominations();
		assertEquals(0, register.getTotal());
	}

	// Test showing inventory
	@Test
	public void testShowInventory() {
		CashRegister register = getCashRegister();
		assertEquals("$0 0 0 0 0 0", register.showInventory());
		register = getCashRegisterAltDenominations();
		assertEquals("$0 0 0 0", register.showInventory());
	}
	
}
