import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Tape {
    private List<Character> cells;
    private int headPosition;
    private char blankCell;

    public Tape(char blankCell) {
        this.cells = new ArrayList<>();
        this.headPosition = 0;
        this.blankCell = blankCell;
        cells.add(blankCell); // Initialisiert das Band mit einer leeren Zelle
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
                sb.append("[").append(cells.get(i)).append("]");
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

    public void run() {
        while (step()) {
            System.out.println(tape);
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