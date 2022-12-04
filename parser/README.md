# Parser

## Required external headers

- [json.hpp](https://json.nlohmann.me/home/releases/)
- [extern/gif-h](https://github.com/charlietangora/gif-h)

extern/gif-h must be synchronised from GitHub. If you have not cloned recursively, execute the following commands in the root of the project:

```
git submodule init --recursive
git submodule update --init --recursive
```


## Build

Simply execute `make` and the binary called *parser* is built.