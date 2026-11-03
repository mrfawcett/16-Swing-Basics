import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/** READ FIRST
 * A bar chart: one blue bar per value in an int[] and a black baseline. This
 * is the panel where Graphics makes you do the arithmetic yourself - there is
 * no forward() and turnRight(), you compute every x and y.
 *
 * The panel is 400 x 300. The baseline is at y = 280. Bars stand ON the
 * baseline and grow upward (smaller y) - each value is doubled to get pixels.
 *
 *   bar i:   g.fillRect(20 + i * 60,  280 - values[i] * 2,  40,  values[i] * 2)   in Color.BLUE
 *   baseline: g.drawLine(10, 280, 390, 280)                                        in Color.BLACK
 *
 * With values {40, 90, 25, 70, 55}:
 *
 *   i  value  x (left)  height  top y   pixels covered
 *   -  -----  --------  ------  -----   ------------------------
 *   0   40      20        80     200    x 20..59,   y 200..279
 *   1   90      80       180     100    x 80..119,  y 100..279
 *   2   25     140        50     230    x 140..179, y 230..279
 *   3   70     200       140     140    x 200..239, y 140..279
 *   4   55     260       110     170    x 260..299, y 170..279
 *
 * A value of 0 draws a bar of height 0 - nothing - and that is correct.
 *
 * Arrays are Unit 3. Everything you need today: values.length is how many,
 * values[i] is the i-th one, and for (int i = 0; i < values.length; i++)
 * visits them all.
 */
public class BarChartPanel extends JPanel {

    // Declared for you; null until the constructor stores the parameter.
    private int[] values;    // one bar per entry

    /** COMPLETE THIS METHOD
     * Precondition: values is not null; every value is between 0 and 130
     * Stores values in the field. The two provided lines set the panel size
     * and background - leave them.
     * Example: new BarChartPanel(new int[] {40, 90}) will draw two bars.
     */
    public BarChartPanel(int[] values) {
        setPreferredSize(new Dimension(400, 300));
        setBackground(Color.WHITE);
        // Insert your code below

    }

    /** COMPLETE THIS METHOD
     * super.paintComponent(g) first. Then one blue bar per value using the
     * formula above, then the black baseline. Use the FIELD, not a fixed
     * array - the tests build charts with several different arrays.
     * Example: with {40, 90, 25, 70, 55} the pixel (100, 150) is BLUE,
     * (70, 270) is WHITE (the gap between bars 0 and 1) and (200, 280) is
     * BLACK (the baseline).
     * Hint: the bars stop at row 279; the baseline is row 280. Draw the
     * baseline after the bars or before them - it makes no difference here.
     */
    public void paintComponent(Graphics g) {
        // Insert your code below

    }
}
