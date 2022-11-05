#include "parser.hpp"
#include <cstring>
#include "caff.h"
#include <iostream>
#include <stdexcept>

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
    delete bytes;

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
    bytes = next_block(file, blk_len);
    try
    {
        credits = parse_credits(bytes, blk_len);
    }
    catch (ParserException &e)
    {
        delete bytes;
        throw e;
    }
    delete bytes;

    // Animation blocks
    animation = new CaffAnimation[header.num_anim];
    for (size_t i = 0; i < header.num_anim; i++)
    {
        /* code */
    }

    ParsedInfo pi;
    pi.caff_header = header;
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
    header.num_anim = (uint32_t)(*(bytes + 12));
    return header;
}

CaffCredits CAFFParser::parse_credits(char *bytes, uint64_t blk_len)
{
}
