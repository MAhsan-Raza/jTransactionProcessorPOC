# jTransactionProcessorPOC
Simple integration layer &amp; transaction processor between a TCP/IP based system communicating on delimited protocol &amp; a RESTful web service using JSON as its communication protocol.

It uses the Asynchronous Message Broker as a base pattern, 
It is divided into three main module types 
  - Adaptors (TCP/IP & RESTful)
  - Parsers (Delimiter based message parsers & JSON)
  - Channel Processor
Each module (Parsers, adaptors, processors) have their own configurable thread-pools, so we can configure each of them based on load requirements.
