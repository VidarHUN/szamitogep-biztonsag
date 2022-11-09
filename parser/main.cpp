#include <iostream>
#include <fstream>
#include <cstdint>
#include "parser.hpp"

using namespace std;
using json = nlohmann::json_abi_v3_11_2::json;

// nem lehetett tagfüggvény, csak felül kell írni és meghívni
void serialize(json &j, const ParsedInfo &info)
{
    j["num_anim"] = info.caff_header.num_anim;
    j["year"] = info.credits.YY;
    j["month"] = info.credits.M;
    j["day"] = info.credits.D;
    j["hour"] = info.credits.h;
    j["minute"] = info.credits.m;
    j["creator"] = info.credits.creator;

    auto durations = json::array();
    for (size_t i = 0; i < info.caff_header.num_anim; i++)
    {
        durations += info.animation[0]->duration;
    }
    j["durations"] = durations;

    j["caption"] = info.animation[0]->header->caption;
    j["width"] = info.animation[0]->header->width;
    j["height"] = info.animation[0]->header->height;

    auto tags = json::array();
    for (size_t k = 0; k < info.animation[0]->header->num_tags; k++)
    {
        tags += *(info.animation[0]->header->tags[k]);
    }
    j["tags"] = tags;
}

// prints the json object to stdout
static void serialize_write(const json &j)
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
            serialize(j, info);
            serialize_write(j);
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
