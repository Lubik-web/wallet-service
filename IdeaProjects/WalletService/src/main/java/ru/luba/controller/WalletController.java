package ru.luba.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.luba.dto.AmountRequestDto;
import ru.luba.dto.WalletDto;
import ru.luba.service.WalletService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
@AllArgsConstructor
@Slf4j
public class WalletController {

    private final WalletService walletService;

    /**
     * Получение текущего баланса кошелька по его ID.
     *
     * @param walletId UUID кошелька
     * @return текущий баланс в виде WalletDto
     */
    @GetMapping("/{walletId}")
    public WalletDto getBalance(@PathVariable UUID walletId) {
        log.info("Получение баланса кошелька {}", walletId);
        return walletService.getBalance(walletId);
    }

    /**
     * Пополнение кошелька на указанную сумму.
     *
     * @param walletId UUID кошелька
     * @param amountRequest тело запроса с полем amount
     * @return обновлённый баланс в виде WalletDto
     */
    @PostMapping("/{walletId}/deposit")
    public WalletDto deposit(@PathVariable UUID walletId,
                             @RequestBody @Valid AmountRequestDto amountRequest) {
        log.info("Пополнение кошелька {} на сумму {}", walletId, amountRequest.getAmount());
        return walletService.deposit(walletId, amountRequest.getAmount());
    }

    /**
     * Снятие средств с кошелька.
     *
     * @param walletId UUID кошелька
     * @param amountRequest тело запроса с полем amount
     * @return обновлённый баланс в виде WalletDto
     */
    @PostMapping("/{walletId}/withdraw")
    public WalletDto withdraw(@PathVariable UUID walletId,
                              @RequestBody @Valid AmountRequestDto amountRequest) {
        log.info("Снятие с кошелька {} суммы {}", walletId, amountRequest.getAmount());
        return walletService.withdraw(walletId, amountRequest.getAmount());
    }
}
