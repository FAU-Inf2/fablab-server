package de.fau.cs.mad.fablab.rest.server.resources;

import org.junit.Test;
import org.junit.runners.model.InitializationError;

import static org.junit.Assert.*;

/**
 * Some tests for SpaceAPIResource.UpdateDataTest
 */
public class UpdateDataTest {

    @Test
    public void testUpdateDataConstructor()
    {
        SpaceAPIResource.UpdateData d = new SpaceAPIResource.UpdateData("open:123456");

        assertEquals(d.time, 123456);
        assertEquals(d.state, SpaceAPIResource.UpdateData.State.open);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testUpdateDataFail()
    {
        SpaceAPIResource.UpdateData d = new SpaceAPIResource.UpdateData("open");
    }

}
