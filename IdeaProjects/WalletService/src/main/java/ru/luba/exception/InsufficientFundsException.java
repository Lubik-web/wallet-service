package ru.luba.exception;

import java.util.UUID;

/**
 * Исключение, выбрасываемое при попытке снять со счета сумму, превышающую текущий баланс кошелька.
 */
public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(UUID walletId) {
        super("Недостаточно средств на кошельке с ID " + walletId);
    }
}