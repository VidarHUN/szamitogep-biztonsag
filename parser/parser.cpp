#include "parser.hpp"
#include <cstring>
#include "caff.h"
#include <iostream>
#include <stdexcept>

ParsedInfo CAFFParser::parse_file(std::ifstream *file)
{
    CaffHeader header;
    CaffCredits credits;
    // First block --> CAFF HEADER
    auto typ = read_block_type(file);
    if (typ != 1)
        throw ParserException("First block must be a valid CAFF header");
    auto blk_len = read_block_len(file);
    char *bytes = new char[blk_len];
    file->read(bytes, blk_len);
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

    // Second block
    typ = read_block_type(file);
    if (typ != 2 && typ != 3)
    {
        throw ParserException("Credits or Animation block must come after the header.");
    }
    blk_len = read_block_len(file);
    bytes = new char[blk_len];
    file->read(bytes, blk_len);

    if (typ == 2)
    {
        /* code */
    }
    else // typ == 3
    {
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