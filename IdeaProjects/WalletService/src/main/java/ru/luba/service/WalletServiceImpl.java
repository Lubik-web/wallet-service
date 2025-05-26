package ru.luba.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.luba.dto.WalletDto;
import ru.luba.entity.Wallet;
import ru.luba.exception.InsufficientFundsException;
import ru.luba.exception.WalletNotFoundException;
import ru.luba.mapper.WalletMapper;
import ru.luba.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Slf4j
@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    /**
     * Получение баланса кошелька по его ID.
     *
     * @param walletId UUID кошелька
     * @return DTO с текущим балансом кошелька
     * @throws WalletNotFoundException если кошелек не найден
     */
    @Transactional
    @Override
    public WalletDto getBalance(UUID walletId) {
        log.info("Получение баланса для кошелька с ID {}", walletId);

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> {
                    log.error("Кошелек с ID {} не найден", walletId);
                    return new WalletNotFoundException(walletId);
                });

        // Преобразуем сущность Wallet в DTO для передачи клиенту
        WalletDto walletDto = walletMapper.walletToWalletDto(wallet);
        log.info("Баланс для кошелька с ID {}: {}", walletId, walletDto.getBalance());
        return walletDto;
    }

    /**
     * Пополнение баланса кошелька на указанную сумму.
     *
     * @param walletId UUID кошелька
     * @param amount   сумма пополнения (должна быть положительной)
     * @return DTO с обновленным балансом кошелька
     * @throws WalletNotFoundException если кошелек не найден
     */
    @Transactional
    @Override
    public WalletDto deposit(UUID walletId, BigDecimal amount) {
        log.info("Пополнение баланса кошелька с ID {} на сумму {}", walletId, amount);

        // Получаем кошелек с блокировкой записи для предотвращения конкурентных изменений
        Wallet wallet = walletRepository.findByIdForUpdate(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));

        // Создаем новый объект кошелька с увеличенным балансом (иммутабельность)
        Wallet updatedWallet = new Wallet(wallet.getWalletId(), wallet.getBalance().add(amount));
        walletRepository.save(updatedWallet);

        WalletDto walletDto = walletMapper.walletToWalletDto(updatedWallet);
        log.info("Баланс кошелька с ID {} успешно пополнен. Новый баланс: {}", walletId, walletDto.getBalance());
        return walletDto;
    }

    /**
     * Снятие суммы с баланса кошелька.
     *
     * @param walletId UUID кошелька
     * @param amount   сумма снятия (должна быть положительной)
     * @return DTO с обновленным балансом кошелька
     * @throws WalletNotFoundException    если кошелек не найден
     * @throws InsufficientFundsException если на балансе недостаточно средств
     */
    @Transactional
    @Override
    public WalletDto withdraw(UUID walletId, BigDecimal amount) {
        log.info("Снятие суммы {} с кошелька с ID {}", amount, walletId);

        Wallet wallet = walletRepository.findByIdForUpdate(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));

        // Проверяем, достаточно ли средств для снятия
        if (wallet.getBalance().compareTo(amount) < 0) {
            log.error("Недостаточно средств на кошельке с ID {}: запрошено {}, доступно {}", walletId, amount, wallet.getBalance());
            throw new InsufficientFundsException(walletId);
        }

        // Создаем новый объект кошелька с уменьшенным балансом
        Wallet updatedWallet = new Wallet(wallet.getWalletId(), wallet.getBalance().subtract(amount));
        walletRepository.save(updatedWallet);

        WalletDto walletDto = walletMapper.walletToWalletDto(updatedWallet);
        log.info("Снятие суммы {} с кошелька с ID {} успешно выполнено. Новый баланс: {}", amount, walletId, walletDto.getBalance());
        return walletDto;
    }
}