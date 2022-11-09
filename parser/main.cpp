#include <iostream>
#include <fstream>
#include <cstdint>
#include "parser.hpp"

using namespace std;
using json = nlohmann::json_abi_v3_11_2::json;

//nem lehetett tagfüggvény, csak felül kell írni és meghívni
void serialize(json& j, const ParsedInfo& info){
    j["num_anim"] =  info.caff_header.num_anim;
    j["year"] = info.credits.YY;
    j["month"] = info.credits.M;
    j["day"]= info.credits.D;
    j["hour"]=info.credits.h;
    j["minute"]=info.credits.m;
    j["creator"]= info.credits.creator;

    /*ToDo
    Ez a struktura nem sikerült
     "animation": [
        {
            "duration": 1,
            "ppm": "ppm name"
        }
    ]
    */
    //for (int i = 0; i < info.caff_header.num_anim; i++){
        /*Lehet ezek nem is kellenek :
        j["captions"]=info.animation[i]->header->caption;
        j["content_size"]=info.animation[i]->header->caption;
        j["width"]=info.animation[i]->header->width;
        j["height"]=info.animation[i]->header->height;
        j["header_size"]=info.animation[i]->header->header_size;
        */

        //j["duration"]=info.animation[i]->duration;
        /*for(size_t k = 0; k < info.animation[i]->header->num_tags; k++)
                {   
                    j["tags"]={info.animation[i]->header->tags[k]};
                }
    
    }*/
    
}

//prints the json object to stdout 
static void serialize_write(const json& j)
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
            serialize(j,info);
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
