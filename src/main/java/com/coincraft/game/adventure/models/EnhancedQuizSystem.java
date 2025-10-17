package com.coincraft.game.adventure.models;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

/**
 * Enhanced quiz system with difficulty levels, adaptive questions, and comprehensive feedback
 */
public class EnhancedQuizSystem {
    
    public enum DifficultyLevel {
        BEGINNER(1, "Beginner", 25, 10, "Perfect for newcomers"),
        INTERMEDIATE(2, "Intermediate", 50, 20, "For those with some knowledge"),
        ADVANCED(3, "Advanced", 75, 30, "For experienced learners"),
        EXPERT(4, "Expert", 100, 50, "For financial wizards"),
        MASTER(5, "Master", 150, 75, "Ultimate challenge");
        
        private final int level;
        private final String name;
        private final int baseReward;
        private final int basePenalty;
        private final String description;
        
        DifficultyLevel(int level, String name, int baseReward, int basePenalty, String description) {
            this.level = level;
            this.name = name;
            this.baseReward = baseReward;
            this.basePenalty = basePenalty;
            this.description = description;
        }
        
        public int getLevel() { return level; }
        public String getName() { return name; }
        public int getBaseReward() { return baseReward; }
        public int getBasePenalty() { return basePenalty; }
        public String getDescription() { return description; }
    }
    
    public static class QuizQuestion {
        private final String question;
        private final String[] options;
        private final int correctAnswer;
        private final String explanation;
        private final String category;
        private final DifficultyLevel difficulty;
        private final String[] keywords;
        private final String hint;
        
        public QuizQuestion(String question, String[] options, int correctAnswer, 
                          String explanation, String category, DifficultyLevel difficulty, 
                          String[] keywords, String hint) {
            this.question = question;
            this.options = options;
            this.correctAnswer = correctAnswer;
            this.explanation = explanation;
            this.category = category;
            this.difficulty = difficulty;
            this.keywords = keywords;
            this.hint = hint;
        }
        
        // Getters
        public String getQuestion() { return question; }
        public String[] getOptions() { return options; }
        public int getCorrectAnswer() { return correctAnswer; }
        public String getExplanation() { return explanation; }
        public String getCategory() { return category; }
        public DifficultyLevel getDifficulty() { return difficulty; }
        public String[] getKeywords() { return keywords; }
        public String getHint() { return hint; }
    }
    
    public static class QuizResult {
        private final boolean correct;
        private final int pointsEarned;
        private final int pointsLost;
        private final String feedback;
        private final String encouragement;
        private final String learningTip;
        private final int streakBonus;
        private final boolean isPerfect;
        
        public QuizResult(boolean correct, int pointsEarned, int pointsLost, 
                         String feedback, String encouragement, String learningTip, 
                         int streakBonus, boolean isPerfect) {
            this.correct = correct;
            this.pointsEarned = pointsEarned;
            this.pointsLost = pointsLost;
            this.feedback = feedback;
            this.encouragement = encouragement;
            this.learningTip = learningTip;
            this.streakBonus = streakBonus;
            this.isPerfect = isPerfect;
        }
        
        // Getters
        public boolean isCorrect() { return correct; }
        public int getPointsEarned() { return pointsEarned; }
        public int getPointsLost() { return pointsLost; }
        public String getFeedback() { return feedback; }
        public String getEncouragement() { return encouragement; }
        public String getLearningTip() { return learningTip; }
        public int getStreakBonus() { return streakBonus; }
        public boolean isPerfect() { return isPerfect; }
    }
    
    public static class QuizSession {
        private final List<QuizQuestion> questions;
        private final DifficultyLevel difficulty;
        private final String category;
        private int currentQuestionIndex;
        private int correctAnswers;
        private final int totalQuestions;
        private int totalPoints;
        private int currentStreak;
        private int maxStreak;
        private final List<QuizResult> results;
        private final Map<String, Integer> categoryPerformance;
        private final long startTime;
        private long endTime;
        
        public QuizSession(List<QuizQuestion> questions, DifficultyLevel difficulty, String category) {
            this.questions = questions;
            this.difficulty = difficulty;
            this.category = category;
            this.currentQuestionIndex = 0;
            this.correctAnswers = 0;
            this.totalQuestions = questions.size();
            this.totalPoints = 0;
            this.currentStreak = 0;
            this.maxStreak = 0;
            this.results = new ArrayList<>();
            this.categoryPerformance = new HashMap<>();
            this.startTime = System.currentTimeMillis();
        }
        
        public QuizResult answerQuestion(int selectedAnswer) {
            if (currentQuestionIndex >= questions.size()) {
                return null;
            }
            
            QuizQuestion question = questions.get(currentQuestionIndex);
            boolean isCorrect = (selectedAnswer == question.getCorrectAnswer());
            
            // Calculate points
            int basePoints = isCorrect ? difficulty.getBaseReward() : -difficulty.getBasePenalty();
            int streakBonus = calculateStreakBonus();
            int pointsEarned = basePoints + streakBonus;
            
            // Update statistics
            if (isCorrect) {
                correctAnswers++;
                currentStreak++;
                maxStreak = Math.max(maxStreak, currentStreak);
            } else {
                currentStreak = 0;
            }
            
            this.totalPoints += pointsEarned;
            
            // Update category performance tracking
            String questionCategory = question.getCategory();
            int currentPerformance = categoryPerformance.getOrDefault(questionCategory, 0);
            categoryPerformance.put(questionCategory, currentPerformance + (isCorrect ? 1 : -1));
            
            // Create result
            QuizResult result = new QuizResult(
                isCorrect,
                isCorrect ? pointsEarned : 0,
                isCorrect ? 0 : Math.abs(pointsEarned),
                generateFeedback(question, isCorrect),
                generateEncouragement(isCorrect, currentStreak),
                generateLearningTip(question, isCorrect),
                streakBonus,
                isCorrect && currentStreak >= 3
            );
            
            results.add(result);
            currentQuestionIndex++;
            
            return result;
        }
        
        private int calculateStreakBonus() {
            if (currentStreak >= 5) return 50;
            if (currentStreak >= 3) return 25;
            if (currentStreak >= 2) return 10;
            return 0;
        }
        
        private String generateFeedback(QuizQuestion question, boolean isCorrect) {
            if (isCorrect) {
                return "🎉 Correct! " + question.getExplanation();
            } else {
                return "❌ Incorrect. " + question.getExplanation();
            }
        }
        
        private String generateEncouragement(boolean isCorrect, int streak) {
            if (isCorrect) {
                if (streak >= 5) return "🔥 You're on fire! Amazing streak!";
                if (streak >= 3) return "🚀 Great job! Keep it up!";
                if (streak >= 2) return "👍 Nice work!";
                return "✅ Good answer!";
            } else {
                if (streak == 0) return "💪 Don't give up! Every expert was once a beginner.";
                return "📚 Learning opportunity! Use this to improve.";
            }
        }
        
        private String generateLearningTip(QuizQuestion question, boolean isCorrect) {
            if (isCorrect) {
                return "💡 Tip: You're mastering " + question.getCategory() + "!";
            } else {
                return "📖 Study tip: Review " + question.getCategory() + " concepts.";
            }
        }
        
        public boolean hasNextQuestion() {
            return currentQuestionIndex < questions.size();
        }
        
        public QuizQuestion getCurrentQuestion() {
            if (currentQuestionIndex < questions.size()) {
                return questions.get(currentQuestionIndex);
            }
            return null;
        }
        
        public QuizSessionSummary getSummary() {
            endTime = System.currentTimeMillis();
            return new QuizSessionSummary(
                totalQuestions,
                correctAnswers,
                totalPoints,
                maxStreak,
                (endTime - startTime) / 1000.0,
                calculateGrade()
            );
        }
        
        private String calculateGrade() {
            double percentage = (double) correctAnswers / totalQuestions;
            if (percentage >= 0.9) return "A+";
            if (percentage >= 0.8) return "A";
            if (percentage >= 0.7) return "B";
            if (percentage >= 0.6) return "C";
            if (percentage >= 0.5) return "D";
            return "F";
        }
        
        // Getters
        public List<QuizQuestion> getQuestions() { return questions; }
        public DifficultyLevel getDifficulty() { return difficulty; }
        public String getCategory() { return category; }
        public int getCurrentQuestionIndex() { return currentQuestionIndex; }
        public Map<String, Integer> getCategoryPerformance() { return new HashMap<>(categoryPerformance); }
        public int getCorrectAnswers() { return correctAnswers; }
        public int getTotalQuestions() { return totalQuestions; }
        public int getTotalPoints() { return totalPoints; }
        public int getCurrentStreak() { return currentStreak; }
        public int getMaxStreak() { return maxStreak; }
        public List<QuizResult> getResults() { return results; }
    }
    
    public static class QuizSessionSummary {
        private final int totalQuestions;
        private final int correctAnswers;
        private final int totalPoints;
        private final int maxStreak;
        private final double timeSpent;
        private final String grade;
        
        public QuizSessionSummary(int totalQuestions, int correctAnswers, int totalPoints, 
                                int maxStreak, double timeSpent, String grade) {
            this.totalQuestions = totalQuestions;
            this.correctAnswers = correctAnswers;
            this.totalPoints = totalPoints;
            this.maxStreak = maxStreak;
            this.timeSpent = timeSpent;
            this.grade = grade;
        }
        
        public double getSuccessRate() {
            return (double) correctAnswers / totalQuestions;
        }
        
        public String getPerformanceMessage() {
            double successRate = getSuccessRate();
            if (successRate >= 0.9) return "🏆 Outstanding performance! You're a financial wizard!";
            if (successRate >= 0.8) return "🌟 Excellent work! You're mastering financial concepts!";
            if (successRate >= 0.7) return "👍 Good job! You're on the right track!";
            if (successRate >= 0.6) return "📚 Keep studying! You're making progress!";
            return "💪 Don't give up! Practice makes perfect!";
        }
        
        // Getters
        public int getTotalQuestions() { return totalQuestions; }
        public int getCorrectAnswers() { return correctAnswers; }
        public int getTotalPoints() { return totalPoints; }
        public int getMaxStreak() { return maxStreak; }
        public double getTimeSpent() { return timeSpent; }
        public String getGrade() { return grade; }
    }
    
    private static final Map<String, List<QuizQuestion>> QUESTION_BANK = new HashMap<>();
    private static final Random RANDOM = new Random();
    
    static {
        initializeQuestionBank();
    }
    
    private static void initializeQuestionBank() {
        // Basic Financial Concepts
        List<QuizQuestion> basicQuestions = new ArrayList<>();
        basicQuestions.add(new QuizQuestion(
            "What is the primary purpose of an emergency fund?",
            new String[]{"To invest in stocks", "To cover unexpected expenses without going into debt", 
                        "To pay for vacations", "To buy luxury items"},
            1,
            "An emergency fund is designed to cover unexpected expenses without going into debt. This financial safety net prevents you from using credit cards or loans for emergencies.",
            "Emergency Funds",
            DifficultyLevel.BEGINNER,
            new String[]{"emergency", "fund", "safety", "debt"},
            "Think about what happens when unexpected expenses arise."
        ));
        
        basicQuestions.add(new QuizQuestion(
            "What percentage of your income should you save according to the 50/30/20 rule?",
            new String[]{"10%", "15%", "20%", "25%"},
            2,
            "The 50/30/20 rule suggests 50% for needs, 30% for wants, and 20% for savings and debt repayment.",
            "Budgeting",
            DifficultyLevel.BEGINNER,
            new String[]{"budget", "savings", "percentage", "rule"},
            "Remember the 50/30/20 rule breakdown."
        ));
        
        QUESTION_BANK.put("Basic Financial Concepts", basicQuestions);
        
        // Investment Knowledge
        List<QuizQuestion> investmentQuestions = new ArrayList<>();
        investmentQuestions.add(new QuizQuestion(
            "What is the main advantage of dollar-cost averaging?",
            new String[]{"It guarantees higher returns", "It reduces the impact of market volatility", 
                        "It eliminates all investment risk", "It only works with stocks"},
            1,
            "Dollar-cost averaging reduces the impact of market volatility by spreading your investments over time. This strategy helps you avoid the stress of timing the market.",
            "Investment Strategies",
            DifficultyLevel.INTERMEDIATE,
            new String[]{"dollar-cost", "averaging", "volatility", "timing"},
            "Think about how spreading investments over time affects risk."
        ));
        
        investmentQuestions.add(new QuizQuestion(
            "What is compound interest?",
            new String[]{"Interest on the principal only", "Interest on interest", 
                        "Simple interest calculation", "Interest paid monthly"},
            1,
            "Compound interest is interest calculated on the initial principal and the accumulated interest of previous periods. It's often called 'interest on interest' and is a powerful wealth-building tool.",
            "Compound Interest",
            DifficultyLevel.INTERMEDIATE,
            new String[]{"compound", "interest", "principal", "accumulated"},
            "Think about earning interest on your interest."
        ));
        
        QUESTION_BANK.put("Investment Knowledge", investmentQuestions);
        
        // Credit Knowledge
        List<QuizQuestion> creditQuestions = new ArrayList<>();
        creditQuestions.add(new QuizQuestion(
            "What percentage of your credit score is based on payment history?",
            new String[]{"15%", "25%", "35%", "45%"},
            2,
            "Payment history accounts for 35% of your credit score, making it the most important factor. Consistent on-time payments are crucial for building good credit.",
            "Credit Scores",
            DifficultyLevel.INTERMEDIATE,
            new String[]{"credit", "score", "payment", "history"},
            "Payment history is the most important factor in credit scoring."
        ));
        
        creditQuestions.add(new QuizQuestion(
            "What is the recommended credit utilization ratio?",
            new String[]{"Below 30%", "Below 50%", "Below 70%", "Below 90%"},
            0,
            "It's recommended to keep your credit utilization below 30%. This shows lenders that you're not overextending yourself with credit.",
            "Credit Utilization",
            DifficultyLevel.INTERMEDIATE,
            new String[]{"credit", "utilization", "ratio", "percentage"},
            "Lower utilization shows better credit management."
        ));
        
        QUESTION_BANK.put("Credit Knowledge", creditQuestions);
        
        // Advanced Financial Planning
        List<QuizQuestion> advancedQuestions = new ArrayList<>();
        advancedQuestions.add(new QuizQuestion(
            "What is the 'Rule of 72' used for?",
            new String[]{"Calculating tax deductions", "Estimating how long it takes for money to double at a given interest rate", 
                        "Determining credit card interest", "Calculating mortgage payments"},
            1,
            "The Rule of 72 helps estimate how long it takes for money to double at a given interest rate. Divide 72 by the interest rate to get the approximate years.",
            "Financial Planning",
            DifficultyLevel.ADVANCED,
            new String[]{"rule", "72", "double", "interest"},
            "This rule helps estimate investment growth time."
        ));
        
        advancedQuestions.add(new QuizQuestion(
            "What is the difference between a Roth IRA and a Traditional IRA?",
            new String[]{"No difference", "Roth IRA contributions are tax-deductible", 
                        "Traditional IRA withdrawals are tax-free", "Roth IRA withdrawals are tax-free"},
            3,
            "Roth IRA contributions are made with after-tax money, but withdrawals in retirement are tax-free. Traditional IRA contributions may be tax-deductible, but withdrawals are taxed.",
            "Retirement Planning",
            DifficultyLevel.ADVANCED,
            new String[]{"roth", "traditional", "ira", "tax"},
            "Think about when taxes are paid on contributions vs. withdrawals."
        ));
        
        QUESTION_BANK.put("Advanced Financial Planning", advancedQuestions);
    }
    
    /**
     * Create a quiz session with adaptive difficulty
     */
    public static QuizSession createQuizSession(String category, DifficultyLevel difficulty, int questionCount) {
        List<QuizQuestion> availableQuestions = QUESTION_BANK.get(category);
        if (availableQuestions == null || availableQuestions.isEmpty()) {
            return null;
        }
        
        // Filter questions by difficulty
        List<QuizQuestion> filteredQuestions = new ArrayList<>();
        for (QuizQuestion question : availableQuestions) {
            if (question.getDifficulty().getLevel() <= difficulty.getLevel()) {
                filteredQuestions.add(question);
            }
        }
        
        // Select random questions
        List<QuizQuestion> selectedQuestions = new ArrayList<>();
        int count = Math.min(questionCount, filteredQuestions.size());
        for (int i = 0; i < count; i++) {
            int randomIndex = RANDOM.nextInt(filteredQuestions.size());
            selectedQuestions.add(filteredQuestions.remove(randomIndex));
        }
        
        return new QuizSession(selectedQuestions, difficulty, category);
    }
    
    /**
     * Get available categories
     */
    public static List<String> getAvailableCategories() {
        return new ArrayList<>(QUESTION_BANK.keySet());
    }
    
    /**
     * Get difficulty levels
     */
    public static DifficultyLevel[] getDifficultyLevels() {
        return DifficultyLevel.values();
    }
}
