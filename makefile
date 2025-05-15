PROG=program.java
CLASS=program.class
FILE=program.txt

all: clean $(PROG) $(FILE)
	javac $(PROG)
	java $(PROG) $(FILE)

.PHONY: clean

clean:
	@if [ -f $(CLASS) ]; then rm -rf $(CLASS); fi
