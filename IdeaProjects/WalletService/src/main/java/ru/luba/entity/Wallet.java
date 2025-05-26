package ru.luba.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
/**
 * Класс представляет собой сущность кошелька пользователя,
 * отображаемую в таблицу "wallets" базы данных PostgreSQL.
 *
 * Используется для хранения и управления балансом пользователя.
 * Каждый кошелек идентифицируется уникальным UUID и содержит поле баланса
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "wallets")
public class Wallet {
    /**
     * Уникальный идентификатор кошелька.
     * Генерируется как UUID.
     */
    @Id
    @Column(name = "wallet_id", nullable = false, unique = true)
    private UUID walletId;

    /**
     * Текущий баланс кошелька
     * Используется BigDecimal для точности денежных операций.
     */
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;
}
