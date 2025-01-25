package org.ozanercan.Service;

import org.ozanercan.Match;

import java.util.List;

public interface IMatchService {
    void startMatch(String homeTeam, String awayTeam);
    void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore);
    void finishMatch(String homeTeam, String awayTeam);
    List<Match> getSummary();
}