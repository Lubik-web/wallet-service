package ru.luba.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.luba.entity.Wallet;

import java.util.Optional;
import java.util.UUID;
/**
 * Репозиторий для доступа к данным кошельков @link Wallet
 */
@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    /**
     * Поиск кошелька по ID с блокировкой записи для обновления (FOR UPDATE).
     *
     * @param walletId UUID кошелька
     * @return Optional с кошельком, если найден
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select w from Wallet w where w.walletId = :walletId")
    Optional<Wallet> findByIdForUpdate(@Param("walletId") UUID walletId);
}