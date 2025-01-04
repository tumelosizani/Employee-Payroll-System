package dev.dini.employee.payroll.system.performance;

import java.util.List;

public interface PerformanceReviewService {
    List<PerformanceReview> getAllReviewsForEmployee(Integer employeeId);
    PerformanceReview getReviewById(Integer reviewId);
    PerformanceReview createReview(PerformanceReview performanceReview);
    PerformanceReview updateReview(Integer reviewId, PerformanceReview performanceReview);
    void deleteReview(Integer reviewId);
}
