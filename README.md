# Swing Basics

**Unit 2 — Graphics, GUI & Applied Class Design** · Pairs with lecture 2.9 Swing Basics

Your first real windows. A `JFrame` is the picture frame; a `JPanel` is the
paper; `paintComponent(Graphics g)` is the method the *system* calls to draw
you. You will write four panels — three shapes, the house from Turtle Basics
rebuilt in Swing, a bar chart driven by an array, and a face — and every one
of them is a list of exact pixel coordinates. That is the trade: Turtle was
relative and stateful; Graphics is absolute, and exact placement is trivial.

Two rules from the lecture run the whole assignment:

1. **`super.paintComponent(g)` is the first line** of every `paintComponent`.
   It clears the panel and paints the background. Skip it and the background
   test fails (and, on a real screen, old drawings smear).
2. **You never call `paintComponent`.** Swing calls it when the window opens.
   The tests call it for you, off-screen, and read the pixels that come out.

`extends JPanel` and overriding `paintComponent` are inheritance — a Unit 4
idea you are borrowing today because Swing gives you no other way in. Follow
the shape; the formal version arrives in January.

## What you are given

| File | Status | Purpose |
|---|---|---|
| `src/main/java/ShapesPanel.java` | **you complete this** | `paintComponent`: a rectangle, a circle, a bar |
| `src/main/java/HousePanel.java` | **you complete this** | `paintComponent`: sky, wall, roof, door, window |
| `src/main/java/BarChartPanel.java` | **you complete this** | constructor stores an `int[]`; `paintComponent` draws one bar per value |
| `src/main/java/FacePanel.java` | **you complete this** | `paintComponent`: ovals in boxes and an arc |
| `src/main/java/Main.java` | provided | opens one window per panel when there is a screen |
| `src/test/java/*Test.java` | provided | the autograder's tests — read them |
| `pom.xml`, `grading.json` | provided | build and grading setup — do not edit |

Each panel's constructor is provided: it sets the preferred size and calls
`setBackground(Color.WHITE)`. `super.paintComponent(g)` is what actually paints
that white.

## What to write

| Class | Points | What it draws |
|---|---|---|
| `ShapesPanel` (400 × 300) | 20 | red `fillRect`, blue `fillOval`, green `fillRect` |
| `HousePanel` (400 × 400) | 35 | sky, wall, roof polygon, door, window — in that order |
| `BarChartPanel` (400 × 300) | 25 | a blue bar per value in the array, a black baseline |
| `FacePanel` (400 × 400) | 20 | yellow head, two black eyes, a black smile arc |

### The coordinate system

`(0, 0)` is the **top-left** of the panel. `x` grows right, `y` grows **down**
— exactly like `Turtle.moveTo`. `fillRect(x, y, w, h)` covers columns `x`
through `x + w - 1` and rows `y` through `y + h - 1`. `fillOval(x, y, w, h)`
fills the oval that fits in that same box — there is no centre-and-radius
version; to centre a circle at `(cx, cy)` with radius `r` you write
`fillOval(cx - r, cy - r, 2 * r, 2 * r)`.

### `ShapesPanel` — 400 × 300

| colour | call | covers |
|---|---|---|
| `Color.WHITE` | background (`super.paintComponent`) | everything nothing else covers |
| `Color.RED` | `g.fillRect(50, 50, 100, 80)` | x 50..149, y 50..129 |
| `Color.BLUE` | `g.fillOval(200, 50, 100, 100)` | the circle in the box x 200..299, y 50..149; centre (250, 100), radius 50 |
| `Color.GREEN` | `g.fillRect(50, 200, 300, 40)` | x 50..349, y 200..239 |

**The trap:** `setColor` is a *mode*. It stays set until you change it, so a
shape without its own `setColor` comes out in whatever colour was set last.
Three shapes, three `setColor` calls.

### `HousePanel` — 400 × 400, in this order

| # | part | colour | call | covers |
|---|---|---|---|---|
| 1 | sky | `new Color(135, 206, 235)` | `g.fillRect(0, 0, getWidth(), getHeight())` | the whole panel |
| 2 | wall | `new Color(205, 133, 63)` | `g.fillRect(100, 200, 200, 150)` | x 100..299, y 200..349 |
| 3 | roof | `Color.RED` | `g.fillPolygon(xs, ys, 3)` with corners (90,200) (310,200) (200,100) | a triangle; base at y = 200, peak at (200, 100) |
| 4 | door | `new Color(101, 67, 33)` | `g.fillRect(180, 280, 40, 70)` | x 180..219, y 280..349 |
| 5 | window | `Color.YELLOW` | `g.fillRect(120, 220, 40, 40)` | x 120..159, y 220..259 |

```java
int[] xs = {90, 310, 200};
int[] ys = {200, 200, 100};
g.fillPolygon(xs, ys, 3);      // the i-th x and the i-th y are the i-th corner
```

**The trap:** painter's order. Later shapes cover earlier ones. Sky first or
it covers the house; door and window last or the wall covers them. The tests
sample the centre of the door and expect door-brown, not wall-brown.

### `BarChartPanel(int[] values)` — 400 × 300

The constructor stores the array in the field. `paintComponent` draws, for
each `i`:

```java
g.fillRect(20 + i * 60,  280 - values[i] * 2,  40,  values[i] * 2);   // Color.BLUE
```

then the baseline `g.drawLine(10, 280, 390, 280)` in `Color.BLACK`. Bars stand
on row 279 and grow upward; the baseline is row 280.

With `{40, 90, 25, 70, 55}` (the lecture's array):

| i | value | left x | height | top y | covers |
|---|---|---|---|---|---|
| 0 | 40 | 20 | 80 | 200 | x 20..59, y 200..279 |
| 1 | 90 | 80 | 180 | 100 | x 80..119, y 100..279 |
| 2 | 25 | 140 | 50 | 230 | x 140..179, y 230..279 |
| 3 | 70 | 200 | 140 | 140 | x 200..239, y 140..279 |
| 4 | 55 | 260 | 110 | 170 | x 260..299, y 170..279 |

**The trap:** use the *field*. The tests build charts from `{10}`, `{}` and
`{0, 30, 130}` as well — a panel that draws the lecture's array no matter
what it was given fails three tests. A value of 0 draws a bar of height 0,
which is nothing, which is correct.

### `FacePanel` — 400 × 400

| part | colour | call | notes |
|---|---|---|---|
| head | `Color.YELLOW` | `g.fillOval(100, 100, 200, 200)` | centre (200, 200), radius 100 |
| left eye | `Color.BLACK` | `g.fillOval(150, 160, 30, 30)` | centre (165, 175) |
| right eye | `Color.BLACK` | `g.fillOval(220, 160, 30, 30)` | centre (235, 175) |
| mouth | `Color.BLACK` | `g.drawArc(150, 200, 100, 60, 0, -180)` | the lower half of the oval in the box (150, 200) 100 × 60 |

`drawArc(x, y, w, h, start, sweep)` draws part of the outline of the oval in
the box. Angles are degrees, 0 is 3 o'clock, and positive goes
**counter-clockwise**. Start at 0, sweep −180: clockwise from 3 o'clock through
6 o'clock to 9 o'clock — the bottom half, a smile. Sweep +180 is the top
half, a frown, and the test that samples the top of the box catches it.

**The traps:** `fillOval` for the eyes (the test samples each eye's centre),
`drawArc` not `fillArc` for the mouth (the test samples inside the arc's box
and expects yellow).

## Examples

Pixels the tests read, and what they must be:

```
ShapesPanel   (100, 90) RED     (250, 100) BLUE    (200, 220) GREEN
              (150, 90) WHITE   (205, 55) WHITE    (10, 10) WHITE
HousePanel    (50, 50) sky      (250, 300) wall    (200, 150) RED
              (200, 320) door   (140, 240) YELLOW  (300, 300) sky
BarChart      (100, 150) BLUE   (70, 270) WHITE    (200, 280) BLACK
{40,90,25,70,55}
FacePanel     (200, 200) YELLOW (165, 175) BLACK   (200, 260) BLACK
              (200, 240) YELLOW (105, 105) WHITE
```

## Running the tests

`mvn test` runs everything; `mvn test -Dtest=HousePanelTest` runs one rubric line.

| Test class | Rubric line | Points |
|---|---|---|
| `ShapesPanelTest` | ShapesPanel: three shapes on a white panel | 20 |
| `HousePanelTest` | HousePanel: the house in painter's order | 35 |
| `BarChartPanelTest` | BarChartPanel: one bar per value plus baseline | 25 |
| `FacePanelTest` | FacePanel: ovals in boxes and an arc | 20 |

The autograder awards a rubric line only when **every** test in that class
passes. The tests never open a window: each one gives your panel its size,
paints it into an off-screen image, and reads pixels that must be inside and
just outside each shape. Every failure message names the pixel and what it
should have been.

## Suggested order

1. `ShapesPanel`. Write `super.paintComponent(g);` first, then the three
   shapes. Run `ShapesPanelTest`. If the background test fails, the `super`
   line is missing.
2. `FacePanel`. Same structure; the oval rule is the only new thing.
3. `HousePanel`. Five shapes, one order. Sky first.
4. `BarChartPanel`. Store the array in the constructor, loop in
   `paintComponent`. Check `{10}` in your head: one bar, x 20..59, y 260..279.
5. Run `Main` and look at all four.

## Why is my window blank?

Work down this list in order before you ask for help:

- You forgot `setVisible(true)` — the frame exists but was never shown.
- You forgot `frame.add(panel)` — the frame is up, the canvas was never put in it.
- Your method is not named exactly `paintComponent(Graphics g)` — a typo makes
  a new method that nobody calls. It compiles, it runs, and nothing appears.
- You drew off-screen — y grows downward, and the panel is only as tall as the frame.
- Your shape is the same colour as the background, or another shape is drawn on top of it.
- You forgot `super.paintComponent(g)` — the background never got painted.

## Rules of the road

- AP Java subset only, plus the Swing calls named above: `setColor`,
  `fillRect`, `fillOval`, `fillPolygon`, `drawLine`, `drawArc`, `getWidth()`,
  `getHeight()`. Arrays appear only in `HousePanel` (three corners) and
  `BarChartPanel` (`values.length`, `values[i]`) — that is all you need to
  know about them today.
- Do not change method headers or the provided constructors.
- Nothing in your panels may open a window. Only `Main` does, and only inside
  `if (!GraphicsEnvironment.isHeadless())`. A panel that constructs a `JFrame`
  crashes the autograder and scores zero.
- Do not touch `src/test`, `pom.xml`, `grading.json`, or `.github`. The
  autograder checks that they are byte-identical to the template before it
  runs a single test; if they differ it stops and awards nothing, and the
  change shows up in the roster.
