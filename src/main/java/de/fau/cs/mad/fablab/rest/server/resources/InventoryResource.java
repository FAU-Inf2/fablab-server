
package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.api.InventoryApi;
import de.fau.cs.mad.fablab.rest.core.InventoryItem;
import de.fau.cs.mad.fablab.rest.core.Roles;
import de.fau.cs.mad.fablab.rest.core.User;
import de.fau.cs.mad.fablab.rest.server.core.InventoryFacade;
import de.fau.cs.mad.fablab.rest.server.exceptions.Http401Exception;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.commons.lang.StringEscapeUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;



public class InventoryResource implements InventoryApi {

    private final InventoryFacade facade;
    private final static String CSV_SEPARATOR = ";";
    private final static String CSV_LINE_SEPARATOR = "\n";

    public InventoryResource(InventoryFacade facade) {
        this.facade = facade;
    }

    @UnitOfWork
    @Override
    public InventoryItem add(@Auth User user, InventoryItem obj) {

        if (!user.hasRole(Roles.INVENTORY))
            throw new Http401Exception("Inventory role required");

        return this.facade.create(obj);
    }

    @UnitOfWork
    @Override
    public List<InventoryItem> getAll() {
        return this.facade.getAll();
    }

    @UnitOfWork
    @Override
    public Boolean deleteAll(@Auth User user) {

        if (!user.hasRole(Roles.INVENTORY))
            throw new Http401Exception("Inventory role required");

        return this.facade.deleteAll();
    }

    @UnitOfWork
    @Override
    public Response csvExport() {
        StringBuilder str = new StringBuilder();

        // header row
        str.append("ProductID");
        str.append(CSV_SEPARATOR);
        str.append("UUID");
        str.append(CSV_SEPARATOR);
        str.append("ProductName");
        str.append(CSV_SEPARATOR);
        str.append("Amount");
        str.append(CSV_SEPARATOR);
        str.append("LastUpdate");
        str.append(CSV_SEPARATOR);
        str.append("LastUpdateBy");
        str.append(CSV_LINE_SEPARATOR);

        // item rows
        for (InventoryItem item : this.facade.getAll()) {
            str.append(item.getProductId());
            str.append(CSV_SEPARATOR);
            str.append("\"" + StringEscapeUtils.escapeCsv(item.getUUID()) + "\"");
            str.append(CSV_SEPARATOR);;
            str.append("\"" + StringEscapeUtils.escapeCsv(item.getProductName()) + "\"");
            str.append(CSV_SEPARATOR);
            str.append(item.getAmount());
            str.append(CSV_SEPARATOR);
            str.append(item.getUpdated_at());
            str.append(CSV_SEPARATOR);
            str.append("\"" + StringEscapeUtils.escapeCsv(item.getUserName()) + "\"");
            str.append(CSV_LINE_SEPARATOR);
        }

        // send as "inventory.csv" file
        return Response.ok(getOut(str.toString().getBytes())).build();
    }

    private StreamingOutput getOut(final byte[] excelBytes) {
        return new StreamingOutput() {
            @Override
            public void write(OutputStream out) throws IOException, WebApplicationException {
                out.write(excelBytes);
            }
        };
    }
}
