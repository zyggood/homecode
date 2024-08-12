package com.youcheng.demo.service;

import com.youcheng.demo.controller.vo.AddUserVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;
    private static final String TEST_FILE = "test_access.txt";

    @BeforeEach
    void setUp() {
        userService = Mockito.spy(new UserService());
    }

    @Test
    void testAddUserAccess() throws IOException {
        AddUserVo addUserVo = new AddUserVo();
        addUserVo.setUserId(123456L);
        addUserVo.setEndpoint(Arrays.asList("resource A", "resource B"));

        userService.addUserAccess(addUserVo);

        // Verify
        Map<String, List<String>> accessMap = userService.loadAccessData();
        assertTrue(accessMap.containsKey("123456"));
        assertEquals(Arrays.asList("resource A", "resource B"), accessMap.get("123456"));
    }

    @Test
    void testCheckUserAccess_Granted() throws IOException {
        AddUserVo addUserVo = new AddUserVo();
        addUserVo.setUserId(123456L);
        addUserVo.setEndpoint(Arrays.asList("resource A", "resource B"));

        userService.addUserAccess(addUserVo);

        boolean hasAccess = userService.checkUserAccess("123456", "resource A");

        // Assert
        assertTrue(hasAccess);
    }

    @Test
    void testCheckUserAccess_Denied() throws IOException {
        AddUserVo addUserVo = new AddUserVo();
        addUserVo.setUserId(123456L);
        addUserVo.setEndpoint(Arrays.asList("resource A", "resource B"));

        userService.addUserAccess(addUserVo);

        boolean hasAccess = userService.checkUserAccess("123456L", "resource C");

        // Assert
        assertFalse(hasAccess);
    }

    @Test
    void testLoadAccessData() throws IOException {
        AddUserVo addUserVo = new AddUserVo();
        addUserVo.setUserId(123456L);
        addUserVo.setEndpoint(Arrays.asList("resource A", "resource B"));

        userService.addUserAccess(addUserVo);

        Map<String, List<String>> accessMap = userService.loadAccessData();

        // Assert
        assertNotNull(accessMap);
        assertTrue(accessMap.containsKey("123456"));
    }

    @Test
    void testSaveAccessData() throws IOException {
        Map<String, List<String>> accessMap = new HashMap<>();
        accessMap.put("123456L", Arrays.asList("resource A", "resource B"));

        userService.saveAccessData(accessMap);

        // Verify
        Map<String, List<String>> loadedData = userService.loadAccessData();
        assertEquals(accessMap, loadedData);
    }
}