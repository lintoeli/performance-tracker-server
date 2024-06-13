package com.diversolab.security;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Token {

    private String token;

    public Token() {
        loadToken();
    }

    private void loadToken() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(".env"));
            for (String line : lines) {
                if (line.startsWith("GITHUB_TOKEN=")) {
                    this.token = line.substring(13).trim();  // "GITHUB_TOKEN=" tiene 13 caracteres
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            this.token = null; // o alguna acción por defecto
        }
    }

    public String getToken() {
        return this.token;
    }
}
