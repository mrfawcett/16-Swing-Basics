import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ShapesPanelTest {
    static { System.setProperty("java.awt.headless", "true"); }

    private static final int W = 400, H = 300;
    private BufferedImage img;

    @BeforeEach
    void paintIt() {
        ShapesPanel panel = new ShapesPanel();
        panel.setSize(W, H);
        img = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        panel.paint(g);            // the system's way in: paint -> paintComponent
        g.dispose();
    }

    private int at(int x, int y) { return img.getRGB(x, y) & 0xFFFFFF; }
    private static int rgb(Color c) { return c.getRGB() & 0xFFFFFF; }
    /** "rgb(r,g,b)" so a failure reads as colours, not as one big int */
    private static String name(int packed) {
        return "rgb(" + ((packed >> 16) & 255) + "," + ((packed >> 8) & 255) + "," + (packed & 255) + ")";
    }
    private void expect(Color c, int x, int y, String why) {
        assertEquals(name(rgb(c)), name(at(x, y)), "pixel (" + x + ", " + y + ") " + why);
    }

    @DisplayName("ShapesPanel: the background is WHITE where nothing is drawn - super.paintComponent(g) must be the first line")
    @Test
    void shapes_Test01() {
        expect(Color.WHITE, 10, 10, "top-left corner should be the white background");
        expect(Color.WHITE, 390, 290, "bottom-right corner should be the white background");
        expect(Color.WHITE, 175, 100, "between the rectangle and the circle should be white");
        expect(Color.WHITE, 200, 170, "between the circle and the green bar should be white");
    }

    @DisplayName("ShapesPanel: fillRect(50, 50, 100, 80) is RED inside")
    @Test
    void shapes_Test02() {
        expect(Color.RED, 100, 90, "centre of the rectangle should be RED");
        expect(Color.RED, 52, 52, "just inside the top-left corner should be RED");
        expect(Color.RED, 147, 127, "just inside the bottom-right corner should be RED");
    }

    @DisplayName("ShapesPanel: the red rectangle stops at x = 149 and y = 129 - width 100, height 80, not two corners")
    @Test
    void shapes_Test03() {
        expect(Color.WHITE, 150, 90, "x = 150 is just outside the rectangle (50 + 100 = 150 is the first column NOT covered)");
        expect(Color.WHITE, 100, 130, "y = 130 is just outside the rectangle (50 + 80 = 130)");
        expect(Color.WHITE, 49, 90, "x = 49 is just left of the rectangle");
        expect(Color.WHITE, 100, 49, "y = 49 is just above the rectangle");
    }

    @DisplayName("ShapesPanel: fillOval(200, 50, 100, 100) is BLUE - centre (250, 100), radius 50")
    @Test
    void shapes_Test04() {
        expect(Color.BLUE, 250, 100, "centre of the circle should be BLUE (its own setColor, or it comes out RED)");
        expect(Color.BLUE, 250, 60, "10 px below the top of the circle");
        expect(Color.BLUE, 250, 140, "10 px above the bottom of the circle");
        expect(Color.BLUE, 210, 100, "10 px inside the left edge");
        expect(Color.BLUE, 290, 100, "10 px inside the right edge");
    }

    @DisplayName("ShapesPanel: the circle is an OVAL in a box - the box's corners stay white, and nothing is drawn beyond the box")
    @Test
    void shapes_Test05() {
        expect(Color.WHITE, 205, 55, "the top-left corner of the oval's bounding box is outside the circle");
        expect(Color.WHITE, 295, 145, "the bottom-right corner of the oval's bounding box is outside the circle");
        expect(Color.WHITE, 250, 45, "above the circle");
        expect(Color.WHITE, 250, 155, "below the circle");
        expect(Color.WHITE, 195, 100, "left of the circle");
        expect(Color.WHITE, 305, 100, "right of the circle");
    }

    @DisplayName("ShapesPanel: fillRect(50, 200, 300, 40) is GREEN inside")
    @Test
    void shapes_Test06() {
        expect(Color.GREEN, 200, 220, "centre of the bar should be GREEN");
        expect(Color.GREEN, 52, 202, "just inside the top-left corner of the bar");
        expect(Color.GREEN, 347, 237, "just inside the bottom-right corner of the bar");
    }

    @DisplayName("ShapesPanel: the green bar spans x 50..349 and y 200..239 exactly")
    @Test
    void shapes_Test07() {
        expect(Color.WHITE, 200, 199, "y = 199 is just above the bar");
        expect(Color.WHITE, 200, 240, "y = 240 is just below the bar (200 + 40 = 240)");
        expect(Color.WHITE, 49, 220, "x = 49 is just left of the bar");
        expect(Color.WHITE, 350, 220, "x = 350 is just right of the bar (50 + 300 = 350)");
    }

    @DisplayName("ShapesPanel: the shapes are FILLED, not outlined (fillRect / fillOval, not drawRect / drawOval)")
    @Test
    void shapes_Test08() {
        expect(Color.RED, 75, 70, "a point inside the rectangle but away from its edges must be RED");
        expect(Color.BLUE, 235, 115, "a point inside the circle but away from its edge must be BLUE");
        expect(Color.GREEN, 120, 230, "a point inside the bar but away from its edges must be GREEN");
    }
}
