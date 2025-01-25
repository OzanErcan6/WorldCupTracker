package org.ozanercan;

import org.junit.jupiter.api.Test;
import org.ozanercan.Exceptions.DuplicateMatchFoundException;
import org.ozanercan.Exceptions.MatchNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class AppTest {

    @Test
    public void testScoreboardStartMatchDuplicateValue() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.startMatch("Arsenal FC", "Ajax");
        assertThrows(DuplicateMatchFoundException.class,
                () -> scoreboard.startMatch("Arsenal FC", "Real Madrid"));
    }

    @Test
    public void testScoreboardStartMatchNullOrWhitespaceName() {
        Scoreboard scoreboard = new Scoreboard();
        assertThrows(IllegalArgumentException.class,
                () -> scoreboard.startMatch(null, "Real Madrid"));
    }

    @Test
    public void testScoreboardStartMatchSameTeams() {
        Scoreboard scoreboard = new Scoreboard();
        assertThrows(IllegalArgumentException.class,
                () -> scoreboard.startMatch("Real Madrid", "Real Madrid"));
    }

    @Test
    public void testScoreboardNotStartedMatchUpdateScore() {
        Scoreboard scoreboard = new Scoreboard();
        assertThrows(MatchNotFoundException.class,
                () -> scoreboard.updateScore("Arsenal FC", "Ajax", 1, 2));
    }

    @Test
    public void testScoreboardStartAndUpdateNonExistsMatch() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.startMatch("Arsenal FC", "Ajax");
        assertThrows(MatchNotFoundException.class,
                () -> scoreboard.updateScore("NE Team1", "NE Team2", 1, 2));
    }

    @Test
    public void testScoreboardStartAndUpdateNegativeScore() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.startMatch("Arsenal FC", "Ajax");
        assertThrows(IllegalArgumentException.class,
                () -> scoreboard.updateScore("Arsenal FC", "Ajax", -1, -2));
    }

    @Test
    public void testScoreboardStartAndUpdateNonExistentTeamNegativeScore() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.startMatch("Arsenal FC", "Ajax");
        assertThrows(IllegalArgumentException.class,
                () -> scoreboard.updateScore("NE Team", "Ajax", -1, -2));
    }


    @Test
    public void testMatchTotalScore(){
        Match match = new Match("Real Madrid", "Liverpool FC");
        match.updateScore(3,3);
        assertEquals(6, match.getTotalScore());
    }

    @Test
    public void testMatchTotalScore2() {
        Match match = new Match("Real Madrid", "Liverpool FC");
        match.updateScore(3, 4);
        assertEquals(7, match.getTotalScore());
    }

    @Test
    public void testMatchTotalScoreCannotBeNegative() {
        Match match = new Match("Real Madrid", "Liverpool FC");
        assertThrows(IllegalArgumentException.class, () -> match.updateScore(-1, -1));
    }

    @Test
    public void testMatchTeamNameCannotBeEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Match("", "Liverpool FC"));
    }

    @Test
    public void testScoreboardFinishMatchWithIllegalNames() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.startMatch("Arsenal FC", "Ajax");
        scoreboard.updateScore("Arsenal FC", "Ajax", 1, 2);

        assertThrows(IllegalArgumentException.class,
                () -> scoreboard.finishMatch("", "Ajax"));
        assertThrows(IllegalArgumentException.class,
                () -> scoreboard.finishMatch(null, "Ajax"));
    }

    @Test
    public void testScoreboardFinishMatchWithNoException() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.startMatch("Arsenal FC", "Ajax");
        scoreboard.updateScore("Arsenal FC", "Ajax", 1, 2);
        scoreboard.finishMatch("Arsenal FC", "Ajax");
    }

    @Test
    public void testScoreboardFinishNonExistedMatch() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.startMatch("Arsenal FC", "Ajax");
        scoreboard.updateScore("Arsenal FC", "Ajax", 1, 2);

        assertThrows(MatchNotFoundException.class,
                () -> scoreboard.finishMatch("team1", "team2"));
    }

    @Test
    public void testScoreboardUpdateAlreadyFinishedMatch() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.startMatch("Arsenal FC", "Ajax");
        scoreboard.updateScore("Arsenal FC", "Ajax", 1, 2);
        scoreboard.finishMatch("Arsenal FC", "Ajax");

        assertThrows(MatchNotFoundException.class,
                () -> scoreboard.updateScore("Arsenal FC", "Ajax", 1, 2));
    }

    @Test
    public void testScoreboardSummaryBasicCase() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.startMatch("Arsenal FC", "Ajax");
        scoreboard.updateScore("Arsenal FC", "Ajax", 1, 2);
        List<Match> summary = scoreboard.getSummary();
        assertEquals(1, summary.size());
        scoreboard.finishMatch("Arsenal FC", "Ajax");
        assertEquals(0, summary.size());
    }

    @Test
    public void testScoreboardSummaryBasicCase2() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.startMatch("Arsenal FC", "Ajax");
        scoreboard.updateScore("Arsenal FC", "Ajax", 1, 2);
        scoreboard.startMatch("Real Madrid", "Liverpool FC");
        List<Match> summary = scoreboard.getSummary();
        assertEquals(2, summary.size());
        scoreboard.finishMatch("Arsenal FC", "Ajax");
        assertEquals(1, summary.size());
    }

    @Test
    public void testScoreboardFinishAlreadyFinishedMatch() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.startMatch("Arsenal FC", "Ajax");
        scoreboard.updateScore("Arsenal FC", "Ajax", 1, 2);
        scoreboard.finishMatch("Arsenal FC", "Ajax");

        assertThrows(MatchNotFoundException.class,
                () -> scoreboard.finishMatch("Arsenal FC", "Ajax"));
    }

    @Test
    public void testEmptyScoreboardReturnsEmptySummary() {
        Scoreboard scoreboard = new Scoreboard();
        List<Match> summary = scoreboard.getSummary();
        assertEquals(0, summary.size());
    }

    @Test
    public void testScoreboardStartMatchBasic() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.startMatch("Arsenal FC", "Ajax");
        List<Match> summary = scoreboard.getSummary();
        assertEquals(1, summary.size());
    }

    @Test
    public void testScoreboardStartMatch() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.startMatch("Arsenal FC", "Ajax");
        List<Match> summary = scoreboard.getSummary();
        assertEquals("Arsenal FC", summary.get(0).getHomeTeam());
        assertEquals("Ajax", summary.get(0).getAwayTeam());
        assertEquals(0, summary.get(0).getHomeScore());
        assertEquals(0, summary.get(0).getAwayScore());
    }

    @Test
    public void testScoreboardStartAndUpdateMatch() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.startMatch("Arsenal FC", "Ajax");
        scoreboard.updateScore("Arsenal FC", "Ajax", 1, 2);
        List<Match> summary = scoreboard.getSummary();
        assertEquals(1, summary.get(0).getHomeScore());
        assertEquals(2, summary.get(0).getAwayScore());
    }

    @Test
    public void testScoreboardStartAndUpdateMatch2() {
        Scoreboard scoreboard = new Scoreboard();

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

}
