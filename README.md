# Challenge - Fault Detection

## Background

Cohesion connects with commercial building's automation systems that manages Heating,Air Conditioning and
Ventilation (HVAC) and lighting controllers. These systems employs various devices and sensors that generates
important information to understand building operations and system efficiency. Cohesion is interested in pulling
data from these devices and analyzing with other data sources.


## Current Requirements

There are two parts to the application

1. Device data publisher
+ Create 5 device actors that generates random number (readings) every 3 seconds
+ Publish data from each actor to kafka topic
   
2. Data subscriber
+ Subscribe to kafka topic to ingest streaming data from publisher app
+ Save raw data to the database
+ Create aggregate actor that tracks last values from each device

## Overview


+ Modelling Device Data on Scala, Generate random device data on Akka and publish to Kafka,
+ Scheduled to obtain device data every 3s and publish to Kafka,
+ Use Akka streaming to consume all device data messages from Kafka,
+ To facilitate the execution of the solution, we use docker-compose container that includes: Zookeeper, Kafka and PostgreSQL DB, defined on *docker-compose.yml*
+ Application settings configured on *resources/application.conf*, for example: kafka configuration, batch duration, interval time to generate data, num of devices to sense.

## Execute the solution

Requirements: Scala 2.12, Docker, Docker-compose

+ Go to *cohesion* directory
+ Start up docker-composer (Kafka/Zookeeper/Postgres) *sudo docker-compose run*
+ Run application *sbt run*

After that you should see activity like:

```
2021-06-21 12:42:47.525 [FaultDetection-akka.actor.default-dispatcher-7] INFO  c.c.c.subscriber.flow.Tracker(17) - Last value receiving from deviceId=[e1750bd8-5af3-4684-be22-334033e289c0] value=[0.8758813]
2021-06-21 12:42:47.528 [FaultDetection-akka.actor.default-dispatcher-7] INFO  c.c.c.subscriber.flow.Tracker(17) - Last value receiving from deviceId=[076a0fb0-6529-43a3-aed3-9f3b3ce51776] value=[0.2366463]
2021-06-21 12:42:47.530 [FaultDetection-akka.actor.default-dispatcher-7] INFO  c.c.c.subscriber.flow.Tracker(17) - Last value receiving from deviceId=[b3181a8b-f90e-4205-8b45-9cc8906233a3] value=[0.41048872]
2021-06-21 12:42:47.532 [FaultDetection-akka.actor.default-dispatcher-7] INFO  c.c.c.subscriber.flow.Tracker(17) - Last value receiving from deviceId=[f6d23b9e-bf2d-43cf-96d6-c118d34e8934] value=[0.8398168]
```

### Next steps

+ Split the application in two components:
1. Generate and publish device data and
2. Consumer to consume and analyzed the data.
