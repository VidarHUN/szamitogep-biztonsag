#include "parser.hpp"
#include <cstring>
#include "caff.h"
#include <iostream>
#include <stdexcept>
#include <cassert>

ParsedInfo CAFFParser::parse_file(std::ifstream *file)
{
    // First block --> CAFF HEADER
    file->read(buf1, 1);
    uint8_t typ = (uint8_t)(*buf1);
    assert(typ == 1);
    file->read(buf8, 8);
    uint64_t blk_size = (uint64_t)(*buf8);
    char *header_bytes = new char[blk_size];
    file->read(header_bytes, blk_size);
    CaffHeader header;
    try
    {
        header = parse_header(header_bytes, blk_size);
    }
    catch (ParserException &e)
    {
        std::cout << e.what() << std::endl;
        exit(-1);
    }
    ParsedInfo pi;
    pi.blk_info.type = typ;
    pi.blk_info.length = blk_size;
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