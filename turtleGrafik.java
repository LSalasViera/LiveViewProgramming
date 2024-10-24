/*Befehl | Bedeutung
-------|----------
penDown() | Setze den Stift auf die Zeichenfläche (Anfangseinstellung)
penUp()   | Hebe den Stift von der Zeichenfläche ab
forward(double distance)  | Bewege dich um distance vorwärts
backward(double distance) | Bewege dich um distance rückwärts 
right(double degrees)     | Drehe dich um die Gradzahl degrees nach rechts
left(double degrees)      | Drehe dich um die Gradzahl degrees nach links
color(int red, int green, int blue) | Setze Stiftfarbe mit den RGB-Farbanteilen red, green und blue
color(int rgb)            | Setze Stiftfarbe auf den kodierten RGB-Farbwert rgb
lineWidth(double width)   | Setze Stiftbreite auf width
text(String text, Font font, double size, Font.Align align) | Schreibe Text vor deinen Kopf mit Angabe des Text-Fonts, der Größe und der Ausrichtung
text(String text) | Schreibe Text vor deinen Kopf
reset()                   | Lösche Zeichenfläche, gehe zurück in Bildmitte*/

public class TurtleDrawing {
    private Turtle myFirstTurtle;
    private Turtle mySecondTurtle;

    public TurtleDrawing(int width, int height) {
        myFirstTurtle = new Turtle(width, height);
        mySecondTurtle = new Turtle(width, height);
        
        drawHexagonalPattern();
        drawSpiralRecursive(1200, 1, 40);    
    }

    public void drawSpiralRecursive(int steps, double length, double angle) {
        if (steps == 0) {
            return; 
        }

        int red = (steps * 5) % 255;
        int green = (steps * 3) % 255;
        int blue = (steps * 7) % 255;
        myFirstTurtle.color(red, green, blue);
        myFirstTurtle.forward(length);
        myFirstTurtle.left(angle);

        drawSpiralRecursive(steps - 1, length * 1.005, angle);
    }

    public void drawHexagonalPattern() {
        for (int i = 0; i < 90; i++) {
            mySecondTurtle.color(255, i * 255 / 5, i * 180 / 5);
            mySecondTurtle.lineWidth(1 + i / 15);

            for (int j = 0; j < 6; j++) {
                mySecondTurtle.forward(60 + i * 3);
                mySecondTurtle.left(60);
            }
            mySecondTurtle.left(15);
        }
    }
}

TurtleDrawing myDrawings = new TurtleDrawing(800, 800);


