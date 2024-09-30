package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "Alyssa", "Another tweet by Alyssa", d3);
    private static final Tweet tweet4 = new Tweet(4, "carl", "This tweet does not mention Rivest", d2);

    @Test
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> result = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "alyssa");
        
        assertEquals("expected two tweets by Alyssa", 2, result.size());
        assertTrue("expected list to contain tweet1", result.contains(tweet1));
        assertTrue("expected list to contain tweet3", result.contains(tweet3));
    }

    @Test
    public void testWrittenByNoMatches() {
        List<Tweet> result = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "nonexistent");
        assertTrue("expected empty list for nonexistent user", result.isEmpty());
    }

    @Test
    public void testInTimespanMultipleTweetsMultipleResults() {
        Timespan timespan = new Timespan(Instant.parse("2016-02-17T09:00:00Z"), Instant.parse("2016-02-17T11:30:00Z"));
        
        List<Tweet> result = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet4), timespan);
        
        assertFalse("expected non-empty list", result.isEmpty());
        assertTrue("expected list to contain tweet1", result.contains(tweet1));
        assertTrue("expected list to contain tweet2", result.contains(tweet2));
        assertEquals("expected same order", 0, result.indexOf(tweet1));
    }

    @Test
    public void testInTimespanNoResults() {
        Timespan timespan = new Timespan(Instant.parse("2016-02-17T11:30:00Z"), Instant.parse("2016-02-17T12:00:00Z"));
        
        List<Tweet> result = Filter.inTimespan(Arrays.asList(tweet1, tweet2), timespan);
        assertTrue("expected empty list for timespan with no matching tweets", result.isEmpty());
    }

    @Test
    public void testContaining() {
        List<Tweet> result = Filter.containing(Arrays.asList(tweet1, tweet2, tweet4), Arrays.asList("talk", "rivest"));
        
        assertFalse("expected non-empty list", result.isEmpty());
        assertTrue("expected list to contain tweet1", result.contains(tweet1));
        assertTrue("expected list to contain tweet2", result.contains(tweet2));
        assertEquals("expected same order", 0, result.indexOf(tweet1));
    }

    @Test
    public void testContainingNoMatches() {
        List<Tweet> result = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("nonexistent"));
        assertTrue("expected empty list for no matching words", result.isEmpty());
    }
}
