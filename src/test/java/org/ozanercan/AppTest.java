package org.ozanercan;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.List;


public class AppTest
    extends TestCase
{

    public AppTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    public void testEmptyScoreboardReturnsEmptySummary() {
        Scoreboard scoreboard = new Scoreboard();
        List<Match> summary = scoreboard.getSummary();
        assertEquals(0, summary.size());
    }

    public void testMatchTotalScore(){
        Match match = new Match("Real Madrid", "Liverpool FC");
        match.updateScore(3,3);
        assertEquals(6, match.getTotalScore());
    }
}
