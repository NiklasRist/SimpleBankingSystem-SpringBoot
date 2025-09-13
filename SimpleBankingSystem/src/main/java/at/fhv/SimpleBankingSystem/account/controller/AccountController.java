package at.fhv.SimpleBankingSystem.account.controller;

import at.fhv.SimpleBankingSystem.account.dto.AccountDTO;
import at.fhv.SimpleBankingSystem.account.dto.CreateAccountDTO;
import at.fhv.SimpleBankingSystem.account.service.AccountService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts(){
        List<AccountDTO> accountDTOList = accountService.getAllAccounts();

        if(accountDTOList.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(accountDTOList);
    }

    @GetMapping("/{name}")
    public ResponseEntity<AccountDTO> getAccountByName(@PathVariable String name){
        try{
            AccountDTO accountDTO = accountService.getAccountByName(name);
            return ResponseEntity.ok(accountDTO);
        }catch (Exception e){
            return  ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/createAccount")
    public ResponseEntity<?> createAccountByName(@RequestParam String name){
        try{
            AccountDTO accountDTO = accountService.createAccount(name);
            return ResponseEntity.status(HttpStatus.CREATED).body(accountDTO);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteAccountByName(@PathVariable String name){
        int deleted = accountService.deleteAccountByName(name);
        if(deleted > 0){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{name}/deposit")
    public ResponseEntity<AccountDTO> depositMoneyToAccount(@PathVariable String name,@RequestParam BigDecimal value){
        try{
            AccountDTO accountDTO = accountService.depositMoneyTo(name,value);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(accountDTO);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/{name}/withdraw")
    public ResponseEntity<AccountDTO> withdrawMoneyFromAccount(@PathVariable String name, @RequestParam BigDecimal value){
        try{
            AccountDTO accountDTO = accountService.withdrawMoneyFrom(name,value);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(accountDTO);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/{name}/transfer")
    public ResponseEntity<List<AccountDTO>> transferMoneyTo(@PathVariable String name,@RequestParam String sendTo,@RequestParam BigDecimal value){
        try{
            List<AccountDTO> accountDTOList = accountService.sendMoneyTo(name,sendTo,value);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(accountDTOList);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }








}
