# Mutation operators

The *mutation operators* are models of the artificial faults used to create mutants. In particular, *extreme mutation operators* replace the body of a method by one simple return instruction or just remove all instructions if possible. The set of mutation operators that Descartes uses can be configured. Further details on how to configure are given in ["Running Descartes on your project"](./running-on-your-project.html). You can find frequent configuration examples [here](./frequent-configurations.html).

Below you can find the full list of extreme mutation operators used by Descartes.

## `void` mutation operator
This operator accepts a `void` method and removes all the instructions on its body. For example, with the following class as input:

```java
class A {

  int field = 3;

  public void Method(int inc) {
    field += 3;
  }

}
```
the mutation operator will generate:

```java
class A {

  int field = 3;

  public void Method(int inc) { }

}
```

## `null` mutation operator
This operator accepts a method with a reference return type and replaces all instructions
with `return null`. For example, using the following class as input:
```java
class A {
    public B create() {
        return new B();
    }
}
```
this operator will generate:

```java
class A {
    public B create() {
        return null;
    }
}
```
## `empty` mutation operator
This is a special operator which targets methods that return arrays. It replaces the entire
body with a `return` statement that produces an empty array of the corresponding type.
For example, the following class:

```java
class A {
  public int[] getRange(int count) {
    int[] result = new int[count];
    for(int i=0; i < count; i++) {
      result[i] = i;
    }
    return result;
  }
}
```
will become:

```java
class A {
  public int[] getRange(int count) {
    return new int[0];
  }
}
```

## Constant mutation operator
This operator accepts any method with a primitive or `String` return type. It replaces the method body
with a single instruction returning a defined constant.
For example, if the integer constant `3` is specified, then for the following class:

```java
class A {
    int field;

    public int getAbsField() {
        if(field >= 0)
            return field;
        return -field;
    }
}
```
this operator will generate:

```java
class A {
    int field;

    public int getAbsField() {
        return 3;
    }
}
```

## `new` mutation operator
*Added in version 1.2.6*

This operator accepts any method whose return type has a constructor with no parameters and belongs to a `java` package.
It replaces the code of the method by a single instruction returning a new instance.

For example:

```java
class A {
    int field;
    
    public ArrayList range(int end) {
        ArrayList l = new ArrayList();
        for(int i = 0; i < size; i++) {
            A a = new A();
            a.field = i;
            l.add(a);
        }
        return l;
    }
}  
```

is transformed to:

```java
class A {
    int field;
    
    public List range(int end) {
        return new ArrayList();
    }
}  
```

This operator handles the following special cases:

| Return Type  | Replacement  |
|--------------|--------------|
| `Collection` | `ArrayList`  |
| `Iterable`   | `ArrayList`  |
| `List`       | `ArrayList`  |
| `Queue`      | `LinkedList` |
| `Set`        | `HashSet`    |
| `Map`        | `HashMap`    |

This means that if a method returns an instance of `Collection` the code of the mutated method will be
`return new ArrayList();`.

This operator is not enabled by default.

## `optional` mutation operator
*Added in version 1.2.6*

This operator accepts any method whose return type is `java.util.Optional`.
It replaces the code of the method by a single instruction returning an *empty* instance.

For example:

```java
class A {
    int field;
    
    public Optional<Integer> getOptional() {
        return Optional.of(field);
    }
}  
```

is transformed to:

```java
class A {
    int field;
    
   public Optional<Integer> getOptional() {
           return Optional.empty();
   }
}  
```

This operator is not enabled by default.

## `argument` mutation operator
*Added in version 1.3*

This operator replaces the body of a method by returning the value of the first parameter that has the same type as the return type of the method.

For example:

```java
class A {
    public int m(int x, int y) {
        return x + 2 * y;
    }
}
```

is transformed to:

```java
class A {
    public int m(int x) {
        return x;
    }
}
```

This operator is not enabled by default.

## `this` mutation operator
*Added in version 1.3*

Replaces the body of a method by `return this;` if applicable. The goal of this operator is to perform better transformations targeting fluent APIs.

For example:

```java
class A {

    int value = 0;
    public A addOne() {
        value += 1;
        return this;
    }
}
```

is transformed to:

```java
class A {

    int value = 0;
    public A addOne() {
        return this;
    }
}
```

This operator is not enabled by default.
