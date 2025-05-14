# LZ78 Compression Algorithm
A Java implementation of the LZ78 compression algorithm.
## Description
This project implements the LZ78 compression algorithm in Java. LZ78 is a dictionary-based lossless data compression technique that builds a dictionary of previously seen patterns to encode data more efficiently.
## Features

1. Text file compression using LZ78 dictionary-based approach
2. File decompression to restore original data
3. Binary string representation of compressed data
4. Command-line interface for user interaction
5. Compression statistics including bit savings analysis

## Usage
The program provides a simple command-line interface with three options:
- Choose an option: compress, decompress, or exit
### Compression
- Enter input file name: example.txt
- Enter output binary file name: example.bin
### Decompression
- Enter binary file name: example.bin
- Enter output text file name: restored.txt
- Implementation Details
  
## This implementation:

* Builds a dictionary of patterns found in the input data
* Encodes each pattern as a (index, character) pair
* Represents compressed data in binary form
* Provides bit-level analysis of compression efficiency

## Getting Started
1.Clone the repository:

```bash
git clone https://github.com/yourusername/lz78-compression.git
```

2. Compile the Java code:
   
```
javac App.java
```

3. Place your input text files in the 'resources' directory
4. Follow the prompts to compress or decompress files
5. The compressed/decompressed files will be saved in the 'resources' directory


## Performance
The algorithm provides efficient compression for text files with repeated patterns. Compression ratio depends on the repetitive nature of the input data.
## License
MIT
