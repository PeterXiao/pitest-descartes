# How does Descartes work?

Descartes finds the worst tested methods in a project by using *mutation testing*. This is a well known technique to assess the quality of a test suite by introducing artificial faults and then checking if the tests are able to detect them. In particular the tool uses *extreme mutation testing* that works at the method level.

## Mutation testing
Mutation testing verifies if your test suite can detect bugs.
The technique works by introducing small changes or faults into the original program. These modified versions are called **mutants**.
A good test suite should be able to detect or *kill* a mutant. That is, at least one test case should fail when the test suite is executed with the mutant instead of the original program. 
[Read more](https://en.wikipedia.org/wiki/Mutation_testing).
Traditional mutation testing works at the instruction level, e.g., replacing ">" by "<=", so the number of generated mutants is huge, and it takes a lot of time to check the entire test suite.
That's why the authors of [Will my tests tell me if I break this code?](http://dl.acm.org/citation.cfm?doid=2896941.2896944) proposed an *Extreme Mutation* strategy, which works at the method level.

## Extreme Mutation Testing
In Extreme Mutation testing, instead of changing one instruction at a time, the whole logic of a method under test is eliminated.
All statements in a `void` method are removed. If the method is not `void`, the body is replaced by a single return statement producing a constant value. 
This approach generates fewer mutants. Code from the authors can be found in [their GitHub repository](https://github.com/cqse/test-analyzer).

The goal of Descartes is to bring an effective implementation of this kind of mutation operator into the world of [PIT](https://pitest.org) and check its performance and effectiveness in real world projects.

## Method classification
Extreme mutation classify methods according to the outcome of the extreme mutants. If no extreme mutant created for a method are detected by the test suite it is then considered as **pseudo-tested**. These are the worst tested methods in the project. If there are mixed results, that is, for the same method some extreme mutants are detected and others are not, then the method is classified as **partially-tested**. These are not well tested either. Otherwise, if all mutants are detected the method is considered as **tested**.

Descartes finds and reports all **pseudo-tested** and **partially-tested** methods in a given project.

Descartes uses a set of transformation models or *extreme mutation operators* to analyse each method. The full list and details of these mutation operators can be checked [here](./mutation-operators.html).
