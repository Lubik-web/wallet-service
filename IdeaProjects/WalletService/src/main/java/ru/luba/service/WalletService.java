package ru.luba.service;

import ru.luba.dto.WalletDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface WalletService {
    WalletDto getBalance(UUID walletId);
    WalletDto deposit(UUID walletId, BigDecimal amount);
    WalletDto withdraw(UUID walletId, BigDecimal amount);
}
