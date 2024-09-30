package twitter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.time.Instant;

public class Extract {

    public static Timespan getTimespan(List<Tweet> tweets) {
        if (tweets.isEmpty()) {
            return null; // Return null for an empty list
        }
        
        Instant start = Instant.MAX;
        Instant end = Instant.MIN;
        
        for (Tweet tweet : tweets) {
            Instant tweetTime = tweet.getTimestamp();
            if (tweetTime.isBefore(start)) {
                start = tweetTime;
            }
            if (tweetTime.isAfter(end)) {
                end = tweetTime;
            }
        }
        
        return new Timespan(start, end);
    }

    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        Set<String> mentionedUsers = new HashSet<>();
        
        for (Tweet tweet : tweets) {
            String text = tweet.getText();
            int length = text.length();
            StringBuilder username = new StringBuilder();
            boolean isMention = false;

            for (int i = 0; i < length; i++) {
                char currentChar = text.charAt(i);
                
                if (currentChar == '@') {
                    if (isMention) {
                        // We found a previous mention; add it before starting a new one
                        addIfValid(mentionedUsers, username.toString());
                        username.setLength(0); // Reset username
                    }
                    isMention = true; // Start a new mention
                } else if (isMention && (Character.isLetterOrDigit(currentChar) || currentChar == '_')) {
                    username.append(Character.toLowerCase(currentChar)); // Build the username, case-insensitive
                } else {
                    if (isMention) {
                        addIfValid(mentionedUsers, username.toString());
                        username.setLength(0); // Reset username
                        isMention = false; // End the current mention
                    }
                }
            }
            // If we finish while still in a mention
            if (isMention) {
                addIfValid(mentionedUsers, username.toString());
            }
        }
        
        return mentionedUsers;
    }

    private static void addIfValid(Set<String> mentionedUsers, String username) {
        // Valid username should not be empty
        if (!username.isEmpty()) {
            mentionedUsers.add(username);
        }
    }
}
