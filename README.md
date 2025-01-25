# Scoreboard Library

A Java library to manage live sports match scores, validate inputs, and provide a sorted summary of ongoing matches.

## Features
- Start, update, and finish matches.
- Validate team names and ensure no duplicate or invalid matches.
- Maintain a summary of matches, sorted by total score.

## Usage
### Setup
Ensure JAVA_HOME is set to your JDK 21
```java
mvn clean package
        
java -jar target/WorldCupTracker-1.0-SNAPSHOT.jar
        
mvn test
```

### Basic Operations
- **Start Match**:  
  `scoreboard.startMatch("Team A", "Team B");`
- **Update Score**:  
  `scoreboard.updateScore("Team A", "Team B", 1, 2);`
- **Finish Match**:  
  `scoreboard.finishMatch("Team A", "Team B");`
- **Get Summary**:  
  `List<Match> summary = scoreboard.getSummary();`

## Validation
- Team names must be unique, non-empty, and under 50 characters.
- Scores must be non-negative.

## Tests
Includes JUnit tests for all functionalities, ensuring reliability and edge case handling.
