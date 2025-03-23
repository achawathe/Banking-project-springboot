# Banking Project Application

---
A Banking Project Application made with the Java SpringBoot Framework.
___
## Features:
- User Creation, Updating, Listing, and Deletion
- Account Creation (opening), Updating, Listing, and Deletion (closing)
    - Account Linking to a Single User (Many to One Unidirectional)
    - Logging of account opening, as well as closing in the Transactions database
- Transaction Logging and Listing
    - Transactions containing the following data:
        - Account To (Linked)
        - Account From (Linked)
        - User Linked to
        - Amount
        - Transaction types (opening, closing, transfer, withdrawal, deposit)
    - Each time a transaction has been made, it is logged
    - Get transactions by User, Account to and Account From, as well as by Account.
- Error Messages that return specific errors
- Input Verification (See assumptions Section)
    - Insufficient Funds
    - Invalid Balance or Amount
    - Invalid IDs
- Test Suite
- PostgresQL Integration (can be in-memory in needed)
- Dependency Injection in Service and Repository Layers
- Three Layer Design
- User of DTOs and Mappers to Map DTOs to Entities
- Robust Design and Extensibility

## How to run:
1. Go to https://www.azul.com/downloads/?package=jdk#zulu to download OpenJDK, and download JDK 17 for your system
2. Download the code from this GitHub with your method of choice (The simplest way is like so):

3. Click download code at the top right and download as ZIP.

4. Save into a location of your choice.
5. If you use IntelliJ IDEA (the Community Edition will suffice for this), open , and 