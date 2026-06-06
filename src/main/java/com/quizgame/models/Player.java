package com.quizgame.models;

public class Player {
    private String name;
    private int score;
    private int correctAnswers;
    private int totalQuestionsAttempted;
    private double totalTime;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.correctAnswers = 0;
        this.totalQuestionsAttempted = 0;
        this.totalTime = 0.0;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void incrementCorrectAnswers() {
        this.correctAnswers++;
    }

    public int getTotalQuestionsAttempted() {
        return totalQuestionsAttempted;
    }

    public void incrementTotalQuestions() {
        this.totalQuestionsAttempted++;
    }

    public void updateAverageTime(double seconds) {
        this.totalTime += seconds;
    }

    public double getAverageTimePerQuestion() {
        if (totalQuestionsAttempted == 0) {
            return 0.0;
        }
        return totalTime / totalQuestionsAttempted;
    }

    public double getAccuracy() {
        if (totalQuestionsAttempted == 0) {
            return 0.0;
        }
        return ((double) correctAnswers / totalQuestionsAttempted) * 100.0;
    }
}
