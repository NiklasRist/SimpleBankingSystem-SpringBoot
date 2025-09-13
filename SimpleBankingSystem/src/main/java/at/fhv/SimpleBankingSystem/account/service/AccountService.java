package at.fhv.SimpleBankingSystem.account.service;


import at.fhv.SimpleBankingSystem.account.dto.AccountDTO;
import at.fhv.SimpleBankingSystem.account.dto.CreateAccountDTO;
import at.fhv.SimpleBankingSystem.account.model.Account;
import at.fhv.SimpleBankingSystem.account.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<AccountDTO> getAllAccounts(){
        List<Account> accList = accountRepository.findAll();
        List<AccountDTO> accountDTOList = new ArrayList<>();
        for(Account acc: accList){
            AccountDTO accountDTO = new AccountDTO(acc.getName(),acc.getBalance());
            accountDTOList.add(accountDTO);
        }
        return accountDTOList;
    }

    public AccountDTO createAccount(String name){
        try{
            Account account = new Account();
            account.setName(name);
            account.setBalance(BigDecimal.ZERO);
            Account saved = accountRepository.save(account);
            return new AccountDTO(saved.getName(),saved.getBalance());
        }catch (Exception e){
            throw new RuntimeException("Account with name already exists");
        }

    }

    public AccountDTO getAccountByName(String name){
        Optional<Account> optionalAccount = accountRepository.findAccountByName(name);
        if(optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            return new AccountDTO(account.getName(),account.getBalance());
        }else{
            throw new RuntimeException("Account with name " +name+" not found");
        }

    }

    public int deleteAccountByName(String name){
        int deleted = accountRepository.deleteAccountByName(name);
        if(deleted > 0){
            return 1;
        }else{
            return 0;
        }
    }

    public AccountDTO depositMoneyTo(String name, BigDecimal value){
        int accountOptional = accountRepository.depositToAccount(value,name);
        if(accountOptional == 0){
            throw new RuntimeException("Account doesn't exist");
        }
        Account account = accountRepository.findAccountByName(name).orElseThrow();
        return new AccountDTO(account.getName(),account.getBalance());
    }

    public AccountDTO withdrawMoneyFrom(String name, BigDecimal value){
        int accountOptional = accountRepository.withdrawFromAccount(value,name);
        if(accountOptional == 0){
            throw new RuntimeException("Account doesn't exist");
        }
        Account account = accountRepository.findAccountByName(name).orElseThrow();
        return new AccountDTO(account.getName(),account.getBalance());
    }

    public List<AccountDTO> sendMoneyTo(String name, String sendTo, BigDecimal value){
        if(value == null || value.signum() <= 0){
            throw new IllegalArgumentException("Amount must be > 0");
        }

        int withdraw = accountRepository.withdrawFromAccount(value,name);
        if(withdraw == 0){
            throw new IllegalStateException("Sender not found");
        }

        int deposit = accountRepository.depositToAccount(value,sendTo);
        if(deposit == 0){
            throw new IllegalStateException("Receiver not found");
        }
        List<AccountDTO> accountDTOList = new ArrayList<>();
        Account account1 = accountRepository.findAccountByName(name).orElseThrow();
        Account account2 = accountRepository.findAccountByName(sendTo).orElseThrow();
        AccountDTO accountDTO1 = new AccountDTO(account1.getName(),account1.getBalance());
        AccountDTO accountDTO2 = new AccountDTO(account2.getName(),account2.getBalance());

        accountDTOList.add(accountDTO1);
        accountDTOList.add(accountDTO2);

        return accountDTOList;
    }

}
