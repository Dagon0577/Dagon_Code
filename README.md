# Dagon_Code
- [Chinese documentation](README-ZH.md)

Just a small experiment.

Obtain CPU, motherboard, MAC address, virtual ID (docker, machine-rkt) from Windows, Linux, and generate Dagon_Code.
## Quick Start

Run testCase01 and the machine information is stored in Info.txt

## Encryption scheme design

(1) Use the SHA1 algorithm to encrypt the original information resources to generate a 128-bit hash value;

(2) Initialize the Blowfish encryption algorithm by using the fixed-length hash value generated in step (1) as the user key of the Blowfish algorithm;

(3) The Blowfish algorithm is used to encrypt the original information resources after initialization. After encryption, high-security ciphertext data will be generated.
