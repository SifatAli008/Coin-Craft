package com.coincraft.ui.components.parent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.coincraft.models.Task;
import com.coincraft.models.User;
import com.coincraft.services.FirebaseService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Family analytics dashboard component for Parent Dashboard
 * Features progress charts, spending analytics, and achievement tracking
 * Provides comprehensive insights into family financial learning progress
 */
public class FamilyAnalytics {
    private VBox root;
    private final FirebaseService firebaseService;
    
    // Real analytics data
    private List<User> children;
    private List<Task> allTasks;
    private Map<String, Integer> weeklyActivity;
    private Map<String, Double> spendingData;
    
    public FamilyAnalytics(User user) {
        this.firebaseService = FirebaseService.getInstance();
        loadRealData();
        initializeUI();
    }
    
    private void loadRealData() {
        // Load children data
        children = new ArrayList<>();
        List<User> allUsers = firebaseService.getAllUsers();
        for (User user : allUsers) {
            if (user.getRole() == com.coincraft.models.UserRole.CHILD) {
                children.add(user);
            }
        }
        
        // Load tasks data
        allTasks = firebaseService.loadAllTasks();
        
        // Calculate real analytics data
        calculateWeeklyActivity();
        calculateSpendingData();
    }
    
    private void calculateWeeklyActivity() {
        weeklyActivity = new HashMap<>();
        String[] weekDays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        
        // Initialize with zeros
        for (String day : weekDays) {
            weeklyActivity.put(day, 0);
        }
        
        // Calculate coins earned per day for the last 7 days
        LocalDateTime now = LocalDateTime.now();
        for (Task task : allTasks) {
            if (task.isCompleted() && task.getCompletedAt() != null) {
                LocalDateTime completedAt = task.getCompletedAt();
                
                // Check if task was completed in the last 7 days
                if (completedAt.isAfter(now.minusDays(7))) {
                    String dayOfWeek = completedAt.getDayOfWeek().toString().substring(0, 3);
                    String dayKey = dayOfWeek.equals("MON") ? "Mon" :
                                   dayOfWeek.equals("TUE") ? "Tue" :
                                   dayOfWeek.equals("WED") ? "Wed" :
                                   dayOfWeek.equals("THU") ? "Thu" :
                                   dayOfWeek.equals("FRI") ? "Fri" :
                                   dayOfWeek.equals("SAT") ? "Sat" : "Sun";
                    
                    int currentCoins = weeklyActivity.getOrDefault(dayKey, 0);
                    weeklyActivity.put(dayKey, currentCoins + task.getRewardCoins());
                }
            }
        }
    }
    
    private void calculateSpendingData() {
        spendingData = new HashMap<>();
        
        int learningRewards = 0;
        int challengeBonuses = 0;
        int realWorldTasks = 0;
        
        // Calculate spending by category based on task types
        for (Task task : allTasks) {
            if (task.isCompleted() && task.getRewardCoins() > 0) {
                switch (task.getType()) {
                    case LEARNING -> learningRewards += task.getRewardCoins();
                    case CHALLENGE -> challengeBonuses += task.getRewardCoins();
                    case CHORE, QUEST -> realWorldTasks += task.getRewardCoins();
                    default -> learningRewards += task.getRewardCoins();
                }
            }
        }
        
        // Add shop purchases (placeholder - would need shop transaction data)
        int shopPurchases = Math.max(0, getTotalFamilyCoins() - learningRewards - challengeBonuses - realWorldTasks);
        
        spendingData.put("Learning Rewards", (double) learningRewards);
        spendingData.put("Challenge Bonuses", (double) challengeBonuses);
        spendingData.put("Real-World Tasks", (double) realWorldTasks);
        spendingData.put("Shop Purchases", (double) shopPurchases);
    }
    
    private int getTotalFamilyCoins() {
        int total = 0;
        for (User child : children) {
            total += child.getSmartCoinBalance();
        }
        return total;
    }
    
    private void initializeUI() {
        root = new VBox(24);
        root.setPadding(new Insets(24));
        root.setAlignment(Pos.TOP_CENTER);
        
        createAnalyticsHeader();
        createChartsGrid();
        createInsightsSection();
    }
    
    private void createAnalyticsHeader() {
        VBox header = new VBox(8);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label("📊 Adventure Analytics Dashboard");
        titleLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label subtitleLabel = new Label("Track your adventurers' financial learning journey with detailed insights and progress metrics");
        subtitleLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        subtitleLabel.setWrapText(true);
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        root.getChildren().add(header);
    }
    
    private void createChartsGrid() {
        GridPane chartsGrid = new GridPane();
        chartsGrid.setHgap(24);
        chartsGrid.setVgap(24);
        chartsGrid.setAlignment(Pos.CENTER);
        
        // Weekly Activity Chart
        VBox activityChartCard = createActivityChart();
        GridPane.setConstraints(activityChartCard, 0, 0);
        
        // Spending Distribution Chart
        VBox spendingChartCard = createSpendingChart();
        GridPane.setConstraints(spendingChartCard, 1, 0);
        
        // Progress Overview Cards
        VBox progressCards = createProgressCards();
        GridPane.setConstraints(progressCards, 0, 1, 2, 1);
        
        chartsGrid.getChildren().addAll(activityChartCard, spendingChartCard, progressCards);
        root.getChildren().add(chartsGrid);
    }
    
    private VBox createActivityChart() {
        VBox chartCard = new VBox(16);
        chartCard.setPadding(new Insets(20));
        chartCard.setPrefWidth(400);
        chartCard.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 16;" +
            "-fx-border-radius: 16;" +
            "-fx-border-color: rgba(255, 255, 255, 0.4);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 10);"
        );
        
        Label chartTitle = new Label("Weekly Activity");
        chartTitle.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        // Create area chart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("SmartCoins Earned");
        
        AreaChart<String, Number> areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setTitle("Daily Progress This Week");
        areaChart.setPrefHeight(250);
        areaChart.setLegendVisible(false);
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("SmartCoins");
        
        // Use real data from weeklyActivity map
        String[] weekDays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (String day : weekDays) {
            int coins = weeklyActivity.getOrDefault(day, 0);
            series.getData().add(new XYChart.Data<>(day, coins));
        }
        
        areaChart.getData().add(series);
        
        // Style the chart
        areaChart.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        chartCard.getChildren().addAll(chartTitle, areaChart);
        return chartCard;
    }
    
    private VBox createSpendingChart() {
        VBox chartCard = new VBox(16);
        chartCard.setPadding(new Insets(20));
        chartCard.setPrefWidth(400);
        chartCard.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 16;" +
            "-fx-border-radius: 16;" +
            "-fx-border-color: rgba(255, 255, 255, 0.4);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 10);"
        );
        
        Label chartTitle = new Label("SmartCoin Distribution");
        chartTitle.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        // Create pie chart
        PieChart pieChart = new PieChart();
        pieChart.setPrefHeight(250);
        pieChart.setLegendVisible(true);
        
        // Use real spending data
        for (Map.Entry<String, Double> entry : spendingData.entrySet()) {
            if (entry.getValue() > 0) { // Only show categories with actual spending
                PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
                pieChart.getData().add(slice);
            }
        }
        
        // Style the chart
        pieChart.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        chartCard.getChildren().addAll(chartTitle, pieChart);
        return chartCard;
    }
    
    private VBox createProgressCards() {
        VBox progressSection = new VBox(16);
        
        Label sectionTitle = new Label("Family Progress Overview");
        sectionTitle.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        HBox cardsRow = new HBox(16);
        cardsRow.setAlignment(Pos.CENTER);
        
        // Calculate real statistics
        int totalCoins = getTotalFamilyCoins();
        int completedTasks = getCompletedTasksCount();
        int longestStreak = getLongestLearningStreak();
        double achievementRate = getAchievementRate();
        
        // Total SmartCoins Earned
        VBox totalCoinsCard = createStatCard("💰", "Total SmartCoins", String.valueOf(totalCoins), 
            "+" + getWeeklyCoinsChange() + " this week", "#4CAF50");
        
        // Tasks Completed
        VBox tasksCard = createStatCard("✅", "Tasks Completed", String.valueOf(completedTasks), 
            "+" + getWeeklyTasksChange() + " this week", "#2196F3");
        
        // Learning Streak
        VBox streakCard = createStatCard("🔥", "Learning Streak", longestStreak + " days", 
            longestStreak > 7 ? "Excellent!" : "Keep going!", "#FA8A00");
        
        // Achievement Rate
        VBox achievementCard = createStatCard("🏆", "Achievement Rate", String.format("%.0f%%", achievementRate), 
            achievementRate > 80 ? "Outstanding!" : "Good progress!", "#9C27B0");
        
        cardsRow.getChildren().addAll(totalCoinsCard, tasksCard, streakCard, achievementCard);
        
        progressSection.getChildren().addAll(sectionTitle, cardsRow);
        return progressSection;
    }
    
    private VBox createStatCard(String icon, String title, String value, String change, String color) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(16));
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(180);
        card.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: " + color + ";" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle(
            "-fx-font-size: 24px;"
        );
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        titleLabel.setWrapText(true);
        
        Label valueLabel = new Label(value);
        valueLabel.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: " + color + ";" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label changeLabel = new Label(change);
        changeLabel.setStyle(
            "-fx-font-size: 10px;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        changeLabel.setWrapText(true);
        
        card.getChildren().addAll(iconLabel, titleLabel, valueLabel, changeLabel);
        return card;
    }
    
    private void createInsightsSection() {
        VBox insightsSection = new VBox(16);
        insightsSection.setPadding(new Insets(20));
        insightsSection.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 16;" +
            "-fx-border-radius: 16;" +
            "-fx-border-color: rgba(255, 255, 255, 0.4);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 10);"
        );
        
        Label insightsTitle = new Label("💡 Smart Insights");
        insightsTitle.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        VBox insightsList = new VBox(12);
        
        // Generate real insights based on data
        List<String> insights = generateRealInsights();
        for (String insight : insights) {
            String[] parts = insight.split("\\|");
            if (parts.length >= 2) {
                insightsList.getChildren().add(createInsightItem(parts[0], parts[1]));
            }
        }
        
        insightsSection.getChildren().addAll(insightsTitle, insightsList);
        root.getChildren().add(insightsSection);
    }
    
    private HBox createInsightItem(String icon, String text) {
        HBox item = new HBox(12);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(8));
        item.setStyle(
            "-fx-background-color: rgba(76, 175, 80, 0.1);" +
            "-fx-background-radius: 8;"
        );
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 16px;");
        
        Label textLabel = new Label(text);
        textLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        textLabel.setWrapText(true);
        
        item.getChildren().addAll(iconLabel, textLabel);
        return item;
    }
    
    private int getCompletedTasksCount() {
        int count = 0;
        for (Task task : allTasks) {
            if (task.isCompleted()) {
                count++;
            }
        }
        return count;
    }
    
    private int getLongestLearningStreak() {
        int maxStreak = 0;
        for (User child : children) {
            if (child.getDailyStreaks() > maxStreak) {
                maxStreak = child.getDailyStreaks();
            }
        }
        return maxStreak;
    }
    
    private double getAchievementRate() {
        if (allTasks.isEmpty()) return 0.0;
        
        int completed = 0;
        for (Task task : allTasks) {
            if (task.isCompleted()) {
                completed++;
            }
        }
        return (double) completed / allTasks.size() * 100.0;
    }
    
    private int getWeeklyCoinsChange() {
        int weeklyCoins = 0;
        LocalDateTime now = LocalDateTime.now();
        
        for (Task task : allTasks) {
            if (task.isCompleted() && task.getCompletedAt() != null) {
                if (task.getCompletedAt().isAfter(now.minusDays(7))) {
                    weeklyCoins += task.getRewardCoins();
                }
            }
        }
        return weeklyCoins;
    }
    
    private int getWeeklyTasksChange() {
        int weeklyTasks = 0;
        LocalDateTime now = LocalDateTime.now();
        
        for (Task task : allTasks) {
            if (task.isCompleted() && task.getCompletedAt() != null) {
                if (task.getCompletedAt().isAfter(now.minusDays(7))) {
                    weeklyTasks++;
                }
            }
        }
        return weeklyTasks;
    }
    
    private List<String> generateRealInsights() {
        List<String> insights = new ArrayList<>();
        
        // Insight 1: Performance comparison
        int weeklyCoins = getWeeklyCoinsChange();
        int weeklyTasks = getWeeklyTasksChange();
        if (weeklyCoins > 0 && weeklyTasks > 0) {
            insights.add("🎯|Your adventurers earned " + weeklyCoins + " SmartCoins this week across " + weeklyTasks + " completed tasks!");
        }
        
        // Insight 2: Task type analysis
        Map<String, Integer> taskTypeCounts = new HashMap<>();
        for (Task task : allTasks) {
            if (task.isCompleted()) {
                String type = task.getType().toString();
                taskTypeCounts.put(type, taskTypeCounts.getOrDefault(type, 0) + 1);
            }
        }
        
        if (!taskTypeCounts.isEmpty()) {
            String mostPopularType = taskTypeCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("LEARNING");
            insights.add("📈|" + mostPopularType + " tasks are most popular - consider adding more " + mostPopularType.toLowerCase() + " content!");
        }
        
        // Insight 3: Learning streak
        int maxStreak = getLongestLearningStreak();
        if (maxStreak > 0) {
            insights.add("💪|Your adventurers have maintained a " + maxStreak + "-day learning streak - excellent consistency!");
        }
        
        // Insight 4: Achievement rate
        double achievementRate = getAchievementRate();
        if (achievementRate > 0) {
            String rateMessage = achievementRate > 80 ? "Outstanding achievement rate!" : 
                               achievementRate > 60 ? "Good progress on tasks!" : 
                               "Keep encouraging your adventurers!";
            insights.add("🏆|" + String.format("%.0f%%", achievementRate) + " task completion rate - " + rateMessage);
        }
        
        // Insight 5: Family size and activity
        if (children.size() > 1) {
            insights.add("👥|Managing " + children.size() + " adventurers - great family engagement!");
        }
        
        return insights;
    }
    
    public VBox getRoot() {
        return root;
    }
    
    public void refreshData() {
        loadRealData();
        initializeUI();
    }
}
