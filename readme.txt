how to compile and run:

javac *.java
java Main initials.txt commands.txt output.txt

initials.txt

line after board must be an nxn string

line after calliance must be calliance pieces.

<piecename></tab><pieceid></tab><x></tab><y>

line after zorde must be zorde pieces.

<piecename></tab><pieceid></tab><x></tab><y>

commands.txt:

each command starts with a pieceid followed by a space and move commands seperated by ";"

for example a piece that can move twice in a round must have:

pieceid x;y;x;y

a legal move can be 0, 1 or -1.

attributes of the pieces are in pieces.PNG