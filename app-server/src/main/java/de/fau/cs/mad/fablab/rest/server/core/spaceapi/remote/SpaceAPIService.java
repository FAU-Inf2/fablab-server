package de.fau.cs.mad.fablab.rest.server.core.spaceapi.remote;


import net.spaceapi.HackerSpace;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Interface for accessing (remote) SpaceAPI
 * Information about SpaceAPI: https://spaceapi.net/
 */
@Path("/")
public interface SpaceAPIService {

   // should be used, when querying spaceapi.net directly
   //@GET
   //@Path("/cache/{space}")
   //HackerSpace space(@PathParam("space") String space);

   @GET
   HackerSpace space();
}
