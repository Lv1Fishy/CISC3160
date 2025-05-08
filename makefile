PROG=main.java
CLASS=main.class

all: clean $(PROG)
	javac $(PROG)
	java $(PROG)

.PHONY: clean

clean: $(CLASS)
	rm -f $(CLASS)

