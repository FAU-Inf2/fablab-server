package de.fau.cs.mad.fablab.rest.server.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EE on 12.05.15.
 * -Status
 -id
 -Produkte
 -Produkt Anzahl
 */
@Entity
@Table(name="cart")
public class Cart implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "status")
    private CartStatusEnum status;

    @Column(name = "products")
    private ArrayList<CartEntry> products;


    public Cart(){
        status = CartStatusEnum.SHOPPING;
        products = new ArrayList<>();
    }


    @JsonProperty
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty
    public CartStatusEnum getStatus() {
        return status;
    }
    public void setStatus(CartStatusEnum status) {
        this.status = status;
    }

    @JsonProperty
    public ArrayList<CartEntry> getProducts(){
        return products;
    }

    public void setProducts(ArrayList<CartEntry> products){
        this.products = products;
    }



    public ArrayList<CartEntry> addProduct(Product product, double count){
        for(CartEntry e : products){
            if(e.productId == product.getId()){
                e.count += count;
                return products;
            }
        }
        products.add(new CartEntry(product,count));
        return products;
    }

    public ArrayList<CartEntry> removeProduct(Product product){
        for(CartEntry e : products){
            if(e.productId == product.getId()){
                products.remove(e);
            }
        }
        return products;
    }

}
