package org.ozanercan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ozanercan.Exceptions.DuplicateMatchFoundException;
import org.ozanercan.Exceptions.MatchNotFoundException;
import org.ozanercan.Repository.IMatchRepository;
import org.ozanercan.Repository.MatchRepositoryImpl;
import org.ozanercan.Service.IMatchService;
import org.ozanercan.Service.MatchService;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class AppTest {
    private Scoreboard scoreboard;
    private IMatchRepository matchRepository;
    private IMatchService matchService;

    @BeforeEach
    public void setUp() {
        matchRepository = new MatchRepositoryImpl();
        matchService = new MatchService(matchRepository);
        scoreboard = new Scoreboard(matchService);
    }

    @Test
    public void testScoreboardStartMatchDuplicateValue() {
        scoreboard.startMatch("Arsenal FC", "Ajax");
        assertThrows(DuplicateMatchFoundException.class,
                () -> scoreboard.startMatch("Arsenal FC", "Real Madrid"));
    }

    @Test
    public void testScoreboardStartMatchNullOrWhitespaceName() {
        assertThrows(IllegalArgumentException.class,
                () -> scoreboard.startMatch(null, "Real Madrid"));
    }

    @Test
    public void testScoreboardStartMatchSameTeams() {
        assertThrows(IllegalArgumentException.class,
                () -> scoreboard.startMatch("Real Madrid", "Real Madrid"));
    }

    @Test
    public void testScoreboardNotStartedMatchUpdateScore() {
        assertThrows(MatchNotFoundException.class,
                () -> scoreboard.updateScore("Arsenal FC", "Ajax", 1, 2));
    }

    @Test
    public void testScoreboardStartAndUpdateNonExistsMatch() {
        scoreboard.startMatch("Arsenal FC", "Ajax");
        assertThrows(MatchNotFoundException.class,
                () -> scoreboard.updateScore("NE Team1", "NE Team2", 1, 2));
    }

    @Test
    public void testScoreboardStartAndUpdateNegativeScore() {
        scoreboard.startMatch("Arsenal FC", "Ajax");
        assertThrows(IllegalArgumentException.class,
                () -> scoreboard.updateScore("Arsenal FC", "Ajax", -1, -2));
    }

    @Test
    public void testScoreboardStartAndUpdateNonExistentTeamNegativeScore() {
        scoreboard.startMatch("Arsenal FC", "Ajax");
        assertThrows(IllegalArgumentException.class,
                () -> scoreboard.updateScore("NE Team", "Ajax", -1, -2));
    }


    @Test
    public void testMatchTotalScore(){
        Match match = new Match("Real Madrid", "Liverpool FC", 0);
        match.updateScore(3,3);
        assertEquals(6, match.getTotalScore());
    }

    @Test
    public void testMatchTotalScore2() {
        Match match = new Match("Real Madrid", "Liverpool FC",0);
        match.updateScore(3, 4);
        assertEquals(7, match.getTotalScore());
    }

    @Test
    public void testMatchTotalScoreCannotBeNegative() {
        Match match = new Match("Real Madrid", "Liverpool FC",0);
        assertThrows(IllegalArgumentException.class, () -> match.updateScore(-1, -1));
    }

    @Test
    public void testMatchTeamNameCannotBeEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Match("", "Liverpool FC",0));
    }

    @Test
    public void testScoreboardFinishMatchWithIllegalNames() {
        scoreboard.startMatch("Arsenal FC", "Ajax");
        scoreboard.updateScore("Arsenal FC", "Ajax", 1, 2);

        assertThrows(IllegalArgumentException.class,
                () -> scoreboard.finishMatch("", "Ajax"));
        assertThrows(IllegalArgumentException.class,
                () -> scoreboard.finishMatch(null, "Ajax"));
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

        assertThrows(MatchNotFoundException.class,
                () -> scoreboard.finishMatch("team1", "team2"));
    }

    @Test
    public void testScoreboardUpdateAlreadyFinishedMatch() {
        scoreboard.startMatch("Arsenal FC", "Ajax");
        scoreboard.updateScore("Arsenal FC", "Ajax", 1, 2);
        scoreboard.finishMatch("Arsenal FC", "Ajax");

        assertThrows(MatchNotFoundException.class,
                () -> scoreboard.updateScore("Arsenal FC", "Ajax", 1, 2));
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

        assertThrows(MatchNotFoundException.class,
                () -> scoreboard.finishMatch("Arsenal FC", "Ajax"));
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
    public void testScoreboard() {
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

}
