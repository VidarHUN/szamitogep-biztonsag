parser: parser.o main.cpp
	g++ main.cpp parser.o -o ../parser -std=c++17 -O3
	rm *.o

parser.o: parser.cpp parser.hpp
	g++ -c parser.cpp -std=c++17 -O3

