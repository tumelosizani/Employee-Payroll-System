package dev.dini.taxservice.deduction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeductionRepository extends JpaRepository<Deductions, Integer> {
}
