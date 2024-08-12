package com.youcheng.demo.service;

import com.youcheng.demo.controller.vo.AddUserVo;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private static final String ACCESS_FILE = "access.txt";


    public void addUserAccess(AddUserVo body) throws IOException {
        String userId = String.valueOf(body.getUserId());
        List<String> resources = body.getEndpoint();

        Map<String, List<String>> accessMap = loadAccessData();
        accessMap.put(userId, resources);

        saveAccessData(accessMap);
    }

    public boolean checkUserAccess(String userId, String resource) throws IOException {
        Map<String, List<String>> accessMap = loadAccessData();
        List<String> userResources = accessMap.get(userId);

        return userResources != null && userResources.contains(resource);

    }


    Map<String, List<String>> loadAccessData() throws IOException {
        Map<String, List<String>> accessMap = new HashMap<>();

        if (Files.exists(Paths.get(ACCESS_FILE))) {
            try (BufferedReader reader = new BufferedReader(new FileReader(ACCESS_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    String userId = parts[0];
                    List<String> resources = Arrays.asList(parts[1].split(","));
                    accessMap.put(userId, resources);
                }
            }
        }

        return accessMap;
    }

    void saveAccessData(Map<String, List<String>> accessMap) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCESS_FILE))) {
            for (Map.Entry<String, List<String>> entry : accessMap.entrySet()) {
                writer.write(entry.getKey() + ":" + String.join(",", entry.getValue()));
                writer.newLine();
            }
        }
    }
}