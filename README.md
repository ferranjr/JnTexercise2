# Exercise 2: modelling image manipulations

You have to implement an API in your language to expose image manipulation functionality.
This API must implement the following operations:

- Flip an image
- Rotate an image
- Crop an image
- Blend two different images, as long as coloured images cannot be mixed with
black&white ones (and vice versa).

You have to implement a code that provides, in your opinion, the most convenient API to
perform such actions.

# Implementation

I look to get the benefit of the Interpreter Pattern in order to generate a DSL that is 
totally separated of the possible implementations about it.


## Trade Offs

I focused on the DSL for the library, which means I left a lot of work pending for the
interpreter. 

I used exceptions to handle errors on the interpreter as I used one for the
```
type Id[A] = A
```
which could be easily improved by using one for 
```
Either[FreeImageException, A]
```
or one of the many other options to handle errors ina  more type safe manner, or at least
more functional.

As spend some time playing and designing the DSL, I didn't applied proper TDD, and added some tests later
when I had a clearer idea of the DSL.

## How to Run the tests

```bash
sbt test
```

## How to Build and Run application

This run and does a small manipulation on the src/main/resources folder

```bash
sbt run
```