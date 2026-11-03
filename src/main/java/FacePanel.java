import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/** READ FIRST
 * A face: a yellow head, two black eyes and a smile. The lesson here is the
 * OVAL RULE - an oval is drawn inside a bounding BOX, not around a centre and
 * radius. fillOval(100, 100, 200, 200) is the circle that fills the box from
 * (100, 100) that is 200 wide and 200 tall; its centre is (200, 200) and its
 * radius is 100.
 *
 * The panel is 400 x 400, background white. Draw exactly this, in order:
 *
 *   part       colour        call                                    notes
 *   ---------  ------------  --------------------------------------  --------------------------------
 *   head       Color.YELLOW  g.fillOval(100, 100, 200, 200)          centre (200, 200), radius 100
 *   left eye   Color.BLACK   g.fillOval(150, 160, 30, 30)            centre (165, 175), radius 15
 *   right eye  Color.BLACK   g.fillOval(220, 160, 30, 30)            centre (235, 175), radius 15
 *   mouth      Color.BLACK   g.drawArc(150, 200, 100, 60, 0, -180)   the LOWER half of the oval in the
 *                                                                    box (150,200) 100x60 - a smile
 *
 * drawArc(x, y, w, h, start, sweep) draws part of the outline of the oval in
 * the box. Angles are in degrees; 0 is 3 o'clock and positive angles go
 * COUNTER-clockwise. Starting at 0 and sweeping -180 goes clockwise from
 * 3 o'clock, down through 6 o'clock, to 9 o'clock: the bottom half, a smile.
 * A sweep of +180 gives the top half - a frown.
 */
public class FacePanel extends JPanel {

    /** PROVIDED - do not change */
    public FacePanel() {
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.WHITE);
    }

    /** COMPLETE THIS METHOD
     * super.paintComponent(g) first, then the head, the two eyes and the
     * mouth from the table above.
     * Example: (200, 200) ends up YELLOW, (165, 175) BLACK, (200, 260) BLACK
     * (the bottom of the smile), (200, 240) YELLOW (the arc is an outline,
     * not filled) and (10, 10) WHITE.
     * Hint: draw the head first - the eyes and mouth go on top of it.
     */
    public void paintComponent(Graphics g) {
        // Insert your code below

    }
}
