#include <iostream>
#include <fstream>
#include <cstdint>
#include "parser.hpp"
#include "../../extern/gif-h/gif.h"
#include "getopt.h"

using namespace std;
using json = nlohmann::json_abi_v3_11_2::json;

namespace metadata
{
    void serialize(json &j, const ParsedInfo &info)
    {
        j["year"] = info.credits.YY;
        j["month"] = info.credits.M;
        j["day"] = info.credits.D;
        j["hour"] = info.credits.h;
        j["minute"] = info.credits.m;
        j["creator"] = info.credits.creator;
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

    static void serialize_write(const json &j, string name)
    {
        size_t lastindex = name.find_last_of(".");
        string rawname = name.substr(0, lastindex);
        string s = j.dump();
        ofstream meta(rawname + ".json", ios::out);
        meta << s;
        meta.close();
    }

}

namespace gifdata
{
    void createGIF(ParsedInfo &info, string fname)
    {
        GifWriter g;
        int width = info.animation[0]->header->width;
        int height = info.animation[0]->header->height;
        GifBegin(&g, fname.c_str(), width, height, info.animation[0]->duration / 10);
        for (size_t i = 0; i < info.caff_header.num_anim; i++)
        {
            width = info.animation[i]->header->width;
            height = info.animation[i]->header->height;
            auto ptr = info.animation[i]->img;
            uint8_t *img = new uint8_t[width * height * 4];
            auto ptri = img;
            for (size_t j = 0; j < width * height * 3; j++)
            {
                *(ptri++) = (uint8_t)*ptr++;
                if (j % 3 == 2)
                    *(ptri++) = 255;
            }

            GifWriteFrame(&g, img, width, height, info.animation[i]->duration / 10);
            delete[] img;
        }
        GifEnd(&g);
    }
}

void gen_output(ParsedInfo &info, string gifname)
{
    json j;
    metadata::serialize(j, info);
    metadata::serialize_write(j, gifname);
    gifdata::createGIF(info, gifname);
}

int main(int argc, char **argv)
{
    if (argc != 5)
    {
        return 1;
    }
    string outGIF;
    string caffname;
    ifstream caff;
    int opt = 0;

    struct option longopts[] = {
        {"file", required_argument, NULL, 'f'},
        {"output", required_argument, NULL, 'o'},
        {0}};

    while (true)
    {
        opt = getopt_long(argc, argv, "hf:o:", longopts, 0);
        if (opt == -1)
            break;
        switch (opt)
        {
        case 'f':
            caffname = string(optarg);
            break;
        case 'o':
            outGIF = string(optarg);
            break;
        case '?':
            return 1;
        default:
            break;
        }
    }

    try
    {
        caff.open(caffname.c_str(), ios::in | ios::out | ios::binary);
        CAFFParser parser;
        ParsedInfo info = parser.parse_file(&caff);
        gen_output(info, outGIF);
    }
    catch (ParserException &e)
    {
        cout << e.what() << endl;
        if (caff.is_open())
            caff.close();
        return 1;
    }
    catch (json::exception &e)
    {
        cout << e.what() << endl;
        if (caff.is_open())
            caff.close();
        return 1;
    }
    caff.close();
    return 0;
}
