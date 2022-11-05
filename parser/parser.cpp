#include "parser.hpp"
#include <cstring>
#include "caff.h"
#include <iostream>
#include <stdexcept>
#include <ctime>

// Ezzel kell majd vmit kezdeni mert elég hoszzúúúúú függvény
ParsedInfo CAFFParser::parse_file(std::ifstream *file)
{
    CaffHeader header;
    CaffCredits credits;
    CaffAnimation *animation;
    uint8_t blk_type;
    uint64_t blk_len;

    // First block --> CAFF HEADER
    blk_type = next_block_info(file, blk_len);
    if (blk_type != CAFFBlockType::Header)
        throw ParserException("First block must be a valid CAFF header");
    char *bytes = next_block(file, blk_len);
    try
    {
        header = parse_header(bytes, blk_len);
    }
    catch (ParserException &e)
    {
        delete bytes;
        throw e;
    }

    // El kellene dönteni, hogy:
    // a) csak a header után jöhet a credits, vagy
    // b) csak a header után vagy csak a legvégén, vagy
    // c) jöhet két animation block között is akár

    // most az a) verzió működik

    // Second block
    blk_type = next_block_info(file, blk_len);
    // if (typ != 2 && typ != 3)
    if (blk_type != CAFFBlockType::Credits)
        throw ParserException("Credits block must come after the header.");
    delete bytes;
    bytes = next_block(file, blk_len);
    try
    {
        credits = parse_credits(bytes, blk_len);
    }
    catch (std::exception &e)
    {
        delete bytes;
        throw e;
    }

    // Animation blocks
    // animation = new CaffAnimation[header.num_anim];
    // for (size_t i = 0; i < header.num_anim; i++)
    // {
    //     /* code */
    // }

    ParsedInfo pi;
    pi.caff_header = header;
    pi.credits = credits;
    return pi;
}

CaffHeader CAFFParser::parse_header(char *bytes, uint64_t blk_len)
{
    char magic[4];
    std::memcpy(magic, bytes, 4);

    if ((int)*magic != (int)*_magic)
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
    try
    {
        credits.YY = (uint16_t)((uint8_t)(bytes[1]) << 8 | (uint8_t)(bytes[0]));
        check_interval(credits.YY, 1900, 1900 + ltm->tm_year);
        credits.M = (uint8_t)(bytes[2]);
        check_interval(credits.M, 1, 12);
        credits.D = (uint8_t)(bytes[3]);
        check_interval(credits.D, 1, ltm->tm_mday);
        credits.h = (uint8_t)(bytes[4]);
        check_interval(credits.h, 0, 24);
        credits.m = (uint8_t)(bytes[5]);
        check_interval(credits.m, 0, 60);
        auto creator_len = bytes_to_int(bytes, 6, 14);
        credits.creator = "Sanyi";
        return credits;
    }
    catch (std::invalid_argument &e)
    {
        throw;
    }
}
