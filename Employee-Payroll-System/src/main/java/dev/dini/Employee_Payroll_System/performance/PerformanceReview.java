package dev.dini.Employee_Payroll_System.performance;

import dev.dini.Employee_Payroll_System.employees.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "performance_reviews")
public class PerformanceReview {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer reviewId;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private LocalDate reviewDate;

    // Add reviewPeriod fields for start and end dates
    private LocalDate reviewPeriodStart;
    private LocalDate reviewPeriodEnd;

    @Enumerated(EnumType.STRING)
    private PerformanceRating performanceRating;

    private String reviewerComments;
    private String employeeComments;
    private String developmentGoals;

    public LocalDate getReviewPeriodStart() {
        return reviewPeriodStart;
    }

    public void setReviewPeriodStart(LocalDate reviewPeriodStart) {
        this.reviewPeriodStart = reviewPeriodStart;
    }

    public LocalDate getReviewPeriodEnd() {
        return reviewPeriodEnd;
    }

    public void setReviewPeriodEnd(LocalDate reviewPeriodEnd) {
        this.reviewPeriodEnd = reviewPeriodEnd;
    }

    // Other getters and setters remain the same
    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public String getReviewerComments() {
        return reviewerComments;
    }

    public String getEmployeeComments() {
        return employeeComments;
    }

    public String getDevelopmentGoals() {
        return developmentGoals;
    }

    public PerformanceRating getPerformanceRating() {
        return performanceRating;
    }

    public void setPerformanceRating(PerformanceRating performanceRating) {
        this.performanceRating = performanceRating;
    }

    public void setReviewerComments(String reviewerComments) {
        this.reviewerComments = reviewerComments;
    }

    public void setEmployeeComments(String employeeComments) {
        this.employeeComments = employeeComments;
    }

    public void setDevelopmentGoals(String developmentGoals) {
        this.developmentGoals = developmentGoals;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}



