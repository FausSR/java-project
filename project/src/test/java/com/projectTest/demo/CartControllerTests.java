package com.projectTest.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.google.gson.Gson;
import com.projectTest.demo.controllers.CartController;
import com.projectTest.demo.exceptions.ServiceException;
import com.projectTest.demo.models.Cart;
import com.projectTest.demo.models.Product;
import com.projectTest.demo.services.CartService;

@JsonIgnoreProperties(ignoreUnknown = true)
@ExtendWith(MockitoExtension.class)
public class CartControllerTests {
    
    private MockMvc mvc;

    @InjectMocks
    private CartController cartController;

    @Mock
    private CartService cartService;

    private Cart cart;

    @BeforeEach
    public void setup(){
        cart = new Cart();
        mvc = MockMvcBuilders.standaloneSetup(cartController)
                .build();
    }

    @Test
    public void whenPost_status200() throws Exception {
        given(cartService.createCart()).willReturn(cart);
        
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .post("/cart")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void whenGet_withExistantId_status200() throws Exception {
        given(cartService.getCartIfExists(any(Integer.class))).willReturn(cart);
        
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .get("/cart/" + cart.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void whenGet_withUnexistantId_status404() throws Exception {
        given(cartService.getCartIfExists(any(Integer.class))).willThrow(new ServiceException("That cart doesn't exist"));
        
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .get("/cart/" + cart.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void whenPut_withUnexistantId_status404() throws Exception {
        Product product = new Product("firstDesc", 1, 1);
        ArrayList<Product> products = new ArrayList<Product>();
        products.add(product);
        String json = new Gson().toJson(products);
        
        given(cartService.addItemToCart(any(), any(Integer.class))).willThrow(new ServiceException("That cart doesn't exist"));
        
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .put("/cart/" + cart.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    
    @Test
    public void whenPut_withExistantId_status200() throws Exception {
        Product product = new Product("firstDesc", 1, 1);
        ArrayList<Product> products = new ArrayList<Product>();
        products.add(product);
        String json = new Gson().toJson(products);
        
        cart.addProducts(products);
        given(cartService.addItemToCart(any(), any(Integer.class))).willReturn(cart);
        
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .put("/cart/" + cart.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void whenPut_withEmptyList_status400() throws Exception {        
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .put("/cart/" + cart.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("[]"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
