package org.ozanercan.Service;

import org.ozanercan.Exceptions.DuplicateMatchFoundException;
import org.ozanercan.Exceptions.MatchNotFoundException;
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
        try {
            MatchValidator.checkForValidTeamNames(homeTeam, awayTeam);
            MatchValidator.checkForDuplicateTeamsInProgress(homeTeam, awayTeam, matchRepository);

            matchRepository.addMatch(new Match(homeTeam, awayTeam, matchCounter++));
        } catch (DuplicateMatchFoundException | IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }


    @Override
    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        try {
            MatchValidator.checkIfScoresAreValidOtherwiseThrowException(homeScore, awayScore);
            MatchValidator.checkForValidTeamNames(homeTeam, awayTeam);
            Match match = matchRepository.findMatch(homeTeam, awayTeam);
            match.setHomeScore(homeScore);
            match.setAwayScore(awayScore);
        } catch (MatchNotFoundException | IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    public void finishMatch(String homeTeam, String awayTeam) {
        try {
            MatchValidator.checkForValidTeamNames(homeTeam, awayTeam);
            Match match = matchRepository.findMatch(homeTeam, awayTeam);
            matchRepository.removeMatch(match);
        } catch (MatchNotFoundException | IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    public List<Match> getSummary() {
        return matchRepository.getAllMatches().stream().sorted().toList();
    }
}
