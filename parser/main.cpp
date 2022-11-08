#include <iostream>
#include <fstream>
#include <cstdint>
#include "parser.hpp"

using namespace std;

//overrideing to_json methode
//nem lehetett tagfüggvény, csak felül kell írni és meghívni
inline void to_json(json& j, const ParsedInfo& info){
    j["blk_info"] = {{"blk_type", info.blk_info.type}, {"blk_length", info.blk_info.length}};
    j["caff_header"] = {{"caff_header_size", info.caff_header.header_size}, 
                       {"caff_header_num_anim", info.caff_header.num_anim}};
    j["caff_credits"]={{"year", info.credits.YY},
                       {"month", info.credits.M},
                       {"day", info.credits.D},
                       {"hour", info.credits.h},
                       {"minute", info.credits.m},
                       {"creator", info.credits.creator}};
    j["animations"]={
                    //{"duration", info.animation->duration},
                    {{"caff_header_size", info.caff_header.header_size}, 
                        {"caff_header_num_anim", info.caff_header.num_anim}},
                    //{"img", info.animation->img}      
    };
}

//prints the json object to stdout 
static void serialize(const json& j)
{
    string s = j.dump();
    std::cout << "serialization: " << s << std::endl;

    std::cout << "serialization with pretty printing: " << j.dump(4) << std::endl;
}

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
        json j;
        try
        {   
            ParsedInfo info = parser.parse_file(&file);
            to_json(j, info);
            serialize(j);
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
