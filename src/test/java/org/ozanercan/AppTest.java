package org.ozanercan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ozanercan.Repository.IMatchRepository;
import org.ozanercan.Repository.MatchRepository;
import org.ozanercan.Service.IMatchService;
import org.ozanercan.Service.MatchService;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class AppTest {
    private Scoreboard scoreboard;
    private IMatchRepository matchRepository;
    private IMatchService matchService;
    private ByteArrayOutputStream errorOutput;

    @BeforeEach
    public void setUp() {
        matchRepository = new MatchRepository();
        matchService = new MatchService(matchRepository);
        scoreboard = new Scoreboard(matchService);

        errorOutput = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errorOutput));
    }

    @Test
    public void testScoreboardStartMatchDuplicateValue() {
        scoreboard.startMatch("Arsenal FC", "Ajax");
        scoreboard.startMatch("Arsenal FC", "Real Madrid");

        assertTrue(errorOutput.toString().contains("Error: One or both teams are already in progress"));
    }

    @Test
    public void testScoreboardStartMatchNullOrWhitespaceName() {
        scoreboard.startMatch(null, "Real Madrid");

        assertTrue(errorOutput.toString().contains("Error: Team name cannot be null or empty."));
    }

    @Test
    public void testScoreboardStartMatchSameTeams() {
        scoreboard.startMatch("Real Madrid", "Real Madrid");

        assertTrue(errorOutput.toString().contains("Error: Team names cannot be same."));
    }

    @Test
    public void testScoreboardNotStartedMatchUpdateScore() {
        scoreboard.updateScore("Arsenal FC", "Ajax", 1, 2);

        assertTrue(errorOutput.toString().contains("Error: Match between Arsenal FC and Ajax does not exist."));
    }

    @Test
    public void testScoreboardStartAndUpdateNonExistsMatch() {
        scoreboard.startMatch("Arsenal FC", "Ajax");
        scoreboard.updateScore("NE Team1", "NE Team2", 1, 2);

        assertTrue(errorOutput.toString().contains("Error: Match between NE Team1 and NE Team2 does not exist."));
    }

    @Test
    public void testScoreboardStartAndUpdateNegativeScore() {
        scoreboard.startMatch("Arsenal FC", "Ajax");
        scoreboard.updateScore("Arsenal FC", "Ajax", -1, -2);

        assertTrue(errorOutput.toString().contains("Error: Home score cannot be negative: -1"));
    }

    @Test
    public void testScoreboardStartAndUpdateNonExistentTeamNegativeScore() {
        scoreboard.startMatch("Arsenal FC", "Ajax");
        scoreboard.updateScore("NE Team", "Ajax", -1, -2);

        assertTrue(errorOutput.toString().contains("Error: Home score cannot be negative: -1"));
    }

    @Test
    public void testScoreboardFinishMatchWithIllegalNames() {
        scoreboard.startMatch("Arsenal FC", "Ajax");
        scoreboard.updateScore("Arsenal FC", "Ajax", 1, 2);

        scoreboard.finishMatch("", "Ajax");
        assertTrue(errorOutput.toString().contains("Error: Team name cannot be null or empty."));

        scoreboard.finishMatch(null, "Ajax");
        assertTrue(errorOutput.toString().contains("Error: Team name cannot be null or empty."));
    }

    @Test
    public void testScoreboardFinishMatchWithNoException() {
        scoreboard.startMatch("Arsenal FC", "Ajax");
        scoreboard.updateScore("Arsenal FC", "Ajax", 1, 2);
        scoreboard.finishMatch("Arsenal FC", "Ajax");
    }

    @Test
    public void testScoreboardFinishNonExistedMatch() {
        scoreboard.startMatch("Arsenal FC", "Ajax");
        scoreboard.updateScore("Arsenal FC", "Ajax", 1, 2);
        scoreboard.finishMatch("team1", "team2");

        assertTrue(errorOutput.toString().contains("Error: Match between team1 and team2 does not exist."));
    }

    @Test
    public void testScoreboardUpdateAlreadyFinishedMatch() {
        scoreboard.startMatch("Arsenal FC", "Ajax");
        scoreboard.updateScore("Arsenal FC", "Ajax", 1, 2);
        scoreboard.finishMatch("Arsenal FC", "Ajax");

        scoreboard.updateScore("Arsenal FC", "Ajax", 1, 2);
        assertTrue(errorOutput.toString().contains("Error: Match between Arsenal FC and Ajax does not exist."));
    }

    @Test
    public void testScoreboardSummaryBasicCase() {
        scoreboard.startMatch("Arsenal FC", "Ajax");
        scoreboard.updateScore("Arsenal FC", "Ajax", 1, 2);
        List<Match> summary = scoreboard.getSummary();
        assertEquals(1, summary.size());
        scoreboard.finishMatch("Arsenal FC", "Ajax");
        summary = scoreboard.getSummary();
        assertEquals(0, summary.size());
    }

    @Test
    public void testScoreboardSummaryBasicCase2() {
        scoreboard.startMatch("Arsenal FC", "Ajax");
        scoreboard.updateScore("Arsenal FC", "Ajax", 1, 2);
        scoreboard.startMatch("Real Madrid", "Liverpool FC");
        List<Match> summary = scoreboard.getSummary();
        assertEquals(2, summary.size());
        scoreboard.finishMatch("Arsenal FC", "Ajax");
        summary = scoreboard.getSummary();
        assertEquals(1, summary.size());
    }

    @Test
    public void testScoreboardFinishAlreadyFinishedMatch() {
        scoreboard.startMatch("Arsenal FC", "Ajax");
        scoreboard.updateScore("Arsenal FC", "Ajax", 1, 2);
        scoreboard.finishMatch("Arsenal FC", "Ajax");
        scoreboard.finishMatch("Arsenal FC", "Ajax");

        assertTrue(errorOutput.toString().contains("Error: Match between Arsenal FC and Ajax does not exist."));
    }

    @Test
    public void testEmptyScoreboardReturnsEmptySummary() {
        List<Match> summary = scoreboard.getSummary();
        assertEquals(0, summary.size());
    }

    @Test
    public void testScoreboardStartMatchBasic() {
        scoreboard.startMatch("Arsenal FC", "Ajax");
        List<Match> summary = scoreboard.getSummary();
        assertEquals(1, summary.size());
    }

    @Test
    public void testScoreboardStartMatch() {
        scoreboard.startMatch("Arsenal FC", "Ajax");
        List<Match> summary = scoreboard.getSummary();
        assertEquals("Arsenal FC", summary.get(0).getHomeTeam());
        assertEquals("Ajax", summary.get(0).getAwayTeam());
        assertEquals(0, summary.get(0).getHomeScore());
        assertEquals(0, summary.get(0).getAwayScore());
    }

    @Test
    public void testScoreboardStartAndUpdateMatch() {
        scoreboard.startMatch("Arsenal FC", "Ajax");
        scoreboard.updateScore("Arsenal FC", "Ajax", 1, 2);
        List<Match> summary = scoreboard.getSummary();
        assertEquals(1, summary.get(0).getHomeScore());
        assertEquals(2, summary.get(0).getAwayScore());
    }

    @Test
    public void testScoreboardStartAndUpdateMatch2() {
        scoreboard.startMatch("Real Madrid", "Liverpool FC");
        scoreboard.startMatch("Arsenal FC", "Ajax");

        scoreboard.updateScore("Real Madrid", "Liverpool FC", 1, 2);
        scoreboard.updateScore("Arsenal FC", "Ajax", 3, 2);
        List<Match> summary = scoreboard.getSummary();

        for (Match match : summary){
            if(match.getHomeTeam().equals("Arsenal FC")){
                assertEquals(3, match.getHomeScore());
                assertEquals(2, match.getAwayScore());
            }
        }
    }

    @Test
    public void testScoreboardSummaryWithMultipleMatchesBasic() {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.startMatch("Spain", "Brazil");

        scoreboard.updateScore("Mexico", "Canada", 0, 5);
        scoreboard.updateScore("Spain", "Brazil", 10, 2);

        List<Match> summary = scoreboard.getSummary();

        assertEquals("Spain", summary.get(0).getHomeTeam());
        assertEquals("Brazil", summary.get(0).getAwayTeam());
        assertEquals(10, summary.get(0).getHomeScore());
        assertEquals(2, summary.get(0).getAwayScore());

        assertEquals("Mexico", summary.get(1).getHomeTeam());
        assertEquals("Canada", summary.get(1).getAwayTeam());
        assertEquals(0, summary.get(1).getHomeScore());
        assertEquals(5, summary.get(1).getAwayScore());
    }

    @Test
    public void testScoreboardSummaryWithMultipleMatchesAdvanced() {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.startMatch("Spain", "Brazil");
        scoreboard.startMatch("Germany", "France");
        scoreboard.startMatch("Uruguay", "Italy");
        scoreboard.startMatch("Argentina", "Australia");

        scoreboard.updateScore("Mexico", "Canada", 0, 5);
        scoreboard.updateScore("Spain", "Brazil", 10, 2);
        scoreboard.updateScore("Germany", "France", 2, 2);
        scoreboard.updateScore("Uruguay", "Italy", 6, 6);
        scoreboard.updateScore("Argentina", "Australia", 3, 1);

        List<Match> summary = scoreboard.getSummary();

        assertEquals("Uruguay", summary.get(0).getHomeTeam());
        assertEquals("Italy", summary.get(0).getAwayTeam());
        assertEquals(6, summary.get(0).getHomeScore());
        assertEquals(6, summary.get(0).getAwayScore());

        assertEquals("Spain", summary.get(1).getHomeTeam());
        assertEquals("Brazil", summary.get(1).getAwayTeam());
        assertEquals(10, summary.get(1).getHomeScore());
        assertEquals(2, summary.get(1).getAwayScore());

        assertEquals("Mexico", summary.get(2).getHomeTeam());
        assertEquals("Canada", summary.get(2).getAwayTeam());
        assertEquals(0, summary.get(2).getHomeScore());
        assertEquals(5, summary.get(2).getAwayScore());

        assertEquals("Argentina", summary.get(3).getHomeTeam());
        assertEquals("Australia", summary.get(3).getAwayTeam());
        assertEquals(3, summary.get(3).getHomeScore());
        assertEquals(1, summary.get(3).getAwayScore());

        assertEquals("Germany", summary.get(4).getHomeTeam());
        assertEquals("France", summary.get(4).getAwayTeam());
        assertEquals(2, summary.get(4).getHomeScore());
        assertEquals(2, summary.get(4).getAwayScore());
    }

    @Test
    public void testStartingFinishingAndRestartingSameMatch() {
        scoreboard.startMatch("England", "Scotland");
        scoreboard.updateScore("England", "Scotland", 1, 0);
        scoreboard.finishMatch("England", "Scotland");

        List<Match> summaryAfterFinish = scoreboard.getSummary();
        assertTrue(summaryAfterFinish.stream().noneMatch(match ->
                match.getHomeTeam().equals("England") && match.getAwayTeam().equals("Scotland")
        ));

        scoreboard.startMatch("England", "Scotland");
        scoreboard.updateScore("England", "Scotland", 2, 2);

        List<Match> summaryAfterRestart = scoreboard.getSummary();
        assertTrue(summaryAfterRestart.stream().anyMatch(match ->
                match.getHomeTeam().equals("England") && match.getAwayTeam().equals("Scotland") &&
                        match.getHomeScore() == 2 && match.getAwayScore() == 2
        ));

        scoreboard.finishMatch("England", "Scotland");

        List<Match> summaryAfterSecondFinish = scoreboard.getSummary();
        assertTrue(summaryAfterSecondFinish.stream().noneMatch(match ->
                match.getHomeTeam().equals("England") && match.getAwayTeam().equals("Scotland")
        ));
    }

    @Test
    public void testFinishAllMatchesWithoutUpdateScore() {
        scoreboard.startMatch("England", "Scotland");
        scoreboard.finishMatch("England", "Scotland");

        List<Match> summary = scoreboard.getSummary();
        assertTrue(summary.isEmpty());
    }

    @Test
    public void testLargeNumberOfMatches() {
        for (int i = 1; i <= 10000; i++) {
            scoreboard.startMatch("Team" + i, "Opponent" + i);
            scoreboard.updateScore("Team" + i, "Opponent" + i, i, i + 1);
        }

        List<Match> summary = scoreboard.getSummary();
        assertEquals(10000, summary.size());
        assertEquals("Team10000", summary.get(0).getHomeTeam());
        assertEquals("Opponent10000", summary.get(0).getAwayTeam());

        for (int i = 1; i <= 10000; i++) {
            scoreboard.finishMatch("Team" + i, "Opponent" + i);
        }
        List<Match> summaryAfterFinish = scoreboard.getSummary();
        assertTrue(summaryAfterFinish.isEmpty());
    }

    @Test
    public void testRemoveMiddleMatch() {
        scoreboard.startMatch("Atletico Madrid", "Juventus");
        scoreboard.startMatch("Tottenham Hotspur", "Inter Milan");
        scoreboard.startMatch("Napoli", "RB Leipzig");

        scoreboard.updateScore("Atletico Madrid", "Juventus", 2, 1);
        scoreboard.updateScore("Tottenham Hotspur", "Inter Milan", 3, 4);
        scoreboard.updateScore("Napoli", "RB Leipzig", 1, 1);

        scoreboard.finishMatch("Tottenham Hotspur", "Inter Milan");

        List<Match> summary = scoreboard.getSummary();
        assertEquals(2, summary.size());
        assertEquals("Atletico Madrid", summary.get(0).getHomeTeam());
        assertEquals("Napoli", summary.get(1).getHomeTeam());
    }

    @Test
    public void testDynamicSortingInSummarySameScores() {
        scoreboard.startMatch("Borussia Dortmund", "Sevilla FC");
        scoreboard.startMatch("AS Roma", "Benfica");
        scoreboard.startMatch("Napoli", "RB Leipzig");
        scoreboard.startMatch("Lazio", "Porto");

        scoreboard.updateScore("Borussia Dortmund", "Sevilla FC", 2, 3);
        scoreboard.updateScore("AS Roma", "Benfica", 1, 1);
        scoreboard.updateScore("Napoli", "RB Leipzig",1,1);
        scoreboard.updateScore("Lazio", "Porto", 4, 2);

        List<Match> summary = scoreboard.getSummary();

        assertEquals("Lazio", summary.get(0).getHomeTeam());
        assertEquals("Borussia Dortmund", summary.get(1).getHomeTeam());
        assertEquals("Napoli", summary.get(2).getHomeTeam());
        assertEquals("AS Roma", summary.get(3).getHomeTeam());
    }

    @Test
    public void testUpdateScoreOfStartedMatchWithSwitchedTeamNames() {
        scoreboard.startMatch("Chelsea", "PSG");
        scoreboard.updateScore("PSG", "Chelsea",1,1);

        assertTrue(errorOutput.toString().contains("Error: Match between PSG and Chelsea does not exist."));
    }

    @Test
    public void testStartMatchesWithSameTeamInOneRole() {
        scoreboard.startMatch("Chelsea", "Barcelona");
        scoreboard.startMatch("Chelsea", "Real Madrid");
        assertTrue(errorOutput.toString().contains("Error: One or both teams are already in progress"));

        scoreboard.startMatch( "Real Madrid", "Chelsea");
        assertTrue(errorOutput.toString().contains("Error: One or both teams are already in progress"));

        scoreboard.startMatch("Barcelona", "Real Madrid");
        assertTrue(errorOutput.toString().contains("Error: One or both teams are already in progress"));

        scoreboard.startMatch( "Real Madrid", "Barcelona");
        assertTrue(errorOutput.toString().contains("Error: One or both teams are already in progress"));

    }

    @Test
    public void testUpdateScoreAndFinishMatchWithNullOrEmptyTeamNames() {
        scoreboard.startMatch("Chelsea", "PSG");

        scoreboard.updateScore(null, "PSG", 1, 1);
        assertTrue(errorOutput.toString().contains("Error: Team name cannot be null or empty."));

        scoreboard.updateScore("Chelsea", null, 1, 1);
        assertTrue(errorOutput.toString().contains("Error: Team name cannot be null or empty."));

        scoreboard.updateScore(null, null, 1, 1);
        assertTrue(errorOutput.toString().contains("Error: Team name cannot be null or empty."));

        scoreboard.updateScore("", "PSG", 1, 1);
        assertTrue(errorOutput.toString().contains("Error: Team name cannot be null or empty."));

        scoreboard.updateScore("Chelsea", "", 1, 1);
        assertTrue(errorOutput.toString().contains("Error: Team name cannot be null or empty."));

        scoreboard.updateScore("", "", 1, 1);
        assertTrue(errorOutput.toString().contains("Error: Team name cannot be null or empty."));

        scoreboard.finishMatch(null, "PSG");
        assertTrue(errorOutput.toString().contains("Error: Team name cannot be null or empty."));

        scoreboard.finishMatch("Chelsea", null);
        assertTrue(errorOutput.toString().contains("Error: Team name cannot be null or empty."));

        scoreboard.finishMatch(null, null);
        assertTrue(errorOutput.toString().contains("Error: Team name cannot be null or empty."));

        scoreboard.finishMatch("", "PSG");
        assertTrue(errorOutput.toString().contains("Error: Team name cannot be null or empty."));

        scoreboard.finishMatch("Chelsea", "");
        assertTrue(errorOutput.toString().contains("Error: Team name cannot be null or empty."));

        scoreboard.finishMatch("", "");
        assertTrue(errorOutput.toString().contains("Error: Team name cannot be null or empty."));

    }




}
