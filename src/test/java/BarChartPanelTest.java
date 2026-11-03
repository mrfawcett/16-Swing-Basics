import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class BarChartPanelTest {
    static { System.setProperty("java.awt.headless", "true"); }

    private static final int W = 400, H = 300;

    private static BufferedImage paint(int[] values) {
        BarChartPanel panel = new BarChartPanel(values);
        panel.setSize(W, H);
        BufferedImage img = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        panel.paint(g);
        g.dispose();
        return img;
    }

    private static int at(BufferedImage img, int x, int y) { return img.getRGB(x, y) & 0xFFFFFF; }
    private static int rgb(Color c) { return c.getRGB() & 0xFFFFFF; }
    /** "rgb(r,g,b)" so a failure reads as colours, not as one big int */
    private static String name(int packed) {
        return "rgb(" + ((packed >> 16) & 255) + "," + ((packed >> 8) & 255) + "," + (packed & 255) + ")";
    }
    private static void expect(BufferedImage img, Color c, int x, int y, String why) {
        assertEquals(name(rgb(c)), name(at(img, x, y)), "pixel (" + x + ", " + y + ") " + why);
    }

    private static final int[] LECTURE = {40, 90, 25, 70, 55};

    @DisplayName("BarChartPanel: the background is WHITE where nothing is drawn - super.paintComponent(g) first")
    @Test
    void bars_Test01() {
        BufferedImage img = paint(LECTURE);
        expect(img, Color.WHITE, 10, 10, "top-left corner should be white");
        expect(img, Color.WHITE, 390, 10, "top-right corner should be white");
        expect(img, Color.WHITE, 200, 295, "below the baseline should be white");
        expect(img, Color.WHITE, 330, 270, "there is no 6th bar, so x = 330 is white");
    }

    @DisplayName("BarChartPanel {40, 90, 25, 70, 55}: every bar is BLUE at its centre")
    @Test
    void bars_Test02() {
        BufferedImage img = paint(LECTURE);
        expect(img, Color.BLUE, 40, 240, "bar 0 (value 40) covers x 20..59, y 200..279");
        expect(img, Color.BLUE, 100, 150, "bar 1 (value 90) covers x 80..119, y 100..279");
        expect(img, Color.BLUE, 160, 255, "bar 2 (value 25) covers x 140..179, y 230..279");
        expect(img, Color.BLUE, 220, 200, "bar 3 (value 70) covers x 200..239, y 140..279");
        expect(img, Color.BLUE, 280, 220, "bar 4 (value 55) covers x 260..299, y 170..279");
    }

    @DisplayName("BarChartPanel {40, 90, 25, 70, 55}: bar heights are value * 2 - just inside the top is blue, just above is white")
    @Test
    void bars_Test03() {
        BufferedImage img = paint(LECTURE);
        expect(img, Color.BLUE, 40, 202, "bar 0 top is y = 280 - 80 = 200, so y = 202 is blue");
        expect(img, Color.WHITE, 40, 198, "y = 198 is above bar 0");
        expect(img, Color.BLUE, 100, 102, "bar 1 top is y = 280 - 180 = 100");
        expect(img, Color.WHITE, 100, 98, "y = 98 is above bar 1");
        expect(img, Color.BLUE, 160, 232, "bar 2 top is y = 280 - 50 = 230");
        expect(img, Color.WHITE, 160, 228, "y = 228 is above bar 2");
    }

    @DisplayName("BarChartPanel {40, 90, 25, 70, 55}: bars are 40 wide and 60 apart - the gaps between them are white")
    @Test
    void bars_Test04() {
        BufferedImage img = paint(LECTURE);
        expect(img, Color.WHITE, 70, 270, "the gap between bar 0 (ends x = 59) and bar 1 (starts x = 80)");
        expect(img, Color.WHITE, 130, 270, "the gap between bars 1 and 2");
        expect(img, Color.WHITE, 190, 270, "the gap between bars 2 and 3");
        expect(img, Color.WHITE, 250, 270, "the gap between bars 3 and 4");
        expect(img, Color.WHITE, 15, 270, "left of bar 0 (starts x = 20)");
        expect(img, Color.BLUE, 22, 270, "x = 22 is just inside bar 0's left edge");
        expect(img, Color.BLUE, 57, 270, "x = 57 is just inside bar 0's right edge (20 + 40 - 1 = 59)");
        expect(img, Color.WHITE, 60, 270, "x = 60 is just outside bar 0");
    }

    @DisplayName("BarChartPanel: the baseline drawLine(10, 280, 390, 280) is BLACK along row 280")
    @Test
    void bars_Test05() {
        BufferedImage img = paint(LECTURE);
        expect(img, Color.BLACK, 12, 280, "near the left end of the baseline");
        expect(img, Color.BLACK, 70, 280, "the baseline runs through the gaps between bars");
        expect(img, Color.BLACK, 200, 280, "the baseline runs under the bars (bars stop at row 279)");
        expect(img, Color.BLACK, 388, 280, "near the right end of the baseline");
        expect(img, Color.WHITE, 5, 280, "x = 5 is before the line starts at x = 10");
        expect(img, Color.WHITE, 395, 280, "x = 395 is after the line ends at x = 390");
        expect(img, Color.WHITE, 70, 281, "row 281 is below the baseline");
        expect(img, Color.WHITE, 70, 279, "row 279 in a gap is white - the line is one pixel tall");
    }

    @DisplayName("BarChartPanel {10}: uses the array it was GIVEN - one short bar and nothing where bar 1 would be")
    @Test
    void bars_Test06() {
        BufferedImage img = paint(new int[] {10});
        expect(img, Color.BLUE, 40, 270, "bar 0 (value 10) covers y 260..279");
        expect(img, Color.WHITE, 40, 258, "y = 258 is above a bar of height 20");
        expect(img, Color.WHITE, 100, 270, "no second value, so no second bar - use the field, not a fixed array");
        expect(img, Color.WHITE, 100, 150, "and certainly nothing tall at x = 100");
        expect(img, Color.BLACK, 200, 280, "the baseline is still there");
    }

    @DisplayName("BarChartPanel {}: an empty array draws only the baseline")
    @Test
    void bars_Test07() {
        BufferedImage img = paint(new int[0]);
        for (int i = 0; i < 6; i++) {
            expect(img, Color.WHITE, 40 + i * 60, 270, "no values means no bar " + i);
        }
        expect(img, Color.BLACK, 200, 280, "the baseline is drawn even with no bars");
        expect(img, Color.WHITE, 10, 10, "background");
    }

    @DisplayName("BarChartPanel {0, 30, 130}: a value of 0 draws no bar; 130 reaches y = 20")
    @Test
    void bars_Test08() {
        BufferedImage img = paint(new int[] {0, 30, 130});
        expect(img, Color.WHITE, 40, 278, "value 0 is a bar of height 0 - nothing, even just above the baseline");
        expect(img, Color.BLUE, 100, 250, "bar 1 (value 30) covers y 220..279");
        expect(img, Color.WHITE, 100, 218, "y = 218 is above bar 1");
        expect(img, Color.BLUE, 160, 22, "bar 2 (value 130) reaches up to y = 280 - 260 = 20");
        expect(img, Color.WHITE, 160, 18, "y = 18 is above bar 2");
        expect(img, Color.BLACK, 40, 280, "baseline under the empty slot");
    }
}
