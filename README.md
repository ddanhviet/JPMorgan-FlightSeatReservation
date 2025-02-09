


## How to compile program

### Self-contained Unix executable

Requirements
- Graalvm JDK21
```

```
- Maven

`mvn clean -Pnative package`

After running the command, a file named JPMorgan-FlightSeatReservation will be created in target subdirectory 

### Executable JAR

Requirements
- JDK21
- Maven

```
mvn clean compile assembly:single  
java -jar target/JPMorgan-FlightSeatReservation-1.0-SNAPSHOT-jar-with-dependencies.jar
```


## Possible Improvement
- Add retry to persistence service
- Provide absolute path instead of relative path for persistence filename
- Have a constant for reserved state