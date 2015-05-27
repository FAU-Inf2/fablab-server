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
            return getMessagesAsString(requestLog, size);
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Path("/requests")
    @GET
    public String getRequestLog(@QueryParam("size") int size){
        try {
            List<String> requestLog = Files.readAllLines(Paths.get(REQUEST_LOG_PATH), Charset.defaultCharset());
            return getMessagesAsString(requestLog, size);
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    /***
     * Helper method which returns a String containing the last n strings of a given list of strings
     * @param messageList the list of strings
     * @param n the number of strings to get
     * @return a String of n lines. if n <= 0 || n > messageList.size() the whole messageList is returned
     */
    private String getMessagesAsString(List<String> messageList, int n){
        String toReturn = "";

        if(n > messageList.size() || n <= 0)
            n = messageList.size();

        for(int i = 0; i < n; i++){
            toReturn += messageList.get(messageList.size() - n + i)+ "\n";
        }

        return toReturn;
    }
}
