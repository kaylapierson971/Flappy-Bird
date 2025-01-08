public class ScoreManager implements java.io.Serializable {
    
    private int highScore, currentScore;
    
    /**
     * Creates a new ScoreManager object with a current score of zero and a save high score
     */
    public ScoreManager() {
        currentScore = 0;
        readScore();
    }
    
    /**
     * Updates the current score
     *
     * @param increaseScore An integer that represents the amount to increase the score
     */
    public void updateScore(int increaseScore) {
        currentScore += increaseScore;
    }
    
    /**
     * Resets the current score back to zero for a new game
     */
    public void resetScore() {
        currentScore = 0;
    }
    
    /**
     * Saves the high score of the game to a serialized file
     */
    public void saveScore() {
        highScore = currentScore;
    }
    
    /**
     * Reads in the high score of the game from a serialized file
     */
    public void readScore() {
    
    }
    

    public int getCurrentScore() {
        return currentScore;
    }
    

    public int getHighScore() {
        return highScore;
    }
    
}
