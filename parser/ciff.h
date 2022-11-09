#ifndef CIFF_H
#define CIFF_H

#include <cstdint>
#include <string>

struct CiffHeader
{
    uint64_t header_size;  // length of the whole header
    uint64_t content_size; // size of the image pixels
    uint64_t width;        // width of the image from 0 to somewhere
    uint64_t height;       // height of the image from 0 to somewhere
    std::string caption;   // ASCII string and the caption of the image
    int num_tags;
    std::string **tags; // \0 separated tags. Each tag is variable-length
                        // ASCII strings

    CiffHeader() : tags(nullptr) {}
    ~CiffHeader()
    {
        if (tags != nullptr)
        {
            for (size_t i = 0; i < num_tags; i++)
                delete tags[i];
            delete[] tags;
        }
    }
};

// Each pixel is in RGB format and taking 1 byte.

#endif // CIFF_H
