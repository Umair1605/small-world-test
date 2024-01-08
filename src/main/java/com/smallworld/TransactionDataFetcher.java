package com.smallworld;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionDataFetcher {
    private  List<Transaction> transactions;
    private TransactionDataReader reader;
    public TransactionDataFetcher(TransactionDataReader reader) throws IOException {
        this.reader = reader;
    }

    public List<Transaction> getAll() {
        return this.transactions = this.reader.readTransactionsFromFile("transactions.json");
    }
    /**
     * Calculates the total transaction amount, considering only unique transactions based on their MTN (Mobile Transaction Number).
     *
     * @return The sum of amounts for unique transactions.
     */
    public double getTotalTransactionAmount() {
        // Set to track unique MTNs
        Set<Integer> uniqueMtns = new HashSet<>();

        return this.getAll()
                .stream()
                // Filter transactions by adding only those with unique MTNs to the set
                .filter(transaction -> uniqueMtns.add(transaction.getMtn()))
                // Map the unique transactions to their amounts and sum them up
                .mapToDouble(Transaction::getAmount)
                .sum();
    }


    /**
     * Calculates the total transaction amount sent by a specific sender, considering only unique transactions based on their MTN (Mobile Transaction Number).
     *
     * @param senderFullName The full name of the sender.
     * @return The sum of amounts for unique transactions sent by the specified sender.
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {
        // Set to track unique MTNs for the specified sender
        Set<Integer> uniqueMtns = new HashSet<>();

        return this.getAll().stream()
                // Filter transactions by the specified sender and add only those with unique MTNs to the set
                .filter(transaction -> senderFullName.equals(transaction.getSenderFullName()) && uniqueMtns.add(transaction.getMtn()))
                // Map the unique transactions to their amounts and sum them up
                .mapToDouble(Transaction::getAmount)
                .sum();
    }


    /**
     * Retrieves the maximum transaction amount among unique transactions based on their MTN (Mobile Transaction Number).
     *
     * @return The maximum transaction amount or 0.0 if there are no transactions.
     */
    public double getMaxTransactionAmount() {
        // Set to track unique MTNs
        Set<Integer> uniqueMtns = new HashSet<>();

        return this.getAll().stream()
                // Filter transactions by unique MTNs
                .filter(transaction -> uniqueMtns.add(transaction.getMtn()))
                // Map unique transactions to their amounts
                .mapToDouble(Transaction::getAmount)
                // Find the maximum amount, or return 0.0 if no transactions
                .max()
                .orElse(0.0);
    }


    /**
     * Counts the number of unique clients involved in the transactions, considering both senders and beneficiaries.
     *
     * @return The count of unique clients.
     */
    public long countUniqueClients() {
        // Set to track unique client names
        Set<String> uniqueClients = this.getAll()
                .stream()
                // FlatMap to stream of sender and beneficiary names, filter out null values
                .flatMap(transaction -> Stream.of(transaction.getSenderFullName(), transaction.getBeneficiaryFullName()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return uniqueClients.size();
    }


    /**
     * Checks if a client has open compliance issues by examining both sender and beneficiary names.
     *
     * @param clientFullName The full name of the client to check for open compliance issues.
     * @return True if the client has open compliance issues, otherwise false.
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        return this.getAll().stream()
                .anyMatch(transaction ->
                        (clientFullName.equals(transaction.getSenderFullName()) || clientFullName.equals(transaction.getBeneficiaryFullName()))
                                && transaction.hasOpenIssue());
    }


    /**
     * Groups transactions by beneficiary name, considering only the first occurrence of each unique MTN.
     *
     * @return A map where keys are beneficiary names, and values are lists of transactions associated with each beneficiary.
     */
    public Map<String, List<Transaction>> getTransactionsByBeneficiaryName() {
        Set<Integer> uniqueMtns = new HashSet<>();
        return this.getAll()
                .stream()
                .filter(transaction -> uniqueMtns.add(transaction.getMtn()))
                .collect(Collectors.groupingBy(Transaction::getBeneficiaryFullName));
    }


    /**
     * Retrieves a set of unique unsolved issue IDs from the transactions.
     *
     * @return A set containing unique issue IDs associated with unsolved issues in the transactions.
     */
    public Set<Integer> getUnsolvedIssueIds() {
        return this.getAll().stream()
                .filter(Transaction::hasOpenIssue)
                .map(Transaction::getIssueId)
                .collect(Collectors.toSet());
    }

    /**
     * Retrieves a list of all solved issue messages from the transactions.
     *
     * @return A list containing all solved issue messages from the transactions.
     */
    public List<String> getAllSolvedIssueMessages() {
        return this.getAll().stream()
                .filter(transaction -> transaction.isIssueSolved() && transaction.getIssueMessage() != null)
                .map(Transaction::getIssueMessage)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the top 3 transactions with the highest amounts, considering unique MTNs.
     *
     * @return A list containing the top 3 transactions by amount.
     */
    public List<Transaction> getTop3TransactionsByAmount() {
        Set<Integer> uniqueMtns = new HashSet<>();
        return this.getAll()
                .stream()
                .filter(transaction -> uniqueMtns.add(transaction.getMtn()))
                .sorted(Comparator.comparingDouble(Transaction::getAmount).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    public Optional<Object> getTopSender() {
        Map<String, Double> senderTotalAmounts = this.getAll().stream()
                .collect(Collectors.groupingBy(Transaction::getSenderFullName, Collectors.summingDouble(Transaction::getAmount)));

        return senderTotalAmounts.entrySet().stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey);
    }
}
