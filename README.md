# Congestion Tax Calculator
Springboot application to calculate congestion tax for vehicles in Gothenburg city.

# Configuration
* The application configurations for date wise tax related information and tax-exempt vehicles are configured
in `application.yml` (under `src/main/resources`)
* We can support multiple cities by two ways:

  * Quick solution: Provide application-{city-name}.yml and run the springboot application providing that config.
  * As a next step for externaliztion, save city and its datetime-based tax related price to database and setup scripts
  to create and populate tables
* Allowed vehicle types are saved as an ENUM
* Current scope is limited to Year 2013


# Prerequisites
* Java 17
* Maven


# Build and run application
git clone https://github.com/anupreeta/congestion-tax-calculator.git

**mvn clean install**
**mvn clean package**
**mvn spring-boot:run**

## Run application with default application.yml (for gothenburg city in this case)
**java -jar target/volvo-0.0.1-SNAPSHOT.jar** 

## Run application for different city like skane for example
**java -jar target/volvo-0.0.1-SNAPSHOT.jar --spring.config.location=src/main/resources/application-skane.yml**

## Testing REST ENDPOINT
To calculate tax for one or more date entries for a vehicle type, send a **POST** request through CURL or POSTMAN
with JSON request body as below:

 ``http://localhost:8080/tax/calculator``

### Request Body:
  
 `````
 
 {
        "vehicleType": "Car",
        "dates": [
                    "2013-01-14 06:00:00","2013-01-14 07:30:00",
                    "2013-01-14 15:33:27","2013-01-14 16:40:00",
                    "2013-02-08 06:27:00","2013-02-08 06:20:27",
                    "2013-02-08 14:35:00","2013-02-08 15:29:00",
                    "2013-02-08 15:47:00","2013-02-08 16:01:00",
                    "2013-02-08 16:48:00","2013-02-08 17:49:00",
                    "2013-02-08 18:29:00","2013-02-08 18:35:00",
                    "2013-03-26 14:25:00","2013-03-28 14:07:27"
        ]
 }
 
`````

### Request Response:

       {
    		"tax": 60,
    		"message": "Tax calculated successfully for vehicle :Car Amount : 60",
    		"error": null,
    		"timestamp": 1670173709482
       }


