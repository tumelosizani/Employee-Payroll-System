package dev.dini.Employee_Payroll_System.banks;


import dev.dini.Employee_Payroll_System.exception.BankTransactionNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing bank transactions in the payroll system.
 */
public interface BankTransactionService {

    /**
     * Saves a new bank transaction.
     * @param transaction the transaction to save
     * @return the saved bank transaction
     */
    BankTransaction saveTransaction(BankTransaction transaction);

    /**
     * Fetches all bank transactions with pagination and sorting.
     * @return a page of bank transactions
     */
    List<BankTransaction> findAllTransactions();

    /**
     * Finds a bank transaction by its ID.
     * @param id the ID of the transaction
     * @return an Optional containing the transaction if found, otherwise empty
     */
    Optional<BankTransaction> findTransactionById(Integer id);

    /**
     * Deletes a bank transaction by its ID.
     * @param id the ID of the transaction to delete
     */
    void deleteTransaction(Integer id);

    /**
     * Finds transactions by the employee's ID.
     * @param employeeId the ID of the employee
     * @return a list of transactions associated with the employee
     */
    List<BankTransaction> findByEmployeeId(Integer employeeId);

    /**
     * Finds transactions by the payroll's ID.
     * @param payrollId the ID of the payroll
     * @return a list of transactions associated with the payroll
     */
    List<BankTransaction> findByPayrollId(Integer payrollId);

    /**
     * Finds transactions by their type.
     * @param transactionType the type of the transaction
     * @return a list of transactions matching the transaction type
     */
    List<BankTransaction> findByTransactionType(TransactionType transactionType);

    /**
     * Finds transactions that occurred within a date range.
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return a list of transactions within the specified date range
     */
    List<BankTransaction> findByTransactionDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Searches for transactions with flexible filtering options.
     * @param employeeId the ID of the employee (optional)
     * @param payrollId the ID of the payroll (optional)
     * @param startDate the start date (optional)
     * @param endDate the end date (optional)
     * @param transactionType the transaction type (optional)
     * @return a list of transactions that match the criteria
     */
    List<BankTransaction> searchTransactions(Integer employeeId, Integer payrollId, LocalDate startDate, LocalDate endDate, TransactionType transactionType);

    /**
     * Finds a bank transaction by its ID, throws exception if not found.
     * @param id the ID of the transaction
     * @return the bank transaction
     * @throws BankTransactionNotFoundException if the transaction is not found
     */
    BankTransaction findTransactionByIdOrThrow(Integer id) throws BankTransactionNotFoundException;

    /**
     * Calculates the total amount of transactions for a specific employee.
     * @param employeeId the ID of the employee
     * @return the total amount of transactions
     */
    Double calculateTotalTransactionAmountForEmployee(Integer employeeId);

    /**
     * Calculates the total amount of transactions for a specific payroll.
     * @param payrollId the ID of the payroll
     * @return the total amount of transactions
     */
    Double calculateTotalTransactionAmountForPayroll(Integer payrollId);

    /**
     * Reverses a bank transaction by creating a new transaction with the opposite amount and marking it as a reversal.
     * @param transactionId the ID of the transaction to reverse
     * @return the newly created reversed transaction
     * @throws BankTransactionNotFoundException if the transaction is not found
     */
    BankTransaction reverseTransaction(Integer transactionId) throws BankTransactionNotFoundException;

    /**
     * Generates a summary report of transactions within a date range.
     * @param startDate the start date
     * @param endDate the end date
     * @param transactionType the type of transaction to filter by (optional)
     * @return a list of summary reports
     */
    List<TransactionSummary> generateTransactionSummaryReport(LocalDate startDate, LocalDate endDate, TransactionType transactionType);

    /**
     * Imports a list of transactions from an Excel or CSV file.
     * @param file the file containing the transaction data
     * @return the list of saved transactions
     */
    List<BankTransaction> importTransactionsFromFile(MultipartFile file);


    /**
     * Updates the status of a transaction.
     * @param transactionId the ID of the transaction to update
     * @param status the new status
     * @return the updated transaction
     */
    BankTransaction updateTransactionStatus(Integer transactionId, TransactionStatus status);

    /**
     * Fetches the history of changes for a specific transaction.
     * @param transactionId the ID of the transaction
     * @return a list of transaction history logs
     */
    List<TransactionHistory> getTransactionHistory(Integer transactionId);

    /**
     * Searches for transactions with multiple filtering criteria.
     * @param employeeId the employee ID (optional)
     * @param payrollId the payroll ID (optional)
     * @param transactionType the transaction type (optional)
     * @param status the transaction status (optional)
     * @param minAmount the minimum transaction amount (optional)
     * @param maxAmount the maximum transaction amount (optional)
     * @param startDate the start date (optional)
     * @param endDate the end date (optional)
     * @return a list of transactions matching the criteria
     */
    List<BankTransaction> searchTransactionsWithFilters(
            Integer employeeId,
            Integer payrollId,
            TransactionType transactionType,
            TransactionStatus status,
            Double minAmount,
            Double maxAmount,
            LocalDate startDate,
            LocalDate endDate
    );






}
