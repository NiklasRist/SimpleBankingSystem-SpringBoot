package at.fhv.SimpleBankingSystem.service;


import at.fhv.SimpleBankingSystem.dto.AccountDTO;
import at.fhv.SimpleBankingSystem.model.Account;
import at.fhv.SimpleBankingSystem.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountDTO findAccountByName(String name){
        Optional<Account> opt = accountRepository.findAccountByName(name);
        Account acc = opt.orElseThrow(() -> new RuntimeException("Account not found")) ;
        AccountDTO accDTO = new AccountDTO(acc.getName(),acc.getBalance());
        return accDTO;
    }

    public boolean findAccountByNameBool(String name) {
        return false;
    }

    public AccountDTO createAccountByName(String name){
        if(accountRepository.findAccountByName(name).isPresent()){
            throw new RuntimeException("Account name already exists");
        }
        Account acc = new Account();
        acc.setName(name);
        acc.setBalance(BigDecimal.ZERO);

        accountRepository.save(acc);
        return new AccountDTO(acc.getName(),acc.getBalance());
    }


    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        List<AccountDTO> accountDTOS = accounts.stream().map(account -> new AccountDTO(account.getName(),account.getBalance())).toList();
        return accountDTOS;
    }

    public AccountDTO depositMoneyToAccount(String name, BigDecimal value) {
        Optional<Account> account = accountRepository.findAccountByName(name);
        Account acc = account.orElseThrow(() -> new RuntimeException("Account not found"));
        acc.setBalance(acc.getBalance().add(value));
        accountRepository.save(acc);
        return new AccountDTO(acc.getName(), acc.getBalance());
    }

    public AccountDTO withdrawMoneyFromAccount(String name, BigDecimal value) {
        Optional<Account> account = accountRepository.findAccountByName(name);
        Account acc = account.orElseThrow(() -> new RuntimeException("Account not found"));
        acc.setBalance(acc.getBalance().subtract(value));
        accountRepository.save(acc);
        return new AccountDTO(acc.getName(), acc.getBalance());
    }

    @Transactional
    public List<AccountDTO> transferMoneyTo(String name, String sendTo, BigDecimal value) {

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        Optional<Account> account = accountRepository.findAccountByName(name);
        Account senderAccount = account.orElseThrow(() -> new RuntimeException("Account not found"));
        Optional<Account> account2 = accountRepository.findAccountByName(sendTo);
        Account recipientAccount = account2.orElseThrow(() -> new RuntimeException("Account not found"));

        if (senderAccount.getBalance().compareTo(value) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        senderAccount.setBalance(senderAccount.getBalance().subtract(value));
        recipientAccount.setBalance(recipientAccount.getBalance().add(value));

        List<Account> accounts = new ArrayList<>();
        accounts.add(senderAccount);
        accounts.add(recipientAccount);
        accountRepository.saveAll(accounts);

        List<AccountDTO> accountDTOS = accounts.stream().map(a -> new AccountDTO(a.getName(), a.getBalance())).toList();
        return accountDTOS;
    }

    public void deleteAccountByName(String name) {
        Optional<Account> account = accountRepository.findAccountByName(name);
        Account acc = account.orElseThrow(() -> new RuntimeException("Account not found"));
        accountRepository.delete(acc);
    }
}
