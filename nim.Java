// Den Code bespreche ich ausführlich im Podcast "Herzbergs Hörsaal" in der Episode
// https://anchor.fm/dominikusherzberg/episodes/PiS-Das-Nim-Spiel-in-Java-programmiert-edks2t
//
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

class Move {
    final int row, number;
    static Move of(int row, int number) {
        return new Move(row, number);
    }
    private Move(int row, int number) {
        if (row < 0 || number < 1) throw new IllegalArgumentException();
        this.row = row;
        this.number = number;
    }
    public String toString() {
        return "(" + row + ", " + number + ")";
    }
}

interface NimGame {
    static boolean isWinning(int... numbers) {
        return Arrays.stream(numbers).reduce(0, (i,j) -> i ^ j) != 0;
        // klassische Variante:
        // int res = 0;
        // for(int number : numbers) res ^= number;
        // return res != 0;
    }
    NimGame play(Move... moves);
    Move randomMove();
    Move bestMove();
    boolean isGameOver();
    String toString();
}

class Nim implements NimGame {
    private Random r = new Random();
    int[] rows;
    public static Nim of(int... rows) {
        return new Nim(rows);
    }
    public Nim(int... rows) {
        this.rows = Arrays.copyOf(rows, rows.length);
    }

    private Nim play(Move m) {
        assert !isGameOver();
        assert m.row < rows.length && m.number <= rows[m.row];
        Nim nim = Nim.of(rows);
        nim.rows[m.row] -= m.number;
        return nim;
    }
    public Nim play(Move... moves) {
        Nim nim = this;
        for(Move m : moves) nim = nim.play(m);
        NimView view = new NimView(nim.rows);
        view.show();
        return nim;
    }
    public Move randomMove() {
        assert !isGameOver();
        int row;
        do {
            row = r.nextInt(rows.length);
        } while (rows[row] == 0);
        int number = r.nextInt(rows[row]) + 1;
        return Move.of(row, number);
    }
    public Move bestMove() {
        assert !isGameOver();
        if (!NimGame.isWinning(rows)) return randomMove();
        Move m;
        do {
            m = randomMove();
        } while(NimGame.isWinning(play(m).rows));
        return m;
    }
    public boolean isGameOver() {
        return Arrays.stream(rows).allMatch(n -> n == 0);
    }
    public String toString() {
        String s = "";
        for(int n : rows) s += "\n" + "I ".repeat(n);
        return s;
    }
    // Getter-Methode für die rows
    public int[] getRows() {
        return Arrays.copyOf(rows, rows.length); // Gebe eine Kopie des Arrays zurück
    }
    @Override
    public boolean equals(Object other) {
        if (other == null) return false;  // Null-Check
        if (other == this) return true;   // Identitäts-Check
        if (other.getClass() != getClass()) return false;  // Typ-Check
        Nim that = (Nim) other;  // Casten des Objekts
        return Arrays.equals(this.rows, that.rows);  // Vergleichen der Arrays
    }

    @Override
    public int hashCode() {
    return Objects.hash(Arrays.hashCode(rows));  // Verwendung von Objects.hash()
    }
}

class NimView extends Nim {
    
    // Konstruktor, der die Superklasse initialisiert
    public NimView(int... rows) {
        this.rows = rows;
    }

    public void show() {
        Turtle turtle = new Turtle(500, 500);

        int rowHeight = 25;
        turtle.left(90);

        for (int row = 0; row < rows.length; row++) {
            turtle.penUp();
            turtle.backward(rowHeight);
            turtle.penDown();
            int sticks = rows[row]; // Anzahl der Sticks in der Reihe

            for (int i = 0; i < sticks; i++) {
                turtle.color(139, 69, 19);
                turtle.forward(20);
                turtle.backward(20);
                turtle.right(90);
                turtle.penUp();
                turtle.forward(5);
                turtle.left(90);
                turtle.penDown();
            }
        }
        turtle.penUp();
        turtle.penDown();
    }

    public void update(int... newRows) {
        this.rows = newRows; // Aktualisiere die rows
        show(); // Zeige die aktualisierte Ansicht
    }
}

Nim nim = Nim.of(2,3,4);
assert nim != nim.play(Move.of(1,2)) : "Return a new Nim instance";

int[] randomSetup(int... maxN) {
    Random r = new Random();
    int[] rows = new int[maxN.length];
    for(int i = 0; i < maxN.length; i++) {
        rows[i] = r.nextInt(maxN[i]) + 1;
    }
    return rows;
}

ArrayList<Move> autoplay(NimGame nim) {
    ArrayList<Move> moves = new ArrayList<>();
    while (!nim.isGameOver()) {
        Move m = nim.bestMove();
        moves.add(m);
        nim = nim.play(m);
    }
    return moves;
}

boolean simulateGame(int... maxN) {
    Nim nim = Nim.of(randomSetup(maxN));
    // System.out.println(nim);
    // System.out.println((NimGame.isWinning(nim.rows) ? "first" : "second") + " to win"); 
    ArrayList<Move> moves = autoplay(nim);
    // System.out.println(moves);
    return (NimGame.isWinning(nim.rows) && (moves.size() % 2) == 1) ||
           (!NimGame.isWinning(nim.rows) && (moves.size() % 2) == 0); 
}

assert IntStream.range(0,100).allMatch(i -> simulateGame(3,4,5));
assert IntStream.range(0,100).allMatch(i -> simulateGame(3,4,6,8));

/* // Beispielhaftes Spiel über JShell
jshell> Nim n = Nim.of(2,3,4)
n ==>
I I
I I I
I I I I
jshell> n = n.play(n.bestMove())
n ==>
I I
I I I
I
jshell> n = n.play(Move.of(2,1))
n ==>
I I
I I I
jshell> n = n.play(n.bestMove())
n ==>
I I
I I
jshell> n = n.play(Move.of(1,1))
n ==>
I I
I
jshell> n = n.play(n.bestMove())
n ==>
I
I
jshell> n = n.play(Move.of(1,1))
n ==>
I
jshell> n = n.play(n.bestMove())
n ==>
jshell> n.isGameOver()
$25 ==> true

Open Nim in jshell
Create new Nim
nim.play(Move.of(1,2))
*/

/*  Equals and HashCode

Objekte erzeugen:

Nim nim1 = Nim.of(2, 3, 4);
Nim nim2 = Nim.of(2, 3, 4);
Nim nim3 = Nim.of(1, 3, 5);

Equals:

nim1.equals(nim2); // true
nim1.equals(nim3); // false

HashCode:

nim1.hashCode() == nim2.hashCode(); // true
nim1.hashCode() == nim3.hashCode(); // false
*/
