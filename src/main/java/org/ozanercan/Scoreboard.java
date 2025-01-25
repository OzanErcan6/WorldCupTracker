package org.ozanercan;

import java.util.ArrayList;
import java.util.List;

public class Scoreboard {
    List<Match> matchesInProgress = new ArrayList<>();
    public List<Match> getSummary() {
        return matchesInProgress;
    }

    public void startMatch(String homeTeam, String awayTeam) {
        matchesInProgress.add(new Match(homeTeam,awayTeam));
    }

    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        matchesInProgress.get(0).setHomeScore(homeScore);
        matchesInProgress.get(0).setAwayScore(awayScore);
    }
}
