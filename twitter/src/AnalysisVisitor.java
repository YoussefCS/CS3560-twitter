package com.twitter;

public class AnalysisVisitor implements Visitor {
    private int totalUsers;
    private int totalGroups;
    private int totalTweets;
    private int positiveTweets;

    @Override
    public void visit(User user) {
        totalUsers++;
        totalTweets += user.getNewsFeedSize();
        for (String tweet : user.getNewsFeed()) {
            if (containsPositiveWords(tweet)) {
                positiveTweets++;
            }
        }
    }

    @Override
    public void visit(UserGroup userGroup) {
        totalGroups++;
    }

    private boolean containsPositiveWords(String tweet) {
        // Implement your logic for detecting positive words
        String[] positiveWords = {"good", "great", "excellent"};
        for (String word : positiveWords) {
            if (tweet.toLowerCase().contains(word)) {
                return true;
            }
        }
        return false;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public int getTotalGroups() {
        return totalGroups;
    }

    public int getTotalTweets() {
        return totalTweets;
    }

    public double getPositiveTweetsPercentage() {
        return totalTweets == 0 ? 0 : ((double) positiveTweets / totalTweets) * 100;
    }
}
