package org.ozanercan;

public class Match {
    String homeTeam;
    String awayTeam;
    int homeScore = 0;
    int awayScore = 0;

    public Match(String homeTeam, String awayTeam) {
        if(homeTeam.isEmpty() || awayTeam.isEmpty())
            throw new IllegalArgumentException("Team name cannot be empty.");
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
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
}
