package de.fau.cs.mad.fablab.rest.server.core.doorstate;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Some tests for DoorState
 */
public class DoorStateTest {
    @Test
    public void testUpdateDataConstructor()
    {
        DoorState d = new DoorState("123456:open");

        assertEquals(d.time, 123456);
        assertEquals(d.state, DoorState.State.open);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateDataFailOutOfBound()
    {
        DoorState d = new DoorState("123445");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateDataFailNumber()
    {
        DoorState d = new DoorState("open");
    }

}