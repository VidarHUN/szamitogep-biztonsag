program: parser.o caff.o
	g++ main.cpp parser.o caff.o -o program

parser.o: parser.cpp parser.hpp
	g++ -c parser.cpp

caff.o: caff.cpp caff.h
	g++ -c caff.cpp

clean:
	rm *.o program
