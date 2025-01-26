package org.ozanercan.Repository;

import org.ozanercan.Exceptions.MatchNotFoundException;
import org.ozanercan.Match;

import java.util.ArrayList;
import java.util.List;


public class MatchRepository implements IMatchRepository {
    private List<Match> matchesInProgress = new ArrayList<>();

    @Override
    public void addMatch(Match match) {
        matchesInProgress.add(match);
    }

    @Override
    public Match findMatch(String homeTeam, String awayTeam) {
        return matchesInProgress.stream()
                .filter(match -> match.getHomeTeam().equals(homeTeam) && match.getAwayTeam().equals(awayTeam))
                .findFirst()
                .orElseThrow(() -> new MatchNotFoundException("Match between " + homeTeam + " and " + awayTeam + " does not exist."));
    }

    @Override
    public List<Match> getAllMatches() {
        return matchesInProgress.stream().sorted().toList();
    }

    @Override
    public void removeMatch(Match match) {
        matchesInProgress.remove(match);
    }
}

