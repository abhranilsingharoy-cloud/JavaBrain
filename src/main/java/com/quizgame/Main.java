package com.quizgame;

import com.quizgame.core.QuizManager;
import com.quizgame.features.Leaderboard;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        
        displayWelcomeBanner();
        
        while (!exit) {
            displayMainMenu();
            System.out.print("Enter your choice (1-4): ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                switch (choice) {
                    case 1:
                        startNewGame(scanner);
                        break;
                    case 2:
                        Leaderboard.displayTopScores();
                        break;
                    case 3:
                        displayInstructions();
                        break;
                    case 4:
                        exit = true;
                        System.out.println("\n👋 Thank you for playing! Goodbye!");
                        break;
                    default:
                        System.out.println("⚠ Invalid choice! Please select 1-4.");
                }
            } catch (Exception e) {
                System.out.println("⚠ Please enter a valid number!");
                scanner.nextLine(); // Clear invalid input
            }
        }
        
        scanner.close();
    }
    
    private static void displayWelcomeBanner() {
        System.out.println("\n" + "═".repeat(55));
        System.out.println("              🎮 INTERACTIVE QUIZ GAME 🎮");
        System.out.println("              Version 1.0 • Java Edition");
        System.out.println("═".repeat(55));
        System.out.println();
    }
    
    private static void displayMainMenu() {
        System.out.println("\n" + "═".repeat(40));
        System.out.println("                MAIN MENU");
        System.out.println("═".repeat(40));
        System.out.println("1. 🎯 Start New Game");
        System.out.println("2. 🏆 View Leaderboard");
        System.out.println("3. 📖 Instructions");
        System.out.println("4. 🚪 Exit");
        System.out.println("═".repeat(40));
    }
    
    private static void startNewGame(Scanner scanner) {
        QuizManager quizManager = new QuizManager();
        quizManager.startQuiz();
        quizManager.close();
        
        System.out.print("\n↩️  Return to main menu? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        
        if (response.equals("no") || response.equals("n")) {
            System.out.println("\n👋 Thanks for playing!");
            System.exit(0);
        }
    }
    
    private static void displayInstructions() {
        System.out.println("\n" + "📘 GAME INSTRUCTIONS ".repeat(3));
        System.out.println("\n🎮 HOW TO PLAY:");
        System.out.println("1. Enter your name when prompted");
        System.out.println("2. Answer multiple-choice questions (1-4)");
        System.out.println("3. Enter 0 to quit during the quiz");
        System.out.println("\n🏆 SCORING:");
        System.out.println("• Easy questions: ★ = 10 points");
        System.out.println("• Medium questions: ★★ = 20 points");
        System.out.println("• Hard questions: ★★★ = 30 points");
        System.out.println("• Speed bonus: +5 points for answering <5 seconds");
        System.out.println("\n🎯 TIPS:");
        System.out.println("• Read questions carefully");
        System.out.println("• Quick answers earn bonus points");
        System.out.println("• Check your progress after each question");
        System.out.println("\n" + "─".repeat(50));
        System.out.print("Press Enter to continue...");
        new Scanner(System.in).nextLine();
    }
}
