import cab302softwaredevelopment.outbackweathertrackerapplication.models.Theme;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.PreferencesService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class PreferencesServiceTest {

    String fileContent;
    Path preferencesFilePath;

    @BeforeEach
    public void setUp() {
        try {
            preferencesFilePath = Paths.get("user_preferences.json");
            fileContent = new String(Files.readAllBytes(preferencesFilePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PreferencesService.loadPreferences();
    }

    @AfterEach
    public void cleanup() {
        try {
            Files.writeString(Paths.get("user_preferences.json"), fileContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetCurrentTheme_defaultTheme() {
        // Check if the default theme is Light
        assertEquals(Theme.Light, PreferencesService.getCurrentTheme());
    }

    @Test
    public void testSetCurrentTheme_updatesTheme() {
        // Change the theme to Dark
        PreferencesService.setCurrentTheme(Theme.Dark);
        assertEquals(Theme.Dark, PreferencesService.getCurrentTheme());
    }

    @Test
    public void testSavePreferences_createsPreferencesFile() {
        PreferencesService.savePreferences();
        assertTrue(Files.exists(preferencesFilePath));
    }

    @Test
    public void testGetLayouts_returnsValidLayouts() {
        String[] layouts = PreferencesService.getLayouts();
        assertNotNull(layouts);
        assertTrue(layouts.length > 0);
    }

    @Test
    public void testSetCurrentLayout_validLayout() {
        String[] layouts = PreferencesService.getLayouts();
        String validLayout = layouts[0];
        PreferencesService.setSelectedLayout(validLayout);
        assertEquals(validLayout, PreferencesService.getSelectedLayout());
    }
}
