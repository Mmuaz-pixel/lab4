package twitter;

import java.util.ArrayList;
import java.util.List;

public class Filter {

	public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
	    List<Tweet> result = new ArrayList<>();
	    for (Tweet tweet : tweets) {
	        if (tweet.getAuthor().equalsIgnoreCase(username)) {
	            result.add(tweet);
	        }
	    }
	    return result;
	}

	public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
	    List<Tweet> result = new ArrayList<>();
	    for (Tweet tweet : tweets) {
	        if (timespan.includes(tweet.getTimestamp())) {
	            result.add(tweet);
	        }
	    }
	    return result;
	}


	public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
	    List<Tweet> result = new ArrayList<>();
	    for (Tweet tweet : tweets) {
	        String tweetText = tweet.getText().toLowerCase();
	        for (String word : words) {
	            if (tweetText.contains(word.toLowerCase())) {
	                result.add(tweet);
	                break; // No need to check more words for this tweet
	            }
	        }
	    }
	    return result;
	}
}
