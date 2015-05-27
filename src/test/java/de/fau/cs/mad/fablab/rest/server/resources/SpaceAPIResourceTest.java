package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.server.configuration.SpaceApiConfiguration;
import org.junit.Test;

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

        SpaceAPIResource resource = new SpaceAPIResource(new SpaceApiConfiguration());

        resource.updateDoorState("hash", "data");
    }

    // parsing data should return valid values
    @Test
    public void testParseDataOpen()
    {
        SpaceAPIResource.UpdateData data = SpaceAPIResource.parseData("12345:open");
        assertEquals(data.time, 12345);
        assertEquals(data.state, SpaceAPIResource.UpdateData.State.open);
    }

    @Test
    public void testParseDataClosed()
    {
        SpaceAPIResource.UpdateData data = SpaceAPIResource.parseData("456789:close");
        assertEquals(data.time, 456789);
        assertEquals(data.state, SpaceAPIResource.UpdateData.State.close);
    }

    // parsing data should fail, if format does not match the regex
    @Test(expected = BadRequestException.class)
    public void testParseDataOpenExpectedFailLess()
    {
        SpaceAPIResource.UpdateData data = SpaceAPIResource.parseData("open:");
    }

    // parsing data should fail, if format does not match the regex
    @Test(expected = BadRequestException.class)
    public void testParseDataOpenExpectedFailMore()
    {
        SpaceAPIResource.UpdateData data = SpaceAPIResource.parseData("open:12345:fail");
    }

    // parsing data should fail, if format does not match the regex
    @Test(expected = BadRequestException.class)
    public void testParseDataOpenExpectedFailInvalid()
    {
        SpaceAPIResource.UpdateData data = SpaceAPIResource.parseData("12345:hangingdoor");
    }

    @Test
    public void testCheckDataValid()
    {
        long currentTime = System.currentTimeMillis() / 1000L;
        SpaceAPIResource resource = new SpaceAPIResource(new SpaceApiConfiguration());
        SpaceAPIResource.UpdateData data = new SpaceAPIResource.UpdateData(String.valueOf(currentTime) + ":open");

        boolean checkResult = resource.checkData(data);

        assertTrue(checkResult);
    }

    @Test(expected = BadRequestException.class)
    public void testCheckDataInValidPast()
    {
        long currentTime = System.currentTimeMillis() / 1000L;
        SpaceAPIResource resource = new SpaceAPIResource(new SpaceApiConfiguration());
        SpaceAPIResource.UpdateData data = new SpaceAPIResource.UpdateData(String.valueOf(currentTime - 1000) + ":open");

        boolean checkResult = resource.checkData(data);

        assertFalse(checkResult);
    }

    @Test(expected = BadRequestException.class)
    public void testCheckDataInValidFuture()
    {
        long currentTime = System.currentTimeMillis() / 1000L;
        SpaceAPIResource resource = new SpaceAPIResource(new SpaceApiConfiguration());
        SpaceAPIResource.UpdateData data = new SpaceAPIResource.UpdateData(String.valueOf(currentTime + 1000) + ":open");

        boolean checkResult = resource.checkData(data);

        assertFalse(checkResult);
    }
}
