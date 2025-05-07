PROG=main.java
CLASS=main.class
ARG0=assignment.txt

all: clean $(PROG) $(ARG0)
	javac $(PROG)
	java $(PROG) $(ARG0)

.PHONY: clean

clean: $(CLASS)
	rm -f $(CLASS)

