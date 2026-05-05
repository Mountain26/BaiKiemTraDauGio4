package ra.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.model.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}

