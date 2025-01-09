package dev.dini.taxservice.bracket;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxBracketRepository extends JpaRepository<TaxBracket, Integer> {
    TaxBracket findByLowerLimitLessThanEqualAndUpperLimitGreaterThanEqual(Double lowerLimit, Double upperLimit);

    TaxBracket findTopByOrderByUpperLimitDesc();
}
