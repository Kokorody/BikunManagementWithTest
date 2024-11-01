
## Testing Approach

To ensure a reliable and bug-free platform, we used a comprehensive testing strategy combining several methods:

1. **JUnit Testing**: We used JUnit with Java NetBeans to test key methods in the Data Access Object (DAO), Beans, and Plain Old Java Object (POJO) classes.
2. **JaCoCo Test Coverage**: We used JaCoCo to check JUnit test coverage and optimize for high coverage.
3. **Black Box Testing**: This method tests the web application's functionality as a whole, independent of code specifics, ensuring that user workflows behave as expected.
4. **JMeter Load Testing**: We used JMeter to perform load testing, ensuring the system can handle multiple simultaneous users and large data loads.

### Tested Classes and Methods

The Total of the test case/@test method for this is project is 134, we also mainly focuses testing the DAO and Bean cause there's where our business logic located. 
Here is an overview of the classes and their tested methods for thorough coverage:

- **DAO Tests**:
  - **AdminDAOTest**: Tests for admin authentication, password, and username validation.
  - **BusDAOTest**: Tests for bus retrieval, addition, deletion, updating, and availability checks.
  - **RuteDAOTest**: Tests for route management, including additions, deletions, and updates.
  - **DriverDAOTest**: Tests for driver retrieval, adding, updating, deletion, and validation.
  - **SPJDAOTest**: Tests for fleet retrieval, SPJ creation, updating, deletion, and bus status updates.

- **Bean Tests**:
  - **LoginBeanTest**: Tests for user authentication, username and password validation.
  - **BusBeanTest**: Tests for bus management in the frontend, validation, and reset methods.
  - **RuteBeanTest**: Tests for adding, updating, deleting, and managing route data.
  - **DriverBeanTest**: Tests for driver data validation, addition, updating, and reset functions.
  - **SPJBeanTest**: Tests for SPJ operations, including addition, deletion, and error handling.

- **POJO Tests**:
  - **AdminTest**: Tests constructors, setters, and getters for admin attributes.
  - **BusTest**: Tests for bus attributes, constructors, and `toString` methods.
  - **RuteTest**: Tests for route attributes, including constructors and `toString`.
  - **DriverTest**: Tests driver attributes, constructors, `toString`, and equality checks.
  - **FleetTest**: Tests constructors and methods for fleet relationships with buses, drivers, and routes.

## Test Summary
![Screenshot 2024-11-01 203827](https://github.com/user-attachments/assets/86a20176-f840-42ca-8018-ed487cd3a57d)
![Screenshot 2024-11-01 203358](https://github.com/user-attachments/assets/d952956d-14d7-4096-9954-d1ec11515f5b)
![Screenshot 2024-11-01 203406](https://github.com/user-attachments/assets/25fd8ffc-f6a0-4840-83f6-565b46f773f9)
![Screenshot 2024-11-01 203411](https://github.com/user-attachments/assets/bebb744b-0309-499b-8c99-55d4a656b963)


