#ifndef CIFF_H
#define CIFF_H

#include <cstdint>
#include <string>

#define CIFF_MAGIC 0x43494646

struct CiffHeader
{
    uint64_t header_size;  // length of the whole header
    uint64_t content_size; // size of the image pixels
    uint64_t width;        // width of the image from 0 to somewhere
    uint64_t height;       // height of the image from 0 to somewhere
    std::string caption;   // ASCII string and the caption of the image
    std::string *tags;     // \0 separated tags. Each tag is variable-length
                           // ASCII strings

    CiffHeader() : tags(nullptr) {}
    CiffHeader(int num_tags) : tags(new std::string[num_tags]) {}
    ~CiffHeader()
    {
        if (tags != nullptr)
            delete tags;
    }
};

// Each pixel is in RGB format and taking 1 byte.

#endif // CIFF_H
