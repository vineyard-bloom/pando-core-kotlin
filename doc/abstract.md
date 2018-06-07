# Motivation for Pando

## Terms

The following terms will be used throughout this document:

* BitEth: A reference to both bitcoin, bitcoin clones, and ethereum.  Used to refer to attributes they share.

## Problems with BitEth

### 1. Centralized

BitEth currencies are marketed as decentralized, but that is relative.  The custodianship of BitEth ledgers are decentralized, while the ledgers themselves are more centralized than any government currency.

The internet scales because each website does not need to know about every other website.  Inversely, search engines like Google provide their services by cataloging as much of the internet as possible.  Within the paradigm of the web, localized websites are the norm and comprehensive services like Google are the exception.

BitEth turns this arrangement on its head.  In order to accurately query the transaction history of a single address, an entire blockchain is needed.  In order to broadcast a transaction, an entire blockchain is needed.  Out of the box, BitEth requires everyone to be Google.

Some services such as block explorers are attempting to act the part of Google by taking on the weight of a blockchain for their end-users, but that is an even harder scaling problem than creating a search engine.  A search engine is an auxilary tool that assists in web browsing.  It provides higher level features on top of a lower level infrastructure.  A block explorer has to provide higher level features *and* the lower level infrastructure.  It is as if each website needed to redundantly host both itself and every other website on the web.

### 2. Trust

Bitcoin leveraged Proof-of-Work to create a functional peer-maintained currency.  That is impressive, but not as useful as it can appear.

BitEth is sometimes referred to as trustless.  That is a misnomer.  Everyone needs to trust something for something.  BitEth is sometimes referred to as a system of distributed trust, where trust is still required but spread out across many mini-authorities.  That is more accurate but still misleading.  In order to use BitEth you need to trust a centralized, singular authority.  That authority is BitEth consensus.  BitEth must follow whatever the majority of miners agree on.

The only way BitEth currently retains any measure of universal trust is due to a delicate balance of power, where the major stakeholders of BitEth are competing for mining power.  If any single organization gains at least 51% of mining power of a blockchain, that organization will become the singular authority that blockchain follows as long as the 51% hold is maintained.

The problem with BitEth is not that such an occurance is possible, but that such an occurance is surprising.  It is a natural rule of this world that power consolidates.  Power consolidation is inevitable.  The best that can be done is to design systems that work with along grain and minimize the problems of consolidated power.

Historically it is normal for each government to control its own currency.  Governements and currencies work closely together.  Bitcoin was created to be a currency independent of government, but in the end has become itself a government, replete with its own politicians, taxes, and civil wars.

Some of the initial blockchain movement is an attempt to replace human governance with machine governance, as though the creations of humanity are more trustworthy than humanity.  Machines are only as trustworthy as the humans that create and use them.

### 3. Impractical Logic and Accounting

#### Transient Addresses

Bitcoin was designed to use transient addresses, where each address would only be used once.  Such a design might appeal to mathematicians and criminals, but is an accounting atrocity.

#### Missing Account Ledger Support

Because of this design, Bitcoin has no built-in support for viewing a single address ledger.  Bitcoind has some deprecated wallet support but that mostly works with addresses grouped by account and still only has limited support for dealing with individual addresses.

Ethereum likewise has no built-in means of getting an address ledger.  The simplest approach is to query every block in the blockchain and gather all of the transactions related to that address.

Operations requiring an address ledger are one of the most common tasks employed by blockchain business applications.  Some form of address ledger is required to send transactions.

On top of that, BitEth was designed with the assumption that most use cases would only need light nodes with a minimal amount of data.  Much of the current BitEth infrascture is only successfully operating because it is using light nodes, which have even less queryable data at their finger tips.

#### Internal Transactions

Ethereum has a notion of messages or "internal transactions" that are not logged and not queryable by any normal Ethereum infrastructure.  These transactions are invisible to most ethereum business applications.  There are some solutions that can detect internal transactions but they are not common and require hacking standard ethereum tools.

#### Impractical Data Storage
 
BitEth data is tabular and cross-referencing by nature, like a SQL database.  That is how the data structures are defined in all of the major BitEth node code bases, and how the data is transmitted across consuming APIs.

BitEth nodes largely store their data as flat key/value pairs using LevelDB.

There are benchmarks that claim SQL databases are less efficient than LevelDB.  However, the test cases of those benchmarks use both SQL and LevelDB to store key/value pairs, and are not using indexes.  That is like comparing a machine gun and a baseball bat in a fight where the combatants can only use their weapons as clubs. 

### 4. Infrastructure Logic Embedded into the Protocol

There have been periods in Bitcoin's history when transaction fees have been prohibitively expensive for certain business applications.  Solutions have been presented to mitigate this problem, but they generally require modifications to the underlying protocol, and require a certain amount of consensus because everyone who uses Bitcoin has to use the same fee structure.  This is like having to pay postage even when you hand someone a letter.

TODO Integrated Gas fees also a blocker for smart contract scaling

## Problems with Other Methods

### Proof of Stake

Proof of Stake is superior to Proof of Work in that it minimizes the need for excessive computation.  However, it is still ultimately another form of Monarch-for-a-day that has the same scaling issues.

## The Solution

Pando is to BitEth what Git is to Subversion.

### Primary Features

### a. A Blockchain Per Address

In this model, each address has a unique blockchain.  Each block contains a single transaction involving that address.  Any time value is transfered from one address to another, an identical transaction record is added to the blockchain of each address.

### b. A Protocol with Abstracted, Relative Trust

Nodes in a Pando network are not directly peer-to-peer.  A Pando network is comprised of two kinds of nodes: publishers and consumers.  Publishers provide data and consumers gather it.  A single node can be both a publisher and a consumer.

Pando is not trustless.  Consumers need to trust publishers, but a consumer can choose which publishers it trusts.

Publishers can publicly distribute a distinct address and public key that represent that publisher.  When a block is passed along nodes, that block is accompanied by an array of signatures.  When a publisher transmits a block, it can sign that block and attach the signature to that block.  As a block passes along nodes it can gather more signatures.  This allows a consumer to recieve blocks from a single aggregate publisher and verify that the data originated from other trusted publishers.

To broadcast a new Pando transaction, two new blocks (one for each related blockchain) are published from a small node to a larger node.

With Pando it is possible to create a private blockchain and later graft it into a public blockchain.  This can be done by submitting a private blockchain to a major publisher.  The publisher would scan the private blockchain to verify that it is compatible with the public blockchain.

One common compatibility question between Pando blockchains is whether all funds passing through each chain can be traced back to trusted coinbases, and that the passage of funds do not involve forks.  This is to prevent indiscriminate minting.

If an organization wants to spin up a private blockchain, they could choose to mint their own Pando coins and alienate themselves from other blockchains.  However, if the private blockchain was only funded through external sources, that blockchain could later be grafted into the blockchain those funds originated from.

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
