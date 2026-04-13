package se.chasacademy.databaser.banktransfer.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import se.chasacademy.databaser.banktransfer.TestData;
import se.chasacademy.databaser.banktransfer.domain.Account;
import se.chasacademy.databaser.banktransfer.domain.TransactionLog;
import se.chasacademy.databaser.banktransfer.domain.TransactionStatus;
import se.chasacademy.databaser.banktransfer.exception.InsufficientFundsException;
import se.chasacademy.databaser.banktransfer.exception.InvalidAmountException;
import se.chasacademy.databaser.banktransfer.exception.TechnicalFailureException;
import se.chasacademy.databaser.banktransfer.repository.AccountRepository;
import se.chasacademy.databaser.banktransfer.repository.TransactionLogRepository;
import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class TransferServiceTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");


    @Autowired
    TransferService transferService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionLogRepository transactionLogRepository;

    @Test
    void successful_transfer_updates_balances_and_logs_success(){
        Account a = accountRepository.save(TestData.account("Lukas", "1000.00"));
        Account b = accountRepository.save(TestData.account("Berra", "500.00"));

        transferService.transfer(a.getId(), b.getId(), new BigDecimal("200.00"));

        Account updatedA = accountRepository.findById(a.getId()).orElseThrow();
        Account updatedB = accountRepository.findById(b.getId()).orElseThrow();

        assertThat(updatedA.getBalance()).isEqualByComparingTo("800.00");
        assertThat(updatedB.getBalance()).isEqualByComparingTo("700.00");

        List<TransactionLog> logs = transactionLogRepository.findAll();
        assertThat(logs).hasSize(1);
        assertThat(logs.get(0).getStatus()).isEqualTo(TransactionStatus.SUCCESS);
    }

    @Test
    void transfer_fails_when_insufficient_funds(){
        Account a = accountRepository.save(TestData.account("Jonas", "100.00"));
        Account b = accountRepository.save(TestData.account("Gunnar", "500.00"));

        assertThatThrownBy(() ->
                transferService.transfer(a.getId(), b.getId(), new BigDecimal("200.00"))
        ).isInstanceOf(InsufficientFundsException.class);

        Account updatedA = accountRepository.findById(a.getId()).orElseThrow();
        Account updatedB = accountRepository.findById(b.getId()).orElseThrow();

        assertThat(updatedA.getBalance()).isEqualByComparingTo("100.00");
        assertThat(updatedB.getBalance()).isEqualByComparingTo("500.00");

        List<TransactionLog> logs = transactionLogRepository.findAll();
        assertThat(logs).hasSize(1);
        assertThat(logs.get(0).getStatus()).isEqualTo(TransactionStatus.FAILED);
    }

    @Test
    void transfer_rolls_back_and_logs_failed_on_technical_error(){
        Account a = accountRepository.save(TestData.account("Bertil", "3000.00"));
        Account b = accountRepository.save(TestData.account("Birgitta", "400.00"));


        assertThatThrownBy(() ->
                transferService.transfer(a.getId(), b.getId(), new BigDecimal("999.00"))
        ).isInstanceOf(TechnicalFailureException.class);

        Account updatedA = accountRepository.findById(a.getId()).orElseThrow();
        Account updatedB = accountRepository.findById(b.getId()).orElseThrow();

        assertThat(updatedA.getBalance()).isEqualByComparingTo("3000.00");
        assertThat(updatedB.getBalance()).isEqualByComparingTo("400.00");

        List<TransactionLog> logs = transactionLogRepository.findAll();
        assertThat(logs).hasSize(1);
        assertThat(logs.get(0).getStatus()).isEqualTo(TransactionStatus.FAILED);
    }

    @Test
    void transfer_fails_when_amount_is_negative(){
        Account a = accountRepository.save(TestData.account("Lukas", "1000.00"));
        Account b = accountRepository.save(TestData.account("Berra", "500.00"));

        assertThatThrownBy(() ->
                transferService.transfer(a.getId(), b.getId(), new BigDecimal("-10.00"))
        ).isInstanceOf(InvalidAmountException.class);

        Account updatedA = accountRepository.findById(a.getId()).orElseThrow();
        Account updatedB = accountRepository.findById(b.getId()).orElseThrow();

        assertThat(updatedA.getBalance()).isEqualByComparingTo("1000.00");
        assertThat(updatedB.getBalance()).isEqualByComparingTo("500.00");

        List<TransactionLog> logs = transactionLogRepository.findAll();
        assertThat(logs).isEmpty();
    }
}
