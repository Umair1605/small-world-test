package com.smallworld;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.*;

class TransactionDataFetcherTest {
    private TransactionDataFetcher dataFetcher;
    private TransactionDataReader mockReader;

    @BeforeEach
    public void setup() throws IOException {
        mockReader = mock(TransactionDataReader.class);
        dataFetcher = new TransactionDataFetcher(mockReader);
    }
    @Test
    void testGetTotalTransactionAmount() {

        List<Transaction> mockTransactions = Arrays.asList(
                new Transaction(1, 100.0, "Sender1", 25, "Beneficiary1", 30, null, false, null),
                new Transaction(2, 150.0, "Sender2", 28, "Beneficiary2", 35, null, false, null)
        );

        when(dataFetcher.getAll()).thenReturn(mockTransactions);
        assertEquals(250.0, dataFetcher.getTotalTransactionAmount());
    }

    @Test
    void testGetTotalTransactionAmountSentBy() {
        List<Transaction> mockTransactions = Arrays.asList(
                new Transaction(1, 100.0, "Aunt Polly", 25, "Beneficiary1", 30, null, false, null),
                new Transaction(1, 100.0, "Aunt Polly", 25, "Beneficiary2", 35, null, false, null)
        );

        when(dataFetcher.getAll()).thenReturn(mockTransactions);
        assertEquals(100, dataFetcher.getTotalTransactionAmountSentBy("Aunt Polly"));
    }
    @Test
    void testGetMaxTransactionAmount() {
        List<Transaction> mockTransactions = Arrays.asList(
                new Transaction(1, 100.0, "Sender1", 25, "Beneficiary1", 30, null, false, null),
                new Transaction(2, 150.0, "Sender2", 28, "Beneficiary2", 35, null, false, null)
        );
        when(dataFetcher.getAll()).thenReturn(mockTransactions);

        assertEquals(150.0, dataFetcher.getMaxTransactionAmount());
    }
    @Test
    void testCountUniqueClients() {
        List<Transaction> mockTransactions = Arrays.asList(
                new Transaction(1, 100.0, "Sender1", 30, "Beneficiary1", 25, null, false, null),
                new Transaction(2, 150.0, "Sender2", 35, "Beneficiary2", 28, null, false, null),
                new Transaction(3, 200.0, "Sender3", 40, "Beneficiary3", 22, null, false, null),
                new Transaction(4, 120.0, "Sender4", 45, "Beneficiary4", 33, null, false, null),
                new Transaction(5, 80.0, "Sender5", 50, "Beneficiary5", 28, null, false, null)
        );
        when(dataFetcher.getAll()).thenReturn(mockTransactions);
        long result = dataFetcher.countUniqueClients();

        assertEquals(10, result);
    }

    @Test
    void testHasOpenComplianceIssues() {
        List<Transaction> mockTransactions = Arrays.asList(
                new Transaction(1, 100.0, "Aunt Polly", 25, "Beneficiary1", 30, 1, false, null),
                new Transaction(2, 150.0, "Aunt Polly", 28, "Beneficiary2", 35, null, false, null),
                new Transaction(3, 200.0, "Aunt Polly", 25, "Beneficiary3", 30, 3, true, "Issue3")
        );
        when(dataFetcher.getAll()).thenReturn(mockTransactions);
        assertTrue(dataFetcher.hasOpenComplianceIssues("Aunt Polly"));
    }
//
    @Test
    void testGetTransactionsByBeneficiaryName() {
        List<Transaction> mockTransactions = Arrays.asList(
                new Transaction(1, 100.0, "Sender1", 25, "Beneficiary1", 30, null, false, null),
                new Transaction(2, 150.0, "Sender2", 28, "Beneficiary2", 35, null, false, null),
                new Transaction(3, 200.0, "Sender3", 25, "Beneficiary1", 30, null, false, null)
        );

        when(dataFetcher.getAll()).thenReturn(mockTransactions);
        Map<String, List<Transaction>> transactionsByBeneficiary = dataFetcher.getTransactionsByBeneficiaryName();

        assertTrue(transactionsByBeneficiary.containsKey("Beneficiary1"));
        assertEquals(2, transactionsByBeneficiary.get("Beneficiary1").size());
        assertTrue(transactionsByBeneficiary.containsKey("Beneficiary2"));
        assertEquals(1, transactionsByBeneficiary.get("Beneficiary2").size());
    }

    @Test
    void testGetUnsolvedIssueIds() {
        List<Transaction> mockTransactions = Arrays.asList(
                new Transaction(1, 100.0, "Sender1", 30, "Beneficiary1", 25, 101, true, "Issue 1"),
                new Transaction(2, 150.0, "Sender2", 35, "Beneficiary2", 28, null, false, null),
                new Transaction(3, 200.0, "Sender3", 40, "Beneficiary3", 22, 103, false, null),
                new Transaction(4, 120.0, "Sender4", 45, "Beneficiary4", 33, 104, false, null),
                new Transaction(5, 80.0, "Sender5", 50, "Beneficiary5", 28, 105, true, "Issue 2")
        );
        when(dataFetcher.getAll()).thenReturn(mockTransactions);

        Set<Integer> result = dataFetcher.getUnsolvedIssueIds();
        Set<Integer> expectedSet = new HashSet<>(Arrays.asList(103, 104));
        assertEquals(expectedSet, result);
    }
    @Test
    void testGetAllSolvedIssueMessages() {
        List<Transaction> mockTransactions = Arrays.asList(
                new Transaction(1, 100.0, "Sender1", 25, "Beneficiary1", 30, 1, false, null),
                new Transaction(2, 150.0, "Sender2", 28, "Beneficiary2", 35, null, false, null),
                new Transaction(3, 200.0, "Sender3", 25, "Beneficiary3", 30, 3, true, "Issue3"),
                new Transaction(4, 250.0, "Sender4", 25, "Beneficiary4", 30, 4, true, "Issue4")
        );

        when(dataFetcher.getAll()).thenReturn(mockTransactions);
        List<String> solvedIssueMessages = dataFetcher.getAllSolvedIssueMessages();

        assertEquals(2, solvedIssueMessages.size());
        assertTrue(solvedIssueMessages.contains("Issue3"));
        assertTrue(solvedIssueMessages.contains("Issue4"));
    }

    @Test
    void testGetTop3TransactionsByAmount() {
        List<Transaction> mockTransactions = Arrays.asList(
                new Transaction(1, 100.0, "Sender1", 25, "Beneficiary1", 30, null, false, null),
                new Transaction(2, 150.0, "Sender2", 28, "Beneficiary2", 35, null, false, null),
                new Transaction(3, 200.0, "Sender3", 25, "Beneficiary3", 30, null, false, null)
        );

        when(dataFetcher.getAll()).thenReturn(mockTransactions);
        List<Transaction> expected = Arrays.asList(
                new Transaction(3, 200.0, "Sender3", 25, "Beneficiary3", 30, null, false, null),
                new Transaction(2, 150.0, "Sender2", 28, "Beneficiary2", 35, null, false, null),
                new Transaction(1, 100.0, "Sender1", 25, "Beneficiary1", 30, null, false, null)
        );

        List<Transaction> actual = dataFetcher.getTop3TransactionsByAmount();

        // Ensure expected and actual have the same values
        for (int i = 0; i < Math.min(expected.size(), actual.size()); i++) {
            assertEquals(expected.get(i).getAmount(), actual.get(i).getAmount());
            assertEquals(expected.get(i).getSenderFullName(), actual.get(i).getSenderFullName());
            assertEquals(expected.get(i).getBeneficiaryFullName(), actual.get(i).getBeneficiaryFullName());

        }
        // Ensure the lists have the same size
        assertEquals(expected.size(), actual.size());
    }
    @Test
    void testGetTopSender() {
        List<Transaction> mockTransactions = Arrays.asList(
                new Transaction(1, 100.0, "Sender1", 25, "Beneficiary1", 30, null, false, null),
                new Transaction(2, 150.0, "Sender2", 28, "Beneficiary2", 35, null, false, null),
                new Transaction(3, 200.0, "Sender1", 25, "Beneficiary3", 30, null, false, null)
        );
        when(dataFetcher.getAll()).thenReturn(mockTransactions);

        assertEquals("Sender1", dataFetcher.getTopSender().orElse(null));
    }

}