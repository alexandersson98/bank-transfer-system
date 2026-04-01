package se.chasacademy.databaser.banktransfer.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import se.chasacademy.databaser.banktransfer.TestData;
import se.chasacademy.databaser.banktransfer.domain.Account;

import java.math.BigDecimal;
import java.util.Optional;

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
class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;


    @Test
    void findByIdForUpdate_returnsAccount_whenExists() {
        Account account = accountRepository.save(TestData.account("Lukas", "190.000"));

        Long id = account.getId();
        Optional<Account> foundAccount = accountRepository.findByIdForUpdate(id);

        assertThat(foundAccount).isPresent();
        assertThat(foundAccount.get().getId()).isEqualTo(id);
    }

    @Test
    void findByIdForUpdate_returnsEmpty_whenAccountDoesNotExist() {
        Optional<Account> result = accountRepository.findByIdForUpdate(999L);
        assertThat(result).isEmpty();
    }

    @Test
    void shouldSaveAndFindAccountById() {
        Account account = accountRepository.save(TestData.account("Adolf", "10000.00"));

        Long id = account.getId();
        Optional<Account> found = accountRepository.findById(id);

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(id);
    }

    @Test
    void shouldUpdateAccountBalance() {
        Account account = accountRepository.save(TestData.account("Britt-Marie", "1500.00"));

        Account update = accountRepository.findByIdForUpdate(account.getId()).orElseThrow();
        update.setBalance(new BigDecimal("800.00"));
        accountRepository.save(update);

        Account reloaded = accountRepository.findById(account.getId()).orElseThrow();
        assertThat(reloaded.getBalance()).isEqualByComparingTo("800.00");
    }
}