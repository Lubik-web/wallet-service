package ru.luba.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO для представления данных кошелька клиенту
 * Содержит идентификатор кошелька и текущий баланс
 * Используется для возврата информации о состоянии кошелька через REST API
 */
@Data
@AllArgsConstructor
public class WalletDto {
    /**
     * Уникальный идентификатор кошелька
     * Формат UUID
     */
    private UUID walletId;

    /**
     * Текущий баланс кошелька
     * Представлен в формате BigDecimal для точного представления денежных значений
     */
    private BigDecimal balance;
}
