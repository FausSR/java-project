package com.projectTest.demo.repository;

import java.time.Duration;

import org.springframework.stereotype.Repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.projectTest.demo.models.Cart;

@Repository
public class CartRepository{
    private Cache<Integer, Cart> cartDb;

    public Cart get(Integer cartId){
        return cartDb.getIfPresent(cartId);
    }

    public void delete(Integer cartId){
        cartDb.invalidate(cartId);
    }

    public Cart add(Integer cartId, Cart newCart){
        cartDb.put(cartId, newCart);
        return newCart;
    }

    public long estimatedSize(){
        return cartDb.estimatedSize();
    }

    public CartRepository(){
        Cache<Integer, Cart> cartDb = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(10))
            .build();
        this.cartDb = cartDb;
    }
}
