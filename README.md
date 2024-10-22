# BitcoinAddressValidator

A Java library for validating Bitcoin addresses in both Base58 and Bech32 formats.

## Overview

`BitcoinAddressValidator` provides a simple and efficient way to validate Bitcoin addresses. It supports two main address formats:
- Base58 (P2PKH / P2SH addresses starting with '1' or '3')
- Bech32 (SegWit addresses starting with 'bc1')

The library uses cryptographic hashing (SHA-256) to verify the integrity of the addresses.

## Features

- Validates Base58 encoded Bitcoin addresses.
- Validates Bech32 encoded Bitcoin addresses.
- Provides a simple API for easy integration.

## Getting Started

To use `BitcoinAddressValidator`, follow these steps:

1. **Clone the repository**:
```sh
git clone https://github.com/dygogogo/BitcoinAddressValidator.git
cd BitcoinAddressValidator
```
2. **Build the project**:
Use Maven to build the project:
```sh
mvn clean install
```

3. **Add the dependency**:
Add the following dependency to your pom.xml:
```xml
<dependency>
    <groupId>com.dygogogo</groupId>
    <artifactId>bitcoin-address-validator</artifactId>
    <version>1.0.0</version>
</dependency>
```

4. **Use the validator**:
Here's a simple example of how to use the validator:

```java
import com.dygogogo.BitcoinAddressValidator;

public class Main {
    public static void main(String[] args) {
        try {
            boolean isValidBase58 = BitcoinAddressValidator.validateBase58Address("1BoatSLRHtKNngkdXEeobR76b53LETtpyT");
            boolean isValidBech32 = BitcoinAddressValidator.validateBech32Address("bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq");
            System.out.println("Base58 Address Valid: " + isValidBase58);
            System.out.println("Bech32 Address Valid: " + isValidBech32);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## Testing
The project includes a set of unit tests to ensure the validator works correctly. To run the tests:

```sh
mvn test
```

## Contributing
Contributions are welcome! Please follow these guidelines:

1. Fork the repository.
2. Create a new branch for your feature or fix.
3. Commit your changes.
4. Push to your branch and submit a pull request.

## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Contact
For any questions or suggestions, please create an issue on GitHub.