package ru.luba.mapper;

import org.mapstruct.Mapper;
import ru.luba.dto.WalletDto;
import ru.luba.entity.Wallet;

/**
 * Интерфейс для преобразования  сущности Wallet в WalletDto
 *
 * @author Lubov Kolesnikova
 * @since 22.05.2025
 */
@Mapper(componentModel = "spring")
public interface WalletMapper {
    /**
     * Преобразует WalletDto в сущность Wallet.
     *
     * @param walletDto DTO объекта кошелька
     * @return сущность Wallet
     */
    Wallet walletDtoToWallet(WalletDto walletDto);

    /**
     * Преобразует сущность Wallet в WalletDto.
     *
     * @param wallet сущность Wallet из базы данных
     * @return DTO объекта кошелька
     */
    WalletDto walletToWalletDto(Wallet wallet);
}
