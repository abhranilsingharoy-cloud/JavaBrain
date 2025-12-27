package com.quizgame.models;

public class Question {
    private String questionText;
    private String[] options;
    private int correctAnswerIndex;
    private String category;
    private int difficultyLevel; // 1-3
    
    public Question(String questionText, String[] options, int correctAnswerIndex, 
                    String category, int difficultyLevel) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
        this.category = category;
        this.difficultyLevel = difficultyLevel;
    }
    
    // Getters
    public String getQuestionText() { 
        return questionText; 
    }
    
    public String[] getOptions() { 
        return options; 
    }
    
    public int getCorrectAnswerIndex() { 
        return correctAnswerIndex; 
    }
    
    public String getCategory() { 
        return category; 
    }
    
    public int getDifficultyLevel() { 
        return difficultyLevel; 
    }
    
    public boolean isCorrect(int answer) {
        return answer == correctAnswerIndex;
    }
    
    public String getCorrectAnswerText() {
        return options[correctAnswerIndex];
    }
}
