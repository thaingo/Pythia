# Simple simulation of NoSQL database written in java.

## Features
* data model similar to Apache Cassandra
(http://wiki.apache.org/cassandra/DataModel). Keyspace contains column families,
column families are sets of key:row, where row is set of key:value pair called
column
* simple language PQL (pythia query language). Provides basic (really basic)
commands to manage keyspaces, column families etc.
* server and basic client (it's only simulation)

## Most interesting things to watch
* PQL lexer/parser/compiler source code

## PQL

### Commands
* `KEYSPACE <name>` - creates keyspace (value must be unique)
* `KILL <name>` - drop of keyspace
* `USE <name>` - chooses keyspace to use
* `CREATE COLUMNFAMILY <name>`  - creates new column family in actual keyspace
(value must be unique in keyspace)
* `DROP COLUMNFAMILY <name>` - drops column family
* `INSERT INTO <columnfamily> (KEY=<val>,<column>=<value>...)` - insert row to
column family, where KEY is unique index of row
* `DELETE FROM <columnfamily> WHERE KEY=<val>` - delete row indexed by KEY from
column family
* `UPDATE <columnfamily> SET KEY=<val>,<column>=<value>... WHERE KEY=<value>` -
update row columns in column family
* `SELECT FROM <columnfamily> WHERE KEY=<value>` - select row data from column
family, returns data in JSON format

### LL(1) grammar of PQL
    <create_keyspace_stmt> ::= <KEYSPACE> <VAR>
    <drop_keyspace_stmt> ::= <KILL> <VAR>
    <use_keyspace_stmt> ::= <USE> <VAR>
    <create_columnfamily_stmt> ::= <CREATE> <COLUMNFAMILY> <VAR>
    <drop_columnfamily_stmt> ::= <DROP> <COLUMNFAMILY> <VAR>
    <key_values_list'> ::= ,<var><key_values_list'>|Epsilon
    <key_values_list> ::= <KEY>=<VAR><key_values_list'>
    <insert_stmt> ::= <INSERT><INTO><VAR>(<key_values_list>)
    <where_stmt> ::= <WHERE><KEY>=<VAR>
    <update_stmt> ::= <UPDATE><VAR><SET><key_values_list>
    <delete_stmt> ::= <DELETE><FROM><VAR><where_stmt>
    <select_stmt> ::= <SELECT><FROM><VAR><where_stmt>
    <start_stmt> ::= <create_keyspace_stmt> | <drop_keyspace_stmt> |
    <use_keyspace_stmt> | <create_columnfamily_stmt> |
    <drop_columnfamily_stmt> | <insert_stmt> | <update_stmt> |
    <delete_stmt> | <select_stmt>
    <VAR> ::= [a-zA-Z]+ | "[.]*"
    <KEYSPACE> ::= KEYSPACE
    <KILL> ::= KILL
    <USE> ::= USE
    <CREATE> ::= CREATE
    <COLUMNFAMILY> ::= COLUMNFAMILY
    <DROP> ::= DROP
    <KEY> ::= KEY
    <INSERT> ::= INSERT
    <INTO> ::= INTO
    <WHERE> ::= WHERE
    <UPDATE> ::= UPDATE
    <SET> ::= SET
    <DELETE> ::= DELETE
    <FROM> ::= FROM
    <SELECT> ::= SELECT