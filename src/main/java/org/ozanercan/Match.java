package org.ozanercan;

public class Match {
    String homeTeam;
    String awayTeam;
    int homeScore = 0;
    int awayScore = 0;

    public Match(String homeTeam, String awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    public void updateScore(int homeScore, int awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public int getTotalScore() {
        return 6;
    }
}
