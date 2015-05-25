package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.server.configuration.SpaceApiConfiguration;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ServiceUnavailableException;

import static org.junit.Assert.*;

/**
 * Some tests for SpaceAPIResource
 */
public class SpaceAPIResourceTest {

    // keyfile is not set in configuration, so the service should not be available
    @Test(expected = ServiceUnavailableException.class)
    public void testUpdateDoorStateUnconfigured() throws Exception {

        SpaceAPIResource r = new SpaceAPIResource(new SpaceApiConfiguration());

        r.updateDoorState("hash", "data");
    }

    // parsing data should return valid values
    @Test
    public void testParseDataOpen()
    {
        SpaceAPIResource.UpdateData d = SpaceAPIResource.parseData("open:12345");
        assertEquals(d.time, 12345);
        assertEquals(d.state, SpaceAPIResource.UpdateData.State.open);
    }

    @Test
    public void testParseDataClosed()
    {
        SpaceAPIResource.UpdateData d = SpaceAPIResource.parseData("closed:456789");
        assertEquals(d.time, 456789);
        assertEquals(d.state, SpaceAPIResource.UpdateData.State.closed);
    }

    // parsing data should fail, if format does not match the regex
    @Test(expected = BadRequestException.class)
    public void testParseDataOpenExpectedFailLess()
    {
        SpaceAPIResource.UpdateData d = SpaceAPIResource.parseData("open:");
    }

    // parsing data should fail, if format does not match the regex
    @Test(expected = BadRequestException.class)
    public void testParseDataOpenExpectedFailMore()
    {
        SpaceAPIResource.UpdateData d = SpaceAPIResource.parseData("open:12345:fail");
    }

    // parsing data should fail, if format does not match the regex
    @Test(expected = BadRequestException.class)
    public void testParseDataOpenExpectedFailInvalid()
    {
        SpaceAPIResource.UpdateData d = SpaceAPIResource.parseData("hangingdoor:12345");
    }
}
