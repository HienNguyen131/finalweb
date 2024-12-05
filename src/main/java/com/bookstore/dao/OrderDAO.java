/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.dao;

import com.bookstore.entity.BookOrder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */
public class OrderDAO extends JpaDAO<BookOrder> implements GenericDAO<BookOrder> {



    @Override
    public BookOrder update(BookOrder order) {
        return super.update(order); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public BookOrder create(BookOrder order) {
        order.setStatus("Processing");
        order.setOrderDate(new Date());
        return super.create(order); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public BookOrder get(Object orderId) {
        return super.find(BookOrder.class,orderId);
    }
  
    public BookOrder get(Integer orderId, Integer customerId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("orderId", orderId);
        parameters.put("customerId", customerId);
        List<BookOrder> order = super.findWithNamedQuery("BookOrder.findByIdAndCustomer",parameters);
        if(!order.isEmpty()){
            return order.get(0);
        }
        return null;
    }
    @Override
    public void delete(Object id) {
       super.delete(BookOrder.class, id);
    }

    @Override
    public List<BookOrder> listAll() {
       return super.findWithNamedQuery("BookOrder.listAll");
    }

    @Override
    public long count() {
        return super.countWithNamedQuery("BookOrder.countAll");
    }
    public List<BookOrder> listByCustomer(Integer customerId){
        return super.findWithNamedQuery("BookOrder.findByCustomer", "customerId", customerId);
    }
    public List<BookOrder> listRecentSale(){
        return super.findWithNamedQuery("BookOrder.listAll", 0, 3);
    }

}
