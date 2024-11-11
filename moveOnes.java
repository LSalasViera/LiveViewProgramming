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
tape.write('#');

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