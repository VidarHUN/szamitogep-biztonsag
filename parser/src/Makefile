program: parser.o caff.o main.cpp
	g++ main.cpp parser.o caff.o -o ../program -std=c++14 -O3
	rm *.o

parser.o: parser.cpp parser.hpp
	g++ -c parser.cpp -std=c++14 -O3

caff.o: caff.cpp caff.h
	g++ -c caff.cpp -std=c++14 -O3

