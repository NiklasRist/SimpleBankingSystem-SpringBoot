package at.fhv.SimpleBankingSystem.account.repository;

import at.fhv.SimpleBankingSystem.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    @Query("SELECT a FROM Account a WHERE a.name = :name")
    Optional<Account> findAccountByName(@Param("name") String name);

    @Transactional
    @Modifying
    @Query("DELETE FROM Account a WHERE a.name = :name")
    int deleteAccountByName(@Param("name") String name);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Account a SET a.balance = a.balance + :value WHERE a.name = :name")
    int depositToAccount(@Param("value")BigDecimal value, @Param("name") String name);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Account a SET a.balance = a.balance - :value " +
            "WHERE a.name = :name AND a.balance >= :value")
    int withdrawFromAccount(@Param("value") BigDecimal value, @Param("name") String name);


}
