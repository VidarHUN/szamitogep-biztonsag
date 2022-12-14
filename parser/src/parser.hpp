#ifndef PARSER_HPP
#define PARSER_HPP

#include <fstream>
#include "caff.h"
#include "ciff.h"
#include <stdexcept>
#include "json.hpp"

#define STORAGE_MAX (__UINT64_C(31457280)) // 30 MB

struct ParsedInfo
{
    CaffHeader caff_header;
    CaffCredits credits;
    CaffAnimation **animation;

    ParsedInfo() : animation(nullptr) {}
    ~ParsedInfo()
    {
        if (animation != nullptr)
        {
            for (size_t i = 0; i < caff_header.num_anim; i++)
                delete animation[i];
            delete[] animation;
        }
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

    int num_anim_parsed;

    char *buf1, *buf4, *buf8;
    char _caffmagic[4] = {0x43, 0x41, 0x46, 0x46};
    char _ciffmagic[4] = {0x43, 0x49, 0x46, 0x46};

    CaffHeader parse_header(char *bytes, uint64_t blk_len);
    CaffCredits parse_credits(char *bytes, uint64_t);
    CaffAnimation *parse_animation(char *bytes, uint64_t blk_len, int num_anim);
    CiffHeader *parse_ciff_header(char *bytesm, uint64_t blk_len);

    void parse_ciff_strings(char *bytes, CiffHeader &);
    char *parse_caption(char *bytes, CiffHeader &);
    void parse_tags(char *, char *, CiffHeader &);

    CAFFBlockType next_block_info(std::ifstream *file, uint64_t &len)
    {
        uint8_t typ;
        typ = read_block_type(file);
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
        if (len == 0)
            throw ParserException("Length in CAFF block is zero.");
        char *bytes = new char[len];
        try
        {
            file->read(bytes, len);
        }
        catch (std::exception)
        {
            throw ParserException("Cannot read more bytes from file.");
        }
        return bytes;
    }

    inline uint8_t read_block_type(std::ifstream *file)
    {
        try
        {
            file->read(buf1, 1);
        }
        catch (std::exception)
        {
            throw ParserException("Cannot read more bytes from file.");
        }
        return (uint8_t)(*buf1);
    }

    inline uint64_t read_block_len(std::ifstream *file)
    {
        try
        {
            file->read(buf8, 8);
        }
        catch (std::exception)
        {
            throw ParserException("Cannot read more bytes from file.");
        }
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
        num_anim_parsed = 0;
        buf1 = new char[1];
        buf4 = new char[4];
        buf8 = new char[8];
    }
    ~CAFFParser()
    {
        delete[] buf1;
        delete[] buf4;
        delete[] buf8;
    }

    ParsedInfo parse_file(std::ifstream *file);
};

// Convert bytes to int
uint64_t bytes_to_int(const char *buffer, uint64_t start, uint64_t end, bool check = false, uint64_t condition = 0);
string bytes_to_string(const char *buffer, uint64_t start, uint64_t end, bool check = false, string condition = "");

#endif // PARSER_HPP