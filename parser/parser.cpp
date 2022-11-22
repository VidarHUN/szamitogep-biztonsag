#include "parser.hpp"
#include <cstring>
#include "caff.h"
#include <stdexcept>
#include <ctime>
#include <sys/stat.h>

ParsedInfo CAFFParser::parse_file(std::ifstream *file)
{
    CaffHeader header;
    CaffCredits credits;
    CaffAnimation **animation;
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
        delete[] bytes;
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
    delete[] bytes;
    bytes = next_block(file, blk_len);
    try
    {
        credits = parse_credits(bytes);
    }
    catch (std::exception &e)
    {
        delete[] bytes;
        throw e;
    }

    // Animation blocks
    animation = new CaffAnimation *[header.num_anim];
    try
    {
        for (int i = 0; i < header.num_anim; i++)
        {
            blk_type = next_block_info(file, blk_len);
            if (blk_type != CAFFBlockType::Animation)
                throw ParserException("Animation block must come after credits.");
            delete[] bytes;
            bytes = next_block(file, blk_len);
            animation[i] = parse_animation(bytes, blk_len, i);
        }
    }
    catch (exception &e)
    {
        delete[] bytes;
        throw e;
    }

    delete[] bytes;

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

CaffCredits CAFFParser::parse_credits(char *bytes)
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
        auto creator_len = (uint64_t)(bytes[6]);
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
    catch (std::invalid_argument &e)
    {
        throw;
    }
}

void create_ppm_image(char *img, CiffHeader *header, int num_anim)
{
    struct stat st;
    if (stat("ppm", &st) != 0)
    {
        mkdir("ppm", S_IRWXU | S_IRWXG);
    }
    try
    {
        std::ofstream myImage("ppm/image" + to_string(num_anim) + ".ppm", ios::out | ios::binary);

        const int width = header->width, height = header->height;

        {                                              // Image header - Need this to start the image properties
            myImage << "P6" << endl;                   // Declare that you want to use ASCII colour values
            myImage << width << " " << height << endl; // Declare w & h
            myImage << "255" << endl;                  // Declare max colour ID
        }

        for (size_t i = 0; i < width * height * 3; i++)
        {
            myImage << static_cast<unsigned char>(img[i]);
        }
        myImage.close();
    }
    catch (exception &e)
    {
        throw e;
    }

    return;
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
    char *img = new char[animation->header->content_size];
    memcpy(img, bytes + 8 + animation->header->header_size, animation->header->content_size);
    try
    {
        create_ppm_image(img, animation->header, num_anim);
    }
    catch (exception &e)
    {
        throw e;
    }
    animation->img = img;
    return animation;
}

CiffHeader *CAFFParser::parse_ciff_header(char *bytes, uint64_t blk_len)
{
    char magic[4];
    std::memcpy(magic, bytes, 4);
    if ((int)*magic != (int)*_ciffmagic)
        throw ParserException("WRONG CIFF MAGIC");
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
    parse_ciff_strings(bytes, *header);
    return header;
}

void parse_ciff_strings(char *bytes, CiffHeader &header)
{
    char *cap_end = parse_caption(bytes, header);
    if (cap_end < bytes + header.header_size)
    {
        parse_tags(bytes, cap_end, header);
    }
}

char *parse_caption(char *bytes, CiffHeader &header)
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

void parse_tags(char *bytes, char *sep, CiffHeader &header)
{
    int num_tags = 0;
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
