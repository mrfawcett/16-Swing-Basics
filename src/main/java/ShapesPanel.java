import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/** READ FIRST
 * Your first Swing canvas. A JPanel is the paper; the JFrame in Main is the
 * picture frame. All drawing lives in paintComponent(Graphics g), which the
 * SYSTEM calls - you never call it yourself, and you never construct a
 * Graphics object. When the window opens, resizes or is uncovered, Swing
 * hands you a g and runs your method.
 *
 * The panel is 400 wide and 300 tall. (0, 0) is the TOP-LEFT corner, x grows
 * to the right and y grows DOWN - the same as Turtle.moveTo.
 *
 * Draw exactly this (the autograder reads pixels):
 *
 *   colour       call                          covers pixels
 *   -----------  ----------------------------  --------------------------------
 *   white        background (setBackground)    everything nothing else covers
 *   Color.RED    g.fillRect(50, 50, 100, 80)   x 50..149,  y 50..129
 *   Color.BLUE   g.fillOval(200, 50, 100, 100) the circle inside the box
 *                                              x 200..299, y 50..149, centre (250,100)
 *   Color.GREEN  g.fillRect(50, 200, 300, 40)  x 50..349,  y 200..239
 *
 * "extends JPanel" and overriding paintComponent are inheritance - a Unit 4
 * idea you are borrowing today because Swing gives you no other way in. The
 * formal version arrives in January. Today, just follow the shape.
 */
public class ShapesPanel extends JPanel {

    /** PROVIDED - do not change
     * Sets the panel's size and its background colour. super.paintComponent(g)
     * is what actually paints that background - which is why you must call it.
     */
    public ShapesPanel() {
        setPreferredSize(new Dimension(400, 300));
        setBackground(Color.WHITE);
    }

    /** COMPLETE THIS METHOD
     * Draws the three shapes in the table above.
     *
     * The FIRST line must be super.paintComponent(g); - it clears the panel
     * and paints the white background. Skip it and old drawings smear across
     * the screen, and the background test fails.
     *
     * Then, for each shape: g.setColor(...) and then the fill call. setColor
     * is a MODE - the colour stays until you change it, so a shape drawn
     * without its own setColor comes out in whatever colour was set last.
     *
     * Example: the pixel at (100, 90) ends up RED, (250, 100) BLUE,
     * (200, 220) GREEN and (10, 10) WHITE.
     */
    public void paintComponent(Graphics g) {
        // Insert your code below

    }
}
