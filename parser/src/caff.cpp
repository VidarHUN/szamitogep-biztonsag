#include "caff.h"
#include <string>
#include <stdexcept>
#include <ctime>

using namespace std;

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