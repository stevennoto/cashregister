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
		new CashRegister(Arrays.asList(new Integer[]{20, 5, 20}));
	}
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNegative() {
		new CashRegister(Arrays.asList(new Integer[]{20, 5, -1}));
	}
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorZero() {
		new CashRegister(Arrays.asList(new Integer[]{20, 0, 1}));
	}
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNone() {
		new CashRegister(Arrays.asList(new Integer[]{}));
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
	
	// Test adding money
	@Test
	public void testPutMoney() {
		CashRegister register = getCashRegister();
		register.putMoney(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
		assertEquals(68, register.getTotal());
		assertEquals("$68 1 2 3 4 5", register.showInventory());
		register = getCashRegisterAltDenominations();
		register.putMoney(Arrays.asList(new Integer[]{1, 2, 3}));
		assertEquals(36, register.getTotal());
		assertEquals("$36 1 2 3", register.showInventory());
	}
	@Test(expected = IllegalArgumentException.class)
	public void testPutMoneyWrongNumber() {
		CashRegister register = getCashRegister();
		register.putMoney(Arrays.asList(new Integer[]{1, 2, 3}));
	}
	@Test(expected = IllegalArgumentException.class)
	public void testPutMoneyNegative() {
		CashRegister register = getCashRegister();
		register.putMoney(Arrays.asList(new Integer[]{1, 2, 3, 4, -5}));
	}
}
