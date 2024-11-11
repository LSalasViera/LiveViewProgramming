import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public static void decrementTM() {
    Clerk.markdown("""
# Abgabe - Turing Maschine - Salas Viera, Luis Ramon
## Transitionstabelle - Dekrementierung einer Binärzahl

| fromState | read | write | move | toState |
|-----------|------|-------|------|---------|
| S         | #    | #     | L    | S       |
| S         | 1    | 0     | R    | R       |
| R         | 0    | 1     | L    | L       |
| R         | 0    | 0     | R    | R       |
| R         | 1    | 1     | R    | R       |
| R         | #    | #     | L    | W       |
| W         | 1    | 1     | R    | HALT    |
| W         | 0    | 0     | R    | HALT    |
| W         | #    | #     | R    | HALT    |
| L         | 0    | 1     | L    | L       |
| L         | 1    | 0     | R    | R       |
| L         | #    | #     | R    | R       |
""");

Clerk.markdown("""
## LVP - Dekrementierung einer Binärzahl

""");

    Tape tape = new Tape('#');
    // Initialisiere das Band
    tape.write('#');
    tape.move(Move.RIGHT);
    tape.write('1');
    tape.move(Move.RIGHT);
    tape.write('1');
    tape.move(Move.RIGHT);
    tape.write('0');
    tape.move(Move.RIGHT);
    tape.write('0');
    tape.move(Move.RIGHT);
    tape.write('0');
    tape.move(Move.RIGHT);
    tape.write('#'); 

    // Tabelle mit Transitionen
    Table table = new Table();
    table.addTransition("S", '#', '#', Move.LEFT, "S");
    table.addTransition("S", '1', '0', Move.RIGHT, "R");
    table.addTransition("S", '0', '1', Move.LEFT, "L");

    table.addTransition("R", '0', '0', Move.RIGHT, "R");
    table.addTransition("R", '1', '1', Move.RIGHT, "R");
    table.addTransition("R", '#', '#', Move.LEFT, "W");

    table.addTransition("W", '1', '1', Move.RIGHT, "HALT");
    table.addTransition("W", '0', '0', Move.RIGHT, "HALT");
    table.addTransition("W", '#', '#', Move.RIGHT, "HALT");

    table.addTransition("L", '0', '1', Move.LEFT, "L");
    table.addTransition("L", '1', '0', Move.RIGHT, "R");
    table.addTransition("L", '#', '#', Move.RIGHT, "R");

    TM tm = new TM(tape, table, "S");
    tm.run();
}

public static void moveOnesTM() {

    Clerk.markdown("""
## Transitionstabelle - Einsen nach rechts schieben

| fromState | read | write | move | toState |
|-----------|------|-------|------|---------|
| S         | 1    | 1     | L    | S       |
| S         | S    | S     | R    | HALT    |
| S         | 0    | 0     | L    | 0       |
| 0         | 0    | 0     | L    | 0       |
| 0         | 1    | 0     | R    | 1       |
| 0         | S    | S     | R    | HALT    |
| 1         | 0    | 0     | R    | 1       |
| 1         | 1    | 1     | L    | D       |
| 1         | S    | S     | L    | D       |
| D         | 0    | 1     | L    | S       |
""");

Clerk.markdown("""
## LVP - Einsen nach rechts schieben

""");
    
    Tape tape = new Tape('0'); 

    // Initialisiere das Band mit der Binärzahl "S 0 1 0 1 0 1 S"
    tape.write('S');
    tape.move(Move.RIGHT);
    tape.write('0');
    tape.move(Move.RIGHT);
    tape.write('1');
    tape.move(Move.RIGHT);
    tape.write('0');
    tape.move(Move.RIGHT);
    tape.write('1');
    tape.move(Move.RIGHT);
    tape.write('0');
    tape.move(Move.RIGHT);
    tape.write('1');
    tape.move(Move.RIGHT);
    tape.write('S');

    tape.move(Move.LEFT); // Startposition des Kopfes auf erstem Zeichen

    // Tabelle mit Transitionen
    Table table = new Table();
    table.addTransition("S", '1', '1', Move.LEFT, "S");
    table.addTransition("S", 'S', 'S', Move.RIGHT, "HALT");
    table.addTransition("S", '0', '0', Move.LEFT, "0");

    table.addTransition("0", '0', '0', Move.LEFT, "0");
    table.addTransition("0", '1', '0', Move.RIGHT, "1");
    table.addTransition("0", 'S', 'S', Move.RIGHT, "HALT");

    table.addTransition("1", '0', '0', Move.RIGHT, "1");
    table.addTransition("1", '1', '1', Move.LEFT, "D");
    table.addTransition("1", 'S', 'S', Move.LEFT, "D");

    table.addTransition("D", '0', '1', Move.LEFT, "S");

    TM tm = new TM(tape, table, "S");
    tm.run();
}

public class Tape {
    public List<Character> cells;
    public int headPosition;
    public char blankCell;

    public Tape(char blankCell) {
        this.cells = new ArrayList<>();
        this.headPosition = 0;
        this.blankCell = blankCell;
        cells.add(blankCell);
    }

    public char read() {
        ensureCapacity();
        return cells.get(headPosition);
    }

     public void write(char symbol) {
        ensureCapacity();
        cells.set(headPosition, symbol);
    }

     public void move(Move direction) {
        if (direction == Move.RIGHT) {
            headPosition++;
        } else if (direction == Move.LEFT) {
            headPosition--;
        }
        ensureCapacity();
    }

     private void ensureCapacity() {
        if (headPosition < 0) {
            cells.add(0, blankCell);
            headPosition = 0;
        } else if (headPosition >= cells.size()) {
            cells.add(blankCell);
        }
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cells.size(); i++) {
            if (i == headPosition) {
                sb.append("{").append(cells.get(i)).append("}");
            } else {
                sb.append(" ").append(cells.get(i)).append(" ");
            }
        }
        return sb.toString();
    }
}

public class Table{
    private Map<Trigger, Action> transitions;

    public Table() {
        this.transitions = new HashMap<>();
    }

    public void addTransition(String fromState, char read, char write, Move move, String toState) {
        Trigger trigger = new Trigger(fromState, read);
        Action action = new Action(write, move, toState);
        transitions.put(trigger, action);
    }

    public Action getAction(String state, char read) {
        Trigger trigger = new Trigger(state, read);
        return transitions.get(trigger);
    }
}

public class TM {
    private Tape tape;
    private Table table;
    private String currentState;

    public TM(Tape tape, Table table, String startState){
        this.tape = tape;
        this.table = table;
        this.currentState = startState;
    }

    public boolean step() {
        if (currentState.equals("HALT")) {
            return false; // stoppt die Maschine
        }

        char readSymbol = tape.read();
        Action action = table.getAction(currentState, readSymbol);

        if (action == null) {
            currentState = "HALT";
            return false; // stoppt die Maschine
        }

        tape.write(action.write());
        tape.move(action.move());
        currentState = action.toState();

        return true;
    }

    public void drawTurtle(){
        Turtle turtle = new Turtle(550,80);

        int cellWidth = 50;
        int cellHeight = 80;
        int numCells = 11;  // 5 Nachbarzellen links, 1 Zelle für den Kopf, 5 Nachbarzellen rechts
        int centerPosition = 6; // Position der Zelle, die den Kopf enthält
        int testPosition = (6 - tape.headPosition) - 1;
        double displayRange = 5.5;
        double startX = (-displayRange * cellWidth) + (testPosition  * cellWidth);

        turtle.penUp();
        turtle.left(90);
        turtle.forward(30);
        turtle.color(255, 0, 0);
        turtle.text("K");
        turtle.color(0, 0, 0);
        turtle.backward(30);
        turtle.right(90);

        turtle.penUp();
        turtle.forward(startX);
        turtle.penDown();

        for (int i = 0; i <= 6 ; i++) {

            char cellContent = tape.cells.get(i);

            //Zeichne Zelle
            turtle.penUp();
            turtle.forward(cellWidth);
            turtle.penDown();
            turtle.left (90);
            turtle.forward(cellHeight / 2);
            turtle.left (90);
            turtle.forward(cellWidth);
            turtle.left (90);
            turtle.forward(cellHeight);
            turtle.left (90);
            turtle.forward(cellWidth);
            turtle.left (90);
            turtle.forward(cellHeight / 2);
            turtle.right(90);


            turtle.left(180);
            turtle.penUp();
            turtle.forward(25);
            turtle.right(90);
            turtle.text(String.valueOf(cellContent));
            turtle.left(90);
            turtle.backward(25);
            turtle.right(180);
        }
        
    }

    // Methode, die das Band im Markdown-Format ausgibt
    public void printTapeInMarkdown() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tape.cells.size(); i++) {
            char cell = tape.cells.get(i);
            if (i == tape.headPosition) {
                sb.append("{").append(cell).append("} ");
            } else {
                sb.append(cell).append(" ");
            }
        }
        // Ausgabe im Markdown-Format (ohne Nummerierung)
        Clerk.markdown("```\n" + sb.toString().trim() + "\n```");
    }

    public void run() {
        System.out.println(tape);
        printTapeInMarkdown();
        drawTurtle();
        while (step()) {
            System.out.println(tape);
            printTapeInMarkdown();
            drawTurtle();
        }
        System.out.println("Die Maschine hat den Haltezustand erreicht.");
    }
}

record Trigger(String fromState, char read) {}

record Action(char write, Move move, String toState) {}

enum Move {
    LEFT, RIGHT
}



/* Testing the Tape 

Konstruktion und Lesen vom Band:
Tape tape = new Tape('_'); -> '_' als Leerzeichen
System.out.println(tape); -> [_]

Schreiben und Lesen eines Zeichens:
tape.write('A');
System.out.println(tape.read()); -> w'A'
System.out.println(tape); -> [A]

Bewegung des Kopfes nach rechts und Schreiben:
tape.move(Move.RIGHT);
tape.write('B');
System.out.println(tape); -> [A] [B]

Kopf nach links bewegen und nochmal lesen:
tape.move(Move.LEFT);
System.out.println(tape.read()); -> 'A'

Kopf weiter nach links bewegen und prüfen, ob blankCell hinzugefügt wird:
tape.move(Move.LEFT);
System.out.println(tape); -> [ _ ] A  B */

/* Testing the Table

Table table = new Table();
table.addTransition("q0", '1', '0', Move.RIGHT, "q1");
Action action = table.getAction("q0", '1');
System.out.println(action); -> Action[write=0, move=RIGHT, toState=q1] /*