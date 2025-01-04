package dev.dini.employee.payroll.system.config;


import dev.dini.employee.payroll.system.payroll.Payroll;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PayrollLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(PayrollLoggingAspect.class);

    @Pointcut("execution(* dev.dini.employee.payroll.system.payroll.PayrollServiceImpl.*(..))")
    public void payrollServiceMethods() {}

    @AfterReturning(pointcut = "payrollServiceMethods()", returning = "result")
    public void logMethodExecution(JoinPoint joinPoint, Object result) {
        logger.info("Method executed: {} with result: {}", joinPoint.getSignature(), result);

        if (result instanceof Payroll payroll) {
            logger.debug("Payroll Details: Employee: {} {}, Basic Salary: {}, Bonus: {}, Deductions: {}, Net Salary: {}, Pay Date: {}",
                    payroll.getEmployee() != null ? payroll.getEmployee().getFirstName() : "N/A",
                    payroll.getEmployee() != null ? payroll.getEmployee().getLastName() : "N/A",
                    payroll.getBasicSalary(),
                    payroll.getBonus(),
                    payroll.getDeductions(),
                    payroll.getNetSalary(),
                    payroll.getPayDate()
            );
        }
    }
}
