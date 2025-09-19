# Skipping methods

Not every piece of code is worth mutating. PIT already supports exclusion filters to skip specific classes or methods see [the PIT documentation](https://pitest.org/quickstart/maven/#excludedmethods) for more details. However, these filters are often not enough. as they are only based in the method names. Some methods—such as trivial getters, empty methods, or compiler-generated code—don’t need dedicated tests but are difficult to exclude using only naming patterns. Descartes adds features to detect and skip those *stop methods* based on code inspection, along with finer-grained controls to ignore methods via annotations.

## Stop Methods

Descartes avoids some methods that are generally not interesting and may introduce false positives such as simple getters, simple setters, empty void methods or methods returning constant values, delegation patterns as well as deprecated and compiler generated methods. Those methods are automatically detected by inspecting their code.
A complete list of examples can be found [here](https://github.com/STAMP-project/pitest-descartes/blob/master/src/test/java/eu/stamp_project/test/input/StopMethods.java).
The exclusion of stop methods can be configured. For more details see: ["Running Descartes on your project"](./running-on-your-project.html).

## Do not use `null` in methods annotated with `@NotNull`
*Added in version 1.2.6*

Descartes will avoid using the `null` operator in methods annotated with `@NotNull`. This increases its compatibility with Kotlin sources. This feature can be configured. See ["Running Descartes on your project"](./running-on-your-project.html) for more details.

## Skip methods using `@DoNotMutate`
*Added in version 1.3*

Descartes skips all method that are annotated with *any* annotation whose name is `DoNotMutate`.
For example, in the following fragment of code, the method `m` will not be mutated.

```java
class A {
    @DoNotMutate
    public void m() {/* ... */}
}
```

All methods in a class annotated with `@DoNotMutate` will be avoided as well. For example, in the following fragment of code, the method `m` will not be mutated:

```java
@DoNotMutate
class A {
    public void m() {/* ... */}
}
```

The `DoNotMutate` annotation may specify which operators should be considered. For example:

```java
class A {
    @DoNotMutate(operators = "false")
    public boolean m() { return true; }
}
```

will instruct Descartes not to use the `false` mutation operator to mutate `m`.

When specifying operators, a method annotation takes precedence over class annotations. That, is the `@DoNotMutate` of a method overrides the same annotation in the class For example:

```java
@DoNotMutate(operators = "true")
class A {

    public boolean n() { return false; }

    @DoNotMutate(operators = "false")
    public boolean m() { return true; }
}
```

will not mutate method `n` with `true`, as instructed in the class annotation. On the other hand, `m` will be mutated by `true` but not by `false`.

Descartes includes a definition of `DoNotMutate`. However, when the tool inspects the annotations of a class or method it matches only the simple name of the annotation class and ignores the package. So, any `DoNotMutate` annotation will be considered. In this way a project does not need to add Descartes as a dependency, it can declare its own `DoNotMutate` and use it.

This feature is also configurable. See ["Running Descartes on your project"](./running-on-your-project.html) for more details.
