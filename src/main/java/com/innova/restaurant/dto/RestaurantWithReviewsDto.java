package com.innova.restaurant.dto;

import java.util.List;
import java.util.Map;

import com.innova.restaurant.model.document.ReviewDocument;
import com.innova.restaurant.model.entity.Restaurant;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO que combina información de Restaurant (PostgreSQL) con Reviews (MongoDB)
 * Permite obtener restaurantes con sus reviews integrados en una sola respuesta
 */
@Schema(description = "Restaurante con sus reviews integrados desde MongoDB")
public class RestaurantWithReviewsDto {

    @Schema(description = "Información básica del restaurante")
    private Restaurant restaurant;

    @Schema(description = "Lista de reviews del restaurante desde MongoDB")
    private List<ReviewDocument> reviews;

    @Schema(description = "Estadísticas calculadas de las reviews")
    private ReviewStats reviewStats;

    @Schema(description = "Metadatos de paginación para las reviews")
    private ReviewPagination reviewPagination;

    // Constructor por defecto
    public RestaurantWithReviewsDto() {}

    // Constructor completo
    public RestaurantWithReviewsDto(Restaurant restaurant, List<ReviewDocument> reviews, 
                                   ReviewStats reviewStats, ReviewPagination reviewPagination) {
        this.restaurant = restaurant;
        this.reviews = reviews;
        this.reviewStats = reviewStats;
        this.reviewPagination = reviewPagination;
    }

    // Constructor sin paginación (para listas completas)
    public RestaurantWithReviewsDto(Restaurant restaurant, List<ReviewDocument> reviews, ReviewStats reviewStats) {
        this.restaurant = restaurant;
        this.reviews = reviews;
        this.reviewStats = reviewStats;
    }

    // Getters y Setters
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<ReviewDocument> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDocument> reviews) {
        this.reviews = reviews;
    }

    public ReviewStats getReviewStats() {
        return reviewStats;
    }

    public void setReviewStats(ReviewStats reviewStats) {
        this.reviewStats = reviewStats;
    }

    public ReviewPagination getReviewPagination() {
        return reviewPagination;
    }

    public void setReviewPagination(ReviewPagination reviewPagination) {
        this.reviewPagination = reviewPagination;
    }

    /**
     * Clase anidada para estadísticas de reviews
     */
    @Schema(description = "Estadísticas calculadas de las reviews del restaurante")
    public static class ReviewStats {
        
        @Schema(description = "Número total de reviews")
        private long totalReviews;
        
        @Schema(description = "Rating promedio general")
        private double averageRating;
        
        @Schema(description = "Distribución de ratings (1-5 estrellas)")
        private Map<Integer, Long> ratingDistribution;
        
        @Schema(description = "Ratings promedio por categoría detallada")
        private Map<String, Double> averageDetailedRatings;

        // Constructor por defecto
        public ReviewStats() {}

        // Constructor
        public ReviewStats(long totalReviews, double averageRating, 
                          Map<Integer, Long> ratingDistribution, 
                          Map<String, Double> averageDetailedRatings) {
            this.totalReviews = totalReviews;
            this.averageRating = averageRating;
            this.ratingDistribution = ratingDistribution;
            this.averageDetailedRatings = averageDetailedRatings;
        }

        // Getters y Setters
        public long getTotalReviews() {
            return totalReviews;
        }

        public void setTotalReviews(long totalReviews) {
            this.totalReviews = totalReviews;
        }

        public double getAverageRating() {
            return averageRating;
        }

        public void setAverageRating(double averageRating) {
            this.averageRating = averageRating;
        }

        public Map<Integer, Long> getRatingDistribution() {
            return ratingDistribution;
        }

        public void setRatingDistribution(Map<Integer, Long> ratingDistribution) {
            this.ratingDistribution = ratingDistribution;
        }

        public Map<String, Double> getAverageDetailedRatings() {
            return averageDetailedRatings;
        }

        public void setAverageDetailedRatings(Map<String, Double> averageDetailedRatings) {
            this.averageDetailedRatings = averageDetailedRatings;
        }
    }

    /**
     * Clase anidada para metadatos de paginación de reviews
     */
    @Schema(description = "Metadatos de paginación para las reviews")
    public static class ReviewPagination {
        
        @Schema(description = "Página actual (0-indexed)")
        private int currentPage;
        
        @Schema(description = "Tamaño de página")
        private int pageSize;
        
        @Schema(description = "Total de páginas")
        private int totalPages;
        
        @Schema(description = "Total de elementos")
        private long totalElements;
        
        @Schema(description = "Indica si hay página siguiente")
        private boolean hasNext;
        
        @Schema(description = "Indica si hay página anterior")
        private boolean hasPrevious;

        // Constructor por defecto
        public ReviewPagination() {}

        // Constructor
        public ReviewPagination(int currentPage, int pageSize, int totalPages, 
                               long totalElements, boolean hasNext, boolean hasPrevious) {
            this.currentPage = currentPage;
            this.pageSize = pageSize;
            this.totalPages = totalPages;
            this.totalElements = totalElements;
            this.hasNext = hasNext;
            this.hasPrevious = hasPrevious;
        }

        // Getters y Setters
        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public long getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(long totalElements) {
            this.totalElements = totalElements;
        }

        public boolean isHasNext() {
            return hasNext;
        }

        public void setHasNext(boolean hasNext) {
            this.hasNext = hasNext;
        }

        public boolean isHasPrevious() {
            return hasPrevious;
        }

        public void setHasPrevious(boolean hasPrevious) {
            this.hasPrevious = hasPrevious;
        }
    }
}