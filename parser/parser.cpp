#include "parser.hpp"
#include <cstring>
#include "caff.h"
#include <iostream>

ParsedInfo CAFFParser::parse_file(std::ifstream *file)
{
    file->read(buf1, 1);
    file->read(buf8, 8);
    uint64_t blk_size = (uint64_t)(*buf8);
    char *header_bytes = new char[blk_size];
    file->read(header_bytes, blk_size);
    auto header = parse_header(header_bytes, blk_size);
    ParsedInfo pi;
    pi.blk_info.type = (uint8_t)(*buf1);
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
        std::cout << "WRONG MAGIC" << std::endl;
    }
    if ((uint32_t)(bytes[4]) != blk_len)
    {
        std::cout << "WRONG LEN" << std::endl;
        // ERROR
    }
    CaffHeader header;
    header.header_size = (uint32_t)(bytes[4]);
    header.num_anim = (uint32_t)(*(bytes + 12));
    return header;
}