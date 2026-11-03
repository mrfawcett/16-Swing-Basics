import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class FacePanelTest {
    static { System.setProperty("java.awt.headless", "true"); }

    private static final int W = 400, H = 400;
    private BufferedImage img;

    @BeforeEach
    void paintIt() {
        FacePanel panel = new FacePanel();
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
    /** true if any pixel in the box x0..x1, y0..y1 (inclusive) has colour c */
    private boolean anyIn(Color c, int x0, int y0, int x1, int y1) {
        for (int y = y0; y <= y1; y++) {
            for (int x = x0; x <= x1; x++) {
                if (at(x, y) == rgb(c)) return true;
            }
        }
        return false;
    }

    @DisplayName("FacePanel: the background is WHITE where nothing is drawn - super.paintComponent(g) first")
    @Test
    void face_Test01() {
        expect(Color.WHITE, 10, 10, "top-left corner should be white");
        expect(Color.WHITE, 390, 390, "bottom-right corner should be white");
        expect(Color.WHITE, 200, 95, "just above the head (its box starts at y = 100)");
        expect(Color.WHITE, 95, 200, "just left of the head (its box starts at x = 100)");
    }

    @DisplayName("FacePanel: the head fillOval(100, 100, 200, 200) is YELLOW - centre (200, 200), radius 100")
    @Test
    void face_Test02() {
        expect(Color.YELLOW, 200, 200, "centre of the head");
        expect(Color.YELLOW, 200, 105, "5 px inside the top of the circle");
        expect(Color.YELLOW, 200, 295, "5 px inside the bottom of the circle");
        expect(Color.YELLOW, 105, 200, "5 px inside the left of the circle");
        expect(Color.YELLOW, 295, 200, "5 px inside the right of the circle");
    }

    @DisplayName("FacePanel: the head is a circle inside its box, not a square - the box corners stay WHITE")
    @Test
    void face_Test03() {
        expect(Color.WHITE, 105, 105, "the top-left corner of the oval's box is outside the circle (fillOval, not fillRect)");
        expect(Color.WHITE, 295, 105, "the top-right corner of the box is outside the circle");
        expect(Color.WHITE, 105, 295, "the bottom-left corner of the box is outside the circle");
        expect(Color.WHITE, 295, 295, "the bottom-right corner of the box is outside the circle");
    }

    @DisplayName("FacePanel: the eyes fillOval(150,160,30,30) and fillOval(220,160,30,30) are BLACK and filled")
    @Test
    void face_Test04() {
        expect(Color.BLACK, 165, 175, "centre of the left eye (150 + 15, 160 + 15) - fillOval, not drawOval");
        expect(Color.BLACK, 235, 175, "centre of the right eye (220 + 15, 160 + 15)");
        expect(Color.BLACK, 160, 170, "inside the left eye, up and left of centre");
        expect(Color.BLACK, 240, 180, "inside the right eye, down and right of centre");
    }

    @DisplayName("FacePanel: between and around the eyes is still YELLOW - eyes are 30 wide, 70 apart")
    @Test
    void face_Test05() {
        expect(Color.YELLOW, 200, 175, "between the eyes (left eye ends at x = 179, right eye starts at x = 220)");
        expect(Color.YELLOW, 165, 150, "above the left eye (its box starts at y = 160)");
        expect(Color.YELLOW, 235, 150, "above the right eye");
        expect(Color.YELLOW, 145, 175, "left of the left eye (its box starts at x = 150)");
        expect(Color.YELLOW, 255, 175, "right of the right eye (its box ends at x = 249)");
    }

    @DisplayName("FacePanel: the mouth drawArc(150, 200, 100, 60, 0, -180) has its bottom at (200, 260)")
    @Test
    void face_Test06() {
        assertTrue(anyIn(Color.BLACK, 196, 257, 204, 263),
                "expected a black arc pixel near (200, 260), the bottom of the smile - is the mouth drawn, in BLACK?");
    }

    @DisplayName("FacePanel: the mouth's two ends are at (150, 230) and (250, 230) - the box is 100 wide, 60 tall, at (150, 200)")
    @Test
    void face_Test07() {
        assertTrue(anyIn(Color.BLACK, 147, 226, 154, 234),
                "expected a black arc pixel near (150, 230), the left end of the smile");
        assertTrue(anyIn(Color.BLACK, 246, 226, 253, 234),
                "expected a black arc pixel near (250, 230), the right end of the smile");
    }

    @DisplayName("FacePanel: the smile is the LOWER half (sweep -180) - nothing black along the top of the box")
    @Test
    void face_Test08() {
        assertFalse(anyIn(Color.BLACK, 190, 197, 210, 205),
                "black near (200, 200) means the arc is the TOP half - a frown. Sweep -180 from 0, not +180");
    }

    @DisplayName("FacePanel: the mouth is an outline - the inside of the arc's box is still YELLOW (drawArc, not fillArc)")
    @Test
    void face_Test09() {
        expect(Color.YELLOW, 200, 240, "inside the mouth's box should be yellow - drawArc draws only the curve");
        expect(Color.YELLOW, 200, 230, "centre of the arc's box should be yellow");
        expect(Color.YELLOW, 175, 235, "left-centre of the arc's box should be yellow");
    }
}
