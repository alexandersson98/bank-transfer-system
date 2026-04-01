package se.chasacademy.databaser.banktransfer.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "account")
public class Account {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "account_id")
     private Long id;

     @Column(name = "owner_name", nullable = false, length = 100)
     private String ownerName;


     @Column(nullable = false, precision = 19, scale = 4)
     private BigDecimal balance;


     public Account(){
     }

     public Account (String ownerName, BigDecimal balance){
          this.ownerName = ownerName;
          this.balance = balance !=null ? balance : BigDecimal.ZERO;
     }


     public Account (String ownerName){
          this(ownerName, BigDecimal.ZERO);
     }

     public BigDecimal getBalance() {
          return balance;
     }

     public String getOwnerName() {
          return ownerName;
     }

     public Long getId() {
          return id;
     }


     public void setOwnerName(String ownerName) {
          this.ownerName = ownerName;
     }

     public void setBalance(BigDecimal balance) {
          this.balance = balance;
     }

     @Override
     public String toString(){
          return "Account{" +
                  "ID=" + id +
                  ", owner name='" + ownerName + '\'' +
                  ", balance=" + balance +
                  '}';
     }
     @Override
     public boolean equals(Object o) {
          if (this == o) return true;
          if (o == null || getClass() != o.getClass()) return false;

          Account account = (Account) o;

          return id != null ? id.equals(account.id) : account.id == null;
     }

     @Override
     public int hashCode() {
          return id != null ? id.hashCode() : 0;
     }
}
