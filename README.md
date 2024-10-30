# Outback Weather Tracker

### Prerequisites

- Java Development Kit (JDK) amazon corretto 21
- Maven 3.8 or higher

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/CAB302-Software-Development/CAB302_OutbackWeatherTracker.git

### Building

1. run build mvn command:
   ```bash
   mvn clean install

### Testing

1. run test mvn command:
   ```bash
   mvn test
##
# REST API Services

This document provides an overview of the **User Data Service** endpoints, accessible via HTTP methods for user data management and authentication.

## User Api Service

This service allows retrieval, creation, deletion, and updating of user data, as well as authentication. It supports basic user management and authentication through a RESTful API.

## Endpoints

### 1. `getAllUsers()`

**Description**: Retrieve all users' data.

- **Method**: `GET`
- **Endpoint**: `/getAllUsers`
- **Response**: List of `UserDataDTO`
- **Throws**: Exception on error

**Code Example**:
```java
 UserApiService userApiService = new UserApiService();
 
 userApiService.getAllUsers();
```
##

### 2. `getUserByUsername(String username)` 
**Description**: Retrieve user data by username. 
- **Method**: `GET`
- **Endpoint**: `/getUserByUsername/{username}` 
- **Parameters**: 
	- `username` (String): the username to fetch. 
- **Response**: `AllUserDataModel` 
- **Throws**: Exception on error 
- **Code Example**: 
```java
 UserApiService userApiService = new UserApiService();
 
 userApiService.getUserByUsername("username");
```
##
### 3. `getUserById(String id)`

**Description**: Retrieve user data by ID.

- **Method**: `GET`
- **Endpoint**: `/getUserById/{id}`
- **Parameters**:
  - `id` (String): User ID.
- **Response**: `AllUserDataModel`
- **Throws**: Exception on error

**Code Example**:
```java
 UserApiService userApiService = new UserApiService();
 
 userApiService.getUserById("user id");
```
##
### 4. `createUser(CreateUserDTO userDTO)`

**Description**: Create a new user.

- **Method**: `POST`
- **Endpoint**: `/createUser`
- **Parameters**:
  - `userDTO` (CreateUserDTO): New user data.
- **Response**: `CreateUserDTO`
- **Throws**: Exception on error

**Code Example**:
```java
 UserApiService userApiService = new UserApiService();
 CreateUserDTO createUserDTO = new CreateUserDTO();

 createUserDTO.setUserName("my username");
 createUserDTO.setUserEmail("my email");
 createUserDTO.setUserPassword("my password");
 createUserDTO.setUserTheme("my theme");

 userApiService.createUser(createUserDTO);
```
##
### 5. `login(UserLoginRequestDTO loginRequest)`

**Description**: Log in a user and receive a JWT token.

- **Method**: `POST`
- **Endpoint**: `/login`
- **Parameters**:
  - `loginRequest` (UserLoginRequestDTO): Contains username and password.
- **Response**: JWT token (`String`)
- **Throws**: Exception on login failure

**Code Example**:
```java
 UserApiService userApiService = new UserApiService();
 UserLoginRequestDTO userLoginRequestDTO = new UserLoginRequestDTO();

 userLoginRequestDTO.setUserName("my username");
 userLoginRequestDTO.setPassword("my password");

 userApiService.login(userLoginRequestDTO);
```
##
### 6. `deleteUser(String userId, String jwtToken)`

**Description**: Delete a user by ID.

- **Method**: `DELETE`
- **Endpoint**: `/deleteUser`
- **Parameters**:
  - `userId` (String): ID of the user to delete.
  - `jwtToken` (String): Authorization token.
- **Response**: `true` if successful, `false` otherwise
- **Throws**: Exception on error

**Code Example**:
```java
 UserApiService userApiService = new UserApiService();

 userApiService.deleteUser("my id", "my jwt token");
```
##
### 7. `updateUser(String userId, UpdateUserDTO userData, String jwtToken)`

**Description**: Update a user’s information.

- **Method**: `PUT`
- **Endpoint**: `/updateUser/{userId}`
- **Parameters**:
  - `userId` (String): User ID to update.
  - `userData` (AllUserDTO): Updated user information.
  - `jwtToken` (String): Authorization token.
- **Response**: Updated `CreateUserDTO`
- **Throws**: Exception on error

**Code Example**:
```java
 UserApiService userApiService = new UserApiService();
 UpdateUserDTO updateUserDTOUserDTO = new UpdateUserDTO();

 updateUserDTOUserDTO.setUsername("my updated username");
 updateUserDTOUserDTO.setUserEmail("my updated email");
 updateUserDTOUserDTO.setUserTheme("my updated theme");
 updateUserDTOUserDTO.setUserPassword("my updated password");

 userApiService.updateUser(updateUserDTOUserDTO);
```
##
### 8. `getCurrentUser(String jwtToken)`

**Description**: Retrieve current user data using a JWT token.

- **Method**: `GET`
- **Endpoint**: `/getUser`
- **Parameters**:
  - `jwtToken` (String): Authorization token.
- **Response**: `UserModel`
- **Throws**: Exception on error

**Code Example**:
```java
 UserApiService userApiService = new UserApiService();
 
 userApiService.getCurrentUser("my jwt token");
```
## Crowdsourced Api Service

This service allows retrieval, creation, deletion, and updating of crowdsourced data through the RESTful API.

### 1. `createMarker(CrowdsourcedDTO data)`

**Description**: Create a new marker.

- **Parameters**:
  - `data` (CrowdsourcedDTO): The data to create a marker.
- **Response**: `CrowdsourcedModel` with created data.
- **Throws**: Exception

**Code Example**:
```java
 CrowdsourcedApiService crowdsourcedApiService = new CrowdsourcedApiService();
 CrowdsourcedDTO crowdsourcedDTO = new CrowdsourcedDTO();

 crowdsourcedDTO.setUserName("my username");
 crowdsourcedDTO.setLatitude(-27.4698);
 crowdsourcedDTO.setLongitude(153.0251);
 crowdsourcedDTO.setLocation("my location");
 crowdsourcedDTO.setActualTemp(28);
 crowdsourcedDTO.setFeelsLikeTemp(24);

 crowdsourcedApiService.createMarker(crowdsourcedDTO);
```
##
### 2. `getMarkerByUserName(String userName)`

**Description**: Get marker by user name.

- **Parameters**:
  - `userName` (String): The user name to search.
- **Response**: `Optional<CrowdsourcedModel>`
- **Throws**: Exception

**Code Example**:
```java
 CrowdsourcedApiService crowdsourcedApiService = new CrowdsourcedApiService();
 
 crowdsourcedApiService.getMarkerByUserName("my username");
```
##
### 3. `getMarkerByTempRange(int minTemp, int maxTemp)`

**Description**: Get markers by temperature range.

- **Parameters**:
  - `minTemp` (int): Minimum temperature.
  - `maxTemp` (int): Maximum temperature.
- **Response**: `Optional<List<CrowdsourcedModel>>` within range.
- **Throws**: Exception

**Code Example**:
```java
 CrowdsourcedApiService crowdsourcedApiService = new CrowdsourcedApiService();
 
 crowdsourcedApiService.getMarkerByTempRange(10, 34);
```
##
### 4. `deleteMarkerById(String id)`

**Description**: Delete marker by ID.

- **Parameters**:
  - `id` (String): The marker ID to delete.
- **Response**: `true` if deletion was successful, `false` otherwise.
- **Throws**: Exception

**Code Example**:
```java
 CrowdsourcedApiService crowdsourcedApiService = new CrowdsourcedApiService();

 crowdsourcedApiService.deleteMarkerById("my marker id");
```
##
### 5. `getMarkerByGeoRange(double minLat, double maxLat, double minLon, double maxLon)`

**Description**: Get markers by geographic range.

- **Parameters**:
  - `minLat` (double): Minimum latitude.
  - `maxLat` (double): Maximum latitude.27.
  - `minLon` (double): Minimum longitude.
  - `maxLon` (double): Maximum longitude.
- **Response**: Optional list of `CrowdsourcedModel` within range.
- **Throws**: Exception

**Code Example**:
```java
 CrowdsourcedApiService crowdsourcedApiService = new CrowdsourcedApiService();

 crowdsourcedApiService.getMarkerByGeoRange(-27.4698, -28.4322, 153.0251, 156.2411);
```
##
### 6. `getLatestFilteredData()`

**Description**: Get the latest filtered markers.

- **Response**: List of `CrowdsourcedDTO` with the latest data.
- **Throws**: Exception

**Code Example**:
```java
 CrowdsourcedApiService crowdsourcedApiService = new CrowdsourcedApiService();

 crowdsourcedApiService.getLatestFilteredData();
```
