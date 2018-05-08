package com.esie.core.configurationSingleton;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConfigurationSingletonTest {

    @Test
    void getInstance() {
        ConfigurationSingleton testConfig = ConfigurationSingleton.getInstance("src/resources/configuration.properties");
        boolean b = testConfig != null;
        assertTrue(b);
    }

    @Test
    void getValue() {
        ConfigurationSingleton testConfig = ConfigurationSingleton.getInstance("src/resources/configuration.properties");
        boolean b = Boolean.parseBoolean(testConfig.getValue("TEST_VALUE"));
        int i = Integer.parseInt(testConfig.getValue("TEST_VALUE_1"));
        if (b) i++;
        assertEquals(18,i);
    }
}