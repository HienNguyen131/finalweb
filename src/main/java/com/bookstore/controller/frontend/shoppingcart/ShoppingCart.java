/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.controller.frontend.shoppingcart;

import com.bookstore.entity.Book;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author DELL
 */
public class ShoppingCart {
    	private Map<Book, Integer> cart = new HashMap<>();
	
	public void addBook(Book book) {
		if (cart.containsKey(book)) {
			Integer quantity = cart.get(book) + 1;
			cart.put(book, quantity);			
		} else {
			cart.put(book, 1);
		}
	}
	
	public void deleteBook(Book book) {
		cart.remove(book);
	}
	
	public int getTotalQuantity() {
		int total = 0;
		
		Iterator<Book> iterator = cart.keySet().iterator();
		
		while (iterator.hasNext()) {
			Book next = iterator.next();
			Integer quantity = cart.get(next);
			total += quantity;
		}
		
		return total;
	}
	
	public float getTotalAmount() {
		float total = 0.0f;
		
		Iterator<Book> iterator = cart.keySet().iterator();
		
		while (iterator.hasNext()) {
			Book book = iterator.next();
			Integer quantity = cart.get(book);
			double subTotal = quantity * book.getPrice();
			total += subTotal;
		}		
		
		return total;
	}
	
	
	public void updateCart(int[] bookIds, int[] quantities) {
		for (int i = 0; i < bookIds.length; i++) {
			Book key = new Book(bookIds[i]);
			Integer value = quantities[i];
			cart.put(key, value);
		}
	}
	
	public void clear() {
		cart.clear();
	}
	
	public int getTotalItems() {
		return cart.size();
	}
	
	public Map<Book, Integer> getItems() {
		return this.cart;
	}
}
