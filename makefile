PROG=program.java
CLASS=program.class
#FILE=program.txt
FILE=custom.txt

all: clean $(PROG) $(FILE)
	javac $(PROG)
	java $(PROG) $(FILE)

.PHONY: clean

clean: $(CLASS)
	- rm -rf $(CLASS)
