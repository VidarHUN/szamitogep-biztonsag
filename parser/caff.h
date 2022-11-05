#ifndef CAFF_H
#define CAFF_H

#include <cstdint>
#include <string>

using namespace std;

struct CaffHeader
{
    uint64_t header_size; // size of the header
    uint64_t num_anim;    // Number of CIFF animation blocks
} __attribute__((packed));

struct CaffCredits
{
    // date params
    uint16_t YY;
    uint8_t M;
    uint8_t D;
    uint8_t h;
    uint8_t m;

    uint64_t creator_len; // The length of the field specifying the creator
    string creator;       // the creator of the CAFF file
};

struct CaffAnimation
{
    uint64_t duration; // indicates when the CIFF image must be displayed [ms]
    // TODO: I am not sure how to store CIFF image.
    // TODO: Also, we have to find a way show image from binary
} __attribute__((packed));

CaffHeader caff_header(const char *buffer);
CaffCredits caff_credits(const char *buffer);

void check_interval(uint64_t x, uint64_t a, uint64_t b);
// Convert bytes to int
uint64_t bytes_to_int(const char *buffer, uint64_t start, uint64_t end, bool check = false, uint64_t condition = 0);
string bytes_to_string(const char *buffer, uint64_t start, uint64_t end, bool check = false, string condition = "");

#endif // CAFF_H
