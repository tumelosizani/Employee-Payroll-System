package dev.dini.Employee_Payroll_System.banks;


import dev.dini.Employee_Payroll_System.employees.Employee;
import dev.dini.Employee_Payroll_System.employees.EmployeeRepository;
import dev.dini.Employee_Payroll_System.exception.BankTransactionNotFoundException;
import dev.dini.Employee_Payroll_System.exception.EmployeeNotFoundException;
import dev.dini.Employee_Payroll_System.exception.PayrollNotFoundException;
import dev.dini.Employee_Payroll_System.payroll.Payroll;
import dev.dini.Employee_Payroll_System.payroll.PayrollRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.*;

@Service
public class BankTransactionServiceImpl implements BankTransactionService {

    private final BankTransactionRepository bankTransactionRepository;
    private final EmployeeRepository employeeRepository;
    private final PayrollRepository payrollRepository;

    public BankTransactionServiceImpl(BankTransactionRepository bankTransactionRepository, EmployeeRepository employeeRepository, PayrollRepository payrollRepository) {
        this.bankTransactionRepository = bankTransactionRepository;
        this.employeeRepository = employeeRepository;
        this.payrollRepository = payrollRepository;
    }

    @Override
    public BankTransaction saveTransaction(BankTransaction transaction) {
        return bankTransactionRepository.save(transaction);
    }


    @Override
    public List<BankTransaction> findAllTransactions() {
        return bankTransactionRepository.findAll();
    }

    @Override
    public Optional<BankTransaction> findTransactionById(Integer transactionId) {
        return bankTransactionRepository.findById(transactionId);
    }

    @Override
    public void deleteTransaction(Integer transactionId) {
        bankTransactionRepository.deleteById(transactionId);
    }

    @Override
    public List<BankTransaction> findByEmployeeId(Integer employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);

        if (employee.isPresent()) {
            return bankTransactionRepository.findByEmployee(employee.get());
        } else {
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " not found");
        }
    }



    @Override
    public List<BankTransaction> findByPayrollId(Integer payrollId) {
        // Fetch the Payroll object by ID
        Optional<Payroll> payroll = payrollRepository.findById(payrollId);

        // Check if the payroll exists
        if (payroll.isPresent()) {
            return bankTransactionRepository.findByPayroll(payroll.get());
        } else {
            // Handle the case when the payroll is not found (return empty list or throw an exception)
           throw new PayrollNotFoundException("Payroll with ID " + payrollId + " not found");
        }
    }

    @Override
    public List<BankTransaction> findByTransactionType(TransactionType transactionType) {
        return bankTransactionRepository.findByTransactionType(transactionType);
    }

    @Override
    public List<BankTransaction> findByTransactionDateRange(LocalDate startDate, LocalDate endDate) {
        return bankTransactionRepository.findByTransactionDateBetween(startDate, endDate);
    }

    @Override
    public List<BankTransaction> searchTransactions(Integer employeeId, Integer payrollId, LocalDate startDate, LocalDate endDate, TransactionType transactionType) {
        return List.of();
    }

    @Override
    public BankTransaction findTransactionByIdOrThrow(Integer id) throws BankTransactionNotFoundException {
        return bankTransactionRepository.findById(id)
                .orElseThrow(() -> new BankTransactionNotFoundException("Transaction with ID " + id + " not found"));
    }


    @Override
    public Double calculateTotalTransactionAmountForEmployee(Integer employeeId) {
        List<BankTransaction> transactions = findByEmployeeId(employeeId);
        return transactions.stream()
                .mapToDouble(BankTransaction::getAmount)
                .sum();
    }


    @Override
    public Double calculateTotalTransactionAmountForPayroll(Integer payrollId) {
        List<BankTransaction> transactions = findByPayrollId(payrollId);
        return transactions.stream()
                .mapToDouble(BankTransaction::getAmount)
                .sum();
    }


    @Override
    public BankTransaction reverseTransaction(Integer transactionId) throws BankTransactionNotFoundException {
        BankTransaction transaction = findTransactionByIdOrThrow(transactionId);

        // Create a new reversed transaction
        BankTransaction reversedTransaction = new BankTransaction();
        reversedTransaction.setEmployee(transaction.getEmployee());
        reversedTransaction.setPayroll(transaction.getPayroll());
        reversedTransaction.setAmount(-transaction.getAmount());  // Reverse the amount
        reversedTransaction.setTransactionType(TransactionType.REVERSAL);  // Set transaction type to reversal
        reversedTransaction.setTransactionDate(LocalDate.now());  // Set to current date

        return bankTransactionRepository.save(reversedTransaction);
    }


    @Override
    public List<TransactionSummary> generateTransactionSummaryReport(LocalDate startDate, LocalDate endDate, TransactionType transactionType) {
        List<BankTransaction> transactions = bankTransactionRepository.findByTransactionDateBetweenAndTransactionType(startDate, endDate, transactionType);

        Map<Employee, Double> employeeTotalAmount = new HashMap<>();
        Map<Employee, Long> employeeTransactionCount = new HashMap<>();

        for (BankTransaction transaction : transactions) {
            Employee employee = transaction.getEmployee();
            employeeTotalAmount.merge(employee, transaction.getAmount(), Double::sum);
            employeeTransactionCount.merge(employee, 1L, Long::sum);
        }

        List<TransactionSummary> summaries = new ArrayList<>();
        for (Map.Entry<Employee, Double> entry : employeeTotalAmount.entrySet()) {
            Employee employee = entry.getKey();
            Double totalAmount = entry.getValue();
            Long transactionCount = employeeTransactionCount.get(employee);

            TransactionSummary summary = new TransactionSummary(
                    null, // Use Payroll if required
                    employee,
                    startDate,
                    endDate,
                    transactionType,
                    totalAmount,
                    transactionCount
            );

            summaries.add(summary);
        }

        return summaries;
    }



    @Override
    public List<BankTransaction> importTransactionsFromFile(MultipartFile file) {
        List<BankTransaction> transactions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                // Assuming CSV has columns: employeeId, payrollId, amount, date
                Integer employeeId = Integer.parseInt(values[0]);
                Integer payrollId = Integer.parseInt(values[1]);
                Double amount = Double.parseDouble(values[2]);
                LocalDate transactionDate = LocalDate.parse(values[3]);

                BankTransaction transaction = new BankTransaction();
                transaction.setEmployee(new Employee(employeeId));  // Assuming Employee ID is present
                transaction.setPayroll(new Payroll(payrollId));  // Assuming Payroll ID is present
                transaction.setAmount(amount);
                transaction.setTransactionDate(transactionDate);

                transactions.add(transaction);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., invalid file format)
        }

        return bankTransactionRepository.saveAll(transactions);  // Save all transactions
    }


    @Override
    public BankTransaction updateTransactionStatus(Integer transactionId, TransactionStatus status) {
        BankTransaction transaction = findTransactionByIdOrThrow(transactionId);
        transaction.setStatus(status);
        return bankTransactionRepository.save(transaction);
    }


    @Override
    public List<TransactionHistory> getTransactionHistory(Integer transactionId) {
        return List.of();
    }

    @Override
    public List<BankTransaction> searchTransactionsWithFilters(Integer employeeId, Integer payrollId, TransactionType transactionType, TransactionStatus status, Double minAmount, Double maxAmount, LocalDate startDate, LocalDate endDate) {
        return List.of();
    }
}
