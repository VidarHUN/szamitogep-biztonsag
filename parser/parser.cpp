#include "caff.h"
#include "ciff.h"

#include <iostream>

using namespace std;

int main(int argc, char **argv) {
    std::ifstream ifs("caff_files/1.caff", std::ios::binary);
    
    if (!ifs) {
        cout << "FATAL: File caff_files/1.caff could not be opened.\n";
    }

    struct Caff caff;

    cout << "Hello";
    return 0;
}
