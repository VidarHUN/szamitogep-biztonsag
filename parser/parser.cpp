#include "caff.h"
#include "ciff.h"

#include <iostream>
#include <fstream>
#include <cstdint>

using namespace std;

int main(int argc, char **argv) {
    ifstream file("caff_files/1.caff", ios::in | ios::out | ios::binary);

    if (!file.is_open()) {
        cout << "FATAL: File caff_files/1.caff could not be opened.\n";
        return 1;
    }
    else {
        // Code
        file.seekg(0, file.end);
        size_t file_size = file.tellg();
        file.seekg(0, file.beg);

        char* buffer = new char[file_size];
        file.read(buffer, file_size);

        file.close();
        delete[] buffer;
    }

    return 0;
}
