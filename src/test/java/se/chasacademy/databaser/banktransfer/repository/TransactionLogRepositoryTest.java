package se.chasacademy.databaser.banktransfer.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import se.chasacademy.databaser.banktransfer.TestData;
import se.chasacademy.databaser.banktransfer.domain.Account;
import se.chasacademy.databaser.banktransfer.domain.TransactionLog;
import se.chasacademy.databaser.banktransfer.domain.TransactionStatus;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@Sql(
        scripts = "/sql/cleanup.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
public class TransactionLogRepositoryTest {


    @Autowired
    TransactionLogRepository transactionLogRepository;
    @Autowired
    AccountRepository accountRepository;


    @Test
    void shouldSaveTransactionLog() {
        Account fromAccountId = accountRepository.save(TestData.account("Eva", "2344.00"));
        Account toAccountId = accountRepository.save(TestData.account("Jonas", "300.00"));

        transactionLogRepository.save(TestData.transactionLog(fromAccountId.getId(), toAccountId.getId(), "200.00", TransactionStatus.SUCCESS));

        List<TransactionLog> logs = transactionLogRepository.findAll();

        TransactionLog savedLog = logs.get(0);

        assertThat(logs).hasSize(1);
        assertThat(savedLog.getFromAccountId()).isEqualByComparingTo(fromAccountId.getId());
        assertThat(savedLog.getToAccountId()).isEqualByComparingTo(toAccountId.getId());
        assertThat(savedLog.getAmount()).isEqualByComparingTo("200.00");
        assertThat(savedLog.getStatus()).isEqualTo(TransactionStatus.SUCCESS);
    }
}