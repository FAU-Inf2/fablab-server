package de.fau.cs.mad.fablab.rest.server.resources.admin;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Path("/log")
@Produces(MediaType.TEXT_PLAIN)
public class LogResource {

    private final String INFO_LOG_PATH = "src/dist/log/server_info.log";
    private final String REQUEST_LOG_PATH = "src/dist/log/server_requests.log";

    @Path("/info")
    @GET
    public String getServerLog(@QueryParam("size") int size){
        try {
            List<String> requestLog = Files.readAllLines(Paths.get(INFO_LOG_PATH), Charset.defaultCharset());
            String toReturn = "";

            if(size > requestLog.size() || size <= 0)
                size = requestLog.size();

            for(int i = 0; i < size; i++){
                toReturn += requestLog.get(requestLog.size() - size + i)+ "\n";
            }

            return toReturn;
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Path("/requests")
    @GET
    public String getRequestLog(@QueryParam("size") int size){
        try {
            List<String> requestLog = Files.readAllLines(Paths.get(REQUEST_LOG_PATH), Charset.defaultCharset());
            String toReturn = "";

            if(size > requestLog.size() || size <= 0)
                size = requestLog.size();

            for(int i = 0; i < size; i++){
                toReturn += requestLog.get(requestLog.size() - size + i)+ "\n";
            }

            return toReturn;
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}
