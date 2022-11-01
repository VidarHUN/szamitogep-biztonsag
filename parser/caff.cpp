#include "caff.h"
#include <string>

using namespace std;

CaffHeader caff_header(const char* buffer){
    CaffHeader header;
    uint8_t id = bytes_to_int(buffer, 0, 1);
    if (id != 1) {
        throw "Header ID not matching with specification: " + id;
    }
    uint64_t header_length = bytes_to_int(buffer, 1, 8);
    if (header_length != 20) {
        throw "Wrong header length: " + header_length;
    }
    string magic = bytes_to_string(buffer, 8, 12);
    if (magic != "CAFF") {
        throw "Magic is not CAFF: " + magic;
    }
    uint64_t header_size = bytes_to_int(buffer, 12, 20);
    if (header_size != 20) {
        throw "Wrong header size: " + header_size;
    }
    uint64_t num_anim = bytes_to_int(buffer, 20, 28);

    header.num_anim = num_anim;

    return header;
}

uint64_t bytes_to_int(const char* buffer, uint64_t start, uint64_t end) {
    uint64_t result = 0;
    for (uint64_t i = start; i < start + end; i++) {
        result |= (uint8_t) buffer[i] << (i - start) * 8;
    }
    return result;
}

string bytes_to_string(const char* buffer, uint64_t start, uint64_t end) {
    string result;
    for (uint64_t i = start; i < start + end; i++) {
        result += buffer[i];
    }
    return result;
}