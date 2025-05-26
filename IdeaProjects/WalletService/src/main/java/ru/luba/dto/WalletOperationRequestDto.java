package ru.luba.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WalletOperationRequestDto {

    @NotNull(message = "ID кошелька не должен быть пустым")
    private UUID walletId;

    @NotBlank(message = "Тип операции должен быть указан")
    private String operationType;  // "DEPOSIT" или "WITHDRAW"

    @Valid
    @NotNull(message = "Тело с суммой операции не должно быть пустым")
    private AmountRequestDto amountRequest;
}