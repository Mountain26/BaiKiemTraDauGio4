package ra.edu.repository;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ra.edu.model.entity.TransactionHistory;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
    Page<TransactionHistory> findByWalletId(Long walletId, Pageable pageable);

    @Query("select th from TransactionHistory th where th.amount > :minAmount")
    List<TransactionHistory> findByAmountGreaterThan(@Param("minAmount") BigDecimal minAmount);
}

