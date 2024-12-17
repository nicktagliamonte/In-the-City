package com.nicktagliamonte.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Introduction {

    @Expose public String[] introLines;

    public Introduction(String jsonFilePath) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(jsonFilePath), "UTF-8")) {
            introLines = gson.fromJson(reader, String[].class);
        } catch (IOException e) {
            e.printStackTrace();
            introLines = new String[]{};
        }
    }

    public void display() {
        if (introLines.length == 0) {
            System.out.println("The journey begins here:");
            return;
        }

        for (int i = 0; i < introLines.length; i++) {
            if (i == 0) {
                displaySlow();
                i++;
            } else {
                System.out.println(introLines[i]);
            }
            
            try {
                Thread.sleep(2300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void displaySlow() {
        char[] chars = introLines[0].toCharArray();
        for (char letter : chars) {
            System.out.print(letter);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("\n" + introLines[1]);
    }
}