package org.ozanercan;

import org.ozanercan.Exceptions.DuplicateMatchFoundException;
import org.ozanercan.Repository.IMatchRepository;

public class MatchValidator {
    private static final int MAX_TEAM_NAME_LENGTH = 50;
    private static final String TEAM_NAME_REGEX = "^[a-zA-Z0-9\\s'-]+$";

    public static void checkForDuplicateTeamsInProgress(String homeTeam, String awayTeam, IMatchRepository matchRepository) {
        for (Match match : matchRepository.getAllMatches()) {
            if (match.getHomeTeam().equals(homeTeam) || match.getHomeTeam().equals(awayTeam)
                    || match.getAwayTeam().equals(homeTeam) || match.getAwayTeam().equals(awayTeam)) {
                throw new DuplicateMatchFoundException("One or both teams are already in progress");
            }
        }
    }

    public static void checkForValidTeamNames(String homeTeam, String awayTeam) {
        checkForValidTeamName(homeTeam);
        checkForValidTeamName(awayTeam);
        if(homeTeam.equals(awayTeam))
            throw new IllegalArgumentException("Team names cannot be same.");
    }

    public static void checkForValidTeamName(String teamName){
        if (teamName == null || teamName.trim().isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be null or empty.");
        }

        String trimmedName = teamName.trim();

        if (trimmedName.length() > MAX_TEAM_NAME_LENGTH) {
            throw new IllegalArgumentException("Team name cannot exceed " + MAX_TEAM_NAME_LENGTH + " characters.");
        }

        if (!trimmedName.matches(TEAM_NAME_REGEX)) {
            throw new IllegalArgumentException("Team name contains invalid characters.");
        }
    }

    public static void checkIfScoresAreValidOtherwiseThrowException(int homeScore, int awayScore) {
        checkIfScoreIsValid(homeScore, "Home");
        checkIfScoreIsValid(awayScore, "Away");
    }

    public static void checkIfScoreIsValid(int score, String team) {
        if (score < 0) {
            throw new IllegalArgumentException(team + " score cannot be negative: " + score);
        }
    }

}
