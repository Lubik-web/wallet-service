package ru.luba.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.luba.dto.WalletDto;
import ru.luba.entity.Wallet;
import ru.luba.exception.InsufficientFundsException;
import ru.luba.exception.WalletNotFoundException;
import ru.luba.mapper.WalletMapper;
import ru.luba.repository.WalletRepository;
import ru.luba.service.WalletServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private WalletMapper walletMapper;

    @InjectMocks
    private WalletServiceImpl walletService;

    private UUID walletId;
    private Wallet wallet;
    private WalletDto walletDto;

    @BeforeEach
    void setUp() {
        walletId = UUID.randomUUID();
        wallet = new Wallet(walletId, new BigDecimal("1000.00"));
        walletDto = new WalletDto(walletId, new BigDecimal("1000.00"));
    }

    @Test
    void getBalance_success() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(walletMapper.walletToWalletDto(wallet)).thenReturn(walletDto);

        WalletDto result = walletService.getBalance(walletId);

        assertEquals(walletDto, result);
        verify(walletRepository).findById(walletId);
        verify(walletMapper).walletToWalletDto(wallet);
    }

    @Test
    void getBalance_walletNotFound() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());
        assertThrows(WalletNotFoundException.class, () -> walletService.getBalance(walletId));
    }

    @Test
    void deposit_success() {
        BigDecimal depositAmount = new BigDecimal("50.00");
        WalletDto updatedDto = new WalletDto(walletId, new BigDecimal("150.00"));

        when(walletRepository.findByIdForUpdate(walletId)).thenReturn(Optional.of(wallet));
        when(walletMapper.walletToWalletDto(any(Wallet.class))).thenReturn(updatedDto);

        WalletDto result = walletService.deposit(walletId, depositAmount);

        assertEquals(updatedDto, result);
        verify(walletRepository).save(any(Wallet.class));
    }

    @Test
    void deposit_walletNotFound() {
        when(walletRepository.findByIdForUpdate(walletId)).thenReturn(Optional.empty());
        assertThrows(WalletNotFoundException.class, () -> walletService.deposit(walletId, BigDecimal.TEN));
    }

    @Test
    void withdraw_success() {
        BigDecimal withdrawAmount = new BigDecimal("40.00");
        WalletDto updatedDto = new WalletDto(walletId, new BigDecimal("60.00"));

        when(walletRepository.findByIdForUpdate(walletId)).thenReturn(Optional.of(wallet));
        when(walletMapper.walletToWalletDto(any(Wallet.class))).thenReturn(updatedDto);

        WalletDto result = walletService.withdraw(walletId, withdrawAmount);

        assertEquals(updatedDto, result);
        verify(walletRepository).save(any(Wallet.class));
    }

    @Test
    void withdraw_insufficientFunds() {
        when(walletRepository.findByIdForUpdate(walletId)).thenReturn(Optional.of(wallet));
        assertThrows(InsufficientFundsException.class,
                () -> walletService.withdraw(walletId, new BigDecimal("2000.00")));
    }

    @Test
    void withdraw_walletNotFound() {
        when(walletRepository.findByIdForUpdate(walletId)).thenReturn(Optional.empty());
        assertThrows(WalletNotFoundException.class, () -> walletService.withdraw(walletId, BigDecimal.ONE));
    }
}