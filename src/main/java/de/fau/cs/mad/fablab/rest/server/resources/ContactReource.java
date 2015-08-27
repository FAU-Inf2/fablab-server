package de.fau.cs.mad.fablab.rest.server.resources;


import de.fau.cs.mad.fablab.rest.api.ContactApi;
import de.fau.cs.mad.fablab.rest.core.CartEntryServer;
import de.fau.cs.mad.fablab.rest.core.CartServer;
import de.fau.cs.mad.fablab.rest.core.CartStatus;
import de.fau.cs.mad.fablab.rest.core.TestObject;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class ContactReource implements ContactApi{

    @Override
    public Response sendFeedback(String aMessage) {
        System.out.println("Send Feedbac: String: " + aMessage);
        if(aMessage.isEmpty()) {
            return Response.serverError().build();
        }
        return Response.ok().build();
    }



    @Override
    public TestObject create(TestObject obj) {
        System.out.println("asdfasdfasfasfd");
        System.out.println(obj.toString());
        TestObject newTestObject = new TestObject();
        newTestObject.setFirst(3);
        newTestObject.setSecond("Hallo zurueck");
        return newTestObject;
    }

    @Override
    public CartServer getCartServer() {
        CartServer newCartServer = new CartServer();
        newCartServer.setCartCode("exampleCartCode");

        CartEntryServer newCartEntryServer = new CartEntryServer();
        newCartEntryServer.setId(5234);
        newCartEntryServer.setProductId("0042");
        newCartEntryServer.setAmount(23);

        CartEntryServer newCartEntryServer2 = new CartEntryServer();
        newCartEntryServer2.setId(7534);
        newCartEntryServer2.setProductId("0062");
        newCartEntryServer2.setAmount(83);

        List<CartEntryServer> cartEntryList = new ArrayList<>();
        cartEntryList.add(newCartEntryServer);
        cartEntryList.add(newCartEntryServer2);

        newCartServer.setItems(cartEntryList);
        newCartServer.setStatus(CartStatus.CANCELLED);
        newCartServer.setPushToken("asdfas123");
        newCartServer.setSentToServer();


        return newCartServer;
    }

    @Override
    public TestObject getTestObject() {
        TestObject newObject = new TestObject();
        newObject.setFirst(2);
        newObject.setSecond("asdfa");
        return newObject;
    }

    @Override
    public Response sendErrorMessage(long aToolId, String aMessage) {
        System.out.println("Send ErrorMessage: String: " + aToolId + " and " + aMessage);
        if(aToolId == 0){
            return Response.serverError().build();
        }
        return Response.ok().build();
    }
}
