#include "parser.hpp"
#include <cstring>
#include "caff.h"
#include <stdexcept>
#include <ctime>
#include <sys/stat.h>
#include <iostream>

ParsedInfo CAFFParser::parse_file(std::ifstream *file)
{
    CaffHeader header;
    CaffCredits credits;
    CaffAnimation **animation;
    uint8_t blk_type;
    uint64_t blk_len;

    // First block --> must be a CAFF HEADER
    try
    {
        blk_type = next_block_info(file, blk_len);
    }
    catch (ParserException &e)
    {
        throw e;
    }

    if (blk_type != CAFFBlockType::Header)
    {
        throw ParserException("First block must be a valid CAFF header");
    }
    char *bytes = nullptr;

    try
    {
        bytes = next_block(file, blk_len);
        header = parse_header(bytes, blk_len);
    }
    catch (ParserException &e)
    {
        delete[] bytes;
        throw e;
    }
    catch (std::bad_alloc)
    {
        throw ParserException("Invalid memory allocation.");
    }
    delete[] bytes;

    // Second block

    try
    {
        blk_type = next_block_info(file, blk_len);
    }
    catch (ParserException &e)
    {
        throw e;
    }

    if (blk_type != CAFFBlockType::Credits)
    {
        throw ParserException("Credits block must come after the header.");
    }
    try
    {
        bytes = next_block(file, blk_len);
        credits = parse_credits(bytes, blk_len);
    }
    catch (ParserException &e)
    {
        delete[] bytes;
        throw e;
    }
    catch (std::bad_alloc)
    {
        throw ParserException("Invalid memory allocation.");
    }

    // Animation blocks
    try
    {
        animation = new CaffAnimation *[header.num_anim];
        for (int i = 0; i < header.num_anim; i++)
        {
            delete[] bytes;
            bytes = nullptr;
            blk_type = next_block_info(file, blk_len);
            if (blk_type != CAFFBlockType::Animation)
            {
                throw ParserException("Animation block must come after credits.");
            }
            bytes = next_block(file, blk_len);
            animation[i] = parse_animation(bytes, blk_len, i);
            num_anim_parsed++;
        }
    }
    catch (ParserException &e)
    {
        if (bytes != nullptr)
            delete[] bytes;
        delete[] animation;
        throw e;
    }
    catch (std::bad_alloc)
    {
        throw ParserException("Invalid memory allocation.");
    }

    delete[] bytes;
    if (num_anim_parsed < header.num_anim)
        throw ParserException("Could not read the specified amount of animation block. Abort.");
    if (file->peek() != EOF)
        throw ParserException("File unnecessarily longer than needed. You are a bad man.");

    ParsedInfo pi;
    pi.caff_header = header;
    pi.credits = credits;
    pi.animation = animation;
    return pi;
}

CaffHeader CAFFParser::parse_header(char *bytes, uint64_t blk_len)
{
    char magic[4];
    std::memcpy(magic, bytes, 4);

    if ((int)*magic != (int)*_caffmagic)
    {
        throw ParserException("WRONG CAFF MAGIC");
    }
    if ((uint32_t)(bytes[4]) != blk_len)
    {
        throw ParserException("WRONG BLOCK LENGTH");
    }
    CaffHeader header;
    header.header_size = (uint32_t)(bytes[4]);
    header.num_anim = (uint32_t)(bytes[12]);
    return header;
}

CaffCredits CAFFParser::parse_credits(char *bytes, uint64_t blk_len)
{
    CaffCredits credits;
    time_t now = time(0);
    tm *ltm = localtime(&now);

    credits.YY = (uint16_t)((uint8_t)(bytes[1]) << 8 | (uint8_t)(bytes[0]));
    credits.M = (uint8_t)(bytes[2]);
    credits.D = (uint8_t)(bytes[3]);
    credits.h = (uint8_t)(bytes[4]);
    credits.m = (uint8_t)(bytes[5]);
    try
    {
        check_interval(credits.YY, 1900, 1900 + ltm->tm_year);
        check_interval(credits.M, 1, 12);
        check_interval(credits.D, 1, ltm->tm_mday);
        check_interval(credits.h, 0, 24);
        check_interval(credits.m, 0, 60);
    }
    catch (std::invalid_argument &e)
    {
        throw ParserException(string("Invalid argument value in credits: ") + string(e.what()));
    }
    auto creator_len = convert_8_bytes(bytes + 6);
    if (6 + 8 + creator_len != blk_len)
        throw ParserException("Bad kitty, credits block length - creator length mismatch.");

    if (creator_len == 0)
        credits.creator == "";
    else
    {
        string creator;
        for (int i = 14; i < 14 + creator_len; i++)
            creator += bytes[i];
        credits.creator = creator;
    }
    return credits;
}

CaffAnimation *CAFFParser::parse_animation(char *bytes, uint64_t blk_len, int num_anim)
{
    CaffAnimation *animation = new CaffAnimation();
    animation->duration = convert_8_bytes(bytes);
    try
    {
        animation->header = parse_ciff_header(bytes + 8, blk_len);
    }
    catch (ParserException &e)
    {
        delete animation;
        throw e;
    }
    if (animation->header->content_size > STORAGE_MAX || animation->header->content_size < 0)
        throw ParserException("Content size specified is out of bounds");
    char *img = new char[animation->header->content_size];
    memcpy(img, bytes + 8 + animation->header->header_size, animation->header->content_size);
    animation->img = img;
    return animation;
}

CiffHeader *CAFFParser::parse_ciff_header(char *bytes, uint64_t blk_len)
{
    char magic[4];
    std::memcpy(magic, bytes, 4);
    if ((int)*magic != (int)*_ciffmagic)
    {
        throw ParserException("WRONG CIFF MAGIC");
    }
    CiffHeader *header = new CiffHeader();
    header->header_size = convert_8_bytes(bytes + 4);
    header->content_size = convert_8_bytes(bytes + 12);
    header->width = convert_8_bytes(bytes + 20);
    header->height = convert_8_bytes(bytes + 28);
    if (blk_len != header->header_size + header->content_size + 8)
    {
        delete header;
        throw ParserException("CIFF size attributes do not match.");
    }
    if (bytes[header->header_size - 1] != '\n' && bytes[header->header_size - 1] != '\0')
    {
        delete header;
        throw ParserException("Problem with CIFF Caption/tags ending character.");
    }
    try
    {
        parse_ciff_strings(bytes, *header);
    }
    catch (std::bad_alloc)
    {
        throw ParserException("Invalid memory allocation");
    }
    return header;
}

void CAFFParser::parse_ciff_strings(char *bytes, CiffHeader &header)
{
    char *cap_end = parse_caption(bytes, header);
    if (cap_end < bytes + header.header_size)
    {
        parse_tags(bytes, cap_end, header);
    }
}

char *CAFFParser::parse_caption(char *bytes, CiffHeader &header)
{
    char *beg = bytes + 36;
    char *end = beg;
    for (; *end != '\n' && end < bytes + header.header_size; end++)
        ;
    char cap[end - beg + 1];
    std::memcpy(cap, beg, end - beg);
    cap[end - beg] = '\0';
    header.caption = string(cap);
    return end;
}

void CAFFParser::parse_tags(char *bytes, char *sep, CiffHeader &header)
{
    int num_tags = 0;
    if (bytes[header.header_size - 1] != '\0')
        throw ParserException("No trailing zero found in tags section.");
    for (char *p = sep + 1; p < bytes + header.header_size; p++)
        if (*p == '\0')
            num_tags++;
    header.num_tags = num_tags;
    header.tags = new string *[num_tags];
    for (size_t i = 0; i < num_tags; i++)
    {
        header.tags[i] = new string(++sep);
        for (; *sep != '\0'; sep++)
            ;
    }
}

uint64_t bytes_to_int(const char *buffer, uint64_t start, uint64_t end, bool check, uint64_t condition)
{
    uint64_t result = 0;
    for (uint64_t i = start; i < start + end; i++)
    {
        result |= (uint8_t)buffer[i] << (i - start) * 8;
    }
    if (check && result != condition)
    {
        throw std::invalid_argument(to_string(result) + " != " + to_string(condition));
    }
    else
    {
        return result;
    }
}

string bytes_to_string(const char *buffer, uint64_t start, uint64_t end, bool check, string condition)
{
    string result;
    for (uint64_t i = start; i < start + end; i++)
    {
        result += buffer[i];
    }
    if (check && result != condition)
    {
        throw std::invalid_argument(result + " != " + condition);
    }
    else
    {
        return result;
    }
}