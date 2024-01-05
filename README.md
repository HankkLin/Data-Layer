# UVA Bus Routes Database Interface

## Introduction
This project builds and queries a database of UVA Bus Routes using data harvested from the UVA Devhub API. The application translates JSON bus information into a list of objects, stores this list in a SQLite database for persistence, and allows for sophisticated querying of the content. This is not a homework assignment but a comprehensive tool designed for real-world application.

## Getting Started
1. **Clone the Repository:**
```Bash
git clone https://classroom.github.com/a/bfuRBPGn
```
## Features
- **StopReader and BusLineReader:** Return lists of Stops and BusLines (including their Routes) from the API.
- **DatabaseDriver:** Interface for writing to and reading from the database, creating tables, inserting data, and querying information.
- **BusLineService:** Provides methods to find the closest bus stop and recommend the best bus line for given source and destination stops.

## How to Use
1. **Setting Up the Database:**
- Run `DatabaseDriver` to initialize your database and tables.
- Use `addStops` and `addBusLines` methods to populate the database.

2. **Querying the Database:**
- Utilize the `getBusLines`, `getStops`, and other querying methods provided by `DatabaseDriver` to retrieve information.

3. **Finding Closest Stops and Recommended BusLines:**
- Use methods in `BusLineService` to find the closest stop to a given location or to get a recommended bus line for traveling between two stops.

## Contributing
This is a project for course CS3140 in University of Virginia. 
Generated from [here](https://github.com/cs-3140-fa23/hw5-starter-repo)
All contributions are welcome!

## License
[MIT License](LICENSE.md) - feel free to use and modify this project as you see fit.

