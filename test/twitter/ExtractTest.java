package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

    // Sample tweet instances
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes @john", d2);
    private static final Tweet tweet3 = new Tweet(3, "carl", "@Alice, did you see @bob's tweet?", d3);
    private static final Tweet tweet4 = new Tweet(4, "dave", "No mentions here!", d1);
    
    // Timespan test cases
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    @Test
    public void testGetTimespanSingleTweet() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d1, timespan.getEnd());
    }
    
    @Test
    public void testGetTimespanMultipleTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet3));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d3, timespan.getEnd());
    }
    
    @Test
    public void testGetTimespanNoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList());
        assertNull("expected null for empty list", timespan);
    }
    
    // Mentioned users test cases
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet4));
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    @Test
    public void testGetMentionedUsersSingleMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet2));
        assertEquals("expected set size 1", 1, mentionedUsers.size());
        assertTrue("expected mention of 'john'", mentionedUsers.contains("john"));
    }
    
    @Test
    public void testGetMentionedUsersMultipleMentions() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3));
        assertEquals("expected set size 2", 2, mentionedUsers.size());
        assertTrue("expected mention of 'alice'", mentionedUsers.contains("alice"));
        assertTrue("expected mention of 'bob'", mentionedUsers.contains("bob"));
    }
    
    @Test
    public void testGetMentionedUsersCaseInsensitivity() {
        Tweet tweetWithCase = new Tweet(5, "eve", "Hello @ALICE!", d1);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetWithCase));
        assertTrue("expected mention of 'alice'", mentionedUsers.contains("alice"));
    }
    
    @Test
    public void testGetMentionedUsersInvalidMentions() {
        Tweet tweetWithInvalidMention = new Tweet(6, "frank", "Not a mention: @alice123, but this is: @alice!", d1);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetWithInvalidMention));
        assertEquals("expected set size 1", 1, mentionedUsers.size());
        assertTrue("expected mention of 'alice'", mentionedUsers.contains("alice"));
    }

    @Test
    public void testGetMentionedUsersMixedContent() {
        Tweet mixedTweet = new Tweet(7, "grace", "@alice, this is a test! @invalid@user", d1);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(mixedTweet));
        assertEquals("expected set size 1", 1, mentionedUsers.size());
        assertTrue("expected mention of 'alice'", mentionedUsers.contains("alice"));
    }

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
}
