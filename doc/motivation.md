# Motivation for Pando

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

On top of that, BitEth was designed with the assumption that most use cases would only need light nodes with a minimal amount of data.  Much of the current BitEth infrascture is only successfully operating because it is using light nodes, which have even less queryable data at their finger tips.

#### Internal Transactions

Ethereum also has a notion of messages or "internal transactions" that are not logged and not queryable by any normal Ethereum infrastructure.  These transactions are invisible to most ethereum business applications.  There are some solutions that can detect internal transactions but they are not common and require hacking standard ethereum tools.

#### Impractical Data Storage
 
BitEth data is tabular and cross-referencing by nature, like a SQL database.  That is how the data structures are defined in all of the major BitEth node code bases, and how the data is transmitted across consuming APIs.

BitEth nodes largely store their data as flat key/value pairs using LevelDB.

There are benchmarks that claim SQL databases are less efficient than LevelDB.  However, the test cases of those benchmarks use both SQL and LevelDB to store key/value pairs, and are not using indexes.  That is like comparing a machine gun and a baseball bat in a fight where the combatants can only use their weapons as clubs. 

### 5. Infrastructure Logic Embedded into the Protocol

There have been periods in Bitcoin's history when transaction fees have been prohibitively expensive for certain business applications.  Solutions have been presented to mitigate this problem, but they generally require modifications to the underlying protocol, and require a certain amount of consensus because everyone who uses Bitcoin has to use the same fee structure.  This is like having to pay postage even when you hand someone a letter.

TODO Integrated Gas fees also a blocker for smart contract scaling

## Problems with Other Methods

### Proof of Stake

Proof of Stake is superior to Proof of Work in that it removes the need for excessive computation.  However, it is still ultimately another form of Monarch-for-a-day that has the same trust and scaling issues.

## The Solution

Pando is to BitEth what Git is to Subversion.

### Primary Features

### a. A Blockchain Per Address

In this model, each address has a unique blockchain.  These blockchains would be known as Address Block Chains (ABC).  Each block contains an array of transactions involving that address.  Any time value is transfered from one address to another, an identical transaction record is added to the blockchain of each address.

There could also be a second type of blockchain called a Composite Block Chain (CBC). Instead of containing an array of transactions, a CBC block contains an array of blocks from other blockchains.  A CBC can be used to add federated confidence to a collection of ABCs.

### b. A Protocol with Abstracted, Relative Trust

In this model, nodes can have varying degrees of trust for each node, blockchain, and block.  This trust is quantified by confidence ratings. The Pando protocol dictates how a node interacts with other nodes based on its confidence in those nodes and their data.  The Pando protocol does not dictate how confidence ratings are derived, though it's core tools can provide optional out-of-the-box methods for deriving confidence ratings.

Each node in a Pando network can provide data.  In such a role that node is a source.  A node can also use data it receives from other nodes.  In such a role that node is a consumer.  

When a node outputs data, it provides that data with attributed confidence ratings.  A consumer also attributes a confidence rating to each source, which colors the confidence ratings of the data received from that source.

Each block in a chain can have varying confidence ratings, but higher blocks cannot have higher confidence ratings than lower blocks.  This leads to a gradual confidence entropy, with the offset that confidence ratings are not inherent attributes of blockchain data but are mutable values ascribed to blockchain data.  Thus, confidence ratings can be re-evaluated and consider new data.  While a blockchain has an entropic confidence curve, the overall height of that curve can be raised over time through additional validation.

A confidence rating can be a complex value ranging between degrees of three modes: trusted, distrusted and unknown.  Trusted and distrusted sources are diametrically opposed, while an unknown source may or may not be trustworthy.

When multiple sources with varying confidence ratings are aggregated together, the resulting aggregate confidence rating is determined by the "Confidence Purity" of the sources.  When distrusted sources are mixed with trusted sources, the resulting confidence rating is distrust.

When a trusted source refers to data originating from a source that is unknown to the consumer, the unknown data inherits the trusted confidence rating of the trusted source.  In Pando this is referred to as vouching.

With this design, it is possible to create a private blockchain and later graft it into a public blockchain.  Part of the grafting process involves scanning the private blockchain to verify that it is compatible with the public blockchain.

One common compatibility question between Pando blockchains is whether all funds passing through each chain can be traced back to trusted coinbases, and that the passage of funds do not involve forks.  This is to prevent indiscriminate minting.

Thus, if an organization wanted to spin up a private blockchain, they could choose to mint their own Pando coins and alienate themselves from other blockchains.  However, if the private blockchain was only funded through external sources, that blockchain could later be grafted into the blockchain those funds originated from.

That highlights a notable point about Pando's cross-blockchain compatability.  When sending Pando coins from one address to another, the source blockchain does not need to trust the destination blockchain.  This is because at worse case, the coin is being burned, and it is permissible for a coin owner to burn his own coins.  Later on, if the private blockchain is grafted into the public blockchain, the funds that at one time appeared burned would become "resurrected".
   
Because Pando supports multiple confidence rating methods within the same ecosystem, it is possible for methods like Proof of Work and Proof of Stake to both be used within Pando blockchains.

One benefit of abstracted trust is it makes it possible to integrate external data sources into a Pando network.  For example, a trusted Pando source can output data derived from bitcoin and ethereum blockchains.  That data can then be consumed by Pando smart contracts.  Note that this integration is only one-way.  Pando can consume BitEth data, but BitEth has no means of consuming Pando data.

### c. Guarantee that any state change log is readily available

ox Bitcoin readily provides information on state changes, but out-of-the-box provides very little ability to query those state changes in a relational way.  (In other words, it can be hard to get from one state change to another related state change.)

Ethereum provides similar challenges and on top of that contains state changes that are impossible to query out-of-the-box.

Pando ensures that all state changes are readily queryable.  Part of this is an incidental side-effect of having the blockchains grouped per address.

### Secondary Features

### a. A Protocol with Abstracted Fees

### c. A SQL Database
