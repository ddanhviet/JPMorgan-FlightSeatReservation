
### How to compile program
mvn clean compile assembly:single
java -jar target/JPMorgan-FlightSeatReservation-1.0-SNAPSHOT-jar-with-dependencies.jar

### Possible Improvement
- Add retry to persistence service
- Merge handle input and main application handling flow. They share the same logic of printing out FAIL if run into any exception.
- Provide absolute path instead of relative path for persistence filename