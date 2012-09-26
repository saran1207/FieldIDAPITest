package com.n4systems.api.conversion.event;

import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LocationSpecificationTest {

    @Test
    public void test_location_parse() {
        assertEquals("blargh", new LocationSpecification("apple>orange>banana:blargh").getFreeForm());
        List<String> result = new LocationSpecification("apple>orange>banana:blargh").getHierarchy();
        assertEquals("apple", result.get(0));
        assertEquals("orange", result.get(1));
        assertEquals("banana", result.get(2));

        assertEquals("blargh", new LocationSpecification("apple>orange>banana:blargh").getFreeForm());
        result = new LocationSpecification("orange:blargh").getHierarchy();
        assertEquals("orange", result.get(0));

        assertEquals("blargh", new LocationSpecification("apple  >  orange    > banana   :blargh").getFreeForm());
        result = new LocationSpecification("apple  >  orange    > banana   :blargh").getHierarchy();
        assertEquals("apple", result.get(0));
        assertEquals("orange", result.get(1));
        assertEquals("banana", result.get(2));

        assertNull(new LocationSpecification("apple").getFreeForm());
        result = new LocationSpecification("apple").getHierarchy();
        assertEquals("apple", result.get(0));

        assertNull(new LocationSpecification("apple > banana > pear ").getFreeForm());
        result = new LocationSpecification("apple > banana > pear").getHierarchy();
        assertEquals("apple", result.get(0));
        assertEquals("banana", result.get(1));
        assertEquals("pear", result.get(2));

        assertNull(new LocationSpecification("two words > banana > pear ").getFreeForm());
        result = new LocationSpecification("two words> banana > pear").getHierarchy();
        assertEquals("two words", result.get(0));
        assertEquals("banana", result.get(1));
        assertEquals("pear", result.get(2));

        assertEquals("", new LocationSpecification(" hello :    ").getFreeForm());
        result = new LocationSpecification(" hello :   ").getHierarchy();
        assertEquals("hello", result.get(0));

        assertEquals("", new LocationSpecification(" :    ").getFreeForm());
        assertTrue(new LocationSpecification(" :   ").getHierarchy().isEmpty());

        assertEquals("blargh", new LocationSpecification(" :   blargh").getFreeForm());
        assertTrue(new LocationSpecification(" :   blargh").getHierarchy().isEmpty());

        assertEquals("twocolons : hmmm", new LocationSpecification(" : twocolons : hmmm").getFreeForm());
        assertTrue(new LocationSpecification(" : twocolons : hmmm").getHierarchy().isEmpty());

    }


    @Test(expected=IllegalArgumentException.class)
    public void test_exception3() {
        new LocationSpecification("TrailingBracketNoGood >").getHierarchy().isEmpty();
    }


}
