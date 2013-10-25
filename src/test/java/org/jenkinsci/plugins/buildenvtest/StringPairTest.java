package org.jenkinsci.plugins.buildenvtest;

import java.util.logging.Logger;

import org.jenkinsci.plugins.buildenvironment.actions.utils.StringPair;
import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;

/**
 * Tests the class StringPair.
 * @author yboev
 *
 */
public class StringPairTest extends HudsonTestCase {
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger
            .getLogger(StringPairTest.class.getName());
    
    
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
        LOGGER.info("SAME TEST starting.....");
        StringPair sp = new StringPair("aaa", "aaa");
        assertFalse(sp.areDifferent());
    }
}
