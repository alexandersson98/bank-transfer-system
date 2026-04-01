package se.chasacademy.databaser.banktransfer.service;



import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.chasacademy.databaser.banktransfer.domain.Account;
import se.chasacademy.databaser.banktransfer.exception.*;
import se.chasacademy.databaser.banktransfer.repository.AccountRepository;
import java.math.BigDecimal;




@Service
public class TransferService {
    private final AccountRepository accountRepository;
    private final TransactionLogService transactionLogService;


    public TransferService(AccountRepository accountRepository, TransactionLogService transactionLogService) {
        this.accountRepository = accountRepository;
        this.transactionLogService = transactionLogService;
    }


    @Transactional

    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {



        if (amount == null || amount.signum() <= 0) {
            throw new InvalidAmountException("Amount must be positiv. ");
        }
        //validerar så det inte är samma konto
        if (fromAccountId.equals(toAccountId)) {
            throw new SameAccountTransferException("Cannot transfer to same account");
        }

        try{

        Account from = accountRepository.findByIdForUpdate(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException(fromAccountId));

        Account to = accountRepository.findByIdForUpdate(toAccountId)
                .orElseThrow(() -> new AccountNotFoundException(toAccountId));


            if (from.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException("Insufficient funds");
            }

            from.setBalance(from.getBalance().subtract(amount));

            if (amount.compareTo(new BigDecimal("999.00")) == 0) {
                throw new TechnicalFailureException("Simulated technical failure");
            }
            to.setBalance(to.getBalance().add(amount));

            transactionLogService.logSuccess(fromAccountId, toAccountId, amount);

        }  catch (RuntimeException e) {
            transactionLogService.logFailed(fromAccountId, toAccountId, amount, "Transfer failed: " + e.getMessage());
            throw e;
        }
    }
}