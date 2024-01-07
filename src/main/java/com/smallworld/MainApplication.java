package com.smallworld;

import java.util.List;
import java.util.Optional;

public class MainApplication {

    public static void main(String[] args) {
        TransactionDataReader dataReader = new TransactionDataReader();
        List<Transaction> transactions = dataReader.readTransactionsFromFile("transactions.json");
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher(transactions);

        printTotalTransactionAmount(dataFetcher);
        printTotalAmountSentBy(dataFetcher, "Aunt Polly");
        printHighestTransactionAmount(dataFetcher);
        printUniqueClientsCount(dataFetcher);
        printOpenComplianceIssues(dataFetcher, "Aunt Polly");
        printTransactionsByBeneficiaryName(dataFetcher);
        printUnsolvedIssueIds(dataFetcher);
        printSolvedIssueMessages(dataFetcher);
        printTop3TransactionsByAmount(dataFetcher);
        printTopSender(dataFetcher);
    }

    private static void printTotalTransactionAmount(TransactionDataFetcher dataFetcher) {
        System.out.println("Total Transaction Amount: " + dataFetcher.getTotalTransactionAmount());
    }

    private static void printTotalAmountSentBy(TransactionDataFetcher dataFetcher, String senderName) {
        double totalAmount = dataFetcher.getTotalTransactionAmountSentBy(senderName);
        System.out.println(String.format("Total Amount Sent by %s: %.2f", senderName, totalAmount));
    }

    private static void printHighestTransactionAmount(TransactionDataFetcher dataFetcher) {
        double highestAmount = dataFetcher.getMaxTransactionAmount();
        System.out.println("Highest Transaction Amount: " + highestAmount);
    }

    private static void printUniqueClientsCount(TransactionDataFetcher dataFetcher) {
        long uniqueClientsCount = dataFetcher.countUniqueClients();
        System.out.println("Count Unique Clients: " + uniqueClientsCount);
    }

    private static void printOpenComplianceIssues(TransactionDataFetcher dataFetcher, String clientFullName) {
        boolean hasOpenComplianceIssues = dataFetcher.hasOpenComplianceIssues(clientFullName);
        System.out.println("Has Open Compliance Issues for " + clientFullName + ": " + hasOpenComplianceIssues);
    }

    private static void printTransactionsByBeneficiaryName(TransactionDataFetcher dataFetcher) {
        System.out.println("Transactions Indexed by Beneficiary Name: " + dataFetcher.getTransactionsByBeneficiaryName());
    }

    private static void printUnsolvedIssueIds(TransactionDataFetcher dataFetcher) {
        System.out.println("Unsolved Compliance Issue IDs: " + dataFetcher.getUnsolvedIssueIds());
    }

    private static void printSolvedIssueMessages(TransactionDataFetcher dataFetcher) {
        System.out.println("Solved Issue Messages: " + dataFetcher.getAllSolvedIssueMessages());
    }

    private static void printTop3TransactionsByAmount(TransactionDataFetcher dataFetcher) {
        System.out.println("Top 3 Transactions by Amount: " + dataFetcher.getTop3TransactionsByAmount());
    }

    private static void printTopSender(TransactionDataFetcher dataFetcher) {
        Optional<Object> topSender = dataFetcher.getTopSender();
        System.out.println("Top Sender: " + topSender.orElse("No top sender found"));
    }
}
