import { useState, useEffect, useRef } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { BrainCircuit, Sparkles, Trophy, Timer, Target, Play, ShieldAlert } from 'lucide-react';
import './index.css';

function App() {
  const [gameState, setGameState] = useState('start'); // start, generating, playing, end
  const [currentQuestionData, setCurrentQuestionData] = useState(null);
  const [questionCount, setQuestionCount] = useState(0);
  const [score, setScore] = useState(0);
  const [selectedOption, setSelectedOption] = useState(null);
  const [isCorrect, setIsCorrect] = useState(null);
  const [timeLeft, setTimeLeft] = useState(30);
  
  // To handle HTML entities returned by the API
  const decodeHTML = (html) => {
    const txt = document.createElement("textarea");
    txt.innerHTML = html;
    return txt.value;
  };

  const fetchNextQuestion = async () => {
    try {
      // Fetch a random multiple-choice question from Open Trivia DB
      const response = await fetch('https://opentdb.com/api.php?amount=1&type=multiple');
      const data = await response.json();
      
      if (data.results && data.results.length > 0) {
        const q = data.results[0];
        
        // Shuffle options
        const allOptions = [...q.incorrect_answers, q.correct_answer].map(decodeHTML);
        const shuffledOptions = allOptions.sort(() => Math.random() - 0.5);
        const correctIndex = shuffledOptions.indexOf(decodeHTML(q.correct_answer));
        
        // Map difficulty to a 1-3 scale
        let diffScale = 1;
        if (q.difficulty === 'medium') diffScale = 2;
        if (q.difficulty === 'hard') diffScale = 3;

        setCurrentQuestionData({
          question: decodeHTML(q.question),
          options: shuffledOptions,
          answer: correctIndex,
          difficulty: diffScale,
          category: decodeHTML(q.category)
        });
      }
    } catch (error) {
      console.error("Failed to fetch question", error);
      // Fallback question if API fails
      setCurrentQuestionData({
        question: "API Error: Unable to connect to the knowledge database. Please ensure you have an internet connection.",
        options: ["Retry", "Skip", "Abort", "Reload"],
        answer: 0,
        difficulty: 1,
        category: "System Error"
      });
    }
  };

  useEffect(() => {
    let timer;
    if (gameState === 'playing' && timeLeft > 0 && selectedOption === null) {
      timer = setTimeout(() => setTimeLeft(timeLeft - 1), 1000);
    } else if (timeLeft === 0 && selectedOption === null && gameState === 'playing') {
      handleAnswer(-1); // Timeout
    }
    return () => clearTimeout(timer);
  }, [timeLeft, gameState, selectedOption]);

  const startGame = () => {
    setGameState('generating');
    fetchNextQuestion().then(() => {
      setTimeout(() => {
        setGameState('playing');
        setTimeLeft(30);
        setQuestionCount(1);
      }, 1500); // Simulate AI generation delay
    });
  };

  const stopGame = () => {
    setGameState('end');
  };

  const handleAnswer = (index) => {
    if (selectedOption !== null) return;
    
    setSelectedOption(index);
    const correct = currentQuestionData && index === currentQuestionData.answer;
    setIsCorrect(correct);
    
    if (correct) {
      const difficultyMultiplier = currentQuestionData.difficulty * 10;
      const speedBonus = timeLeft > 20 ? 5 : 0;
      setScore(prev => prev + difficultyMultiplier + speedBonus);
    }

    setTimeout(() => {
      setGameState('generating');
      fetchNextQuestion().then(() => {
        setTimeout(() => {
          setSelectedOption(null);
          setIsCorrect(null);
          setTimeLeft(30);
          setQuestionCount(prev => prev + 1);
          setGameState('playing');
        }, 1500);
      });
    }, 2000);
  };

  const renderDifficulty = (level) => {
    return Array(3).fill(0).map((_, i) => (
      <span key={i} style={{ opacity: i < level ? 1 : 0.3 }}>★</span>
    ));
  };

  return (
    <div className="app-container">
      <header className="header">
        <div className="logo">
          <BrainCircuit size={32} color="var(--primary-color)" />
          JavaBrain <span style={{ fontSize: '1rem', color: 'var(--text-muted)' }}>AI</span>
        </div>
        {gameState === 'playing' && (
          <div style={{ display: 'flex', gap: '1.5rem', alignItems: 'center' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <Trophy size={20} color="var(--primary-color)" />
              <span style={{ fontWeight: 600 }}>{score}</span>
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: timeLeft <= 5 ? 'var(--danger)' : 'white' }}>
              <Timer size={20} />
              <span style={{ fontWeight: 600 }}>{timeLeft}s</span>
            </div>
            <button 
              className="btn btn-secondary" 
              style={{ padding: '0.5rem 1rem', fontSize: '0.9rem' }}
              onClick={stopGame}
            >
              End Session
            </button>
          </div>
        )}
      </header>

      <AnimatePresence mode="wait">
        {gameState === 'start' && (
          <motion.div 
            key="start"
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -20 }}
            className="glass-card" 
            style={{ textAlign: 'center', margin: 'auto 0' }}
          >
            <Sparkles size={48} color="var(--secondary-color)" style={{ margin: '0 auto 1.5rem' }} />
            <h1 style={{ fontSize: '2.5rem', marginBottom: '1rem' }}>Welcome to JavaBrain AI</h1>
            <p style={{ color: 'var(--text-muted)', marginBottom: '2.5rem', fontSize: '1.1rem', lineHeight: 1.6 }}>
              Experience infinite knowledge testing. Our engine connects to a vast global database to generate <strong>unlimited questions</strong> tailored dynamically on the fly.
            </p>
            <button className="btn" onClick={startGame}>
              <Play size={20} fill="currentColor" />
              Start Infinite Assessment
            </button>
          </motion.div>
        )}

        {gameState === 'generating' && (
          <motion.div 
            key="generating"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            className="glass-card" 
            style={{ textAlign: 'center', margin: 'auto 0' }}
          >
            <motion.div
              animate={{ rotate: 360 }}
              transition={{ repeat: Infinity, duration: 2, ease: "linear" }}
              style={{ display: 'inline-block', marginBottom: '1.5rem' }}
            >
              <BrainCircuit size={48} color="var(--secondary-color)" />
            </motion.div>
            <h2>Fetching next challenge...</h2>
            <p style={{ color: 'var(--text-muted)', marginTop: '0.5rem' }}>Scanning infinite knowledge matrix</p>
          </motion.div>
        )}

        {gameState === 'playing' && currentQuestionData && (
          <motion.div 
            key="playing"
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            exit={{ opacity: 0, scale: 1.05 }}
            className="glass-card"
          >
            <div className="ai-badge">
              <Sparkles size={14} /> AI Engine • {currentQuestionData.category}
            </div>
            
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
              <div className="difficulty-stars" style={{ margin: 0 }}>
                {renderDifficulty(currentQuestionData.difficulty)}
              </div>
              <div style={{ color: 'var(--text-muted)', fontSize: '0.9rem', fontWeight: 600 }}>
                Question {questionCount}
              </div>
            </div>

            <h2 style={{ fontSize: '1.5rem', marginBottom: '2rem', lineHeight: 1.4 }}>
              {currentQuestionData.question}
            </h2>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
              {currentQuestionData.options.map((option, idx) => {
                let btnClass = "option-btn";
                if (selectedOption !== null) {
                  if (idx === currentQuestionData.answer) btnClass += " correct";
                  else if (idx === selectedOption) btnClass += " incorrect";
                }

                return (
                  <button 
                    key={idx} 
                    className={btnClass}
                    onClick={() => handleAnswer(idx)}
                    disabled={selectedOption !== null}
                  >
                    <span>{String.fromCharCode(65 + idx)}. {option}</span>
                    {selectedOption !== null && idx === currentQuestionData.answer && (
                      <motion.div initial={{ scale: 0 }} animate={{ scale: 1 }}>
                        <Target size={20} color="var(--success)" />
                      </motion.div>
                    )}
                  </button>
                );
              })}
            </div>
          </motion.div>
        )}

        {gameState === 'end' && (
          <motion.div 
            key="end"
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="glass-card"
            style={{ textAlign: 'center', margin: 'auto 0' }}
          >
            <Trophy size={64} color="var(--primary-color)" style={{ margin: '0 auto 1.5rem' }} />
            <h1 style={{ fontSize: '2.5rem', marginBottom: '0.5rem' }}>Session Concluded</h1>
            <p style={{ color: 'var(--text-muted)', marginBottom: '2.5rem' }}>Infinite knowledge matrix evaluation finished.</p>

            <div className="stats-grid">
              <div className="stat-card">
                <div className="stat-value">{score}</div>
                <div className="stat-label">Total Score</div>
              </div>
              <div className="stat-card">
                <div className="stat-value">{questionCount - 1}</div>
                <div className="stat-label">Questions Attempted</div>
              </div>
              <div className="stat-card">
                <div className="stat-value">Rank</div>
                <div className="stat-label" style={{ color: 'var(--primary-color)', fontWeight: 600, fontSize: '1.2rem', marginTop: '0.5rem' }}>
                  {score > 500 ? 'Master' : score > 200 ? 'Expert' : score > 50 ? 'Adept' : 'Novice'}
                </div>
              </div>
            </div>

            <button className="btn" onClick={() => {
              setQuestionCount(0);
              setScore(0);
              setGameState('start');
              setSelectedOption(null);
            }}>
              <Sparkles size={20} />
              Start New Infinite Session
            </button>
          </motion.div>
        )}
      </AnimatePresence>

      <div className="footer">
        Developed by <span>Abhranil Singha Roy</span>
      </div>
    </div>
  );
}

export default App;
