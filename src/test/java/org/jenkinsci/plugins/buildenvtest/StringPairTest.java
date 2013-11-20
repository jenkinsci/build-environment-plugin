package org.jenkinsci.plugins.buildenvtest;

import org.jenkinsci.plugins.buildenvironment.actions.utils.StringPair;
import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;

/**
 * Tests the class StringPair.
 * 
 * @author yboev
 * 
 */
public class StringPairTest extends HudsonTestCase {

    @Test
    public void testpairDifferent() {
        final String first = "first";
        final String second = "second";

        StringPair sp = new StringPair(first, second);
        assertEquals(first, sp.getFirst());
        assertEquals(second, sp.getSecond());
        assertTrue(sp.areDifferent());
    }

    @Test
    public void testPairSame() {
        StringPair sp = new StringPair("aaa", "aaa");
        assertFalse(sp.areDifferent());
    }

    @Test
    public void testNullPair() {
        StringPair nullPair = new StringPair(null, null);
        assertFalse(nullPair.areDifferent());
    }
}
