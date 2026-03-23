package com.bakery.bakery_management.service;

import com.bakery.bakery_management.domain.dto.Request.ImportRequest;
import com.bakery.bakery_management.domain.dto.Response.ImportResponse;
import com.bakery.bakery_management.domain.entity.InventoryBatch;
import com.bakery.bakery_management.domain.entity.StockTransaction;
import com.bakery.bakery_management.domain.enums.BatchStatus;
import com.bakery.bakery_management.domain.enums.TransactionType;
import com.bakery.bakery_management.exception.BusinessException;
import com.bakery.bakery_management.exception.ErrorCode;
import com.bakery.bakery_management.repository.InventoryBatchRepository;
import com.bakery.bakery_management.repository.StockTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryBatchRepository batchRepo;
    private final StockTransactionRepository transactionRepo;

    @Transactional
    public ImportResponse importStock(List<ImportRequest> reqs) {

        for(ImportRequest req: reqs){
            validate(req);

            InventoryBatch batch = batchRepo
                    .findForUpdate(
                            req.getProductCode(),
                            req.getWarehouseCode(),
                            req.getBatchNo()
                    )
                    .orElse(null);

            if (batch == null) {
                batch = new InventoryBatch();
                batch.setProductCode(req.getProductCode());
                batch.setWarehouseCode(req.getWarehouseCode());
                batch.setBatchNo(req.getBatchNo());
                batch.setQuantity(req.getQuantity());
                batch.setExpiryDate(req.getExpiryDate());
                batch.setBatchStatus(resolveStatus(req.getQuantity(), req.getExpiryDate()));
                batch.setCreatedAt(Instant.now());

                batchRepo.save(batch);
            } else {
                BigDecimal newQty = batch.getQuantity().add(req.getQuantity());
                batch.setQuantity(newQty);
                batch.setBatchStatus(resolveStatus(newQty, batch.getExpiryDate()));
                batch.setUpdatedAt(Instant.now());
            }

            // 🔥 luôn insert transaction (không update)
            StockTransaction tx = new StockTransaction();
            tx.setProductCode(req.getProductCode());
            tx.setWarehouseCode(req.getWarehouseCode());
            tx.setBatchId(batch.getId());
            tx.setTransactionType(TransactionType.I);
            tx.setQuantity(req.getQuantity());
            tx.setReferenceType(req.getReferenceType());
            tx.setReferenceId(req.getReferenceId());
            tx.setCreatedAt(Instant.now());

            transactionRepo.save(tx);

            ImportResponse res = new ImportResponse();
            res.setBatchId(batch.getId());
            res.setNewQuantity(batch.getQuantity());
        }
        return null;
    }

    private void validate(ImportRequest req) {
        if (req.getQuantity() == null || req.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCode.INVALID_QUANTITY, "Quantity must be > 0");
        }

        if (req.getExpiryDate() != null &&
                req.getExpiryDate().isBefore(LocalDate.now())) {
            throw new BusinessException(ErrorCode.EXPIRED_BATCH, "Batch already expired");
        }
    }

    private BatchStatus resolveStatus(BigDecimal qty, LocalDate expiry) {

        if (qty.compareTo(BigDecimal.ZERO) == 0) {
            return BatchStatus.DEPLETED;
        }

        if (expiry != null && expiry.isBefore(LocalDate.now())) {
            return BatchStatus.EXPIRED;
        }

        return BatchStatus.ACTIVE;
    }
}
