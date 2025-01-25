package org.ozanercan;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class AppTest {

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
}
