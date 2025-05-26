package ru.luba.integration.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.luba.dto.WalletDto;
import ru.luba.entity.Wallet;
import ru.luba.exception.InsufficientFundsException;
import ru.luba.exception.WalletNotFoundException;
import ru.luba.repository.WalletRepository;
import ru.luba.service.WalletService;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@SpringBootTest
public class WalletServiceIntegrationTest {
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("walletdb")
                    .withUsername("postgres")
                    .withPassword("123456123");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletRepository walletRepository;

    private UUID walletId;

    @BeforeEach
    void setup() {
        // Создаём и сохраняем кошелёк с балансом 100
        Wallet wallet = new Wallet(UUID.randomUUID(), new BigDecimal("100.00"));
        walletRepository.save(wallet);
        walletId = wallet.getWalletId();
    }

    @Test
    void getBalance_ReturnsCorrectBalance() {
        WalletDto walletDto = walletService.getBalance(walletId);
        assertEquals(new BigDecimal("100.00"), walletDto.getBalance());
    }

    @Test
    void deposit_IncreasesBalance() {
        WalletDto walletDto = walletService.deposit(walletId, new BigDecimal("50.00"));
        assertEquals(new BigDecimal("150.00"), walletDto.getBalance());
    }

    @Test
    void withdraw_DecreasesBalance() {
        WalletDto walletDto = walletService.withdraw(walletId, new BigDecimal("40.00"));
        assertEquals(new BigDecimal("60.00"), walletDto.getBalance());
    }

    @Test
    void withdraw_ThrowsIfInsufficientFunds() {
        assertThrows(InsufficientFundsException.class,
                () -> walletService.withdraw(walletId, new BigDecimal("150.00")));
    }

    @Test
    void getBalance_ThrowsIfWalletNotFound() {
        UUID randomId = UUID.randomUUID();
        assertThrows(WalletNotFoundException.class,
                () -> walletService.getBalance(randomId));
    }
}
