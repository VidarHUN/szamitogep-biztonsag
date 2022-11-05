#ifndef PARSER_HPP
#define PARSER_HPP

#include <fstream>
#include "caff.h"

struct BlockInfo
{
    uint8_t type;
    uint64_t length;
};
struct ParsedInfo
{
    BlockInfo blk_info;
    CaffHeader caff_header;
};

class CAFFParser
{
private:
    char *buf1, *buf4, *buf8;
    char _magic[4] = {0x43, 0x41, 0x46, 0x46};

public:
    CAFFParser()
    {
        buf1 = new char[1];
        buf4 = new char[4];
        buf8 = new char[8];
    }
    ~CAFFParser()
    {
        delete[] buf1, buf4, buf8;
    }

    ParsedInfo parse_file(std::ifstream *file);
    CaffHeader parse_header(char *file, uint64_t blk_len);
    CaffCredits parse_credits(std::ifstream *file);
};

#endif // PARSER_HPP