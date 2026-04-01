package se.chasacademy.databaser.banktransfer.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import se.chasacademy.databaser.banktransfer.domain.TransactionLog;
import se.chasacademy.databaser.banktransfer.domain.TransactionStatus;
import se.chasacademy.databaser.banktransfer.repository.TransactionLogRepository;

import java.math.BigDecimal;




@Service
public class TransactionLogService {

    private final TransactionLogRepository repository;


    public TransactionLogService(TransactionLogRepository repository){
        this.repository = repository;
    }


    public void logSuccess(Long fromId, Long toId, BigDecimal amount) {
        repository.save(new TransactionLog(fromId, toId, amount, TransactionStatus.SUCCESS, null));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logFailed(Long fromId, Long toId, BigDecimal amount, String message) {
        repository.save(new TransactionLog(fromId, toId, amount, TransactionStatus.FAILED, message));
    }


}
