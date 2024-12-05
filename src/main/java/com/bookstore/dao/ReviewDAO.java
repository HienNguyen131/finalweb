/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.dao;

import com.bookstore.entity.Review;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */
public class ReviewDAO extends JpaDAO<Review> implements GenericDAO<Review> {

    @Override
    public void delete(Object id) {
        super.delete(Review.class, id); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public Review update(Review review) {
        return super.update(review); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public Review create(Review review) {
        review.setReviewTime(new Date());
        return super.create(review);
    }
    

    @Override
    public Review get(Object id) {
       return super.find(Review.class, id);
    }

    @Override
    public List<Review> listAll() {
        return super.findWithNamedQuery("Review.listAll");
    }

    @Override
    public long count() {
        return super.countWithNamedQuery("Review.countAll");
    }
    public Review findByCustomerAndBook(Integer customerId, Integer bookId){
        Map<String,Object> parameter = new HashMap<>();
        parameter.put("customerId", customerId);
        parameter.put("bookId", bookId);
        
        List<Review> list = super.findWithNamedQuery("Review.findByCustomerAndBook", parameter);
        if(!list.isEmpty()){
            return list.get(0);
        }
        return null;
    }
    public List<Review> listRecentReview(){
        return super.findWithNamedQuery("Review.listAll", 0, 3);
    }
}
