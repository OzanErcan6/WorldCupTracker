package org.ozanercan.Service;

import org.ozanercan.Match;
import org.ozanercan.Repository.IMatchRepository;
import org.ozanercan.MatchValidator;

import java.util.List;

public class MatchService implements IMatchService{
    private static int matchCounter = 0;
    private final IMatchRepository matchRepository;

    public MatchService(IMatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public void startMatch(String homeTeam, String awayTeam) {
        MatchValidator.checkForValidTeamNames(homeTeam, awayTeam);
        MatchValidator.checkForDuplicateTeamsInProgress(homeTeam, awayTeam, matchRepository);

        matchRepository.addMatch(new Match(homeTeam, awayTeam, matchCounter++));
    }

    @Override
    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        MatchValidator.checkIfScoresAreValidOtherwiseThrowException(homeScore, awayScore);
        MatchValidator.checkForValidTeamNames(homeTeam,awayTeam);
        Match match = matchRepository.findMatch(homeTeam, awayTeam);
        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
    }

    @Override
    public void finishMatch(String homeTeam, String awayTeam) {
        MatchValidator.checkForValidTeamNames(homeTeam,awayTeam);
        Match match = matchRepository.findMatch(homeTeam, awayTeam);
        matchRepository.removeMatch(match);
    }

    @Override
    public List<Match> getSummary() {
        return matchRepository.getAllMatches();
    }
}
