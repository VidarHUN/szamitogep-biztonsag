#include "parser.hpp"
#include <cstring>
#include "caff.h"
#include <iostream>
#include <stdexcept>

ParsedInfo CAFFParser::parse_file(std::ifstream *file)
{
    // First block --> CAFF HEADER
    auto typ = read_block_type(file);
    if (typ != 1)
        throw ParserException("First block must be a valid CAFF header");
    auto blk_len = read_block_len(file);
    char *header_bytes = new char[blk_len];
    file->read(header_bytes, blk_len);
    auto header = parse_header(header_bytes, blk_len);

    // Second block
    typ = read_block_type(file);

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