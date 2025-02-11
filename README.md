


## How to compile program

### Self-contained Unix executable

Requirements
- Graalvm JDK21
```
brew install --cask graalvm/tap/graalvm-jdk21
export JAVA_HOME=/Library/Java/JavaVirtualMachines/graalvm-21.jdk/Contents/Home
```
- Maven
```
brew install mvn
```

To spawn the new self-contained executable  
`mvn clean -Pnative package`

After running the command, a file named JPMorgan-FlightSeatReservation will be created in target subdirectory 

### Executable JAR

#### Requirements
- JDK21
```
brew install --cask openjdk@21
export JAVA_HOME=`/usr/libexec/java_home -v 21`
```
- Maven
```
brew install mvn
```
#### To spawn the executable JAR
```
mvn clean compile assembly:single
```
#### To run the program
```
java -jar target/JPMorgan-FlightSeatReservation-1.0-SNAPSHOT-jar-with-dependencies.jar
```


## Possible Improvement
- Add retry to persistence service
- Provide absolute path instead of relative path for persistence filename
- Have a constant for reserved state
- Get filename as a configuration for SeatBookService