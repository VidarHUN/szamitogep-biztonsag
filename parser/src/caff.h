#ifndef CAFF_H
#define CAFF_H

#include <string>
#include <sstream>
#include "ciff.h"

using namespace std;

struct CaffHeader
{
    uint64_t header_size; // size of the header
    uint64_t num_anim;    // Number of CIFF animation blocks
};

struct CaffCredits
{
    // date params
    uint16_t YY;
    uint8_t M;
    uint8_t D;
    uint8_t h;
    uint8_t m;

    string creator; // the creator of the CAFF file

    string toString()
    {
        stringstream ss;
        ss << YY << "-" << (int)M << "-" << (int)D << " " << (int)h << ":" << (int)m << " | " << creator;
        return ss.str();
    }
};

struct CaffAnimation
{
    uint64_t duration; // indicates when the CIFF image must be displayed [ms]
    CiffHeader *header;
    char *img;

    CaffAnimation() : img(nullptr), header(nullptr) {}
    ~CaffAnimation()
    {
        if (img != nullptr)
            delete[] img;
        if (header != nullptr)
            delete header;
    }
};

inline void check_interval(uint64_t x, uint64_t a, uint64_t b)
{
    if (x < a || x > b)
    {
        throw std::invalid_argument(to_string(a) + " < " + to_string(x) + " < " + to_string(b));
    }
}
#endif // CAFF_H
