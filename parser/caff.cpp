#include "caff.h"
#include <string>
#include <stdexcept>
#include <ctime>

using namespace std;

// CaffHeader caff_header(const char* buffer) {
//     CaffHeader header;
//     try {
//         static_cast<void>(bytes_to_int(buffer, 0, 1, true, 1));
//         static_cast<void>(bytes_to_int(buffer, 1, 8, true, 20));
//         static_cast<void>(bytes_to_string(buffer, 8, 12, true, "CAFF"));
//         static_cast<void>(bytes_to_int(buffer, 12, 20, true, 20));
//         header.num_anim = bytes_to_int(buffer, 20, 28);
//         return header;
//     }
//     catch (std::invalid_argument& e) {
//         throw;
//     }
// }

// CaffCredits caff_credits(const char* buffer) {
//     CaffCredits credits;
//     time_t now = time(0);
//     tm *ltm = localtime(&now);
//     try {
//         credits.YY = bytes_to_int(buffer, 28, 30);
//         check_interval(credits.YY, 1900, 1900 + ltm->tm_year);
//         credits.M = bytes_to_int(buffer, 30, 31);
//         check_interval(credits.M, 1, 12);
//         credits.D = bytes_to_int(buffer, 31, 32);
//         check_interval(credits.D, 1, ltm->tm_mday);
//         credits.h = bytes_to_int(buffer, 32, 33);
//         check_interval(credits.h, 0, 24);
//         credits.m = bytes_to_int(buffer, 33, 34);
//         check_interval(credits.m, 0, 60);
//         credits.creator_len = bytes_to_int(buffer, 34, 42);
//         credits.creator = bytes_to_string(buffer, 42, 42 + credits.creator_len);
//         return credits;
//     }
//     catch (std::invalid_argument& e) {
//         throw;
//     }
// }


uint64_t bytes_to_int(const char* buffer, uint64_t start, uint64_t end, bool check, uint64_t condition) {
    uint64_t result = 0;
    for (uint64_t i = start; i < start + end; i++) {
        result |= (uint8_t) buffer[i] << (i - start) * 8;
    }
    if (check && result != condition) {
        throw std::invalid_argument(to_string(result) + " != " + to_string(condition));
    }
    else {
        return result;
    }
}

string bytes_to_string(const char* buffer, uint64_t start, uint64_t end, bool check, string condition) {
    string result;
    for (uint64_t i = start; i < start + end; i++) {
        result += buffer[i];
    }
    if (check && result != condition) {
        throw std::invalid_argument(result + " != " + condition);
    }
    else {
        return result;
    }
}