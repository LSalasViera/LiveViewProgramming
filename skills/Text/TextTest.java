assert Text.fillOut(
    "Das Ergebnis ist ${#1} oder ${#2}.",
    Map.of("#1", 2 + 3, "#2", 42)).equals(
    "Das Ergebnis ist 5 oder 42.");

assert Text.fillOut(
    Map.of("#1", 2 + 3, "#2", 42),
    "Das Ergebnis ist ${#1} oder ${#2}.").equals(
    "Das Ergebnis ist 5 oder 42.");

assert Text.fillOut(
    "Das Ergebnis ist ${0} oder ${1}.", 2 + 3, 42).equals(
    "Das Ergebnis ist 5 oder 42.");