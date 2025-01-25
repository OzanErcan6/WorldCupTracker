package org.ozanercan.Repository;

import org.ozanercan.Match;

import java.util.List;

public interface IMatchRepository {
    void addMatch(Match match);
    Match findMatch(String homeTeam, String awayTeam);
    List<Match> getAllMatches();
    void removeMatch(Match match);
}
