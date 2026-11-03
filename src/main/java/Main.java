import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * PROVIDED - you do not need to change this file.
 *
 * Opens one window per panel so you can look at your drawings. This is the
 * eleven-line Swing template from lecture 2.9, wrapped in a method so it is
 * written once instead of four times: build the panel, build the frame, add,
 * show. setVisible(true) goes LAST.
 *
 * The whole thing is guarded by GraphicsEnvironment.isHeadless(): on a machine
 * with no screen (the autograder) main prints one line and exits. The
 * autograder never runs Main anyway - it only runs the tests, and the tests
 * paint your panels into an off-screen image and read the pixels.
 */
public class Main {

    /** The 2.9 template: panel in, window up. */
    private static void show(JPanel panel, String title, int x, int y) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();                    // size the window to the panel's preferred size
        frame.setLocation(x, y);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("No screen here - run the tests instead: mvn test");
            return;
        }

        int[] values = {40, 90, 25, 70, 55};

        show(new ShapesPanel(), "Shapes", 40, 40);
        show(new HousePanel(), "House", 480, 40);
        show(new BarChartPanel(values), "Bar Chart", 40, 400);
        show(new FacePanel(), "Face", 480, 480);
    }
}
