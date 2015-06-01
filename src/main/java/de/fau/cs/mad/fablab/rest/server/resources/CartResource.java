package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.api.CartApi;
import de.fau.cs.mad.fablab.rest.api.NewsApi;
import de.fau.cs.mad.fablab.rest.core.*;
import de.fau.cs.mad.fablab.rest.server.core.*;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by EE on 12.05.15.
 */

public class CartResource implements CartApi, Checkout {

    private final CartFacade facade;


    public CartResource(CartFacade facade) {
        this.facade = facade;
    }

    @UnitOfWork
    @Override
    public Cart create(Cart obj) {
        return this.facade.create(obj);
    }


    @Override
    public Cart getCurrentCart() {
        Cart cart = new Cart();
        cart.setId(235252342);
        cart.setStatus(CartStatusEnum.PENDING);
        Product product1 = new Product("0", "Schraube 1", 3.14, 1, "Schrauben", "Stück", "Lagerort");
        Product product2 = new Product("1", "Schraube 2", 13.14, 1, "Schrauben", "Stück", "Lagerort");
        Product product3 = new Product("2", "Schraube 3", 73.14, 1, "Schrauben", "Stück", "Lagerort");
        CartEntry cartEntry1 = new CartEntry(product1, 10);
        CartEntry cartEntry2 = new CartEntry(product2, 20);
        CartEntry cartEntry3 = new CartEntry(product3, 30);
        ArrayList<CartEntry> entries = new ArrayList<>();
        entries.add(cartEntry1);
        entries.add(cartEntry2);
        entries.add(cartEntry3);
        cart.setProducts(entries);
        return cart;
    }


    @Override
    public CartStatusEnum getStatus(long id) {
        return null;
    }
}