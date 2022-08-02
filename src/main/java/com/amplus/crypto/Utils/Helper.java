package com.amplus.crypto.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class Helper {
    public  static  byte[] getKeyFileContentFromNameAsByteArray(String fileName){
        try (InputStream inputStream = Helper.class.getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String contents = reader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));
            byte[] bytes = contents.getBytes(StandardCharsets.UTF_8);
            return  bytes;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not read public and private keys from file name");
            return null;
        }
    }
}
