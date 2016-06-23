package com.simplyautomatic.cashregister;

import java.util.ArrayList;
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
		validateAmounts(amountsToAdd);
		Iterator<Integer> denominationIterator = inventory.keySet().iterator();
		for (Integer amountToAdd : amountsToAdd) {
			Integer denomination = denominationIterator.next();
			inventory.put(denomination, inventory.get(denomination) + amountToAdd);
		}
	}
	
	/**
	 * Take money from the cash register
	 * @param amountsToTake Output money, in descending order by register's denominations.
	 * @throws IllegalArgumentException if input denominations do not match register.
	 * @throws InsufficientMoneyException if register has insufficient money.
	 */
	public void takeMoney(List<Integer> amountsToTake) throws InsufficientMoneyException {
		validateAmounts(amountsToTake);
		Iterator<Integer> denominationIterator = inventory.keySet().iterator();
		for (Integer amountToTake : amountsToTake) {
			Integer denomination = denominationIterator.next();
			if (amountToTake > inventory.get(denomination)) {
				throw new InsufficientMoneyException("Insufficient money. Not enough " + denomination + "'s.");
			}
		}
		denominationIterator = inventory.keySet().iterator();
		for (Integer amountToTake : amountsToTake) {
			Integer denomination = denominationIterator.next();
			inventory.put(denomination, inventory.get(denomination) - amountToTake);
		}
	}
	
	/**
	 * Provide change from the cash register for specified amount
	 * @param targetChange Sum to provide change for
	 * @return description of change to provide
	 * @throws InsufficientMoneyException if register cannot make change.
	 */
	public String provideChange(Integer targetChange) throws InsufficientMoneyException {
		if (targetChange > getTotal()) {
			throw new InsufficientMoneyException("Not enough money in register.");
		}
		List<Integer> change = provideChangeRecursive(targetChange, inventory.keySet().iterator().next());
		// Handle failure
		if (change == null) {
			throw new InsufficientMoneyException("Not enough money of proper denominations to make change.");
		}
		// Pad list length
		for(int i = change.size(); i < inventory.size(); i++) {
			change.add(0);
		}
		// Deduct from register
		takeMoney(change);
		// Print result
		String changeText = "";
		for (Integer changeAmount : change) {
			changeText += changeAmount + " ";
		}
		return changeText.trim();
	}
	
	/**
	 * Determine proper change to provide for target amount. Finds a way to 
	 * provide change, starting with a greedy large-to-small approach, but 
	 * recursively explores solutions until one is found (if possible)
	 * @param targetChange target amount of change to provide
	 * @param denomination initial/largest denomination to start with
	 * @return List of change to provide, starting with initial/largest denomination,
	 * of length required to hold non-zero amounts. Null if change cannot be provided.
	 */
	private List<Integer> provideChangeRecursive(Integer targetChange, Integer denomination) {
		// Calculate max number of this denomination to provide as change:
		//   maximum of (amount we have), (amount that wouldn't exceed target value)
		Integer maxAmountCanProvide = inventory.get(denomination);
		Integer maxAmountShouldProvide = targetChange / denomination; // Note rounds down
		Integer maxAmountToProvide = Math.min(maxAmountCanProvide, maxAmountShouldProvide);
		// Greedily try all amounts of denomination, from maxAmountToProvide down to 0
		for (int curAmountToProvide = maxAmountToProvide; curAmountToProvide >= 0; curAmountToProvide--) {
			Integer newTargetChange = targetChange - denomination * curAmountToProvide;
			if (newTargetChange == 0) {
				// If providing this much exactly meets target, return the amount
				return new ArrayList<>(Arrays.asList(new Integer[] {curAmountToProvide}));
			} else {
				// Else, see if there's a lower denomination
				Iterator<Integer> denominationIterator = inventory.keySet().iterator();
				while (!denominationIterator.next().equals(denomination)) { }
				if (denominationIterator.hasNext()) {
					// If there's a lower denomination, call recursively with it
					Integer nextDenomination = denominationIterator.next();
					List<Integer> subChange = provideChangeRecursive(newTargetChange, nextDenomination);
					if (subChange != null) {
						// If sub-call resulted in a solution, add current amount, and return list of amounts
						subChange.add(0, curAmountToProvide);
						return subChange;
					}
				} else {
					// If there's no lower denomination, return failure
					return null;
				}
			}
		}
		// If no sub-call found a solution, and we didn't, return failure
		return null;
	}
	
	// Helper to validate input amounts
	private void validateAmounts(List<Integer> amountsToAdd) {
		if (amountsToAdd == null || amountsToAdd.size() != inventory.keySet().size()) {
			throw new IllegalArgumentException("Number of denominations must match register.");
		}
		for (Integer amountToAdd : amountsToAdd) {
			if (amountToAdd == null || amountToAdd < 0) {
				throw new IllegalArgumentException("Amount must be > 0.");
			}
		}
	}
}
