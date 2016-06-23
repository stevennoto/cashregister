package com.simplyautomatic.cashregister;

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CashRegister
 * @author stevennoto
 */
public class CashRegisterTest {
	// Get a basic default cash register
	private CashRegister getCashRegister() {
		CashRegister cashRegister = new CashRegister();
		return cashRegister;
	}
	
	// Get a cash register that only holds 20's, 5's, and 2's
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
	
	// Test taking money
	@Test
	public void testTakeMoney() throws InsufficientMoneyException {
		CashRegister register = getCashRegister();
		register.putMoney(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
		register.takeMoney(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
		assertEquals(0, register.getTotal());
		assertEquals("$0 0 0 0 0 0", register.showInventory());
		register = getCashRegisterAltDenominations();
		register.putMoney(Arrays.asList(new Integer[]{1, 2, 3}));
		register.takeMoney(Arrays.asList(new Integer[]{1, 2, 3}));
		assertEquals(0, register.getTotal());
		assertEquals("$0 0 0 0", register.showInventory());
	}
	@Test(expected = IllegalArgumentException.class)
	public void testTakeMoneyWrongNumber() throws InsufficientMoneyException {
		CashRegister register = getCashRegister();
		register.takeMoney(Arrays.asList(new Integer[]{1, 2, 3}));
	}
	@Test(expected = IllegalArgumentException.class)
	public void testTakeMoneyNegative() throws InsufficientMoneyException {
		CashRegister register = getCashRegister();
		register.takeMoney(Arrays.asList(new Integer[]{1, 2, 3, 4, -5}));
	}
	@Test(expected = InsufficientMoneyException.class)
	public void testTakeMoneyNotEnough() throws InsufficientMoneyException {
		CashRegister register = getCashRegister();
		register.takeMoney(Arrays.asList(new Integer[]{1, 0, 0, 0, 0}));
	}
	
	// Test making change
	@Test
	public void testProvideChangeSimple() throws InsufficientMoneyException {
		// Verify basic change
		CashRegister register = getCashRegister();
		register.putMoney(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
		String change = register.provideChange(38);
		assertEquals("1 1 1 1 1", change);
		assertEquals(30, register.getTotal());
		assertEquals("$30 0 1 2 3 4", register.showInventory());
		
		register = getCashRegisterAltDenominations();
		register.putMoney(Arrays.asList(new Integer[]{1, 2, 3}));
		change = register.provideChange(27);
		assertEquals("1 1 1", change);
		assertEquals(9, register.getTotal());
		assertEquals("$9 0 1 2", register.showInventory());
		
		// Verify that can give $11 in change as 1 5 and 3 2's
		register = getCashRegister();
		register.putMoney(Arrays.asList(new Integer[]{0, 0, 5, 5, 0}));
		change = register.provideChange(11);
		assertEquals("0 0 1 3 0", change);
	}
	@Test(expected = InsufficientMoneyException.class)
	public void testProvideChangeNoMoney() throws InsufficientMoneyException {
		CashRegister register = getCashRegister();
		register.provideChange(42);
	}
	@Test(expected = InsufficientMoneyException.class)
	public void testProvideChangeNotEnough() throws InsufficientMoneyException {
		CashRegister register = getCashRegister();
		register.putMoney(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
		register.provideChange(100);
	}
	@Test(expected = InsufficientMoneyException.class)
	public void testProvideChangeWrongBills() throws InsufficientMoneyException {
		CashRegister register = getCashRegister();
		register.putMoney(Arrays.asList(new Integer[]{1, 1, 1, 1, 0}));
		register.provideChange(11);
	}
}
