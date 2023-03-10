package com.projectTest.demo.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectTest.demo.exceptions.ControllerException;
import com.projectTest.demo.exceptions.ServiceException;
import com.projectTest.demo.models.Cart;
import com.projectTest.demo.models.Product;
import com.projectTest.demo.services.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping(path = "/{id}")
    Cart getCart(@PathVariable("id") int id){
        try{
            return cartService.getCartIfExists(id);
        }
        catch(ServiceException exc){
            throw new ControllerException(exc.getMessage());
        }
    }

    @PostMapping
    Cart createCart(){
        return cartService.createCart();
    }

    @PutMapping(path = "/{id}")
    Cart addItems(@RequestBody ArrayList<Product> products, @PathVariable("id") int cartId){
        try{
            if(!products.isEmpty())
                return cartService.addItemToCart(products, cartId);
            else throw new ControllerException("There are no products to add to the cart", HttpStatus.BAD_REQUEST);
        }
        catch(ServiceException exc){
            throw new ControllerException(exc.getMessage());
        }
    }

    @DeleteMapping(path = "/{id}")
    void deleteCart(@PathVariable("id") int id){
        cartService.deleteCart(id);
    }
}
