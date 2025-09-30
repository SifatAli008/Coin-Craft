package com.coincraft.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import com.coincraft.models.TransactionRecord;
import com.coincraft.models.User;

/**
 * Centralized SmartCoin ledger to manage atomic credit/debit/transfer
 * and persist updated balances plus transaction history.
 */
public class SmartCoinLedgerService {
    private static final Logger LOGGER = Logger.getLogger(SmartCoinLedgerService.class.getName());
    private static SmartCoinLedgerService instance;

    private final FirebaseService firebaseService;

    private SmartCoinLedgerService() {
        this.firebaseService = FirebaseService.getInstance();
    }

    public static SmartCoinLedgerService getInstance() {
        if (instance == null) {
            instance = new SmartCoinLedgerService();
        }
        return instance;
    }

    public synchronized TransactionRecord credit(User toUser, int amount, String reason) {
        if (toUser == null || amount <= 0) {
            throw new IllegalArgumentException("Invalid credit arguments");
        }
        toUser.setSmartCoinBalance(toUser.getSmartCoinBalance() + amount);
        firebaseService.saveUser(toUser);
        TransactionRecord record = new TransactionRecord(
            generateId(), TransactionRecord.Type.CREDIT, null, toUser.getUserId(), amount, reason, LocalDateTime.now());
        persistRecord(record);
        LOGGER.info(() -> String.format("Credited +%d to %s for '%s'", amount, toUser.getUserId(), reason));
        return record;
    }

    public synchronized TransactionRecord debit(User fromUser, int amount, String reason) {
        if (fromUser == null || amount <= 0) {
            throw new IllegalArgumentException("Invalid debit arguments");
        }
        if (fromUser.getSmartCoinBalance() < amount) {
            throw new IllegalStateException("Insufficient balance");
        }
        fromUser.setSmartCoinBalance(fromUser.getSmartCoinBalance() - amount);
        firebaseService.saveUser(fromUser);
        TransactionRecord record = new TransactionRecord(
            generateId(), TransactionRecord.Type.DEBIT, fromUser.getUserId(), null, amount, reason, LocalDateTime.now());
        persistRecord(record);
        LOGGER.info(() -> String.format("Debited -%d from %s for '%s'", amount, fromUser.getUserId(), reason));
        return record;
    }

    public synchronized TransactionRecord transfer(User fromUser, User toUser, int amount, String reason) {
        if (fromUser == null || toUser == null || amount <= 0) {
            throw new IllegalArgumentException("Invalid transfer arguments");
        }
        if (fromUser.getSmartCoinBalance() < amount) {
            throw new IllegalStateException("Insufficient balance for transfer");
        }
        fromUser.setSmartCoinBalance(fromUser.getSmartCoinBalance() - amount);
        toUser.setSmartCoinBalance(toUser.getSmartCoinBalance() + amount);
        firebaseService.saveUser(fromUser);
        firebaseService.saveUser(toUser);
        TransactionRecord record = new TransactionRecord(
            generateId(), TransactionRecord.Type.TRANSFER, fromUser.getUserId(), toUser.getUserId(), amount, reason, LocalDateTime.now());
        persistRecord(record);
        LOGGER.info(() -> String.format("Transferred %d from %s to %s", amount, fromUser.getUserId(), toUser.getUserId()));
        return record;
    }

    private String generateId() {
        return "txn_" + UUID.randomUUID();
    }

    // For MVP we append to a local file via FirebaseService's local data area
    private void persistRecord(TransactionRecord record) {
        try {
            java.nio.file.Path dataDir = java.nio.file.Paths.get(System.getProperty("user.home"), ".coincraft", "data");
            java.nio.file.Files.createDirectories(dataDir);
            java.nio.file.Path file = dataDir.resolve("transactions.txt");
            String line = String.join("|",
                record.getTransactionId(),
                record.getType().name(),
                record.getFromUserId() != null ? record.getFromUserId() : "",
                record.getToUserId() != null ? record.getToUserId() : "",
                String.valueOf(record.getAmount()),
                record.getReason() != null ? record.getReason() : "",
                record.getTimestamp().toString()
            );
            try (java.io.PrintWriter w = new java.io.PrintWriter(java.nio.file.Files.newBufferedWriter(
                file, java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND))) {
                w.println(line);
            }
        } catch (java.io.IOException e) {
            LOGGER.warning("Failed to persist transaction: " + e.getMessage());
        }
    }

    public List<TransactionRecord> loadHistory(String userId, int limit) {
        List<TransactionRecord> list = new ArrayList<>();
        try {
            java.nio.file.Path dataDir = java.nio.file.Paths.get(System.getProperty("user.home"), ".coincraft", "data");
            java.nio.file.Path file = dataDir.resolve("transactions.txt");
            if (!java.nio.file.Files.exists(file)) return list;
            try (java.io.BufferedReader r = java.nio.file.Files.newBufferedReader(file)) {
                String line;
                while ((line = r.readLine()) != null) {
                    String[] p = line.split("\\|", -1);
                    if (p.length >= 7) {
                        String from = p[2].isEmpty() ? null : p[2];
                        String to = p[3].isEmpty() ? null : p[3];
                        boolean involvesUser = (from != null && from.equals(userId)) || (to != null && to.equals(userId));
                        if (involvesUser) {
                            TransactionRecord rec = new TransactionRecord(
                                p[0], TransactionRecord.Type.valueOf(p[1]), from, to,
                                Integer.parseInt(p[4]), p[5], java.time.LocalDateTime.parse(p[6]));
                            list.add(rec);
                        }
                    }
                }
            }
        } catch (java.io.IOException e) {
            LOGGER.warning("Failed to load transactions: " + e.getMessage());
        }
        if (list.size() > limit) {
            return new ArrayList<>(list.subList(Math.max(0, list.size() - limit), list.size()));
        }
        return list;
    }
}


