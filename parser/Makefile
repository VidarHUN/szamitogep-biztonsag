output: parser.o caff.o
	g++ parser.o caff.o -o output

parser.o: parser.cpp
	g++ -c parser.cpp

caff.o: caff.cpp caff.h
	g++ -c caff.cpp

clean:
	rm *.o output
