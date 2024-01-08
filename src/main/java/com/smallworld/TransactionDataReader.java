package com.smallworld;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class TransactionDataReader {
    private List<Transaction> data;
    public List<Transaction> readTransactionsFromFile(String filePath) {

        try {
            if (this.data == null || this.data.isEmpty() ){
                ObjectMapper objectMapper = new ObjectMapper();
                return List.of(objectMapper.readValue(Paths.get(filePath).toFile(), Transaction[].class));
            }
            return this.data;

        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
