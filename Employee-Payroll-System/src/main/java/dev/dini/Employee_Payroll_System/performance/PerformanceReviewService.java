package dev.dini.Employee_Payroll_System.performance;

import java.util.List;

public interface PerformanceReviewService {
    List<PerformanceReview> getAllReviewsForEmployee(Integer employeeId);
    PerformanceReview getReviewById(Integer reviewId);
    PerformanceReview createReview(PerformanceReview performanceReview);
    PerformanceReview updateReview(Integer reviewId, PerformanceReview performanceReview);
    void deleteReview(Integer reviewId);
}
