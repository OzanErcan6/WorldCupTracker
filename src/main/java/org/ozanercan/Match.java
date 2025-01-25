package org.ozanercan;

public class Match implements Comparable<Match>{
    private String homeTeam;
    private String awayTeam;
    private int homeScore = 0;
    private int awayScore = 0;
    private int matchOrder;

    public Match(String homeTeam, String awayTeam, int matchOrder) {
        Util.validateTeamNames(homeTeam,awayTeam);
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchOrder = matchOrder;
    }

    public void updateScore(int homeScore, int awayScore) {
        if(homeScore < 0 || awayScore <0)
            throw new IllegalArgumentException("Scores cannot be negative.");
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public int getTotalScore() {
        return homeScore + awayScore;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    @Override
    public int compareTo(Match otherMatch) {
        int scoreDiff = Integer.compare(otherMatch.getTotalScore(), this.getTotalScore());
        if (scoreDiff != 0) {
            return scoreDiff;
        }

        return Integer.compare(otherMatch.matchOrder, this.matchOrder);
    }
}
