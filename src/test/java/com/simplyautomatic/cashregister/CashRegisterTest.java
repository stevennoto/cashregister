package com.simplyautomatic.cashregister;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CashRegister
 * @author stevennoto
 */
public class CashRegisterTest {
	public CashRegister getCashRegister() {
		CashRegister cashRegister = new CashRegister();
		return cashRegister;
	}
	
	/**
	 * Test of getTotal method, of class CashRegister.
	 */
	@Test
	public void testGetTotal() {
		CashRegister register = getCashRegister();
		assertEquals(0, register.getTotal());
	}

	/**
	 * Test of showInventory method, of class CashRegister.
	 */
	@Test
	public void testShowInventory() {
		CashRegister register = getCashRegister();
		assertEquals("$0 0 0 0 0 0", register.showInventory());
	}
	
}
