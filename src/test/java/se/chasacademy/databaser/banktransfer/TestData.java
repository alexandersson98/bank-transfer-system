package se.chasacademy.databaser.banktransfer;

import se.chasacademy.databaser.banktransfer.domain.Account;
import se.chasacademy.databaser.banktransfer.domain.TransactionLog;
import se.chasacademy.databaser.banktransfer.domain.TransactionStatus;

import java.math.BigDecimal;

public class TestData {

    public static Account account(String owner, String balance){
        return new Account(owner, new BigDecimal(balance));
    }

    public static TransactionLog transactionLog(Long fromAccountId, Long toAccountId, String amount, TransactionStatus status) {
        return new TransactionLog(fromAccountId, toAccountId, new BigDecimal(amount), status);
    }
}
