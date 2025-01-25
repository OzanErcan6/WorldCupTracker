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
}
