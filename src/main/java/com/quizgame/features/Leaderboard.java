package com.quizgame.features;

import com.quizgame.models.Player;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Leaderboard {
    private static final String LEADERBOARD_FILE = "leaderboard.txt";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static void saveScore(Player player) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LEADERBOARD_FILE, true))) {
            String record = String.format("%s,%d,%.1f,%s",
                player.getName(),
                player.getScore(),
                player.getAccuracy(),
                DATE_FORMAT.format(new Date()));
            
            writer.write(record);
            writer.newLine();
            System.out.println("📝 Score saved to leaderboard!");
        } catch (IOException e) {
            System.out.println("⚠ Could not save score to leaderboard.");
        }
    }
    
    public static void displayTopScores() {
        List<String[]> scores = new ArrayList<>();
        
        File file = new File(LEADERBOARD_FILE);
        if (!file.exists()) {
            System.out.println("\n📊 No scores recorded yet. Play a game to see leaderboard!");
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(LEADERBOARD_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    scores.add(parts);
                }
            }
            
            // Sort by score (descending)
            scores.sort((a, b) -> {
                int scoreA = Integer.parseInt(b[1]); // Score
                int scoreB = Integer.parseInt(a[1]);
                return Integer.compare(scoreA, scoreB);
            });
            
            System.out.println("\n" + "🏆".repeat(25));
            System.out.println("           LEADERBOARD - TOP 10");
            System.out.println("🏆".repeat(25));
            System.out.printf("%-4s %-20s %-10s %-10s %-20s%n", 
                "Rank", "Player", "Score", "Accuracy", "Date");
            System.out.println("─".repeat(70));
            
            int rank = 1;
            int count = Math.min(10, scores.size());
            
            for (int i = 0; i < count; i++) {
                String[] data = scores.get(i);
                String rankSymbol = getRankSymbol(rank);
                
                System.out.printf("%-4s %-20s %-10s %-9.1f%% %-20s%n",
                    rankSymbol + ".",
                    data[0], // Name
                    data[1], // Score
                    Double.parseDouble(data[2]), // Accuracy
                    data[3].substring(0, 10)); // Date
                rank++;
            }
            
            if (scores.isEmpty()) {
                System.out.println("          No scores recorded yet!");
            }
            
            System.out.println("─".repeat(70));
            
        } catch (IOException e) {
            System.out.println("⚠ Error reading leaderboard file.");
        } catch (NumberFormatException e) {
            System.out.println("⚠ Corrupted leaderboard data.");
        }
    }
    
    private static String getRankSymbol(int rank) {
        switch(rank) {
            case 1: return "🥇";
            case 2: return "🥈";
            case 3: return "🥉";
            default: return String.valueOf(rank);
        }
    }
    
    public static void clearLeaderboard() {
        File file = new File(LEADERBOARD_FILE);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("🗑️ Leaderboard cleared!");
            } else {
                System.out.println("⚠ Could not clear leaderboard.");
            }
        }
    }
}
