package com.projectTest.demo;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.projectTest.demo.exceptions.ServiceException;
import com.projectTest.demo.models.Cart;
import com.projectTest.demo.models.Product;
import com.projectTest.demo.repository.CartRepository;
import com.projectTest.demo.services.CartService;

@ExtendWith(MockitoExtension.class)
public class CartServiceTests {

    @InjectMocks
    private CartService service;

    @Mock
    private CartRepository repository;

    private Cart cart;
    private Product product;
    private ArrayList<Product> products;

    @BeforeEach
    public void setup(){
        cart = new Cart();
        product = new Product("firstDesc", 1, 1);
        products = new ArrayList<Product>();
    }

    @Test
    public void whenGetCartIfExists_givingExistantId_returnIsNotNull() {
        given(repository.get(any(Integer.class))).willReturn(cart);

        Cart givenCart = service.getCartIfExists(cart.getId());

        assertThat(givenCart).isNotNull();
    }

    @Test
    public void whenGetCartIfExists_givingExistantId_shouldGiveCart() {
        given(repository.get(any(Integer.class))).willReturn(cart);

        Cart givenCart = service.getCartIfExists(cart.getId());

        assertThat(givenCart).isEqualTo(cart);
    }

    @Test
    public void whenGetCartIfExists_givingUnexistantId_shouldThrowException() {
        given(repository.get(any(Integer.class))).willReturn(null);

        org.junit.jupiter.api.Assertions.assertThrows(ServiceException.class, () -> {
            service.getCartIfExists(cart.getId());
        });
    }

    @Test
    public void whenCreateCart_returnIsNotNull() {
        given(repository.add(any(Integer.class), any(Cart.class))).willReturn(cart);

        Cart givenCart = service.createCart();

        assertThat(givenCart).isNotNull();
    }

    @Test
    public void whenCreateCart_shouldGiveCart() {
        given(repository.add(any(Integer.class), any(Cart.class))).willReturn(cart);

        Cart givenCart = service.createCart();

        assertThat(givenCart).isEqualTo(cart);
    }

    @Test
    public void whenCreateCart_shouldCreateCart() {
        given(repository.add(any(Integer.class), any(Cart.class))).willReturn(cart);

        service.createCart();

        assertThat(repository.estimatedSize() == 1);
    }

    @Test
    public void whenAddItemToCart_givingUnexistantId_shouldThrowException() {
        given(repository.get(any(Integer.class))).willReturn(null);

        products.add(product);

        org.junit.jupiter.api.Assertions.assertThrows(ServiceException.class, () -> {
            service.addItemToCart(products, 1);
        });
    }

    @Test
    public void whenAddItemToCart_withOneItemInTheArray_shouldAddProductToCart() {
        given(repository.get(any(Integer.class))).willReturn(cart);
        products.add(product);

        Cart givenCart = service.addItemToCart(products, cart.getId());

        assertThat(givenCart.getProducts().get(product.getId())).isEqualTo(product);
    }

    @Test
    public void whenAddItemToCart_withTheSameProductTwoTimes_shouldIncreaseAmount() {
        given(repository.get(any(Integer.class))).willReturn(cart);
        products.add(product);
        products.add(product);

        Cart givenCart = service.addItemToCart(products, cart.getId());

        assertThat(givenCart.getProducts().get(product.getId()).getAmount() == product.getAmount()*2);
    }

    @Test
    public void whenAddItemToCart_withTwoProductsInTheArray_shouldAddMoreThanOneElement() {
        given(repository.get(any(Integer.class))).willReturn(cart);
        products.add(product);
        var otherProduct = new Product("secondDesc", 2, 2);
        products.add(otherProduct);

        Cart givenCart = service.addItemToCart(products, cart.getId());

        assertThat(givenCart.getProducts().size() == 2);
        assertThat(givenCart.getProducts().get(product.getId())).isEqualTo(product);
        assertThat(givenCart.getProducts().get(otherProduct.getId())).isEqualTo(otherProduct);
    }

    @Test
    public void whenAddItemToCart_withEmptyArray_shouldAddNothing() {
        given(repository.get(any(Integer.class))).willReturn(cart);

        Cart givenCart = service.addItemToCart(products, cart.getId());

        assertThat(givenCart).isEqualTo(cart);
    }

    @Test
    public void whenDeleteCart_shouldDeleteElement() {
        repository.add(cart.getId(), cart);

        service.deleteCart(cart.getId());

        assertThat(repository.estimatedSize() == 0);
    }

    @Test
    public void whenDeleteCart_givingUnexistantId_shouldDoNothing() {
        var dummyCart = new Cart();
        repository.add(cart.getId(), cart);
        
        service.deleteCart(dummyCart.getId());

        assertThat(repository.estimatedSize() == 1);
    }

    
}
