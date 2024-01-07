package com.smallworld;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionDataFetcher {
    private final List<Transaction> transactions;

    public TransactionDataFetcher(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public double getTotalTransactionAmount() {
        return transactions.stream().mapToDouble(Transaction::getAmount).sum();
    }

    public double getTotalTransactionAmountSentBy(String senderFullName) {
        return transactions.stream()
                .filter(transaction -> senderFullName.equals(transaction.getSenderFullName()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getMaxTransactionAmount() {
        return transactions.stream().mapToDouble(Transaction::getAmount).max().orElse(0.0);
    }

    public long countUniqueClients() {
        Set<String> uniqueClients = transactions.stream()
                .flatMap(transaction -> Stream.of(transaction.getSenderFullName(), transaction.getBeneficiaryFullName()))
                .collect(Collectors.toSet());

        return uniqueClients.size();
    }

    public boolean hasOpenComplianceIssues(String clientFullName) {
        return transactions.stream()
                .anyMatch(transaction ->
                        (clientFullName.equals(transaction.getSenderFullName()) || clientFullName.equals(transaction.getBeneficiaryFullName()))
                                && transaction.hasOpenIssue());
    }

    public Map<String, List<Transaction>> getTransactionsByBeneficiaryName() {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getBeneficiaryFullName));
    }

    public Set<Integer> getUnsolvedIssueIds() {
        return transactions.stream()
                .filter(Transaction::hasOpenIssue)
                .map(Transaction::getIssueId)
                .collect(Collectors.toSet());
    }

    public List<String> getAllSolvedIssueMessages() {
        return transactions.stream()
                .filter(transaction -> transaction.isIssueSolved() && transaction.getIssueMessage() != null)
                .map(Transaction::getIssueMessage)
                .collect(Collectors.toList());
    }

    public List<Transaction> getTop3TransactionsByAmount() {
        return transactions.stream()
                .sorted(Comparator.comparingDouble(Transaction::getAmount).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    public Optional<Object> getTopSender() {
        Map<String, Double> senderTotalAmounts = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getSenderFullName, Collectors.summingDouble(Transaction::getAmount)));

        return senderTotalAmounts.entrySet().stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey);
    }
}
