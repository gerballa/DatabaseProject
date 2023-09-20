# Java Real Time Database

[![API](https://img.shields.io/badge/Java-18-green.svg?style=flat)](https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html)

The objective of this project is to create a simulation of how real database will
work. This app is able to read commands, interpret them and then show the output of
those commands. These commands will operate on a "database" which is stored in
runtime. This means that the data that will be provided from commands in the input, will be
stored in objects in the code and not in external databases.

## How to use
The app is able to read commands from a file. The file should be named `db_instructions.txt`
and should be placed inside the `/resources` folder. After completing, the app will save its output
on the file `db_output.txt` found under the root directory.

You can refer to the next part of this readme for the commands that are supported by the app.

## Commands
### 1- Create Resource
Resources are akin to tables in sql. A resource can have a "Primary Key column (PK)" which is the
column that uniquely identifies one type of that resource. In our case, that PK would be
an ID that is unique among all rows inside that resource. The command needs to have one column
specified as primary key.

Example:
```
INPUT  : CREATE RESOURCE person(id_PK,name,surname,age);
OUTPUT : Created resource:person in the database
```
Missing primary key:
```
INPUT  : CREATE RESOURCE person(id,name,surname,age);
OUTPUT : Invalid operation
```
### 2- Add to Resource
After creating a resource, We have to be able to add data to these resources.
Every resource should be able to keep track of the current value its PK holds. When
adding a new row to the resource, if a value for the PK column is not specified, then the
resource uses the current value it holds for the PK, increments it by one, and assigns it
to the new row that is being added.

Example:
```
INPUT : CREATE RESOURCE person (id_PK,name,surname);
        ADD TO person (id,2) (name,John) (surname,Smith);
        ADD TO person (name, Jim) (surname,Carrey);

OUTPUT: Created resource:person in the database
        Added row to resource:person
        Added row to resource:person
```
Resource does not exist:
```
INPUT  : ADD TO person (id,2) (name,John) (surname,Smith);
OUTPUT : Invalid operation
```
### 3- Remove From Resource
This operation removes data from any resource based on a condition provided.

Syntax: `REMOVE FROM {resourceName} at {colName}={colValue}`

Example:
```
INPUT : CREATE RESOURCE person (id_PK,name,surname);
        ADD TO person (id,2) (name,John) (surname,Smith);
        REMOVE FROM person at id=2;

OUTPUT: Created resource:person in the database
        Added row to resource:person
        Removed row from resource:person
```
Invalid (field doesn't exist):
```
INPUT : CREATE RESOURCE person (id_PK,name,surname);
        REMOVE FROM person at age=24;

OUTPUT: Created resource:person in the database
        Invalid operation
```

### 4- Search
This operation searches for data in any resource based on a condition provided.

Syntax: `SEARCH {name} with {colName}={colValue}`

Example:
```
INPUT : CREATE RESOURCE person (id_PK,name,surname);
        ADD TO person (id,2) (name,John) (surname,Smith);
        ADD TO person (name,John) (surname,Carrey);
        SEARCH person with name=John
        SEARCH person with name=ZII

OUTPUT: Created resource:person in the database
        Added row to resource:person
        Added row to resource:person
        id:2 name:John surname:Smith|id:3 name:John surname:Carrey|
        No results
```

### 5- State of database
This operation prints the state of the database. It prints all the resources and their data.

Syntax: `DB_STATE`

Example:

```
Input:  CREATE RESOURCE person (id_PK,name,surname);
        ADD TO person (id,203) (name,Orges) (surname,Balla);
        ADD TO person (id,204) (name,Aleks) (surname,Ruci);
        CREATE RESOURCE school (id,rank);
        ADD TO school (id,1) (rank,1);
        ADD TO school (id,2);
        DB_STATE

Output: Created resource:person in the database
        Added row to resource:person
        Added row to resource:person
        Created resource:school in the database
        Added row to resource:school
        Added row to resource:school
        Resource:person 2 rows
        id:203 name:Orges surname:Balla|id:204 name:Aleks surname:Ruci|
        Resource:school 2 rows
        id:1 rank:1|id:2 rank:null|
```

#### Author: Gerardo Balla (2023)