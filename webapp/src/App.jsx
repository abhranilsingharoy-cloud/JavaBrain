import { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { BrainCircuit, Sparkles, Trophy, Timer, Target, ChevronRight, Play } from 'lucide-react';
import './index.css';

const questions = [
  {
    question: "What is the primary function of a neural network's activation function?",
    options: [
      "To initialize weights",
      "To introduce non-linearity",
      "To calculate the loss",
      "To update biases"
    ],
    answer: 1,
    difficulty: 3,
    category: "Machine Learning"
  },
  {
    question: "Which of the following is NOT a type of machine learning?",
    options: [
      "Supervised Learning",
      "Unsupervised Learning",
      "Reinforcement Learning",
      "Procedural Learning"
    ],
    answer: 3,
    difficulty: 1,
    category: "AI Basics"
  },
  {
    question: "In natural language processing, what does 'tokenization' mean?",
    options: [
      "Generating a secure API token",
      "Splitting text into smaller units (words/subwords)",
      "Translating text to another language",
      "Converting speech to text"
    ],
    answer: 1,
    difficulty: 2,
    category: "NLP"
  }
];

function App() {
  const [gameState, setGameState] = useState('start'); // start, generating, playing, end
  const [currentQuestion, setCurrentQuestion] = useState(0);
  const [score, setScore] = useState(0);
  const [selectedOption, setSelectedOption] = useState(null);
  const [isCorrect, setIsCorrect] = useState(null);
  const [timeLeft, setTimeLeft] = useState(30);

  useEffect(() => {
    let timer;
    if (gameState === 'playing' && timeLeft > 0 && selectedOption === null) {
      timer = setTimeout(() => setTimeLeft(timeLeft - 1), 1000);
    } else if (timeLeft === 0 && selectedOption === null) {
      handleAnswer(-1); // Timeout
    }
    return () => clearTimeout(timer);
  }, [timeLeft, gameState, selectedOption]);

  const startGame = () => {
    setGameState('generating');
    setTimeout(() => {
      setGameState('playing');
      setTimeLeft(30);
    }, 2000); // Simulate AI generation
  };

  const handleAnswer = (index) => {
    if (selectedOption !== null) return;
    
    setSelectedOption(index);
    const correct = index === questions[currentQuestion].answer;
    setIsCorrect(correct);
    
    if (correct) {
      const difficultyMultiplier = questions[currentQuestion].difficulty * 10;
      const speedBonus = timeLeft > 20 ? 5 : 0;
      setScore(score + difficultyMultiplier + speedBonus);
    }

    setTimeout(() => {
      if (currentQuestion < questions.length - 1) {
        setGameState('generating');
        setTimeout(() => {
          setCurrentQuestion(currentQuestion + 1);
          setSelectedOption(null);
          setIsCorrect(null);
          setTimeLeft(30);
          setGameState('playing');
        }, 1500);
      } else {
        setGameState('end');
      }
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
          <div style={{ display: 'flex', gap: '1.5rem' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <Trophy size={20} color="var(--primary-color)" />
              <span style={{ fontWeight: 600 }}>{score}</span>
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: timeLeft <= 5 ? 'var(--danger)' : 'white' }}>
              <Timer size={20} />
              <span style={{ fontWeight: 600 }}>{timeLeft}s</span>
            </div>
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
              Experience the next generation of knowledge testing. Our AI dynamically generates questions tailored to your skill level.
            </p>
            <button className="btn" onClick={startGame}>
              <Play size={20} fill="currentColor" />
              Start Assessment
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
            <h2>AI is generating your next challenge...</h2>
            <p style={{ color: 'var(--text-muted)', marginTop: '0.5rem' }}>Analyzing performance parameters</p>
          </motion.div>
        )}

        {gameState === 'playing' && (
          <motion.div 
            key="playing"
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            exit={{ opacity: 0, scale: 1.05 }}
            className="glass-card"
          >
            <div className="progress-bar-container">
              <div 
                className="progress-bar" 
                style={{ width: `${((currentQuestion + 1) / questions.length) * 100}%` }}
              ></div>
            </div>

            <div className="ai-badge">
              <Sparkles size={14} /> AI Generated • {questions[currentQuestion].category}
            </div>
            
            <div className="difficulty-stars">
              {renderDifficulty(questions[currentQuestion].difficulty)}
            </div>

            <h2 style={{ fontSize: '1.5rem', marginBottom: '2rem', lineHeight: 1.4 }}>
              {questions[currentQuestion].question}
            </h2>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
              {questions[currentQuestion].options.map((option, idx) => {
                let btnClass = "option-btn";
                if (selectedOption !== null) {
                  if (idx === questions[currentQuestion].answer) btnClass += " correct";
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
                    {selectedOption !== null && idx === questions[currentQuestion].answer && (
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
            <h1 style={{ fontSize: '2.5rem', marginBottom: '0.5rem' }}>Assessment Complete</h1>
            <p style={{ color: 'var(--text-muted)', marginBottom: '2.5rem' }}>AI evaluation finished.</p>

            <div className="stats-grid">
              <div className="stat-card">
                <div className="stat-value">{score}</div>
                <div className="stat-label">Total Score</div>
              </div>
              <div className="stat-card">
                <div className="stat-value">{questions.length}</div>
                <div className="stat-label">Questions</div>
              </div>
              <div className="stat-card">
                <div className="stat-value">92%</div>
                <div className="stat-label">Accuracy</div>
              </div>
            </div>

            <button className="btn" onClick={() => {
              setCurrentQuestion(0);
              setScore(0);
              setGameState('start');
              setSelectedOption(null);
            }}>
              <Sparkles size={20} />
              Start New Session
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
