package com.simplyautomatic.cashregister;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * CashRegister: Business logic object holding cash register state and main
 * operations logic.
 * @author stevennoto
 */
public class CashRegister {
	/**
	 * Cash is kept in a map, with keys corresponding to denominations (5, etc),
	 * and values corresponding to amount of currency in the register. Map is
	 * sorted (by key, reversed) to allow traversal in descending-denomination-order.
	 */
	private final SortedMap<Integer, Integer> inventory =
			new TreeMap<>(Collections.reverseOrder());
	
	/**
	 * Default collection of denominations to use for normal use cases.
	 */
	private final static List<Integer> DEFAULT_DENOMINATIONS = 
			Arrays.asList(new Integer[]{20, 10, 5, 2, 1});
	
	/**
	 * Construct a new cash register, initially empty, with default denominations.
	 */
	public CashRegister() {
		this(DEFAULT_DENOMINATIONS);
	}
	
	/**
	 * Construct a new cash register, initially empty, with specified denominations.
	 * @param denominations Collection of denominations to include.
	 * @throws IllegalArgumentException if invalid denominations.
	 */
	public CashRegister(Collection<Integer> denominations) {
		if (denominations == null || denominations.isEmpty()) {
			throw new IllegalArgumentException("Denominations required.");
		}
		for (Integer denomination : denominations) {
			if (denomination == null || denomination <= 0) {
				throw new IllegalArgumentException("Denominations must be > 0.");
			}
			if (inventory.containsKey(denomination)) {
				throw new IllegalArgumentException("Denominations must be unique.");
			}
			inventory.put(denomination, 0);
		}
	}
	
	/**
	 * Gets total amount of money in cash register.
	 * @return total value of inventory
	 */
	public int getTotal() {
		int total = 0;
		for (Map.Entry<Integer, Integer> entry : inventory.entrySet()) {
			total += (entry.getKey() * entry.getValue());
		}
		return total;
	}
	
	/**
	 * Return text information on the contents of the cash register.
	 * @return Text string, like "$68 1 2 3 4 5"
	 */
	public String showInventory() {
		String inventoryText = "$" + getTotal() + " ";
		for (Map.Entry<Integer, Integer> entry : inventory.entrySet()) {
			inventoryText += entry.getValue() + " ";
		}
		return inventoryText.trim();
	}
	
	/**
	 * Put money into the cash register
	 * @param amountsToAdd Input money, in descending order by register's denominations.
	 * @throws IllegalArgumentException if input denominations do not match register.
	 */
	public void putMoney(List<Integer> amountsToAdd) {
		// Validate
		if (amountsToAdd == null || amountsToAdd.size() != inventory.keySet().size()) {
			throw new IllegalArgumentException("Number of denominations must match register.");
		}
		for (Integer amountToAdd : amountsToAdd) {
			if (amountToAdd == null || amountToAdd < 0) {
				throw new IllegalArgumentException("Amount to add must be > 0.");
			}
		}
		// Add amounts to inventory
		Iterator<Integer> denominationIterator = inventory.keySet().iterator();
		for (Integer amountToAdd : amountsToAdd) {
			Integer denomination = denominationIterator.next();
			inventory.put(denomination, inventory.get(denomination) + amountToAdd);
		}
	}
}
