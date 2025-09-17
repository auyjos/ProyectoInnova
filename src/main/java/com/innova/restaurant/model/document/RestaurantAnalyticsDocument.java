package com.innova.restaurant.model.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Documento RestaurantAnalytics para MongoDB
 * Almacena métricas y análisis de performance de restaurantes
 */
@Document(collection = "restaurant_analytics")
public class RestaurantAnalyticsDocument {

    @Id
    private String id;

    // Referencia al restaurante (ID de PostgreSQL)
    @Field("restaurant_id")
    private Long restaurantId;

    // Período de análisis
    @Field("period_type")
    private String periodType; // "daily", "weekly", "monthly", "yearly"

    @Field("period_start")
    private LocalDateTime periodStart;

    @Field("period_end")
    private LocalDateTime periodEnd;

    // Métricas de reservas
    @Field("reservation_metrics")
    private ReservationMetrics reservationMetrics;

    // Métricas de reviews
    @Field("review_metrics")
    private ReviewMetrics reviewMetrics;

    // Métricas de mesa
    @Field("table_metrics")
    private TableMetrics tableMetrics;

    // Métricas financieras (opcional)
    @Field("financial_metrics")
    private FinancialMetrics financialMetrics;

    // Métricas de popularidad
    @Field("popularity_metrics")
    private PopularityMetrics popularityMetrics;

    // Métricas de horarios
    @Field("time_metrics")
    private Map<String, Integer> timeMetrics; // "hour_0", "hour_1", etc.

    // Comparación con período anterior
    @Field("comparison_metrics")
    private ComparisonMetrics comparisonMetrics;

    // Timestamps de actualización
    @Field("last_updated")
    private LocalDateTime lastUpdated;

    @Field("calculated_at")
    private LocalDateTime calculatedAt;

    // Clase interna para métricas de reservas
    public static class ReservationMetrics {
        private Integer totalReservations;
        private Integer confirmedReservations;
        private Integer cancelledReservations;
        private Integer noShowReservations;
        private Double averagePartySize;
        private Double cancellationRate;
        private Double noShowRate;
        private Integer peakHour;
        private Map<String, Integer> reservationsByDay; // "monday", "tuesday", etc.

        // Constructores
        public ReservationMetrics() {}

        // Getters y Setters
        public Integer getTotalReservations() { return totalReservations; }
        public void setTotalReservations(Integer totalReservations) { this.totalReservations = totalReservations; }

        public Integer getConfirmedReservations() { return confirmedReservations; }
        public void setConfirmedReservations(Integer confirmedReservations) { this.confirmedReservations = confirmedReservations; }

        public Integer getCancelledReservations() { return cancelledReservations; }
        public void setCancelledReservations(Integer cancelledReservations) { this.cancelledReservations = cancelledReservations; }

        public Integer getNoShowReservations() { return noShowReservations; }
        public void setNoShowReservations(Integer noShowReservations) { this.noShowReservations = noShowReservations; }

        public Double getAveragePartySize() { return averagePartySize; }
        public void setAveragePartySize(Double averagePartySize) { this.averagePartySize = averagePartySize; }

        public Double getCancellationRate() { return cancellationRate; }
        public void setCancellationRate(Double cancellationRate) { this.cancellationRate = cancellationRate; }

        public Double getNoShowRate() { return noShowRate; }
        public void setNoShowRate(Double noShowRate) { this.noShowRate = noShowRate; }

        public Integer getPeakHour() { return peakHour; }
        public void setPeakHour(Integer peakHour) { this.peakHour = peakHour; }

        public Map<String, Integer> getReservationsByDay() { return reservationsByDay; }
        public void setReservationsByDay(Map<String, Integer> reservationsByDay) { this.reservationsByDay = reservationsByDay; }
    }

    // Clase interna para métricas de reviews
    public static class ReviewMetrics {
        private Integer totalReviews;
        private Double averageRating;
        private Map<String, Double> averageRatingsByCategory; // "food", "service", "ambiance", etc.
        private Integer positiveReviews; // Rating >= 4
        private Integer negativeReviews; // Rating <= 2
        private Double responseRate; // Porcentaje de reviews con respuesta
        private Double averageResponseTime; // En horas
        private List<String> topKeywords; // Keywords más mencionados

        // Constructores
        public ReviewMetrics() {}

        // Getters y Setters
        public Integer getTotalReviews() { return totalReviews; }
        public void setTotalReviews(Integer totalReviews) { this.totalReviews = totalReviews; }

        public Double getAverageRating() { return averageRating; }
        public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

        public Map<String, Double> getAverageRatingsByCategory() { return averageRatingsByCategory; }
        public void setAverageRatingsByCategory(Map<String, Double> averageRatingsByCategory) { this.averageRatingsByCategory = averageRatingsByCategory; }

        public Integer getPositiveReviews() { return positiveReviews; }
        public void setPositiveReviews(Integer positiveReviews) { this.positiveReviews = positiveReviews; }

        public Integer getNegativeReviews() { return negativeReviews; }
        public void setNegativeReviews(Integer negativeReviews) { this.negativeReviews = negativeReviews; }

        public Double getResponseRate() { return responseRate; }
        public void setResponseRate(Double responseRate) { this.responseRate = responseRate; }

        public Double getAverageResponseTime() { return averageResponseTime; }
        public void setAverageResponseTime(Double averageResponseTime) { this.averageResponseTime = averageResponseTime; }

        public List<String> getTopKeywords() { return topKeywords; }
        public void setTopKeywords(List<String> topKeywords) { this.topKeywords = topKeywords; }
    }

    // Clase interna para métricas de mesas
    public static class TableMetrics {
        private Double occupancyRate;
        private Double turnoverRate;
        private Integer averageSeatingTime; // En minutos
        private Map<String, Double> occupancyByTableSize; // "2_seats", "4_seats", etc.
        private Integer mostPopularTableSize;

        // Constructores
        public TableMetrics() {}

        // Getters y Setters
        public Double getOccupancyRate() { return occupancyRate; }
        public void setOccupancyRate(Double occupancyRate) { this.occupancyRate = occupancyRate; }

        public Double getTurnoverRate() { return turnoverRate; }
        public void setTurnoverRate(Double turnoverRate) { this.turnoverRate = turnoverRate; }

        public Integer getAverageSeatingTime() { return averageSeatingTime; }
        public void setAverageSeatingTime(Integer averageSeatingTime) { this.averageSeatingTime = averageSeatingTime; }

        public Map<String, Double> getOccupancyByTableSize() { return occupancyByTableSize; }
        public void setOccupancyByTableSize(Map<String, Double> occupancyByTableSize) { this.occupancyByTableSize = occupancyByTableSize; }

        public Integer getMostPopularTableSize() { return mostPopularTableSize; }
        public void setMostPopularTableSize(Integer mostPopularTableSize) { this.mostPopularTableSize = mostPopularTableSize; }
    }

    // Clase interna para métricas financieras
    public static class FinancialMetrics {
        private Double totalRevenue;
        private Double averageRevenuePerReservation;
        private Double revenueGrowthRate;
        private Map<String, Double> revenueByDay;

        // Constructores
        public FinancialMetrics() {}

        // Getters y Setters
        public Double getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }

        public Double getAverageRevenuePerReservation() { return averageRevenuePerReservation; }
        public void setAverageRevenuePerReservation(Double averageRevenuePerReservation) { this.averageRevenuePerReservation = averageRevenuePerReservation; }

        public Double getRevenueGrowthRate() { return revenueGrowthRate; }
        public void setRevenueGrowthRate(Double revenueGrowthRate) { this.revenueGrowthRate = revenueGrowthRate; }

        public Map<String, Double> getRevenueByDay() { return revenueByDay; }
        public void setRevenueByDay(Map<String, Double> revenueByDay) { this.revenueByDay = revenueByDay; }
    }

    // Clase interna para métricas de popularidad
    public static class PopularityMetrics {
        private Integer profileViews;
        private Integer searchAppearances;
        private Integer clickThroughRate;
        private Integer bookmarkCount;
        private Integer shareCount;

        // Constructores
        public PopularityMetrics() {}

        // Getters y Setters
        public Integer getProfileViews() { return profileViews; }
        public void setProfileViews(Integer profileViews) { this.profileViews = profileViews; }

        public Integer getSearchAppearances() { return searchAppearances; }
        public void setSearchAppearances(Integer searchAppearances) { this.searchAppearances = searchAppearances; }

        public Integer getClickThroughRate() { return clickThroughRate; }
        public void setClickThroughRate(Integer clickThroughRate) { this.clickThroughRate = clickThroughRate; }

        public Integer getBookmarkCount() { return bookmarkCount; }
        public void setBookmarkCount(Integer bookmarkCount) { this.bookmarkCount = bookmarkCount; }

        public Integer getShareCount() { return shareCount; }
        public void setShareCount(Integer shareCount) { this.shareCount = shareCount; }
    }

    // Clase interna para comparación con período anterior
    public static class ComparisonMetrics {
        private Double reservationGrowthRate;
        private Double ratingGrowthRate;
        private Double reviewGrowthRate;
        private Double revenueGrowthRate;
        private String trend; // "improving", "declining", "stable"

        // Constructores
        public ComparisonMetrics() {}

        // Getters y Setters
        public Double getReservationGrowthRate() { return reservationGrowthRate; }
        public void setReservationGrowthRate(Double reservationGrowthRate) { this.reservationGrowthRate = reservationGrowthRate; }

        public Double getRatingGrowthRate() { return ratingGrowthRate; }
        public void setRatingGrowthRate(Double ratingGrowthRate) { this.ratingGrowthRate = ratingGrowthRate; }

        public Double getReviewGrowthRate() { return reviewGrowthRate; }
        public void setReviewGrowthRate(Double reviewGrowthRate) { this.reviewGrowthRate = reviewGrowthRate; }

        public Double getRevenueGrowthRate() { return revenueGrowthRate; }
        public void setRevenueGrowthRate(Double revenueGrowthRate) { this.revenueGrowthRate = revenueGrowthRate; }

        public String getTrend() { return trend; }
        public void setTrend(String trend) { this.trend = trend; }
    }

    // Constructores
    public RestaurantAnalyticsDocument() {
        this.calculatedAt = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
    }

    public RestaurantAnalyticsDocument(Long restaurantId, String periodType, 
                                      LocalDateTime periodStart, LocalDateTime periodEnd) {
        this();
        this.restaurantId = restaurantId;
        this.periodType = periodType;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }

    public String getPeriodType() { return periodType; }
    public void setPeriodType(String periodType) { this.periodType = periodType; }

    public LocalDateTime getPeriodStart() { return periodStart; }
    public void setPeriodStart(LocalDateTime periodStart) { this.periodStart = periodStart; }

    public LocalDateTime getPeriodEnd() { return periodEnd; }
    public void setPeriodEnd(LocalDateTime periodEnd) { this.periodEnd = periodEnd; }

    public ReservationMetrics getReservationMetrics() { return reservationMetrics; }
    public void setReservationMetrics(ReservationMetrics reservationMetrics) { this.reservationMetrics = reservationMetrics; }

    public ReviewMetrics getReviewMetrics() { return reviewMetrics; }
    public void setReviewMetrics(ReviewMetrics reviewMetrics) { this.reviewMetrics = reviewMetrics; }

    public TableMetrics getTableMetrics() { return tableMetrics; }
    public void setTableMetrics(TableMetrics tableMetrics) { this.tableMetrics = tableMetrics; }

    public FinancialMetrics getFinancialMetrics() { return financialMetrics; }
    public void setFinancialMetrics(FinancialMetrics financialMetrics) { this.financialMetrics = financialMetrics; }

    public PopularityMetrics getPopularityMetrics() { return popularityMetrics; }
    public void setPopularityMetrics(PopularityMetrics popularityMetrics) { this.popularityMetrics = popularityMetrics; }

    public Map<String, Integer> getTimeMetrics() { return timeMetrics; }
    public void setTimeMetrics(Map<String, Integer> timeMetrics) { this.timeMetrics = timeMetrics; }

    public ComparisonMetrics getComparisonMetrics() { return comparisonMetrics; }
    public void setComparisonMetrics(ComparisonMetrics comparisonMetrics) { this.comparisonMetrics = comparisonMetrics; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    public LocalDateTime getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(LocalDateTime calculatedAt) { this.calculatedAt = calculatedAt; }
}