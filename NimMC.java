Clerk.markdown("""
# Nim Spiel mit Monte Carlo Methode
""");

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

class MoveSimulationResult {
    Move move;
    int wins;
    int losses;

    MoveSimulationResult(Move move) {
        this.move = move;
        this.wins = 0;
        this.losses = 0;
    }

    double getScore() {
        double score = (double) wins / (wins + losses);

        BigDecimal roundedScore = new BigDecimal(score).setScale(6, RoundingMode.HALF_UP);

        return roundedScore.doubleValue();
    }
    
    @Override
    public String toString() {
        return "Move: " + move + ", Wins: " + wins + ", Losses: " + losses + ", Score: " + getScore();
    }
}

Clerk.markdown(Text.fillOut(
"""
## Monte Carlo Methode (mcMove) - Hauptmethode

Die Monte Carlo Methode bekommt eine Anzahl(int) von Simulationen übergeben und führt für jeden möglichen Zug auf Basis der übergebenen
Ausgangssituation des Spiels eine Simulation durch. Die Methode gibt den besten Zug zurück welcher auf Basis der Simulationen ermittelt wurde.

```java
${0}
```


""", Text.cutOut("./nimMc.java", "// mcMove")));

Clerk.markdown(Text.fillOut(
"""
## Alle möglichen Züge herausfinden (getAllPossibleMoves) - Hilfsmethode

Die Methode gibt eine Liste von allen möglichen Zügen zurück, die ein Spieler auf Basis der Ausgangssituation des Spiels machen kann. 
Dabei wird für jede Reihe und für jede Anzahl von Hölzchen in der Reihe ein Zug erstellt und dieser der Liste moves hinzugefügt. Letztlich
wird die Liste moves zurückgegeben. 

```java
${0}
```


""", Text.cutOut("./nimMc.java", "// allPossibleMoves")));

Clerk.markdown(Text.fillOut(
"""
## Das Spiel simulieren (simulateRandomPlay) - Hilfsmethode

Die Methode simuliert den weiteren Spielverlauf nach einem Zug. Dabei wird solange gespielt, bis das Spiel beendet ist. 
Beide Spieler spielen zufällig und es wird geprüft, ob der erste Spieler gewonnen hat. Die Methode gibt true zurück,
wenn der erste Spieler gewonnen hat und false, wenn er verloren hat.


```java
${0}
```


""", Text.cutOut("./nimMc.java", "// simulateRandomPlay")));

Clerk.markdown(Text.fillOut(
"""
## Den besten Move auswählen (selectBestMove) - Hilfsmethode

Die Methode wählt den besten Zug aus einer Liste von Ergebnissen aus. Dabei wird die Liste results zufällig sortiert
und der Zug mit dem höchsten Score zurückgegeben.

```java
${0}
```


""", Text.cutOut("./nimMc.java", "// selectBestMove")));

Clerk.markdown(Text.fillOut(
"""
## Beispielaufruf mcMove - Welcher Zug ist der beste?



```java
${0}
```


""", Text.cutOut("./nimMc.java", "// exampleCall")));




class Nim implements NimGame {
    private Random r = new Random();
    int[] rows;
    public static Nim of(int... rows) {
        return new Nim(rows);
    }
    private Nim(int... rows) {
        assert rows.length >= 1;
        assert Arrays.stream(rows).allMatch(n -> n >= 0);
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

    // mcMove
    public Move mcMove(int simulations) {
        // Alle möglichen Züge eines Spielers
        List<Move> possibleMoves = getAllPossibleMoves();
        // Die Ergebnisse der Simulationen gespeichert in einer Liste
        List<MoveSimulationResult> results = new ArrayList<>();

        // Iteration über alle möglichen Züge von possibleMoves
        for (Move move : possibleMoves) {
            // Erstellen eines neuen Ergebnis-Objekts
            MoveSimulationResult result = new MoveSimulationResult(move);

            // Führt die ausgewählte Anzahl von Simulationen für den aktuellen Zug durch
            for (int i = 0; i < simulations; i++) {
                // Simuliere das Spiel nach dem aktuellen Zug
                Nim simulatedGame = this.play(move);
                if (simulateRandomPlay(simulatedGame)) {
                    result.wins++;  // Erster Spieler gewinnt, erhöhe den Gewinnzähler
                } else {
                    result.losses++; // Erster Spieler verliert, erhöhe den Verlustzähler
                }
            }
            // Füge das Ergebnis zur Liste hinzu
            results.add(result);
        }
        // Wähle den besten Zug aus
        return selectBestMove(results);
    }
    // mcMove
    // allPossibleMoves
    // Hilfsmethode, um alle möglichen Züge zu erhalten
     private List<Move> getAllPossibleMoves() {
        List<Move> moves = new ArrayList<>();
        for (int row = 0; row < rows.length; row++) {
            for (int count = 1; count <= rows[row]; count++) {
                moves.add(Move.of(row, count));
            }
        }
        return moves;
    }
    // allPossibleMoves

    // simulateRandomPlay
    // Hilfsmethode, um das Spiel zu simulieren
    private boolean simulateRandomPlay(Nim game) {
        Nim current = game;
        while (!current.isGameOver()) {
            Move randomMove = current.randomMove(); // Erster Spieler spielt zufällig
            current = current.play(randomMove);
            if (current.isGameOver()) { // Hat der Zug des ersten Spielers das Spiel beendet?
            return false; // Der erste Spieler hat verloren bzw. der zweite Spieler hat gewonnen
            }
            randomMove = current.randomMove(); // Zweiter Spieler spielt zufällig
            current = current.play(randomMove);
        }
        return !NimGame.isWinning(current.rows); // gibt true zurück -> erster Spieler hat gewonnen, zweiter Spieler verloren
    }                                            // gibt false zurück -> erster Spieler hat verloren, zweiter Spieler gewonnen
    // simulateRandomPlay

    // selectBestMove
    // Hilfsmethode, um den besten Zug auszuwählen
    private Move selectBestMove(List<MoveSimulationResult> results) {
        Collections.shuffle(results); // Zufälligkeit bei Gleichstand
        return results.stream()
                .max(Comparator.comparingDouble(MoveSimulationResult::getScore))
                .orElseThrow()// falls leer, wirft eine NoSuchElementException
                .move; // Gibt den besten Zug zurück
    }
    // selectBestMove
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

// exampleCall
/* Beispielhafter Monte Carlo Aufruf über JShell
Nim n = Nim.of(2, 3, 4, 7);
Move mcMove = n.mcMove(100000);
*/
// exampleCall

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
*/