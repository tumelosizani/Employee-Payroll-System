package dev.dini.Employee_Payroll_System.banks;


import dev.dini.Employee_Payroll_System.employees.Employee;
import dev.dini.Employee_Payroll_System.payroll.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, Integer> {
    List<BankTransaction> findByEmployee(Employee employee);
    List<BankTransaction> findByPayroll(Payroll payroll);

    List<BankTransaction> findByTransactionType(TransactionType transactionType);
    List<BankTransaction> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);

    List<BankTransaction> findByTransactionDateBetweenAndTransactionType(LocalDate startDate, LocalDate endDate, TransactionType transactionType);
}
