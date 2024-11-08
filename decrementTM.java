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