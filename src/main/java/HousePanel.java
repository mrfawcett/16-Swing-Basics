import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/** READ FIRST
 * The house from Turtle Basics, rebuilt with Graphics. No turtle, no loop, no
 * heading: every shape is one call with its own exact pixel numbers. That is
 * the trade - exact placement is trivial, and moving the house means editing
 * every number.
 *
 * The panel is 400 x 400. Draw exactly this, IN THIS ORDER (painter's order:
 * later shapes cover earlier ones, so the sky goes first and the window last):
 *
 *   #  part    colour                    call                                    covers pixels
 *   -  ------  ------------------------  --------------------------------------  ------------------------
 *   1  sky     new Color(135, 206, 235)  g.fillRect(0, 0, getWidth(), getHeight()) the whole panel
 *   2  wall    new Color(205, 133, 63)   g.fillRect(100, 200, 200, 150)          x 100..299, y 200..349
 *   3  roof    Color.RED                 g.fillPolygon(xs, ys, 3) with the        triangle, base y 200,
 *                                        points (90,200) (310,200) (200,100)      peak (200, 100)
 *   4  door    new Color(101, 67, 33)    g.fillRect(180, 280, 40, 70)            x 180..219, y 280..349
 *   5  window  Color.YELLOW              g.fillRect(120, 220, 40, 40)            x 120..159, y 220..259
 *
 * fillPolygon takes two int arrays and a count:
 *     int[] xs = {90, 310, 200};
 *     int[] ys = {200, 200, 100};
 *     g.fillPolygon(xs, ys, 3);
 * (Arrays are Unit 3; this is the one place you need them today, and this is
 * all you need to know: the i-th x and the i-th y are the i-th corner.)
 */
public class HousePanel extends JPanel {

    /** PROVIDED - do not change */
    public HousePanel() {
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.WHITE);
    }

    /** COMPLETE THIS METHOD
     * Draws the sky, wall, roof, door and window from the table above, in
     * that order. super.paintComponent(g) is the first line.
     *
     * Example: the pixel at (50, 50) ends up sky blue, (250, 300) wall brown,
     * (200, 150) red, (200, 320) door brown, (140, 240) yellow.
     * Hint: g.setColor(new Color(135, 206, 235)) - new Color(r, g, b) with
     * values 0 to 255 makes any colour you like.
     */
    public void paintComponent(Graphics g) {
        // Insert your code below

    }
}
