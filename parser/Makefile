program: parser.o caff.o main.cpp
	g++ -g -O0 main.cpp parser.o caff.o -o program

parser.o: parser.cpp parser.hpp
	g++ -c -g -O0 parser.cpp

caff.o: caff.cpp caff.h
	g++ -c -g -O0 caff.cpp

clean:
	rm *.o program
