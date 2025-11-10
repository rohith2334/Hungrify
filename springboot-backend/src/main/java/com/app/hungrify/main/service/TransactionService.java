package com.app.hungrify.main.service;

import com.app.hungrify.main.dto.transaction.*;
import java.util.List;

/**
 * Service for admin transaction management.
 */
public interface TransactionService {
    List<TransactionSummaryDto> listTransactions(TransactionFilterRequestDto filter);
    TransactionDetailDto getTransactionDetail(String txnId);
}