package at.fhv.SimpleBankingSystem.controller;

import at.fhv.SimpleBankingSystem.dto.AccountDTO;
import at.fhv.SimpleBankingSystem.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @GetMapping("/{name}")
    public ResponseEntity<AccountDTO> getAccountByName(@PathVariable String name){
        try{
            AccountDTO accountDTO = accountService.findAccountByName(name);
            return ResponseEntity.ok(accountDTO);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts(){
        List<AccountDTO> accounts = accountService.getAllAccounts();
        if(accounts.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/createAccount")
    public ResponseEntity<String> createAccountByName(@RequestParam String name){
        try{
            AccountDTO accountDTO = accountService.createAccountByName(name);
            return ResponseEntity.status(HttpStatus.CREATED).body(accountDTO.toString());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Name already taken, try another name");
        }
    }

    @PostMapping("/{name}/deposit")
    public ResponseEntity<AccountDTO> depositMoneyToAccount(@PathVariable String name, @RequestParam BigDecimal value){
        try{
            AccountDTO accountDTO = accountService.depositMoneyToAccount(name, value);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(accountDTO);
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{name}/withdraw")
    public ResponseEntity<AccountDTO> withdrawMoneyFromAccount(@PathVariable String name, @RequestParam BigDecimal value){
        try{
            AccountDTO accountDTO = accountService.withdrawMoneyFromAccount(name, value);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(accountDTO);
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{name}/transfer")
    public ResponseEntity<List<AccountDTO>> transferMoneyTo(@PathVariable String name, @RequestParam String sendTo, @RequestParam BigDecimal value){
        try{
            List<AccountDTO> accountDTOS =  accountService.transferMoneyTo(name, sendTo, value);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(accountDTOS);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteAccountByName(@PathVariable String name){
        try{
            accountService.deleteAccountByName(name);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }






}
