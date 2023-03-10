package com.projectTest.demo.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectTest.demo.exceptions.ServiceException;
import com.projectTest.demo.models.Cart;
import com.projectTest.demo.models.Product;
import com.projectTest.demo.repository.CartRepository;

@Service
public class CartService {
    @Autowired
    CartRepository cartDb;

    public Cart getCartIfExists(int cartId){
        Cart selectedCart = cartDb.get(cartId);
        if(selectedCart != null) return selectedCart;
        else throw new ServiceException("That cart doesn't exist");
    }

    public Cart createCart(){
        Cart newCart = new Cart();
        return cartDb.add(newCart.getId(), newCart);
    }

    public Cart addItemToCart(ArrayList<Product> products, int cartId) throws ServiceException{
        Cart selectedCart = getCartIfExists(cartId);
        selectedCart.addProducts(products);
        return selectedCart;
    }

    public void deleteCart(int id){
        cartDb.delete(id);
    }
}
