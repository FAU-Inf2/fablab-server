package de.fau.cs.mad.fablab.rest.server.core;

import java.io.Serializable;

/**
 * Created by EE on 12.05.15.
 */
public class CartEntry implements Serializable {


    public long productId;
    public String name;
    public String description;
    public double count;
    public CartEntryStatusEnum status;

    public CartEntry(Product product, double count){
        this.productId = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.count = count;
        this.status = CartEntryStatusEnum.AVALABLE; //TODO -> CHANCE LATER?!
    }

}
