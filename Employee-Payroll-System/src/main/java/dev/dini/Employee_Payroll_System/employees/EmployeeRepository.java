package dev.dini.Employee_Payroll_System.employees;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findEmployeeByEmployeeId(Integer employeeId);
    List<Employee> findByPosition(String position);
    List<Employee> findByFirstName(String firstName);
    List<Employee> findByLastName(String lastName);
    Optional<Employee> findByEmail(String email);
    List<Employee> findByFirstNameAndLastName(String firstName, String lastName);
    List<Employee> findByPositionAndEmailLike(String position, String emailDomain);
    List<Employee> findBySalaryGreaterThan(double minSalary);
}