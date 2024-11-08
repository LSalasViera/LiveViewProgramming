// Starte ein neues Nim-Spiel mit einer Beispielkonfiguration.
Nim n = Nim.of(2, 3, 4, 7);

// Anzahl der Simulationen pro Zug in der Monte-Carlo-Analyse.
int simulations = 200000;

// Führe nur einen Zug mit der Monte-Carlo-Methode aus
if (!n.isGameOver()) {
    System.out.println("Aktueller Spielstand:");
    System.out.println(n);

    // Nächster Zug mit der Monte-Carlo-Methode
    Move mcMove = n.mcMove(simulations);
    System.out.println("Empfohlener Monte-Carlo-Zug: " + mcMove);

    // Wende den Zug auf das Nim-Spiel an
    n = n.play(mcMove);
}

// Überprüfen, ob das Spiel beendet ist
if (n.isGameOver()) {
    System.out.println("Spiel beendet!");
    System.out.println(n);
} else {
    System.out.println("Das Spiel lauft noch!");
}