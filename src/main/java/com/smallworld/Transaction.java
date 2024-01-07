package com.smallworld;

import java.util.Objects;

public class Transaction {
    private int mtn;
    private double amount;
    private String senderFullName;
    private int senderAge;
    private String beneficiaryFullName;
    private int beneficiaryAge;
    private Integer issueId;
    private boolean issueSolved;
    private String issueMessage;

    public Transaction() {}

    public Transaction(
            int mtn,
            double amount,
            String senderFullName,
            int senderAge,
            String beneficiaryFullName,
            int beneficiaryAge,
            Integer issueId,
            boolean issueSolved,
            String issueMessage
    ) {
        this.mtn = mtn;
        this.amount = amount;
        this.senderFullName = senderFullName;
        this.senderAge = senderAge;
        this.beneficiaryFullName = beneficiaryFullName;
        this.beneficiaryAge = beneficiaryAge;
        this.issueId = issueId;
        this.issueSolved = issueSolved;
        this.issueMessage = issueMessage;
    }

    public boolean hasOpenIssue() {
        return hasIssue() && !isIssueSolved();
    }

    public int getMtn() {
        return mtn;
    }

    public double getAmount() {
        return amount;
    }

    public String getSenderFullName() {
        return senderFullName;
    }

    public int getSenderAge() {
        return senderAge;
    }

    public String getBeneficiaryFullName() {
        return beneficiaryFullName;
    }

    public int getBeneficiaryAge() {
        return beneficiaryAge;
    }

    public Integer getIssueId() {
        return issueId;
    }

    public boolean hasIssue() {
        return issueId != null;
    }

    public boolean isIssueSolved() {
        return issueSolved;
    }

    public String getIssueMessage() {
        return issueMessage;
    }

    public boolean hasClient(String clientFullName) {
        return Objects.equals(senderFullName, clientFullName) || Objects.equals(beneficiaryFullName, clientFullName);
    }

    @Override
    public String toString() {
        return String.format("MTN: %d%nAmount: %.2f%nSender: %s%nBeneficiary: %s%nIssue ID: %s%nIssue Solved: %b%nIssue Message: %s%n--------------------------%n",
                mtn, amount, senderFullName, beneficiaryFullName, issueId, issueSolved, Objects.requireNonNullElse(issueMessage, "N/A"));
    }
}
