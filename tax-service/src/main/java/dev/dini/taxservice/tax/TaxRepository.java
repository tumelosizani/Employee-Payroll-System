package dev.dini.taxservice.tax;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxRepository extends JpaRepository<Tax, Integer> {
    Tax findByYear(Integer year);
}
