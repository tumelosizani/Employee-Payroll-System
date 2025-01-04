package dev.dini.employee.payroll.system.banks;


import dev.dini.employee.payroll.system.employees.Employee;
import dev.dini.employee.payroll.system.payroll.Payroll;
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
