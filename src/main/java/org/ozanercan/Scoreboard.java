package org.ozanercan;

import org.ozanercan.Exceptions.MatchNotFoundException;

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
        Match match = this.findMatchWithTeamNames(homeTeam, awayTeam);

        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
    }

    private Match findMatchWithTeamNames(String homeTeam, String awayTeam) {
        for(Match match : matchesInProgress)
            if(match.getHomeTeam().equals(homeTeam) && match.getAwayTeam().equals(awayTeam))
                return match;

        throw new MatchNotFoundException("Match between " + homeTeam + " and " + awayTeam + " does not exist.");
    }
}
