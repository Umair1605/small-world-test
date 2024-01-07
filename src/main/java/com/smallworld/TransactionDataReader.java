package com.smallworld;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionDataReader {

    private static final Logger logger = Logger.getLogger(TransactionDataReader.class.getName());

    public List<Transaction> readTransactionsFromFile(String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return List.of(objectMapper.readValue(Paths.get(filePath).toFile(), Transaction[].class));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading transactions from file", e);
            return Collections.emptyList();
        }
    }
}
