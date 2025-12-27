package com.quizgame.core;

import com.quizgame.models.Question;
import com.quizgame.models.Player;
import com.quizgame.features.Leaderboard;
import java.util.*;

public class QuizManager {
    private List<Question> questions;
    private Player currentPlayer;
    private Scanner scanner;
    private Random random;
    private int currentQuestionIndex;
    private long questionStartTime;
    
    public QuizManager() {
        this.scanner = new Scanner(System.in);
        this.random = new Random();
        this.currentQuestionIndex = 0;
        this.questions = new ArrayList<>();
        initializeQuestions();
    }
    
    private void initializeQuestions() {
        // Science Questions
        questions.add(new Question(
            "What is the chemical symbol for Gold?",
            new String[]{"Au", "Ag", "Fe", "Pb"},
            0,
            "Science",
            1
        ));
        
        questions.add(new Question(
            "Which planet is known as the Red Planet?",
            new String[]{"Mars", "Venus", "Jupiter", "Saturn"},
            0,
            "Science",
            1
        ));
        
        questions.add(new Question(
            "What is the hardest natural substance on Earth?",
            new String[]{"Diamond", "Graphite", "Quartz", "Topaz"},
            0,
            "Science",
            2
        ));
        
        // History Questions
        questions.add(new Question(
            "In which year did World War II end?",
            new String[]{"1945", "1939", "1941", "1948"},
            0,
            "History",
            2
        ));
        
        questions.add(new Question(
            "Who was the first President of the United States?",
            new String[]{"George Washington", "Thomas Jefferson", "Abraham Lincoln", "John Adams"},
            0,
            "History",
            1
        ));
        
        // Mathematics Questions
        questions.add(new Question(
            "What is the value of π (pi) rounded to two decimal places?",
            new String[]{"3.14", "3.16", "3.12", "3.18"},
            0,
            "Mathematics",
            1
        ));
        
        questions.add(new Question(
            "Solve for x: 2x + 5 = 15",
            new String[]{"5", "10", "7.5", "2.5"},
            0,
            "Mathematics",
            2
        ));
        
        // Geography Questions
        questions.add(new Question(
            "What is the capital of Japan?",
            new String[]{"Tokyo", "Kyoto", "Osaka", "Seoul"},
            0,
            "Geography",
            1
        ));
        
        questions.add(new Question(
            "Which is the largest ocean on Earth?",
            new String[]{"Pacific Ocean", "Atlantic Ocean", "Indian Ocean", "Arctic Ocean"},
            0,
            "Geography",
            1
        ));
        
        // Sports Questions
        questions.add(new Question(
            "How many players are on a soccer team?",
            new String[]{"11", "10", "12", "9"},
            0,
            "Sports",
            1
        ));
        
        // More challenging questions
        questions.add(new Question(
            "Which element has the highest melting point?",
            new String[]{"Tungsten", "Iron", "Gold", "Copper"},
            0,
            "Science",
            3
        ));
        
        questions.add(new Question(
            "Who wrote 'Romeo and Juliet'?",
            new String[]{"William Shakespeare", "Charles Dickens", "Jane Austen", "Mark Twain"},
            0,
            "History",
            2
        ));
        
        // Shuffle the questions
        Collections.shuffle(questions);
    }
    
    public void startQuiz() {
        displayGameHeader();
        
        // Player Registration
        System.out.print("👤 Enter your name: ");
        String playerName = scanner.nextLine().trim();
        currentPlayer = new Player(playerName);
        
        System.out.println("\n🎮 Hello, " + currentPlayer.getName() + "! Let's begin the quiz.");
        System.out.println("📝 You'll be shown " + questions.size() + " random questions.");
        System.out.println("⚡ Quick answers earn bonus points!");
        System.out.println("\n" + "─".repeat(50));
        
        // Start quiz loop
        for (currentQuestionIndex = 0; currentQuestionIndex < questions.size(); currentQuestionIndex++) {
            if (!askQuestion()) {
                break; // User chose to quit
            }
        }
        
        // Save score and display results
        Leaderboard.saveScore(currentPlayer);
        displayFinalResults();
    }
    
    private void displayGameHeader() {
        System.out.println("\n" + "⭐".repeat(55));
        System.out.println("                 QUIZ GAME STARTING!");
        System.out.println("⭐".repeat(55));
    }
    
    private boolean askQuestion() {
        Question currentQuestion = questions.get(currentQuestionIndex);
        
        // Display question
        System.out.println("\n" + (currentQuestionIndex + 1) + ". [" + currentQuestion.getCategory() + 
                          "] " + getDifficultyStars(currentQuestion.getDifficultyLevel()));
        System.out.println("❓ " + currentQuestion.getQuestionText());
        System.out.println("─".repeat(60));
        
        // Display options
        String[] options = currentQuestion.getOptions();
        for (int i = 0; i < options.length; i++) {
            System.out.println("  " + (i + 1) + ") " + options[i]);
        }
        
        System.out.println("─".repeat(40));
        System.out.println("0️⃣  - Quit Quiz");
        System.out.print("➡️  Your answer (1-4): ");
        
        // Start timer
        questionStartTime = System.currentTimeMillis();
        
        // Get user input
        int userAnswer;
        try {
            userAnswer = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            if (userAnswer == 0) {
                System.out.println("\n⚠ Are you sure you want to quit? (yes/no)");
                String confirm = scanner.nextLine();
                if (confirm.equalsIgnoreCase("yes")) {
                    return false;
                } else {
                    return askQuestion(); // Ask the same question again
                }
            }
            
            if (userAnswer < 1 || userAnswer > options.length) {
                System.out.println("⚠ Invalid choice! Please select 1-4.");
                return askQuestion();
            }
            
        } catch (InputMismatchException e) {
            System.out.println("⚠ Please enter a number!");
            scanner.nextLine(); // Clear invalid input
            return askQuestion();
        }
        
        // Calculate time taken
        long timeTaken = System.currentTimeMillis() - questionStartTime;
        double seconds = timeTaken / 1000.0;
        
        // Check answer
        boolean isCorrect = currentQuestion.isCorrect(userAnswer - 1);
        
        if (isCorrect) {
            int points = currentQuestion.getDifficultyLevel() * 10;
            currentPlayer.addScore(points);
            currentPlayer.incrementCorrectAnswers();
            
            System.out.println("\n✅ CORRECT! +" + points + " points");
            System.out.println("⏱ Time: " + String.format("%.1f", seconds) + " seconds");
            
            // Speed bonus
            if (seconds < 5) {
                int speedBonus = 5;
                currentPlayer.addScore(speedBonus);
                System.out.println("⚡ SPEED BONUS! +" + speedBonus + " extra points!");
            }
            
        } else {
            System.out.println("\n❌ INCORRECT!");
            System.out.println("📘 The correct answer was: " + currentQuestion.getCorrectAnswerText());
        }
        
        currentPlayer.incrementTotalQuestions();
        currentPlayer.updateAverageTime(seconds);
        
        // Show progress
        showProgress();
        
        // Show motivational message
        showMotivationalMessage(isCorrect);
        
        // Pause briefly
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            // Continue if interrupted
        }
        
        return true;
    }
    
    private String getDifficultyStars(int level) {
        switch(level) {
            case 1: return "★ (Easy)";
            case 2: return "★★ (Medium)";
            case 3: return "★★★ (Hard)";
            default: return "";
        }
    }
    
    private void showProgress() {
        int total = questions.size();
        int current = currentQuestionIndex + 1;
        int percentage = (current * 100) / total;
        
        // Progress bar
        int barWidth = 30;
        int progress = (current * barWidth) / total;
        String progressBar = "[" + "=".repeat(progress) + 
                           " ".repeat(barWidth - progress) + "]";
        
        System.out.println("\n📊 PROGRESS: " + progressBar + " " + percentage + "%");
        System.out.println("🏆 Score: " + currentPlayer.getScore() + " points");
        System.out.println("🎯 Accuracy: " + String.format("%.1f", currentPlayer.getAccuracy()) + "%");
    }
    
    private void showMotivationalMessage(boolean wasCorrect) {
        String[] correctMessages = {
            "🎯 Bullseye! You're amazing!",
            "🚀 Excellent! Keep soaring!",
            "💡 Brilliant thinking!",
            "🌟 Star performer!",
            "🔥 You're on fire!"
        };
        
        String[] incorrectMessages = {
            "💪 Don't worry, next question!",
            "📚 Every mistake is a learning opportunity!",
            "🔍 Read carefully, you'll get the next one!",
            "🎯 Focus, you can do this!",
            "🚀 Keep going, you're doing great!"
        };
        
        System.out.println("\n" + (wasCorrect ? 
            correctMessages[random.nextInt(correctMessages.length)] :
            incorrectMessages[random.nextInt(incorrectMessages.length)]));
        System.out.println("─".repeat(50));
    }
    
    private void displayFinalResults() {
        System.out.println("\n" + "🎉".repeat(25));
        System.out.println("            QUIZ COMPLETE!");
        System.out.println("🎉".repeat(25));
        
        System.out.println("\n📋 FINAL RESULTS");
        System.out.println("═".repeat(40));
        System.out.println("👤 Player: " + currentPlayer.getName());
        System.out.println("🏆 Total Score: " + currentPlayer.getScore() + " points");
        System.out.println("✅ Correct Answers: " + currentPlayer.getCorrectAnswers() + "/" + 
                          currentPlayer.getTotalQuestionsAttempted());
        System.out.printf("🎯 Accuracy: %.1f%%\n", currentPlayer.getAccuracy());
        System.out.printf("⏱ Average Time: %.1f seconds\n", currentPlayer.getAverageTimePerQuestion());
        
        // Performance rating
        System.out.println("\n⭐ PERFORMANCE RATING");
        System.out.println("─".repeat(30));
        double accuracy = currentPlayer.getAccuracy();
        if (accuracy >= 90) {
            System.out.println("🏅 QUIZ MASTER - Exceptional!");
        } else if (accuracy >= 75) {
            System.out.println("🥈 EXPERT - Great work!");
        } else if (accuracy >= 60) {
            System.out.println("🥉 GOOD - Well done!");
        } else if (accuracy >= 40) {
            System.out.println("📚 SATISFACTORY - Keep learning!");
        } else {
            System.out.println("🌱 BEGINNER - Practice makes perfect!");
        }
        
        System.out.println("\n" + "═".repeat(40));
        System.out.println("Thanks for playing! 🎮");
    }
    
    public void close() {
        scanner.close();
    }
}
