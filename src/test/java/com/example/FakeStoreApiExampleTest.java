package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

public class FakeStoreApiExampleTest {
    private FakeStoreApiExample fakeStoreApiExample;

    @BeforeEach
    public void setUp() {
        fakeStoreApiExample = new FakeStoreApiExample();
    }

    @Test
    public void testFetchDataFromEndpoint() {
        String endpoint = "https://fakestoreapi.com/products";
        List<String> data = fakeStoreApiExample.fetchDataFromEndpoint(endpoint);

        assertNotNull(data);
        assertFalse(data.isEmpty());
    }

    @Test
    public void testSaveDataToExcel() {
        List<String> endpoints = Arrays.asList(
                "https://fakestoreapi.com/products",
                "https://fakestoreapi.com/users"
        );

        String fileName = "test_output.xlsx";
        fakeStoreApiExample.saveDataToExcel(endpoints, fileName);
    }
}
