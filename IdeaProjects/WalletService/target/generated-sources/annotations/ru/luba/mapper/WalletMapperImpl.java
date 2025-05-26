package ru.luba.mapper;

import java.math.BigDecimal;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.luba.dto.WalletDto;
import ru.luba.entity.Wallet;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-26T15:26:01+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class WalletMapperImpl implements WalletMapper {

    @Override
    public Wallet walletDtoToWallet(WalletDto walletDto) {
        if ( walletDto == null ) {
            return null;
        }

        Wallet wallet = new Wallet();

        wallet.setWalletId( walletDto.getWalletId() );
        wallet.setBalance( walletDto.getBalance() );

        return wallet;
    }

    @Override
    public WalletDto walletToWalletDto(Wallet wallet) {
        if ( wallet == null ) {
            return null;
        }

        UUID walletId = null;
        BigDecimal balance = null;

        walletId = wallet.getWalletId();
        balance = wallet.getBalance();

        WalletDto walletDto = new WalletDto( walletId, balance );

        return walletDto;
    }
}
