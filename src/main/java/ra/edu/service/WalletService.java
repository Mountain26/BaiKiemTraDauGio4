package ra.edu.service;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.model.entity.Wallet;
import ra.edu.repository.WalletRepository;

@Service
public class WalletService {
    private static final Logger log = LoggerFactory.getLogger(WalletService.class);
    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional
    public void transferMoney(Long fromWalletId, Long toWalletId, BigDecimal amount) {
        Wallet fromWallet = walletRepository.findById(fromWalletId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found: " + fromWalletId));
        Wallet toWallet = walletRepository.findById(toWalletId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found: " + toWalletId));

        if (fromWallet.getBalance() == null || fromWallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        fromWallet.setBalance(fromWallet.getBalance().subtract(amount));
        walletRepository.save(fromWallet);

        if (true) {
            throw new RuntimeException("Simulated failure");
        }

        toWallet.setBalance(toWallet.getBalance().add(amount));
        walletRepository.save(toWallet);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveSystemLog(String message) {
        log.error(message);
    }
}

