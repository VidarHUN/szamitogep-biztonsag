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
        try
        {
            ParsedInfo info = parser.parse_file(&file);
            cout << "header len:\t" << info.caff_header.header_size << endl;
            cout << "num anim:\t" << info.caff_header.num_anim << endl;
            cout << "Credits:\t" << info.credits.toString() << endl;
            cout << "Animation:\t"
                 << "duration: " << info.animation->duration
                 << "\theader: " << info.animation->header.header_size
                 << "\tcontent: " << info.animation->header.content_size << endl;
        }
        catch (ParserException &e)
        {
            cout << e.what() << endl;
            file.close();
            return 1;
        }
    }
    file.close();
    return 0;
}
