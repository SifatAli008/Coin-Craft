package com.coincraft.game.adventure.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Quiz System for Financial Literacy Testing
 * Handles quiz questions, scoring, and coin rewards
 */
public class QuizSystem {
    
    public static class QuizQuestion {
        private final String question;
        private final String[] options;
        private final int correctAnswer;
        private final String explanation;
        
        public QuizQuestion(String question, String[] options, int correctAnswer, String explanation) {
            this.question = question;
            this.options = options;
            this.correctAnswer = correctAnswer;
            this.explanation = explanation;
        }
        
        public String getQuestion() { return question; }
        public String[] getOptions() { return options; }
        public int getCorrectAnswer() { return correctAnswer; }
        public String getExplanation() { return explanation; }
    }
    
    public static class QuizSession {
        private final List<QuizQuestion> questions;
        private int currentQuestionIndex;
        private int correctAnswers;
        private int totalQuestions;
        private int coinsEarned;
        private final List<Boolean> answers;
        
        public QuizSession(List<QuizQuestion> questions) {
            this.questions = questions;
            this.currentQuestionIndex = 0;
            this.correctAnswers = 0;
            this.totalQuestions = questions.size();
            this.coinsEarned = 0;
            this.answers = new ArrayList<>();
        }
        
        public QuizQuestion getCurrentQuestion() {
            if (currentQuestionIndex < questions.size()) {
                return questions.get(currentQuestionIndex);
            }
            return null;
        }
        
        public boolean answerQuestion(int answerIndex) {
            QuizQuestion current = getCurrentQuestion();
            if (current == null) return false;
            
            boolean isCorrect = (answerIndex == current.getCorrectAnswer());
            answers.add(isCorrect);
            
            if (isCorrect) {
                correctAnswers++;
                coinsEarned += 10; // 10 coins per correct answer
            } else {
                coinsEarned -= 5; // -5 coins per wrong answer
            }
            
            currentQuestionIndex++;
            return isCorrect;
        }
        
        public boolean isComplete() {
            return currentQuestionIndex >= questions.size();
        }
        
        public int getScore() {
            return (correctAnswers * 100) / totalQuestions;
        }
        
        public int getCoinsEarned() {
            return Math.max(0, coinsEarned); // Don't go below 0
        }
        
        public int getCorrectAnswers() { return correctAnswers; }
        public int getWrongAnswers() { return totalQuestions - correctAnswers; }
        public int getTotalQuestions() { return totalQuestions; }
        public List<Boolean> getAnswers() { return new ArrayList<>(answers); }
    }
    
    /**
     * Create a financial literacy quiz
     */
    public static List<QuizQuestion> createFinancialLiteracyQuiz() {
        List<QuizQuestion> questions = new ArrayList<>();
        
        questions.add(new QuizQuestion(
            "What is the primary purpose of an emergency fund?",
            new String[]{
                "To invest in stocks",
                "To cover unexpected expenses without going into debt",
                "To pay for vacations",
                "To buy luxury items"
            },
            1, // Correct answer is B (index 1)
            "Emergency funds provide financial safety for unexpected expenses like job loss, medical bills, or car repairs."
        ));
        
        questions.add(new QuizQuestion(
            "What is the recommended percentage of income to save?",
            new String[]{
                "5-10%",
                "10-15%",
                "15-20%",
                "20-25%"
            },
            2, // Correct answer is C (index 2)
            "Most financial experts recommend saving 15-20% of your income for long-term financial security."
        ));
        
        questions.add(new QuizQuestion(
            "What does 'diversification' mean in investing?",
            new String[]{
                "Putting all money in one stock",
                "Spreading investments across different assets",
                "Only investing in bonds",
                "Avoiding all investments"
            },
            1, // Correct answer is B (index 1)
            "Diversification reduces risk by spreading investments across different asset classes and sectors."
        ));
        
        questions.add(new QuizQuestion(
            "What is compound interest?",
            new String[]{
                "Interest that decreases over time",
                "Interest earned on both principal and previous interest",
                "Interest only on the original amount",
                "Interest that changes daily"
            },
            1, // Correct answer is B (index 1)
            "Compound interest is powerful for wealth building - you earn interest on your interest!"
        ));
        
        questions.add(new QuizQuestion(
            "What is the 50/30/20 rule?",
            new String[]{
                "50% needs, 30% wants, 20% savings",
                "50% savings, 30% needs, 20% wants",
                "50% wants, 30% needs, 20% savings",
                "50% needs, 30% savings, 20% wants"
            },
            0, // Correct answer is A (index 0)
            "The 50/30/20 rule is a popular budgeting method: 50% for needs, 30% for wants, 20% for savings."
        ));
        
        questions.add(new QuizQuestion(
            "What is a credit score range?",
            new String[]{
                "200-500",
                "300-850",
                "400-900",
                "500-1000"
            },
            1, // Correct answer is B (index 1)
            "Credit scores range from 300-850, with higher scores indicating better creditworthiness."
        ));
        
        questions.add(new QuizQuestion(
            "What is the snowball method for debt payoff?",
            new String[]{
                "Pay highest interest debts first",
                "Pay smallest debts first",
                "Pay all debts equally",
                "Only pay minimum payments"
            },
            1, // Correct answer is B (index 1)
            "The snowball method pays smallest debts first for psychological motivation, then applies payments to larger debts."
        ));
        
        questions.add(new QuizQuestion(
            "What is the rule of 72?",
            new String[]{
                "Save 72% of your income",
                "A way to estimate how long it takes to double your money",
                "Invest in 72 different stocks",
                "Withdraw money at age 72"
            },
            1, // Correct answer is B (index 1)
            "The rule of 72 estimates how long it takes to double your money: divide 72 by your interest rate."
        ));
        
        return questions;
    }
    
    /**
     * Create a beginner quiz (5 questions)
     */
    public static List<QuizQuestion> createBeginnerQuiz() {
        List<QuizQuestion> allQuestions = createFinancialLiteracyQuiz();
        return allQuestions.subList(0, 5);
    }
    
    /**
     * Create an advanced quiz (all 8 questions)
     */
    public static List<QuizQuestion> createAdvancedQuiz() {
        return createFinancialLiteracyQuiz();
    }
}
