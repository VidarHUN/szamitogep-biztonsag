#include <iostream>
#include <fstream>
#include <cstdint>
#include "parser.hpp"

using namespace std;

//overrideing to_json function
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
                    {"duration", info.animation->duration},
                    {{"ciff_header_size", info.animation->header.header_size}, 
                    {"ciff_header_content_size", info.animation->header.content_size},
                    {"ciff_header_width", info.animation->header.width},
                    {"ciff_header_height", info.animation->header.height},
                    {"ciff_header_caption", info.animation->header.caption}}
                    ///TO DO dinamikus tömböket serializálni
                    //{"ciff_header_tags", info.animation->header.tags}}
                    //{"img", info.animation->img}    
    };
}

//overrideing from_json function
////nem lehetett tagfüggvény, csak felül kell írni és meghívni
inline void from_json(const json& j, ParsedInfo& info){
    j.at("blk_info").at("blk_type").get_to(info.blk_info.type);
    j.at("blk_info").at("blk_length").get_to(info.blk_info.length);
    j.at("caff_header").at("caff_header_size").get_to(info.caff_header.header_size);
    j.at("caff_header").at("caff_header_num_anim").get_to(info.caff_header.num_anim);
    j.at("caff_credits").at("year").get_to(info.credits.YY);
    j.at("caff_credits").at("month").get_to(info.credits.M);
    j.at("caff_credits").at("day").get_to(info.credits.D);
    j.at("caff_credits").at("hour").get_to(info.credits.h);
    j.at("caff_credits").at("minute").get_to(info.credits.m);
    j.at("caff_credits").at("creator").get_to(info.credits.creator);
    j.at("animations").at("duration").get_to(info.animation->duration);
    /*TO DO
    a HIBA [json.exception.type_error.304] cannot use at() with array
    
    j.at("animations").at("ciff_header_size").get_to(info.animation->header.header_size);
    j.at("animations").at("ciff_header_content_size").get_to(info.animation->header.content_size);
    j.at("animations").at("ciff_header_width").get_to(info.animation->header.width);
    j.at("animations").at("ciff_header_height").get_to(info.animation->header.height);*/
    //j.at("animations").at("ciff_header_caption").get_to(info.animation->header.caption);
    ///TO DO dinamikus ttömbökre
    //ciff_header_tags
    //image

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
        cout << "FATAL: File caff_files/1.caff could not be opened.\n";
        return 1;
    }
    else
    {
        CAFFParser parser;
        json j;
        json jj;
        try
        {   
            ParsedInfo info = parser.parse_file(&file);
            to_json(j, info);
            //serialize(j);
            ParsedInfo pars;
            from_json(j,pars);
            cout << "header len:\t" << pars.blk_info.length << endl;
            cout << "header len:\t" << pars.caff_header.header_size << endl;
            cout << "num anim:\t" << pars.caff_header.num_anim << endl;
            /*cout << "Credits:\t" << info.credits.toString() << endl;
            for (int i = 0; i < info.caff_header.num_anim; i++)
            {
                cout << "Animation" << i + 1 << ":\t"
                     << "duration: " << info.animation[i].duration
                     << "\theader: " << info.animation[i].header.header_size
                     << "\tcontent: " << info.animation[i].header.content_size << endl;
            }*/
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
