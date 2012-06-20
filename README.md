regere-rule-engine
==================

A fast persisted and none persisted rule engine, which can evaluate a set of condition and manipulate the data within.

Depending on the rule condition which is met a set of action can be executed.

 Main Features
 -------------

 1) Values can be passed as Map, All data that is passed is dynamic, rules are not bound by a domain class.
 2) All rules are preloaded to increase performance.
 3) Supports persisted and non persisted event handling (non persisted can handle upto 1 Million transaction in a quad core processor)
 4) Rules are passed as JSON objects.
 5) Rules can be dynamical added,deleted and changed on the go



 TODO
 -----
 1) Distributed rule processing
 2) JMX call to added,deleted and change rules
 3) Some more things that need to be completed. (This is just a two days work much more have to be done)


 This is not licensed for commercial use yet.