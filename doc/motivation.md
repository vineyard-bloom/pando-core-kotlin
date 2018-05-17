# Motivation for Vinecoin (working title)

## Terms

The following terms will be used throughout this document:

* BitEth: A reference to both bitcoin, bitcoin clones, and ethereum.  Used to refer to attributes they share.

## Problems with BitEth

### 1. Mining and Proof of Work

BitEth is a Monarchy.  Each Monarch is short-lived and elected through lottery.  An expensive lottery.

BitEth's use of Proof of Work is sometimes presented as a revolutionary solution to providing trustless consensus.  Yet by itself Proof of Work is inadequate and BitEth falls back on traditional consensus methods for the final say.  The traditional methods BitEth employs are:

#### a. Passage of time

The more blocks that have been mined above a block, the more likely that block is permanent and true.

#### b. Conflict Resolution

The main point of using Proof of Work within BitEth is to prevent conflicts, but conflicts still happen, at which case formal procedures are applied to resolve that conflict.

#### c. Democracy

A key part of that conflict resolution is democracy.  It is standard for BitEth miners to defer to the majority when conflicts arise.  It is even possible for the majority to overrule a lottery-appointed Monarch.

### 2. Faux Trustless

There is no such thing as a trustless system.  Everything needs to trust something.  When the word "trustless" is applied to BitEth it is used relatively, meaning that there are certain things BitEth does not need to trust.  But that is not a reduction in trust, it is simply trading explicit trust in one thing for implicit trust in another.

TODO list examples of BitEth trust

### 3. Centralized

BitEth currencies are marketed as decentralized, but that is once again relative.  The custodianship of their ledgers is decentralized, while the ledgers themselves are more centralized than any government currency.

The internet scales because each website does not need to know about every other website.  Inversely, search engines like Google provide their services by cataloging as much of the internet as possible.  Within the paradigm of the web, localized websites are the norm and comprehensive services like Google are the exception.

BitEth turns this arrangement on its head.  In order to accurately query the transaction history of a single address, an entire blockchain is needed.  In order to broadcast a transaction, an entire blockchain is needed.  Out of the box, BitEth requires everyone to be Google.

Some services such as block explorers are attempting to act the part of Google by taking on the weight of a blokchain for their end-users, but that is an even harder scaling problem than creating a search engine.  A search engine is an auxilary tool that assists in web browsing.  It provides higher level features on top of a lower level infrastructure.  A block explorer has to provide higher level features *and* the lower level infrastructure.      

### 4. Impractical Logic and Accounting

#### Transient Addresses

Bitcoin was designed to use transient addresses, where each address would only be used once.  Such a design might appeal to mathematicians and criminals, but is an accounting atrocity.

#### Missing Account Ledger Support
Because of this design, Bitcoin has no built-in support for viewing a single address ledger.  Bitcoind has some deprecated wallet support but that mostly works with addresses grouped by account and still only has limited support for dealing with individual addresses.

Ethereum likewise has no built-in means of getting an address ledger.  The simplest approach is to query every block in the blockchain and gather all of the transactions related to that address.

Operations requiring an address ledger are one of the most common tasks employed by blockchain business applications.  Some form of address ledger is required to send transactions.

#### Internal Transactions

Ethereum also has a notion of messages or "internal transactions" that are not logged and not queryable by any normal Ethereum infrastructure.  These transactions are invisible to most ethereum business applications.  There are some solutions that can detect internal transactions but they are not common and require hacking standard ethereum tools.

#### Impractical Data Storage
 
BitEth data is tabular and cross-referencing by nature, like a SQL database.  That is how the data structures are defined in all of the major BitEth node code bases, and how the data is transmitted across consuming APIs.

BitEth nodes largely store their data as flat key/value pairs using LevelDB.

There are benchmarks that claim SQL databases are less efficient than LevelDB.  However, the test cases of those benchmarks use both SQL and LevelDB to store key/value pairs, and are not using indexes.  That is like comparing a machine gun and a baseball bat in a fight where the combatants can only use their weapons as clubs. 

### 5. Infrastructure Logic Embedded into the Protocol

#### a. Transaction Fees

There have been periods in Bitcoin's history when transaction fees have been prohibitively expensive for certain business applications.  Solutions have been presented to mitigate this problem, but they generally require modifications to the underlying protocol, and require a certain amount of consensus because everyone who uses Bitcoin has to use the same fee structure.  This is similar to if FedEx and UPS had 
 
## Problems with Other Methods

### Proof of Stake

Proof of Stake is superior to Proof of Work in that it removes the need for excessive computation.  However, it is still ultimately another form of Monarch-for-a-day that has the same trust and scaling issues.

## The Solution

Vinecoin is to BitEth what Git is to Subversion.

### 1. A Protocol with Abstracted Trust

### 2. A Blockchain Per Address

### 3. Guarantee that any state change log is readily available

### 4. A SQL Database
