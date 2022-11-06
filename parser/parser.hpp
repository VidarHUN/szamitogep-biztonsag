#ifndef PARSER_HPP
#define PARSER_HPP

#include <fstream>
#include "caff.h"
#include "ciff.h"
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
    CaffCredits credits;
    CaffAnimation *animation;

    ParsedInfo() : animation(nullptr) {}
    ~ParsedInfo()
    {
        if (animation != nullptr)
            delete[] animation;
    }
};

class CAFFParser
{
private:
    enum CAFFBlockType
    {
        WRONG,
        Header,
        Credits,
        Animation
    };

    char *buf1, *buf4, *buf8;
    char _caffmagic[4] = {0x43, 0x41, 0x46, 0x46};
    char _ciffmagic[4] = {0x43, 0x49, 0x46, 0x46};

    CaffHeader parse_header(char *bytes, uint64_t blk_len);
    CaffCredits parse_credits(char *bytes);
    CaffAnimation parse_animation(char *bytes, uint64_t blk_len);
    CiffHeader parse_ciff_header(char *bytesm, uint64_t blk_len);

    inline CAFFBlockType next_block_info(std::ifstream *file, uint64_t &len)
    {
        auto typ = read_block_type(file);
        len = read_block_len(file);
        switch (typ)
        {
        case 1:
            return CAFFBlockType::Header;
        case 2:
            return CAFFBlockType::Credits;
        case 3:
            return CAFFBlockType::Animation;
        default:
            return CAFFBlockType::WRONG;
        }
    }

    inline char *next_block(std::ifstream *file, uint64_t len)
    {
        char *bytes = new char[len];
        file->read(bytes, len);
        return bytes;
    }

    inline uint8_t read_block_type(std::ifstream *file)
    {
        file->read(buf1, 1);
        return (uint8_t)(*buf1);
    }

    inline uint64_t read_block_len(std::ifstream *file)
    {
        file->read(buf8, 8);
        return convert_8_bytes(buf8);
    }

    inline uint64_t convert_8_bytes(char *bytes)
    {
        uint64_t val = 0;
        for (int i = 0; i < 8; i++)
        {
            val = val | (uint8_t) * (bytes + i) << i * 8;
        }
        return val;
    }

public:
    CAFFParser()
    {
        buf1 = new char[1];
        buf4 = new char[4];
        buf8 = new char[8];
    }
    ~CAFFParser()
    {
        delete buf1, buf4, buf8;
    }

    ParsedInfo parse_file(std::ifstream *file);
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