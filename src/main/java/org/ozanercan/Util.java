package org.ozanercan;

public class Util {
    private static final int MAX_TEAM_NAME_LENGTH = 50;
    private static final String TEAM_NAME_REGEX = "^[a-zA-Z0-9\\s'-]+$";

    public static void validateTeamNames(String homeTeam, String awayTeam) {
        validateTeamName(homeTeam);
        validateTeamName(awayTeam);
        if(homeTeam.equals(awayTeam))
            throw new IllegalArgumentException("Team names cannot be same.");
    }

    public static void validateTeamName(String teamName){
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
        if (homeScore < 0) {
            throw new IllegalArgumentException("Home score cannot be negative: " + homeScore);
        }

        if (awayScore < 0) {
            throw new IllegalArgumentException("Away score cannot be negative: " + awayScore);
        }
    }

}
