#include <iostream>
#include <fstream>
#include <cstdint>
#include "parser.hpp"

using namespace std;

int main(int argc, char **argv)
{
    ifstream file("caff_files/1.caff", ios::in | ios::out | ios::binary);

    if (!file.is_open())
    {
        cout << "FATAL: File caff_files/1.caff could not be opened.\n";
        return 1;
    }
    else
    {
        CAFFParser parser;
        ParsedInfo info = parser.parse_file(&file);
        cout << "blk type:\t" << (int)info.blk_info.type << endl;
        cout << "blk len:\t" << info.blk_info.length << endl;
        cout << "header len:\t" << info.caff_header.header_size << endl;
        cout << "num anim:\t" << info.caff_header.num_anim << endl;
    }

    return 0;
}
