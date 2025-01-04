package dev.dini.Employee_Payroll_System.performance;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerformanceReviewServiceImpl implements PerformanceReviewService{

    private final PerformanceReviewRepository performanceReviewRepository;

    public PerformanceReviewServiceImpl(PerformanceReviewRepository performanceReviewRepository) {
        this.performanceReviewRepository = performanceReviewRepository;
    }

    @Override
    public List<PerformanceReview> getAllReviewsForEmployee(Integer employeeId) {
        return performanceReviewRepository.findByEmployeeEmployeeId(employeeId);
    }

    @Override
    public PerformanceReview getReviewById(Integer reviewId) {
        return performanceReviewRepository.findById(reviewId).orElse(null);
    }

    @Override
    public PerformanceReview createReview(PerformanceReview performanceReview) {
        return performanceReviewRepository.save(performanceReview);
    }

    @Override
    public PerformanceReview updateReview(Integer reviewId, PerformanceReview performanceReview) {
        if (performanceReviewRepository.existsById(reviewId)) {
            performanceReview.setReviewId(reviewId);
            return performanceReviewRepository.save(performanceReview);
        }
        return null;
    }

    @Override
    public void deleteReview(Integer reviewId) {
        if (performanceReviewRepository.existsById(reviewId)) {
            performanceReviewRepository.deleteById(reviewId);
        }
    }
}
