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
        cout << "FATAL: File caff_files/.caff could not be opened.\n";
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
            for (int i = 0; i < info.caff_header.num_anim; i++)
            {
                cout << "Animation" << i + 1 << ":\t"
                     << "duration: " << info.animation[i]->duration
                     << "\theader: " << info.animation[i]->header->header_size << endl
                     << "\t\twidth: " << info.animation[i]->header->width << endl
                     << "\t\theight: " << info.animation[i]->header->width << endl
                     << "\tcontent: " << info.animation[i]->header->content_size << endl
                     << "\tCaption:\t" << info.animation[i]->header->caption << endl
                     << "\tTags:";
                for (size_t j = 0; j < info.animation[i]->header->num_tags; j++)
                {
                    cout << "\t\t" << *(info.animation[i]->header->tags[j]) << endl;
                }
            }
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
