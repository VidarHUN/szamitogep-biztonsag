#ifndef PARSER_HPP
#define PARSER_HPP

#include <fstream>
#include "caff.h"
#include <stdexcept>

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
    CaffHeader parse_header(char *bytes, uint64_t blk_len);
    CaffCredits parse_credits(std::ifstream *file);
    inline uint8_t read_block_type(std::ifstream *file)
    {
        file->read(buf1, 1);
        return (uint8_t)(*buf1);
    }
    inline uint64_t read_block_len(std::ifstream *file)
    {
        file->read(buf8, 8);
        return (uint64_t)(*buf8);
    }
};

class ParserException : public std::exception
{
private:
    string msg;

public:
    ParserException(string s) : msg(s) {}
    const char *what() { return msg.c_str(); }
};
#endif // PARSER_HPP