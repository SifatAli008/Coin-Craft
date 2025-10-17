package com.coincraft.game.adventure.models;

/**
 * Pre-built conversation trees for different NPC types
 */
public class NPCConversationTrees {
    
    /**
     * Create conversation tree for Strong Adventurer (Game Guide)
     */
    public static ConversationSystem.DialogueNode createAdventurerConversation() {
        // Welcome message
        ConversationSystem.DialogueNode welcome = new ConversationSystem.DialogueNode(
            "Strong Adventurer", 
            "Greetings, fellow adventurer! I'm your guide to this financial literacy world. Welcome to your journey of learning about money management!"
        );
        
        // Main menu
        ConversationSystem.DialogueNode mainMenu = new ConversationSystem.DialogueNode(
            "Strong Adventurer",
            "How can I help you today, brave adventurer?"
        );
        
        // Game controls explanation
        ConversationSystem.DialogueNode controls = new ConversationSystem.DialogueNode(
            "Strong Adventurer",
            """
            Here are the essential controls for your adventure:

            🎮 MOVEMENT:
            • Use WASD keys to move around the world
            • Press SPACE near NPCs to interact with them
            • Press G to toggle go-through mode (pass through NPCs)

            💡 TIPS:
            • Each NPC has unique knowledge to share
            • Answer questions correctly to earn coins
            • Wrong answers will cost you coins!
            """
        );
        
        // NPC introductions
        ConversationSystem.DialogueNode npcIntro = new ConversationSystem.DialogueNode(
            "Strong Adventurer",
            """
            Let me introduce you to the other NPCs in this world:

            🧙‍♀️ WISE LADY: She holds the secrets of financial wisdom. Talk to her to learn about budgeting, saving, and investing.

            👨‍💼 SMART BUSINESSMAN: He will test your financial knowledge with quizzes. Get answers right to earn coins, wrong answers cost coins!

            Each NPC has different lessons to teach you on your financial literacy journey.
            """
        );
        
        // Adventure tips
        ConversationSystem.DialogueNode tips = new ConversationSystem.DialogueNode(
            "Strong Adventurer",
            """
            Here are some tips for your financial literacy adventure:

            💡 EXPLORATION TIPS:
            • Walk around and discover all the NPCs
            • Don't be afraid to ask questions
            • Learn from your mistakes

            💰 COIN MANAGEMENT:
            • Save your coins for important purchases
            • Answer quiz questions correctly to earn more
            • Think carefully before answering questions

            🎯 GOALS:
            • Learn about budgeting and saving
            • Understand how to invest wisely
            • Build your financial knowledge step by step
            """
        );
        
        // Goodbye message
        ConversationSystem.DialogueNode goodbye = new ConversationSystem.DialogueNode(
            "Strong Adventurer",
            """
            Good luck on your financial literacy journey, brave adventurer! Remember, every expert was once a beginner. Take your time, ask questions, and learn at your own pace. May your financial wisdom grow with each step you take!
            """
        );
        
        // Build conversation tree
        welcome.addOption("Tell me about the game controls", controls);
        welcome.addOption("Introduce me to other NPCs", npcIntro);
        welcome.addOption("Give me some adventure tips", tips);
        welcome.addOption("Thank you, I'll explore now", goodbye);
        
        mainMenu.addOption("How do I play this game?", controls);
        mainMenu.addOption("Who are the other NPCs?", npcIntro);
        mainMenu.addOption("Any tips for my adventure?", tips);
        mainMenu.addOption("I'm ready to explore!", goodbye);
        
        controls.addOption("Tell me about other NPCs", npcIntro);
        controls.addOption("Give me some tips", tips);
        controls.addOption("I understand, thank you", goodbye);
        
        npcIntro.addOption("Tell me about game controls", controls);
        npcIntro.addOption("Give me some tips", tips);
        npcIntro.addOption("I'm ready to explore!", goodbye);
        
        tips.addOption("Tell me about controls", controls);
        tips.addOption("Tell me about NPCs", npcIntro);
        tips.addOption("I'm ready to explore!", goodbye);
        
        return welcome;
    }
    
    /**
     * Create conversation tree for Wise Lady (Financial Education)
     */
    public static ConversationSystem.DialogueNode createWiseLadyConversation() {
        // Welcome message
        ConversationSystem.DialogueNode welcome = new ConversationSystem.DialogueNode(
            "Wise Lady",
            "Greetings, seeker of financial wisdom. I am the Wise Lady, keeper of financial knowledge. I sense you wish to learn about the art of money management. What aspect of financial literacy interests you most?"
        );
        
        // Budgeting lesson - Part 1
        ConversationSystem.DialogueNode budgeting = new ConversationSystem.DialogueNode(
            "Wise Lady",
            """
            Ah, budgeting - the foundation of financial wisdom! Let me teach you the ancient art of budgeting:

            📊 THE 50/30/20 RULE:
            • 50% for Needs (rent, food, utilities, insurance)
            • 30% for Wants (entertainment, dining out, hobbies)
            • 20% for Savings and Debt Repayment

            💡 BASIC BUDGETING STRATEGIES:
            • Track every expense for one month
            • Use the envelope method for cash spending
            • Review and adjust your budget monthly
            • Always pay yourself first (savings)
            """
        );
        
        // Budgeting lesson - Part 2 (Advanced)
        ConversationSystem.DialogueNode budgetingAdvanced = new ConversationSystem.DialogueNode(
            "Wise Lady",
            """
            Now for advanced budgeting strategies:

            🎯 ADVANCED TECHNIQUES:
            • Zero-based budgeting: Every dollar has a purpose
            • 80/20 rule: Live on 80%, save 20%
            • Use apps like Mint, YNAB, or Excel spreadsheets
            • Set up automatic transfers to savings

            ⚠️ COMMON MISTAKES:
            • Not accounting for irregular expenses
            • Being too restrictive (leads to failure)
            • Forgetting about annual expenses
            """
        );
        
        // Saving lesson - Part 1
        ConversationSystem.DialogueNode saving = new ConversationSystem.DialogueNode(
            "Wise Lady",
            """
            Saving is the path to financial freedom, young one. Listen to my wisdom:

            💰 EMERGENCY FUND (Priority #1):
            • Save 3-6 months of essential expenses
            • Keep in high-yield savings account (2-4% APY)
            • This is your financial safety net
            • Start with $1,000, then build to full amount

            🎯 BASIC SAVING STRATEGIES:
            • Pay yourself first (automate 20% of income)
            • Use the 24-hour rule for purchases over $100
            • Save windfalls, bonuses, and tax refunds
            """
        );
        
        // Saving lesson - Part 2 (Advanced)
        ConversationSystem.DialogueNode savingAdvanced = new ConversationSystem.DialogueNode(
            "Wise Lady",
            """
            Advanced saving strategies:

            📈 SAVINGS GOALS HIERARCHY:
            1. Emergency fund (3-6 months expenses)
            2. Retirement savings (15% of income)
            3. Specific goals (house, car, vacation)
            4. Investment accounts (after emergency fund)

            💡 PSYCHOLOGICAL TRICKS:
            • Name your savings accounts
            • Visualize your goals with pictures
            • Celebrate small milestones
            • Use separate accounts for different goals
            """
        );
        
        // Investing lesson - Part 1
        ConversationSystem.DialogueNode investing = new ConversationSystem.DialogueNode(
            "Wise Lady",
            """
            Investing is how you make your money work for you. Here is my ancient wisdom:

            📈 INVESTMENT PRINCIPLES:
            • Start early - time is your greatest ally
            • Diversify across asset classes and sectors
            • Invest in low-cost index funds
            • Don't try to time the market
            • Dollar-cost averaging reduces risk

            ⚖️ RISK MANAGEMENT:
            • Only invest what you can afford to lose
            • Higher risk = higher potential return
            • Consider your age and goals
            """
        );
        
        // Investing lesson - Part 2 (Advanced)
        ConversationSystem.DialogueNode investingAdvanced = new ConversationSystem.DialogueNode(
            "Wise Lady",
            """
            Advanced investment strategies:

            🎯 INVESTMENT ACCOUNTS:
            • 401(k) - employer match is free money
            • IRA (Traditional vs Roth) - tax advantages
            • HSA - triple tax advantage for health expenses
            • Brokerage account - after tax-advantaged accounts

            💡 BEGINNER STRATEGY:
            • Start with target-date funds or 3-fund portfolio
            • 60% Total Stock Market, 30% International, 10% Bonds
            • Use robo-advisors if overwhelmed
            • Avoid individual stocks until you have $10k+ invested
            """
        );
        
        // Credit lesson - Part 1
        ConversationSystem.DialogueNode credit = new ConversationSystem.DialogueNode(
            "Wise Lady",
            """
            Credit is a powerful tool when used wisely. Let me share the secrets:

            💳 CREDIT SCORE BASICS (FICO 300-850):
            • Payment History (35%) - Pay on time, every time
            • Credit Utilization (30%) - Keep below 30%, ideally under 10%
            • Credit History Length (15%) - Don't close old accounts
            • Credit Mix (10%) - Different types of credit
            • New Credit (10%) - Limit hard inquiries
            """
        );
        
        // Credit lesson - Part 2 (Advanced)
        ConversationSystem.DialogueNode creditAdvanced = new ConversationSystem.DialogueNode(
            "Wise Lady",
            """
            Advanced credit strategies:

            🛡️ CREDIT BUILDING:
            • Start with secured credit cards if no history
            • Use credit cards for regular purchases, pay in full
            • Keep oldest credit card open
            • Request credit limit increases

            ⚠️ MISTAKES TO AVOID:
            • Making only minimum payments
            • Opening too many accounts at once
            • Using credit cards for things you can't afford
            """
        );
        
        // Debt management lesson - Part 1
        ConversationSystem.DialogueNode debt = new ConversationSystem.DialogueNode(
            "Wise Lady",
            """
            Debt can be a burden or a tool. Learn to master it:

            ⚔️ DEBT PAYOFF STRATEGIES:
            • Snowball Method: Pay smallest debts first
            • Avalanche Method: Pay highest interest first
            • Debt consolidation: Combine multiple debts
            • Balance transfer: Move to 0% cards

            🎯 DEBT PREVENTION:
            • Live below your means
            • Build emergency fund first
            • Avoid lifestyle inflation
            """
        );
        
        // Debt management lesson - Part 2 (Advanced)
        ConversationSystem.DialogueNode debtAdvanced = new ConversationSystem.DialogueNode(
            "Wise Lady",
            """
            Advanced debt management:

            💡 DEBT MANAGEMENT TOOLS:
            • Debt payoff calculators (Bankrate, NerdWallet)
            • Budgeting apps (Mint, YNAB, EveryDollar)
            • Balance transfer credit cards (0% intro APR)
            • Personal loans for debt consolidation

            ⚠️ DEBT TRAPS TO AVOID:
            • Payday loans (400%+ APR)
            • Title loans (using car as collateral)
            • Rent-to-own stores (extremely expensive)
            • Credit card cash advances
            """
        );
        
        // Financial goals lesson - Part 1
        ConversationSystem.DialogueNode goals = new ConversationSystem.DialogueNode(
            "Wise Lady",
            """
            Setting financial goals guides your journey. Here's the ancient wisdom:

            🎯 TYPES OF GOALS:
            • Short-term (1-2 years): Emergency fund, vacation
            • Medium-term (3-5 years): Car, home down payment
            • Long-term (5+ years): Retirement, education

            📋 SMART GOAL FRAMEWORK:
            • Specific: 'Save $10,000 for house'
            • Measurable: Track progress monthly
            • Achievable: Realistic for your income
            • Relevant: Matches your values
            • Time-bound: 'By December 2025'
            """
        );
        
        // Financial goals lesson - Part 2 (Advanced)
        ConversationSystem.DialogueNode goalsAdvanced = new ConversationSystem.DialogueNode(
            "Wise Lady",
            """
            Advanced goal setting strategies:

            💰 GOAL PRIORITIZATION:
            1. Emergency fund (3-6 months)
            2. High-interest debt payoff
            3. Employer 401(k) match
            4. Specific savings goals
            5. Additional retirement savings

            💡 ACHIEVEMENT STRATEGIES:
            • Break large goals into milestones
            • Automate savings transfers
            • Visualize goals with pictures
            • Celebrate small wins
            • Review goals quarterly
            """
        );
        
        // Main menu
        ConversationSystem.DialogueNode mainMenu = new ConversationSystem.DialogueNode(
            "Wise Lady",
            "What aspect of financial wisdom would you like to explore?"
        );
        
        // Goodbye message
        ConversationSystem.DialogueNode goodbye = new ConversationSystem.DialogueNode(
            "Wise Lady",
            "Remember, financial wisdom is not about having all the answers, but about asking the right questions. Apply what you've learned, and your financial future will be bright. May prosperity follow your wise decisions!"
        );
        
        // Build conversation tree
        welcome.addOption("Teach me about budgeting", budgeting);
        welcome.addOption("How should I save money?", saving);
        welcome.addOption("Tell me about investing", investing);
        welcome.addOption("What should I know about credit?", credit);
        welcome.addOption("Help me with debt management", debt);
        welcome.addOption("How do I set financial goals?", goals);
        welcome.addOption("I have other questions", mainMenu);
        
        mainMenu.addOption("Budgeting basics", budgeting);
        mainMenu.addOption("Saving strategies", saving);
        mainMenu.addOption("Investment wisdom", investing);
        mainMenu.addOption("Credit knowledge", credit);
        mainMenu.addOption("Debt management", debt);
        mainMenu.addOption("Financial goal setting", goals);
        mainMenu.addOption("Thank you for your wisdom", goodbye);
        
        budgeting.addOption("Tell me more about advanced budgeting", budgetingAdvanced);
        budgeting.addOption("Tell me about saving", saving);
        budgeting.addOption("What about investing?", investing);
        budgeting.addOption("How do I set goals?", goals);
        budgeting.addOption("I have other questions", mainMenu);
        budgeting.addOption("Thank you for your wisdom", goodbye);
        
        budgetingAdvanced.addOption("Tell me about saving", saving);
        budgetingAdvanced.addOption("What about investing?", investing);
        budgetingAdvanced.addOption("How do I set goals?", goals);
        budgetingAdvanced.addOption("I have other questions", mainMenu);
        budgetingAdvanced.addOption("Thank you for your wisdom", goodbye);
        
        saving.addOption("Tell me more about advanced saving", savingAdvanced);
        saving.addOption("Tell me about budgeting", budgeting);
        saving.addOption("What about investing?", investing);
        saving.addOption("How do I set goals?", goals);
        saving.addOption("I have other questions", mainMenu);
        saving.addOption("Thank you for your wisdom", goodbye);
        
        savingAdvanced.addOption("Tell me about budgeting", budgeting);
        savingAdvanced.addOption("What about investing?", investing);
        savingAdvanced.addOption("How do I set goals?", goals);
        savingAdvanced.addOption("I have other questions", mainMenu);
        savingAdvanced.addOption("Thank you for your wisdom", goodbye);
        
        investing.addOption("Tell me more about advanced investing", investingAdvanced);
        investing.addOption("Tell me about budgeting", budgeting);
        investing.addOption("How should I save?", saving);
        investing.addOption("How do I set goals?", goals);
        investing.addOption("I have other questions", mainMenu);
        investing.addOption("Thank you for your wisdom", goodbye);
        
        investingAdvanced.addOption("Tell me about budgeting", budgeting);
        investingAdvanced.addOption("How should I save?", saving);
        investingAdvanced.addOption("How do I set goals?", goals);
        investingAdvanced.addOption("I have other questions", mainMenu);
        investingAdvanced.addOption("Thank you for your wisdom", goodbye);
        
        credit.addOption("Tell me more about advanced credit", creditAdvanced);
        credit.addOption("Tell me about budgeting", budgeting);
        credit.addOption("How should I save?", saving);
        credit.addOption("What about debt?", debt);
        credit.addOption("How do I set goals?", goals);
        credit.addOption("I have other questions", mainMenu);
        credit.addOption("Thank you for your wisdom", goodbye);
        
        creditAdvanced.addOption("Tell me about budgeting", budgeting);
        creditAdvanced.addOption("How should I save?", saving);
        creditAdvanced.addOption("What about debt?", debt);
        creditAdvanced.addOption("How do I set goals?", goals);
        creditAdvanced.addOption("I have other questions", mainMenu);
        creditAdvanced.addOption("Thank you for your wisdom", goodbye);
        
        debt.addOption("Tell me more about advanced debt management", debtAdvanced);
        debt.addOption("Tell me about budgeting", budgeting);
        debt.addOption("How should I save?", saving);
        debt.addOption("What about credit?", credit);
        debt.addOption("How do I set goals?", goals);
        debt.addOption("I have other questions", mainMenu);
        debt.addOption("Thank you for your wisdom", goodbye);
        
        debtAdvanced.addOption("Tell me about budgeting", budgeting);
        debtAdvanced.addOption("How should I save?", saving);
        debtAdvanced.addOption("What about credit?", credit);
        debtAdvanced.addOption("How do I set goals?", goals);
        debtAdvanced.addOption("I have other questions", mainMenu);
        debtAdvanced.addOption("Thank you for your wisdom", goodbye);
        
        goals.addOption("Tell me more about advanced goal setting", goalsAdvanced);
        goals.addOption("Tell me about budgeting", budgeting);
        goals.addOption("How should I save?", saving);
        goals.addOption("What about investing?", investing);
        goals.addOption("What about credit?", credit);
        goals.addOption("I have other questions", mainMenu);
        goals.addOption("Thank you for your wisdom", goodbye);
        
        goalsAdvanced.addOption("Tell me about budgeting", budgeting);
        goalsAdvanced.addOption("How should I save?", saving);
        goalsAdvanced.addOption("What about investing?", investing);
        goalsAdvanced.addOption("What about credit?", credit);
        goalsAdvanced.addOption("I have other questions", mainMenu);
        goalsAdvanced.addOption("Thank you for your wisdom", goodbye);
        
        return welcome;
    }
    
    /**
     * Create conversation tree for Smart Businessman (Quiz System)
     */
    public static ConversationSystem.DialogueNode createBusinessmanConversation() {
        // Welcome message
        ConversationSystem.DialogueNode welcome = new ConversationSystem.DialogueNode(
            "Smart Businessman",
            "Hello there! I'm the Smart Businessman, and I test financial knowledge. I believe the best way to learn is through practice and challenge. Are you ready to test your financial literacy skills?"
        );
        
        // Quiz introduction
        ConversationSystem.DialogueNode quizIntro = new ConversationSystem.DialogueNode(
            "Smart Businessman",
            """
            Excellent! I have prepared a series of financial literacy questions for you. Here's how it works:

            💰 REWARDS:
            • Correct answers: +10 coins each
            • Wrong answers: -5 coins each
            • Total possible coins: 50 (5 questions)

            🎯 RULES:
            • Read each question carefully
            • Choose the best answer
            • Think before you answer
            • Learn from explanations

            Are you ready to begin the quiz?
            """
        );
        
        // Quiz Question 1
        ConversationSystem.DialogueNode quizQ1 = new ConversationSystem.DialogueNode(
            "Smart Businessman",
            """
            Question 1: What is the primary purpose of an emergency fund?

            A) To invest in stocks
            B) To cover unexpected expenses without going into debt
            C) To pay for vacations
            D) To buy luxury items
            """
        );
        
        // Quiz Question 2
        ConversationSystem.DialogueNode quizQ2 = new ConversationSystem.DialogueNode(
            "Smart Businessman",
            """
            Question 2: What is the recommended percentage of income to save?

            A) 5-10%
            B) 10-15%
            C) 15-20%
            D) 20-25%
            """
        );
        
        // Quiz Question 3
        ConversationSystem.DialogueNode quizQ3 = new ConversationSystem.DialogueNode(
            "Smart Businessman",
            """
            Question 3: What does "diversification" mean in investing?

            A) Putting all money in one stock
            B) Spreading investments across different assets
            C) Only investing in bonds
            D) Avoiding all investments
            """
        );
        
        // Quiz Question 4
        ConversationSystem.DialogueNode quizQ4 = new ConversationSystem.DialogueNode(
            "Smart Businessman",
            """
            Question 4: What is compound interest?

            A) Interest that decreases over time
            B) Interest earned on both principal and previous interest
            C) Interest only on the original amount
            D) Interest that changes daily
            """
        );
        
        // Quiz Question 5
        ConversationSystem.DialogueNode quizQ5 = new ConversationSystem.DialogueNode(
            "Smart Businessman",
            """
            Question 5: What is the 50/30/20 rule?

            A) 50% needs, 30% wants, 20% savings
            B) 50% savings, 30% needs, 20% wants
            C) 50% wants, 30% needs, 20% savings
            D) 50% needs, 30% savings, 20% wants
            """
        );
        
        // Quiz Question 6 (Advanced)
        ConversationSystem.DialogueNode quizQ6 = new ConversationSystem.DialogueNode(
            "Smart Businessman",
            """
            Question 6: What is a credit score range?

            A) 200-500
            B) 300-850
            C) 400-900
            D) 500-1000
            """
        );
        
        // Quiz Question 7 (Advanced)
        ConversationSystem.DialogueNode quizQ7 = new ConversationSystem.DialogueNode(
            "Smart Businessman",
            """
            Question 7: What is the snowball method for debt payoff?

            A) Pay highest interest debts first
            B) Pay smallest debts first
            C) Pay all debts equally
            D) Only pay minimum payments
            """
        );
        
        // Quiz Question 8 (Advanced)
        ConversationSystem.DialogueNode quizQ8 = new ConversationSystem.DialogueNode(
            "Smart Businessman",
            """
            Question 8: What is the rule of 72?

            A) Save 72% of your income
            B) A way to estimate how long it takes to double your money
            C) Invest in 72 different stocks
            D) Withdraw money at age 72
            """
        );
        
        // Quiz Results - Create a dynamic result generator
        ConversationSystem.DialogueNode quizResults = new ConversationSystem.DialogueNode(
            "Smart Businessman",
            "Loading results...",
            (node) -> {
                // This runs when the node is entered - recalculate results
                System.out.println("\n=== QUIZ RESULTS ===");
                System.out.println("📊 SCORE: " + com.coincraft.game.adventure.models.CoinManager.getQuizScore() + "%");
                System.out.println("💰 CURRENT SMARTCOIN BALANCE: " + com.coincraft.game.adventure.models.CoinManager.getTotalCoins() + " coins");
                System.out.println("\n🎯 PERFORMANCE:");
                System.out.println("• Correct Answers: " + com.coincraft.game.adventure.models.CoinManager.getCorrectAnswers());
                System.out.println("• Wrong Answers: " + com.coincraft.game.adventure.models.CoinManager.getWrongAnswers());
                System.out.println("• Coins Earned: " + com.coincraft.game.adventure.models.CoinManager.getCoinsEarned());
                System.out.println("• Coins Lost: " + com.coincraft.game.adventure.models.CoinManager.getCoinsLost());
                System.out.println("\nGreat job! Your financial knowledge is growing. Keep learning and improving!");
            },
            null
        );
        
        // Coin Summary
        ConversationSystem.DialogueNode coinSummary = new ConversationSystem.DialogueNode(
            "Smart Businessman",
            com.coincraft.game.adventure.models.CoinManager.getCoinSummary()
        );
        
        // Quiz Difficulty Selection
        ConversationSystem.DialogueNode quizDifficulty = new ConversationSystem.DialogueNode(
            "Smart Businessman",
            """
            Choose your quiz difficulty:

            🟢 BEGINNER (5 questions)
            • Basic financial concepts
            • Perfect for newcomers
            • Earn up to 50 coins

            🔴 ADVANCED (8 questions)
            • Comprehensive financial knowledge
            • For experienced learners
            • Earn up to 80 coins
            """
        );
        
        // Main menu
        ConversationSystem.DialogueNode mainMenu = new ConversationSystem.DialogueNode(
            "Smart Businessman",
            "What would you like to do? I can test your knowledge or answer any questions you might have about business and finance."
        );
        
        // Business tips - Part 1
        ConversationSystem.DialogueNode businessTips = new ConversationSystem.DialogueNode(
            "Smart Businessman",
            "Here are business and financial tips from my experience:\n\n" +
            "💼 BUSINESS WISDOM:\n" +
            "• Cash flow is king (profit ≠ cash)\n" +
            "• Diversify your income streams\n" +
            "• Invest in yourself (best ROI)\n" +
            "• Network and build relationships\n" +
            "• Track key metrics\n" +
            "• Build systems that work without you\n\n" +
            "📊 FINANCIAL PLANNING:\n" +
            "• Set SMART financial goals\n" +
            "• Create multiple income sources\n" +
            "• Save for emergencies and opportunities\n" +
            "• Always have a backup plan\n" +
            "• Automate your finances"
        );
        
        // Business tips - Part 2 (Advanced)
        ConversationSystem.DialogueNode businessTipsAdvanced = new ConversationSystem.DialogueNode(
            "Smart Businessman",
            "Advanced business strategies:\n\n" +
            "🎯 ENTREPRENEURSHIP BASICS:\n" +
            "• Start with a side hustle while employed\n" +
            "• Validate your idea before investing\n" +
            "• Keep your day job until profitable\n" +
            "• Separate personal and business finances\n" +
            "• Track every expense for tax deductions\n\n" +
            "💰 INVESTMENT STRATEGIES:\n" +
            "• Max out employer 401(k) match\n" +
            "• Consider real estate investing\n" +
            "• Invest in index funds for growth\n" +
            "• Keep 6 months expenses in emergency fund\n" +
            "• Don't invest money you'll need within 5 years"
        );
        
        // Goodbye message
        ConversationSystem.DialogueNode goodbye = new ConversationSystem.DialogueNode(
            "Smart Businessman",
            "Remember, financial success comes from continuous learning and smart decision-making. Keep testing your knowledge, stay curious, and never stop learning. Your financial future is in your hands!"
        );
        
        // Build conversation tree
        welcome.addOption("I'm ready for the quiz!", quizIntro);
        welcome.addOption("Tell me about business tips", businessTips);
        welcome.addOption("I have questions first", mainMenu);
        
        quizIntro.addOption("Start the quiz now!", quizDifficulty);
        quizIntro.addOption("Tell me about business tips first", businessTips);
        quizIntro.addOption("I have questions", mainMenu);
        
        // Quiz difficulty options
        quizDifficulty.addOption("🟢 Beginner Quiz (5 questions)", quizQ1);
        quizDifficulty.addOption("🔴 Advanced Quiz (8 questions)", quizQ1);
        quizDifficulty.addOption("Tell me about business tips first", businessTips);
        quizDifficulty.addOption("I have questions", mainMenu);
        
        // Quiz Question 1 options
        quizQ1.addOption("A) To invest in stocks", quizQ2, (node) -> {
            System.out.println("❌ Wrong! Emergency funds are for unexpected expenses, not investing.");
            CoinManager.removeCoinsForWrongAnswer(1, "Emergency Fund Purpose");
        });
        quizQ1.addOption("B) To cover unexpected expenses without going into debt", quizQ2, (node) -> {
            System.out.println("✅ Correct! Emergency funds provide financial safety.");
            CoinManager.addCoinsForCorrectAnswer(1, "Emergency Fund Purpose");
        });
        quizQ1.addOption("C) To pay for vacations", quizQ2, (node) -> {
            System.out.println("❌ Wrong! Emergency funds are for unexpected expenses, not vacations.");
            CoinManager.removeCoinsForWrongAnswer(1, "Emergency Fund Purpose");
        });
        quizQ1.addOption("D) To buy luxury items", quizQ2, (node) -> {
            System.out.println("❌ Wrong! Emergency funds are for unexpected expenses, not luxury items.");
            CoinManager.removeCoinsForWrongAnswer(1, "Emergency Fund Purpose");
        });
        
        // Quiz Question 2 options
        quizQ2.addOption("A) 5-10%", quizQ3, (node) -> {
            System.out.println("❌ Too low! Most experts recommend 15-20%.");
            CoinManager.removeCoinsForWrongAnswer(2, "Savings Percentage");
        });
        quizQ2.addOption("B) 10-15%", quizQ3, (node) -> {
            System.out.println("⚠️ Close! 15-20% is the recommended range.");
            CoinManager.removeCoinsForWrongAnswer(2, "Savings Percentage");
        });
        quizQ2.addOption("C) 15-20%", quizQ3, (node) -> {
            System.out.println("✅ Correct! 15-20% is the recommended savings rate.");
            CoinManager.addCoinsForCorrectAnswer(2, "Savings Percentage");
        });
        quizQ2.addOption("D) 20-25%", quizQ3, (node) -> {
            System.out.println("✅ Excellent! Even better than the minimum recommendation.");
            CoinManager.addCoinsForCorrectAnswer(2, "Savings Percentage");
        });
        
        // Quiz Question 3 options
        quizQ3.addOption("A) Putting all money in one stock", quizQ4, (node) -> {
            System.out.println("❌ Wrong! That's the opposite of diversification.");
            CoinManager.removeCoinsForWrongAnswer(3, "Diversification");
        });
        quizQ3.addOption("B) Spreading investments across different assets", quizQ4, (node) -> {
            System.out.println("✅ Correct! Diversification reduces risk.");
            CoinManager.addCoinsForCorrectAnswer(3, "Diversification");
        });
        quizQ3.addOption("C) Only investing in bonds", quizQ4, (node) -> {
            System.out.println("❌ Wrong! That's not diversification.");
            CoinManager.removeCoinsForWrongAnswer(3, "Diversification");
        });
        quizQ3.addOption("D) Avoiding all investments", quizQ4, (node) -> {
            System.out.println("❌ Wrong! That's not investing at all.");
            CoinManager.removeCoinsForWrongAnswer(3, "Diversification");
        });
        
        // Quiz Question 4 options
        quizQ4.addOption("A) Interest that decreases over time", quizQ5, (node) -> {
            System.out.println("❌ Wrong! Compound interest grows over time.");
            CoinManager.removeCoinsForWrongAnswer(4, "Compound Interest");
        });
        quizQ4.addOption("B) Interest earned on both principal and previous interest", quizQ5, (node) -> {
            System.out.println("✅ Correct! Compound interest is powerful for wealth building.");
            CoinManager.addCoinsForCorrectAnswer(4, "Compound Interest");
        });
        quizQ4.addOption("C) Interest only on the original amount", quizQ5, (node) -> {
            System.out.println("❌ Wrong! That's simple interest, not compound.");
            CoinManager.removeCoinsForWrongAnswer(4, "Compound Interest");
        });
        quizQ4.addOption("D) Interest that changes daily", quizQ5, (node) -> {
            System.out.println("❌ Wrong! Compound interest is about growth over time.");
            CoinManager.removeCoinsForWrongAnswer(4, "Compound Interest");
        });
        
        // Quiz Question 5 options
        quizQ5.addOption("A) 50% needs, 30% wants, 20% savings", quizQ6, (node) -> {
            System.out.println("✅ Correct! The 50/30/20 rule is a popular budgeting method.");
            CoinManager.addCoinsForCorrectAnswer(5, "50/30/20 Rule");
        });
        quizQ5.addOption("B) 50% savings, 30% needs, 20% wants", quizQ6, (node) -> {
            System.out.println("❌ Wrong! That would be very difficult to achieve.");
            CoinManager.removeCoinsForWrongAnswer(5, "50/30/20 Rule");
        });
        quizQ5.addOption("C) 50% wants, 30% needs, 20% savings", quizQ6, (node) -> {
            System.out.println("❌ Wrong! That's not a sustainable budget.");
            CoinManager.removeCoinsForWrongAnswer(5, "50/30/20 Rule");
        });
        quizQ5.addOption("D) 50% needs, 30% savings, 20% wants", quizQ6, (node) -> {
            System.out.println("❌ Wrong! That's not the standard 50/30/20 rule.");
            CoinManager.removeCoinsForWrongAnswer(5, "50/30/20 Rule");
        });
        
        // Quiz Question 6 options
        quizQ6.addOption("A) 200-500", quizQ7, (node) -> {
            System.out.println("❌ Wrong! Credit scores don't go that low.");
            CoinManager.removeCoinsForWrongAnswer(6, "Credit Score Range");
        });
        quizQ6.addOption("B) 300-850", quizQ7, (node) -> {
            System.out.println("✅ Correct! Credit scores range from 300-850.");
            CoinManager.addCoinsForCorrectAnswer(6, "Credit Score Range");
        });
        quizQ6.addOption("C) 400-900", quizQ7, (node) -> {
            System.out.println("❌ Wrong! Credit scores don't go that high.");
            CoinManager.removeCoinsForWrongAnswer(6, "Credit Score Range");
        });
        quizQ6.addOption("D) 500-1000", quizQ7, (node) -> {
            System.out.println("❌ Wrong! Credit scores don't go that high.");
            CoinManager.removeCoinsForWrongAnswer(6, "Credit Score Range");
        });
        
        // Quiz Question 7 options
        quizQ7.addOption("A) Pay highest interest debts first", quizQ8, (node) -> {
            System.out.println("❌ Wrong! That's the avalanche method, not snowball.");
            CoinManager.removeCoinsForWrongAnswer(7, "Snowball Method");
        });
        quizQ7.addOption("B) Pay smallest debts first", quizQ8, (node) -> {
            System.out.println("✅ Correct! Snowball method pays smallest debts first for motivation.");
            CoinManager.addCoinsForCorrectAnswer(7, "Snowball Method");
        });
        quizQ7.addOption("C) Pay all debts equally", quizQ8, (node) -> {
            System.out.println("❌ Wrong! That's not the snowball method.");
            CoinManager.removeCoinsForWrongAnswer(7, "Snowball Method");
        });
        quizQ7.addOption("D) Only pay minimum payments", quizQ8, (node) -> {
            System.out.println("❌ Wrong! That won't help you get out of debt.");
            CoinManager.removeCoinsForWrongAnswer(7, "Snowball Method");
        });
        
        // Quiz Question 8 options
        quizQ8.addOption("A) Save 72% of your income", quizResults, (node) -> {
            System.out.println("❌ Wrong! That's not what the rule of 72 is about.");
            CoinManager.removeCoinsForWrongAnswer(8, "Rule of 72");
        });
        quizQ8.addOption("B) A way to estimate how long it takes to double your money", quizResults, (node) -> {
            System.out.println("✅ Correct! Divide 72 by your interest rate to estimate doubling time.");
            CoinManager.addCoinsForCorrectAnswer(8, "Rule of 72");
        });
        quizQ8.addOption("C) Invest in 72 different stocks", quizResults, (node) -> {
            System.out.println("❌ Wrong! That's not the rule of 72.");
            CoinManager.removeCoinsForWrongAnswer(8, "Rule of 72");
        });
        quizQ8.addOption("D) Withdraw money at age 72", quizResults, (node) -> {
            System.out.println("❌ Wrong! That's not what the rule of 72 is about.");
            CoinManager.removeCoinsForWrongAnswer(8, "Rule of 72");
        });
        
        // Quiz Results options
        quizResults.addOption("Take the quiz again", quizQ1);
        quizResults.addOption("Check my coin balance", coinSummary);
        quizResults.addOption("Learn more about business", businessTips);
        quizResults.addOption("I have other questions", mainMenu);
        quizResults.addOption("Thank you for the quiz", goodbye);
        
        // Coin Summary options
        coinSummary.addOption("Take the quiz again", quizQ1);
        coinSummary.addOption("Learn more about business", businessTips);
        coinSummary.addOption("I have other questions", mainMenu);
        coinSummary.addOption("Thank you", goodbye);
        
        mainMenu.addOption("I'm ready for the quiz!", quizIntro);
        mainMenu.addOption("Share your business wisdom", businessTips);
        mainMenu.addOption("Thank you for your time", goodbye);
        
        businessTips.addOption("Tell me more about advanced business strategies", businessTipsAdvanced);
        businessTips.addOption("I'm ready for the quiz now!", quizIntro);
        businessTips.addOption("I have more questions", mainMenu);
        businessTips.addOption("Thank you for the advice", goodbye);
        
        businessTipsAdvanced.addOption("I'm ready for the quiz now!", quizIntro);
        businessTipsAdvanced.addOption("I have more questions", mainMenu);
        businessTipsAdvanced.addOption("Thank you for the advice", goodbye);
        
        return welcome;
    }
}
