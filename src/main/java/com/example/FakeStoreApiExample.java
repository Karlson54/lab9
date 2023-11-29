package com.example;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class FakeStoreApiExample {
    public static void main(String[] args) {
        List<String> endpoints = Arrays.asList(
                "https://fakestoreapi.com/products",
                "https://fakestoreapi.com/users",
                "https://fakestoreapi.com/products/categories",
                "https://fakestoreapi.com/carts"
        );
        saveDataToExcel(endpoints, "output.xlsx");
    }

    public static void saveDataToExcel(List<String> endpoints, String fileName) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data from endpoints");

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < endpoints.size(); i++) {
                Cell headerCell = headerRow.createCell(i);
                headerCell.setCellValue("Data from " + endpoints.get(i));
            }

            for (int i = 0; i < endpoints.size(); i++) {
                String endpoint = endpoints.get(i);
                List<String> data = fetchDataFromEndpoint(endpoint);

                for (int j = 0; j < data.size(); j++) {
                    Row dataRow = sheet.getRow(j + 1);
                    if (dataRow == null) {
                        dataRow = sheet.createRow(j + 1);
                    }
                    Cell cell = dataRow.createCell(i);
                    cell.setCellValue(data.get(j));
                }
            }

            try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
                workbook.write(fileOut);
                System.out.println("Data saved to Excel file: " + fileName);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> fetchDataFromEndpoint(String url) {
        HttpClient httpClient = HttpClients.createDefault();

        try {
            URI uri = URI.create(url);
            HttpGet request = new HttpGet(uri);
            HttpResponse response = httpClient.execute(request);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                String line;
                StringBuilder result = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                System.out.println("Response from " + uri + ":");
                System.out.println(result.toString());
                System.out.println("------------------------------");

                return Arrays.asList(result.toString().split("\n"));

            }

        } catch (Exception e) {
            e.printStackTrace();
            return Arrays.asList("Failed to fetch data from " + url);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
}
