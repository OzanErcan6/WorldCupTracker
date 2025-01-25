package org.ozanercan;

import org.ozanercan.Exceptions.DuplicateMatchFoundException;
import org.ozanercan.Exceptions.MatchNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class Scoreboard {
    List<Match> matchesInProgress = new ArrayList<>();
    private static int matchCounter = 0;

    public List<Match> getSummary() {
        return matchesInProgress.stream().sorted().toList();
    }

    public void startMatch(String homeTeam, String awayTeam) {
        Util.validateTeamNames(homeTeam, awayTeam);

        this.checkIfAtLeastOneTeamsIsInProgress(homeTeam, awayTeam);
        matchesInProgress.add(new Match(homeTeam,awayTeam,matchCounter++));
    }

    private void checkIfAtLeastOneTeamsIsInProgress(String homeTeam, String awayTeam) {
        for(Match match : matchesInProgress){
            if(match.getHomeTeam().equals(homeTeam) || match.getHomeTeam().equals(awayTeam)
                    || match.getAwayTeam().equals(homeTeam) || match.getAwayTeam().equals(awayTeam)){
                throw new DuplicateMatchFoundException("One or both teams are already in progress");
            }
        }
    }

    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        Util.checkIfScoresAreValidOtherwiseThrowException(homeScore,awayScore);
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

    public void finishMatch(String homeTeam, String awayTeam) {
        Util.validateTeamNames(homeTeam, awayTeam);
        Match match = this.findMatchWithTeamNames(homeTeam, awayTeam); // check if

        matchesInProgress.remove(match);
    }

}
