package ru.luba.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.luba.dto.AmountRequestDto;
import ru.luba.entity.Wallet;
import ru.luba.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class WalletControllerIntegrationTest {

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
    private MockMvc mockMvc;

    @Autowired
    private WalletRepository walletRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private UUID walletId;

    @BeforeEach
    void setup() {
        walletRepository.deleteAll();
        Wallet wallet = new Wallet(UUID.randomUUID(), new BigDecimal("100.00"));
        walletRepository.save(wallet);
        walletId = wallet.getWalletId();
    }

    @Test
    void getBalance_ReturnsCorrectBalance() throws Exception {
        mockMvc.perform(get("/api/v1/wallet/{walletId}", walletId)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", is(100.00)));
    }

    @Test
    void deposit_IncreasesBalance() throws Exception {
        AmountRequestDto requestDto = new AmountRequestDto(new BigDecimal("50.00"));
        String json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/v1/wallet/{walletId}/deposit", walletId)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", is(150.00)));
    }

    @Test
    void withdraw_DecreasesBalance() throws Exception {
        AmountRequestDto requestDto = new AmountRequestDto(new BigDecimal("40.00"));
        String json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/v1/wallet/{walletId}/withdraw", walletId)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", is(60.00)));
    }
}