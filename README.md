# Signifier-Java

This library allows the creation and the processing of signifiers in Java.

## Reading a Signifier

```java
Signifier signifier = SignifierReader.readSignifier(description, RDFFormat.TURTLE);
```

## Getting a StructuredSignifier from a Signifier

```java
StructuredSignifier structuredSignifier = StructuredSignifier.getAsStructuredSignifier(signifier);
```

## Retrieving information from a StructuredSignifier

Retrieving the expiration date:
```java
Optional<Instant> expirationDate = structuredSignifier.getExpirationDate();
```

Retrieving the salience:
```java
Optional<Integer> salience = structuredSignifier.getSalience();
```

Retrieving the affordances:
```java
Set<Affordance> affordances = structuredSignifier.getAffordances();
```

Retrieving the affordances in a list:
```java
List<Affordance> affordanceList = structuredSignifier.getListAffordances();

## Retrieving information from an Affordance

Retrieving the precondition
```java
Optional<State> optionalPrecondition = affordance.getPrecondition();
if (optionalPrecondition.isPresent(){
State precondition = optionalPrecondition.get();
}
```

Retrieving the objective
```java
Optional<State> optionalObjective = affordance.getObjective();
if (optionalObjective.isPresent(){
State objective = optionalObjective.get();
}
```

Retrieving the value of a property predicate that can be an IRI or a String
```java
Optional<Value> optionalValue = affordance.getValue(predicate);
if (optionalValue.isPresent()){
Value value = optionalValue.getValue();
```

Retrieving the literal of a property predicate that can be an IRI or a String
```java
Optional<Literal> optionalLiteral = affordance.getLiteral(predicate);
if (optionalLiteral.isPresent()){
Literal literal = optionalLiteral.getValue();
```

Retrieving the sequence from a SequenceAffordance
```java
List<Affordance> affordanceList = affordance.getSequence();
```

