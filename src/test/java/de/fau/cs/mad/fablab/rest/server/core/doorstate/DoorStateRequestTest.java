package de.fau.cs.mad.fablab.rest.server.core.doorstate;

import de.fau.cs.mad.fablab.rest.server.configuration.SpaceApiConfiguration;
import org.junit.Test;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ServiceUnavailableException;

import static org.junit.Assert.*;

/**
 * Some tests for DoorStateRequest
 */
public class DoorStateRequestTest {

    // keyfile is not set in configuration, so the service should not be available
    @Test(expected = ServiceUnavailableException.class)
    public void testUpdateDoorStateUnconfigured() throws Exception {

        DoorStateRequest request = DoorStateRequest.fromData(new SpaceApiConfiguration(), "hash", "data");
    }

    // parsing data should return valid values
    @Test
    public void testParseDataOpen()
    {
        DoorStateRequest request = new DoorStateRequest(new SpaceApiConfiguration());
        DoorState data = request.parseData("12345:open");
        assertEquals(data.time, 12345);
        assertEquals(data.state, DoorState.State.open);
    }

    @Test
    public void testParseDataClosed()
    {
        DoorStateRequest request = new DoorStateRequest(new SpaceApiConfiguration());
        DoorState data = request.parseData("456789:close");
        assertEquals(data.time, 456789);
        assertEquals(data.state, DoorState.State.close);
    }

    // parsing data should fail, if format does not match the regex
    @Test(expected = BadRequestException.class)
    public void testParseDataOpenExpectedFailLess()
    {
        DoorStateRequest request = new DoorStateRequest(new SpaceApiConfiguration());
        DoorState data = request.parseData("open:");
    }

    // parsing data should fail, if format does not match the regex
    @Test(expected = BadRequestException.class)
    public void testParseDataOpenExpectedFailMore()
    {
        DoorStateRequest request = new DoorStateRequest(new SpaceApiConfiguration());
        DoorState data = request.parseData("open:12345:fail");
    }

    // parsing data should fail, if format does not match the regex
    @Test(expected = BadRequestException.class)
    public void testParseDataOpenExpectedFailInvalid()
    {
        DoorStateRequest request = new DoorStateRequest(new SpaceApiConfiguration());
        DoorState data = request.parseData("12345:hangingdoor");
    }

    @Test
    public void testCheckDataValid()
    {
        long currentTime = System.currentTimeMillis() / 1000L;
        DoorStateRequest request = new DoorStateRequest(new SpaceApiConfiguration());
        DoorState data = new DoorState(String.valueOf(currentTime) + ":open");

        boolean checkResult = request.checkData(data);

        assertTrue(checkResult);
    }

    @Test(expected = BadRequestException.class)
    public void testCheckDataInValidPast()
    {
        long currentTime = System.currentTimeMillis() / 1000L;
        DoorStateRequest request = new DoorStateRequest(new SpaceApiConfiguration());
        DoorState data = new DoorState(String.valueOf(currentTime - 1000) + ":open");

        boolean checkResult = request.checkData(data);

        assertFalse(checkResult);
    }

    @Test(expected = BadRequestException.class)
    public void testCheckDataInValidFuture()
    {
        long currentTime = System.currentTimeMillis() / 1000L;
        DoorStateRequest resource = new DoorStateRequest(new SpaceApiConfiguration());
        DoorState data = new DoorState(String.valueOf(currentTime + 1000) + ":open");

        boolean checkResult = resource.checkData(data);

        assertFalse(checkResult);
    }
}