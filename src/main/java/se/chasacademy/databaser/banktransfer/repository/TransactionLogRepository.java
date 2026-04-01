package se.chasacademy.databaser.banktransfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.chasacademy.databaser.banktransfer.domain.TransactionLog;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
}
