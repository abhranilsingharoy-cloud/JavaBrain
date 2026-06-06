# JavaBrain 🧠

A comprehensive, interactive Command-Line Interface (CLI) quiz game built purely in Java. JavaBrain tests your knowledge across various subjects including Science, History, Mathematics, Geography, and Sports.

![JavaBrain Banner](https://img.shields.io/badge/Java-1.8+-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Build Status](https://img.shields.io/badge/build-passing-brightgreen?style=for-the-badge)
![Contributions](https://img.shields.io/badge/contributions-welcome-orange.svg?style=for-the-badge)

## 📋 Table of Contents
- [Features](#-features)
- [Architecture & Design](#-architecture--design)
- [Getting Started](#-getting-started)
- [How to Play](#-how-to-play)
- [Scoring System](#-scoring-system)
- [Future Enhancements](#-future-enhancements)

## ✨ Features
- **Interactive CLI Interface:** Clean, stylized console output with emojis and dynamic progress bars.
- **Dynamic Scoring:** Points based on question difficulty and speed bonuses for quick responses.
- **Categorized Questions:** Dive into various categories: Science, History, Mathematics, Geography, and Sports.
- **Player Statistics:** Tracks correct answers, accuracy, and average response time.
- **Leaderboard System:** Persistent top-10 high scores saved to local storage.
- **Performance Ratings:** Awards players with ranks like *Quiz Master* or *Expert* based on accuracy.

## 🏗 Architecture & Design
The application is structured following clean, modular Object-Oriented principles:
- **`com.quizgame.Main`**: The entry point managing the main menu and game lifecycle.
- **`com.quizgame.core.QuizManager`**: Handles the core quiz loop, user input, timer mechanisms, and evaluation.
- **`com.quizgame.features.Leaderboard`**: Manages persistent File I/O for storing and ranking player scores.
- **`com.quizgame.models.*`**: Contains domain models like `Question` and `Player` for encapsulating state and logic.

*Note: The project leverages pure Java without any external dependencies for simplicity and portability.*

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Maven (optional, for running via `pom.xml`)

### Installation & Execution
1. **Clone the repository:**
   ```bash
   git clone https://github.com/abhranilsingharoy-cloud/JavaBrain.git
   cd JavaBrain
   ```

2. **Compile the source code:**
   ```bash
   mkdir out
   javac -d out $(find src/main/java -name "*.java")
   ```

3. **Run the application:**
   ```bash
   java -cp out com.quizgame.Main
   ```

*(Alternatively, use Maven: `mvn compile exec:java`)*

## 🎮 How to Play
1. Launch the application and select **Start New Game** from the main menu.
2. Enter your name when prompted to register for the leaderboard.
3. For each question, type the number corresponding to your chosen answer (1-4) and press `Enter`.
4. Quick answers (under 5 seconds) grant an additional speed bonus!
5. After the quiz concludes, review your final results, accuracy, and see if you made it to the **Leaderboard**.

## 🏆 Scoring System
- **★ (Easy):** 10 points
- **★★ (Medium):** 20 points
- **★★★ (Hard):** 30 points
- **⚡ Speed Bonus:** +5 extra points for answering correctly in <5 seconds.

## 🔮 Future Enhancements
- [ ] **JSON Integration:** Load questions dynamically from the provided `questions.json` file.
- [ ] **Multiplayer Mode:** Support for local hot-seat multiplayer.
- [ ] **Category Selection:** Allow players to choose specific subjects to be tested on.

---

*Built with ❤️ for educational purposes and Java mastery.*