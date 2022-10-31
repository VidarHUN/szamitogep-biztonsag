#ifndef CAFF_H
#define CAFF_H

#include <cstdint>

struct Caff {
    uint8_t id; // 0x1 header | 0x2 credits | 0x3 animation
    uint8_t length; // length of the data field
    // I think we should not store the data field because
    // we have to parse it anyway.
} __attribute__((packed));

struct CaffHeader {
    char magic[4]; // ASCII chars. It should be always 'CAFF'
    uint64_t header_size; // size of the header
    uint64_t num_anim; // Number of CIFF animation blocks
} __attribute__((packed));

struct CaffCredits {
    // date params
    uint16_t YY;
    uint8_t M;
    uint8_t D;
    uint8_t h;
    uint8_t m;

    uint64_t creator_len; // The length of the field specifying the creator
    char* creator; // the creator of the CAFF file
} __attribute__((packed));

struct CaffAnimation {
    uint64_t duration; // indicates when the CIFF image must be displayed [ms]
    // TODO: I am not sure how to store CIFF image.
    // TODO: Also, we have to find a way show image from binary
} __attribute__((packed));

#endif // CAFF_H
