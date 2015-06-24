package de.fau.cs.mad.fablab.rest.server.core;

import de.fau.cs.mad.fablab.rest.core.CartServer;

import java.util.ArrayList;
import java.util.List;

/**
        This class removes old carts from the DB
        --> constructor gets time in mins when cart will be deleted

 */

public class OldCartRemover{
    private final CartDAO dao;
    private int removeAfterMinutes;

    public OldCartRemover(CartDAO dao, int removeAfterMinutes) {
        this.dao = dao;
        this.removeAfterMinutes = removeAfterMinutes;
    }


    public void removeAllOldCarts(){
        List<String> codesOfCartsToRemove = new ArrayList<>();
        long maxTime = System.currentTimeMillis() - removeAfterMinutes*3600;

        for(CartServer cart : dao.findAll())
            if (cart.getSentToServer() < maxTime)
                codesOfCartsToRemove.add(cart.getCartCode());

        for(String code : codesOfCartsToRemove)
            dao.delete(code);
    }
}
