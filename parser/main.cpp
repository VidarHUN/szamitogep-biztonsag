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
    auto filenames = json::array();
    for (size_t i = 0; i < info.caff_header.num_anim; i++)
    {
        stringstream ss;
        ss << i << ".ppm";
        durations += info.animation[0]->duration;
        filenames += ss.str();
    }
    j["durations"] = durations;
    j["filenames"] = filenames;

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
    ofstream meta("meta.json", ios::out);
    meta << s;
    meta.close();
}

int main(int argc, char **argv)
{
    if (argc < 2)
    {
        cout << "Give me a PPM image pls.." << endl;
        return 42;
    }
    string filename = string(argv[1]);
    ifstream file;
    try
    {
        file.open(filename.c_str(), ios::in | ios::out | ios::binary);
        CAFFParser parser;
        json j;

        ParsedInfo info = parser.parse_file(&file);
        serialize(j, info);
        serialize_write(j);
    }
    catch (exception &e)
    {
        cout << e.what() << endl;
        if (file.is_open())
            file.close();
        return 1;
    }

    file.close();
    return 0;
}
