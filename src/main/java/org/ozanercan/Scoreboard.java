package org.ozanercan;

import org.ozanercan.Service.IMatchService;

import java.util.List;

public class Scoreboard {
    private IMatchService matchService;

    public Scoreboard(IMatchService matchService) {
        this.matchService = matchService;
    }

    public void startMatch(String homeTeam, String awayTeam) {
        matchService.startMatch(homeTeam, awayTeam);
    }

    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        matchService.updateScore(homeTeam, awayTeam, homeScore, awayScore);
    }

    public void finishMatch(String homeTeam, String awayTeam) {
        matchService.finishMatch(homeTeam, awayTeam);
    }

    public List<Match> getSummary() {
        return matchService.getSummary().stream().sorted().toList();
    }
}
