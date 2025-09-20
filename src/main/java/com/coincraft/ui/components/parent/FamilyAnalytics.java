package com.coincraft.ui.components.parent;

import java.util.List;

import com.coincraft.models.User;

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
    private User currentUser;
    
    // Mock analytics data
    private List<String> weekDays = List.of("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
    private List<Integer> weeklyActivity = List.of(15, 23, 18, 32, 28, 35, 42);
    private List<String> spendingCategories = List.of("Learning Rewards", "Challenge Bonuses", "Real-World Tasks", "Shop Purchases");
    private List<Double> spendingData = List.of(35.0, 25.0, 30.0, 10.0);
    
    public FamilyAnalytics(User user) {
        this.currentUser = user;
        initializeUI();
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
        
        for (int i = 0; i < weekDays.size(); i++) {
            series.getData().add(new XYChart.Data<>(weekDays.get(i), weeklyActivity.get(i)));
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
        
        for (int i = 0; i < spendingCategories.size(); i++) {
            PieChart.Data slice = new PieChart.Data(spendingCategories.get(i), spendingData.get(i));
            pieChart.getData().add(slice);
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
        
        // Total SmartCoins Earned
        VBox totalCoinsCard = createStatCard("💰", "Total SmartCoins", "2,450", "+15% this week", "#4CAF50");
        
        // Tasks Completed
        VBox tasksCard = createStatCard("✅", "Tasks Completed", "47", "+8 this week", "#2196F3");
        
        // Learning Streak
        VBox streakCard = createStatCard("🔥", "Learning Streak", "12 days", "Personal best!", "#FF9800");
        
        // Achievement Rate
        VBox achievementCard = createStatCard("🏆", "Achievement Rate", "85%", "+5% improvement", "#9C27B0");
        
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
            "-fx-text-fill: #4CAF50;" +
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
        
        insightsList.getChildren().addAll(
            createInsightItem("🎯", "Your adventurers are performing 15% above average this month!"),
            createInsightItem("📈", "Learning quests show the highest engagement - consider adding more educational content."),
            createInsightItem("💪", "Your adventurers have maintained a 12-day learning streak - excellent consistency!"),
            createInsightItem("🏆", "Achievement completion rate has improved by 8% this week.")
        );
        
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
    
    public VBox getRoot() {
        return root;
    }
}
