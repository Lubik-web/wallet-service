package ru.luba.exception;

import java.util.UUID;

/**
 * Исключение, выбрасываемое в случае, если кошелек с указанным идентификатором не найден.
 */
public class WalletNotFoundException extends RuntimeException {

    public WalletNotFoundException(UUID walletId) {
        super("Кошелек с ID " + walletId + " не найден");
    }
}