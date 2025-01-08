import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CustomFont {
    
    Font customFont;
    FileInputStream fontInputStream;
    
    /**
     * Creates a new custom font with the specified font size
     *
     * @param size An integer representing the font size
     */
    public CustomFont(int size) {
        try {
            // Use class loader to access the resource from the 'resources' folder
            InputStream fontInputStream = getClass().getClassLoader().getResourceAsStream("flappy-font.ttf");

            // Check if the file was found
            if (fontInputStream == null) {
                throw new FileNotFoundException("Font file not found in resources folder");
            }

            customFont = Font.loadFont(fontInputStream, size);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public void setFontSize(int size) {
        customFont = Font.loadFont(fontInputStream, size);
    }
    
    public Font getCustomFont() {
        return customFont;
    }
}
