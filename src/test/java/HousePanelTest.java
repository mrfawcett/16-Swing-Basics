import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class HousePanelTest {
    static { System.setProperty("java.awt.headless", "true"); }

    private static final int W = 400, H = 400;
    private static final Color SKY = new Color(135, 206, 235);
    private static final Color WALL = new Color(205, 133, 63);
    private static final Color DOOR = new Color(101, 67, 33);
    private BufferedImage img;

    @BeforeEach
    void paintIt() {
        HousePanel panel = new HousePanel();
        panel.setSize(W, H);
        img = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        panel.paint(g);
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

    @DisplayName("HousePanel: the sky (135, 206, 235) covers the whole panel where nothing else is drawn")
    @Test
    void house_Test01() {
        expect(SKY, 10, 10, "top-left corner should be sky");
        expect(SKY, 390, 10, "top-right corner should be sky");
        expect(SKY, 200, 50, "above the roof peak should be sky");
        expect(SKY, 50, 300, "left of the wall should be sky");
        expect(SKY, 350, 300, "right of the wall should be sky");
        expect(SKY, 200, 370, "below the wall should be sky");
        expect(SKY, 10, 390, "bottom-left corner should be sky - fill the whole panel, getWidth() x getHeight()");
    }

    @DisplayName("HousePanel: the wall fillRect(100, 200, 200, 150) is (205, 133, 63) inside")
    @Test
    void house_Test02() {
        expect(WALL, 250, 300, "the middle-right of the wall should be wall brown");
        expect(WALL, 110, 340, "just inside the wall's bottom-left corner");
        expect(WALL, 290, 210, "just inside the wall's top-right corner");
        expect(WALL, 170, 240, "wall between the window and the door");
        expect(WALL, 200, 260, "wall above the door (the door starts at y = 280)");
    }

    @DisplayName("HousePanel: the wall stops at x = 299 and y = 349 - sky just beyond")
    @Test
    void house_Test03() {
        expect(SKY, 99, 300, "x = 99 is just left of the wall");
        expect(SKY, 300, 300, "x = 300 is just right of the wall (100 + 200)");
        expect(SKY, 200, 350, "y = 350 is just below the wall (200 + 150)");
    }

    @DisplayName("HousePanel: the roof triangle (90,200) (310,200) (200,100) is RED inside")
    @Test
    void house_Test04() {
        expect(Color.RED, 200, 120, "just below the peak, on the centre line");
        expect(Color.RED, 200, 150, "centre of the roof");
        expect(Color.RED, 150, 190, "near the left end of the roof base");
        expect(Color.RED, 250, 190, "near the right end of the roof base");
        expect(Color.RED, 200, 195, "just above the wall on the centre line");
    }

    @DisplayName("HousePanel: outside the roof's slanted edges is still sky (a triangle, not a rectangle)")
    @Test
    void house_Test05() {
        expect(SKY, 100, 150, "left of the roof's left slope at y = 150 (the roof is only x 145..255 there)");
        expect(SKY, 300, 150, "right of the roof's right slope at y = 150");
        expect(SKY, 200, 95, "just above the peak (200, 100)");
        expect(SKY, 120, 120, "well outside the triangle near its upper-left");
        expect(SKY, 280, 120, "well outside the triangle near its upper-right");
    }

    @DisplayName("HousePanel: the roof overhangs the wall - RED at x = 96 and x = 304 just above the wall line")
    @Test
    void house_Test06() {
        expect(Color.RED, 96, 197, "the roof base starts at x = 90, ten pixels left of the wall (100)");
        expect(Color.RED, 304, 197, "the roof base ends at x = 310, ten pixels right of the wall (300)");
        expect(SKY, 96, 260, "below the overhang, left of the wall, is sky");
        expect(SKY, 304, 260, "below the overhang, right of the wall, is sky");
    }

    @DisplayName("HousePanel: the door fillRect(180, 280, 40, 70) is (101, 67, 33) and sits ON TOP of the wall")
    @Test
    void house_Test07() {
        expect(DOOR, 200, 320, "centre of the door should be door brown, not wall brown - draw the door AFTER the wall");
        expect(DOOR, 182, 282, "just inside the door's top-left corner");
        expect(DOOR, 217, 347, "just inside the door's bottom-right corner");
        expect(WALL, 179, 320, "x = 179 is wall, just left of the door");
        expect(WALL, 220, 320, "x = 220 is wall, just right of the door (180 + 40)");
        expect(WALL, 200, 279, "y = 279 is wall, just above the door");
    }

    @DisplayName("HousePanel: the window fillRect(120, 220, 40, 40) is YELLOW and sits ON TOP of the wall")
    @Test
    void house_Test08() {
        expect(Color.YELLOW, 140, 240, "centre of the window should be YELLOW - draw the window AFTER the wall");
        expect(Color.YELLOW, 122, 222, "just inside the window's top-left corner");
        expect(Color.YELLOW, 157, 257, "just inside the window's bottom-right corner");
        expect(WALL, 119, 240, "x = 119 is wall, just left of the window");
        expect(WALL, 160, 240, "x = 160 is wall, just right of the window (120 + 40)");
        expect(WALL, 140, 219, "y = 219 is wall, just above the window");
        expect(WALL, 140, 260, "y = 260 is wall, just below the window (220 + 40)");
    }

    @DisplayName("HousePanel: painter's order - the sky does not cover the house (sky first, then everything else)")
    @Test
    void house_Test09() {
        assertNotEquals(name(rgb(SKY)), name(at(250, 300)), "the wall must be drawn AFTER the sky, or the sky covers it");
        assertNotEquals(name(rgb(SKY)), name(at(200, 150)), "the roof must be drawn AFTER the sky");
        assertNotEquals(name(rgb(Color.WHITE)), name(at(10, 10)), "the corner is WHITE - the sky fill is missing (or super.paintComponent is missing and nothing painted)");
    }
}
