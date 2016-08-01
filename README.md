## Cash Register (Coding Exercise)

This is a coding exercise to create a simple cash register, which can show 
current balance, accept bills in, dispense bills out, and make change.

## Features

The cash register supports a number of interesting features:

- Support for arbitrary user-supplied sets of currency denominations
- Support for non-canonical denominations (denominations which are not multiples - this prevents use of simple greedy algorithms)
- Separate classes encapsulating all business logic, and command-line interface
- Full unit test coverage

## Usage

The cash register can be operated by running Java with the main class, 
CashRegisterCLI.

Type 'usage' to see available commands. Supported commands include:
- show - shows cash register inventory.
- put x y z - puts money in register (a, b, c... are amounts per denomination).
- take x y z - takes money from register (a, b, c... are amounts per denomination).
- change x - shows change necessary to provide x, and takes money from register.
- usage - shows usage help.
- quit - exits the program.

## Contributors

Steven Noto (https://github.com/stevennoto/)

## License

Public Domain
