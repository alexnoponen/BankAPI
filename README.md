# Banking API
The Banking API will manage the bank accounts of its users. It will be managed by the Bank's employees and admins. Employees and Admins count as Standard users with additional abilities.
* Employees can view all customer information, but not modify in any way.
* Admins can both view all user information, as well as directly modify it.
* Standard users should be able to register and login to see their account information.They can have either Checking or Savings accounts.
* All users must be able to update their personal information, such as username, password, first and last names, as well as email.
* Accounts owned by users must support withdrawal, deposit, and transfer.
* Transfer of funds should be allowed between accounts owned by the same user, as well as between accounts owned by different users.

## Models
The below models are an outline that supports the required features. They can be expanded or modified as needed.

### **User**
The User model keeps track of users information.
```java
public class User {
  private int userId; // primary key
  private String username; // not null, unique
  private String password; // not null
  private String firstName; // not null
  private String lastName; // not null
  private String email; // not null
  private Role role;
}
```
You may optionally consider to include a `List<Account>` field in the User model. Some tasks will be easier, and others harder. In particular, this complicates every request that would need an entire User object, such as `Register` and `Update User`, since they would need to include the accounts as well. It would be up to you to resolve this.

### **Role**
The Role model keeps track of user permissions. Can be expanded for more features later. Could be `Admin`, `Employee`, `Standard`, or `Premium`

```java
public class Role {
  private int roleId; // primary key
  private String role; // not null, unique
}
```

### **Account**
The Account model is used to represent a single account for a user
```java
public class Account {
  private int accountId; // primary key
  private double balance;  // not null
  private AccountStatus status;
  private AccountType type;
}
```

### **AccountStatus**
The AccountStatus model is used to track the status of accounts. Status possibilities are `Pending`, `Open`, or `Closed`, or `Denied`
```java
public class AccountStatus {
  private int statusId; // primary key
  private String status; // not null, unique
}
```

### **AccountType**
The AccountType model is used to track what kind of account is being created. Type possibilities are `Checking` or `Savings`
```java
public class AccountType {
  private int typeId; // primary key
  private String type; // not null, unique
}
```

# Endpoints
The below endpoints generally follow a RESTful pattern. Where the URI describes the relevant resource and the HTTP Method describes the action to perform. Path variables (e.g. `/:userId`) are used to identify specific resources as part of the URI. These are placeholders, such as for a userId. If not otherwise specified, the response status code should be `200 OK`.
## Security
  Security should be handled through session storage.
  If a user does not have permission to access a particular endpoint it should return the following:
  * **Status Code:** `401 UNAUTHORIZED`
    **Content:**
    ```json
    {
      "message": "The requested action is not permitted"
    }
    ```
    Occurs if they do not have the appropriate permissions.

## RPC Endpoints
These endpoints are not RESTful, but are included to more conveniently simulate user actions

### **Login**
* **URL:** `/login`

* **Method:** `POST`

* **Request:**
  ```json
  {
    "username": String,
    "password": String
  }
  ```

* **Response:**
  ```json
  User
  ```

* **Error Response:**
  * **Status Code:** `400 BAD REQUEST`

  ```json
  {
    "message": "Invalid Credentials"
  }
  ```

### **Logout**
* **URL:** `/logout`

* **Method:** `POST`

* **Response:**
  ```json
  {
    "message": "You have successfully logged out {username}"
  }
  ```
* **Error Response:**
  * **Status Code:** `400 BAD REQUEST`

  ```json
  {
    "message": "There was no user logged into the session"
  }
  ```

### **Register**
* **URL:** `/register`

* **Method:** `POST`

* **Allowed Roles:** `Admin`

* **Request:**
  Note: All fields must be included and the userId should be zero
  ```json
  User
  ```

* **Response:**
  Note: The userId should be updated
  * **Status Code:** `201 CREATED`
  ```json
  User
  ```

* **Error Response:**
  Note: In case username or email is already used
  * **Status Code:** `400 BAD REQUEST`
  ```json
  {
    "message": "Invalid fields"
  }
  ```

### **Withdraw**
* **URL:** `/accounts/withdraw`

* **Method:** `POST`

* **Allowed Roles:** `Admin` or if the account belongs to the current user

* **Request:**
  ```json
  {
    "accountId": int,
    "amount": double
  }
  ```

* **Response:**
  ```json
  {
    "message": "${amount} has been withdrawn from Account #{accountId}"
  }
  ```

### **Deposit**
* **URL:** `/accounts/deposit`

* **Method:** `POST`

* **Allowed Roles:** `Admin` or if the account belongs to the current user

* **Request:**
  ```json
  {
    "accountId": int,
    "amount": double
  }
  ```

* **Response:**
  ```json
  {
    "message": "${amount} has been deposited to Account #{accountId}"
  }
  ```

### **Transfer**
* **URL:** `/accounts/transfer`

* **Method:** `POST`

* **Allowed Roles:** `Admin` or if the source account belongs to the current user

* **Request:**
  ```json
  {
    "sourceAccountId": int,
    "targetAccountId": int,
    "amount": double
  }
  ```

* **Response:**
  ```json
  {
    "message": "${amount} has been transferred from Account #{sourceAccountId} to Account #{targetAccountId}"
  }
  ```

## RESTful Endpoints
These endpoints *are* RESTful, and generally provide basic CRUD operations for Employees/Admins

### **Find Users**
* **URL:** `/users`

* **Method:** `GET`

* **Allowed Roles:** `Employee` or `Admin`

* **Response:**
  ```json
  [
    User
  ]
  ```

### **Find Users By Id**
* **URL:** `/users/:id`

* **Method:** `GET`

* **Allowed Roles:** `Employee` or `Admin` or if the id provided matches the id of the current user

* **Response:**
  ```json
  User
  ```

### **Update User**
* **URL:** `/users`

* **Method:** `PUT`

* **Allowed Roles:** `Admin` or if the id provided matches the id of the current user

* **Request:**
  Note: All fields must be included
  ```json
  User
  ```

* **Response:**
  ```json
  User
  ```

### **Find Accounts**
* **URL:** `/accounts`

* **Method:** `GET`

* **Allowed Roles:** `Employee` or `Admin`

* **Response:**
  ```json
  [
    Account
  ]
  ```

### **Find Accounts By Id**
* **URL:** `/accounts/:id`

* **Method:** `GET`

* **Allowed Roles:** `Employee` or `Admin` or if the account belongs to the current user

* **Response:**
  ```json
  Account
  ```

### **Find Accounts By Status**
* **URL:** `/accounts/status/:statusId`

* **Method:** `GET`

* **Allowed Roles:** `Employee` or `Admin`

* **Response:**
  ```json
  [
    Account
  ]
    ```

### **Find Accounts By User**
* **URL:** `/accounts/owner/:userId`
  For a challenge you could do this instead: `/accounts/owner/:userId?accountType=type`

* **Method:** `GET`

* **Allowed Roles:** `Employee` or `Admin` or if the id provided matches the id of the current user

* **Response:**
  ```json
  [
    Account
  ]
  ```

### **Submit Account**
* **URL:** `/accounts`

* **Method:** `POST`

* **Allowed Roles:** `Employee` or `Admin` or if the account belongs to the current user

* **Request:**
  The accountId should be 0
  ```json
  Account
  ```

* **Response:**
  * **Status Code:** `201 CREATED`

  ```json
  Account
  ```


### **Update Account**
* **URL:** `/accounts`

* **Method:** `PUT`

* **Allowed Roles:** `Admin`

* **Request:**
  Note: All fields must be included
  ```json
  Account
  ```

* **Response:**
  ```json
  Account
  ```

## Stretch Goals
These are not part of the core requirements but are things that could be worked on once the core requirements are done.
* Password Hashing
* Paging and Sorting endpoints: [Reference For How](https://docs.microsoft.com/en-us/azure/architecture/best-practices/api-design#filter-and-paginate-data)
* Using JSON Web Tokens (JWTs) instead of Session Storage
* Standard Users should be able to upgrade to Premium (the cost of such an upgrade may be freely determined)
  * With this premium status, Users should now also be able to open new joint accounts (accounts with multiple owners), as well as be able to add users to pre-existing accounts
* Supporting DELETE requests for Users and Accounts
* Savings accounts will have interest rates, that can be determined as you see fit.


### **Pass Time**
This endpoint is designed to simulate the passing of time for Savings Accounts to accrue interest
* **URL:** `/passTime`

* **Method:** `POST`

* **Allowed Roles:** `Admin`

* **Request:**
  ```json
  {
    "numOfMonths": int
  }
  ```

* **Response:**
  ```json
  {
    "message": "{numOfMonths} months of interest has been accrued for all Savings Accounts"
  }
  ```

## Evaluation
The project will be evaluated out of 100 points split between two main catagories: 70 points for the functionality and design of your project and 30 points for the presentation of your project during the project showcase. The evaluation will be further subdivided as follows: 

**Project Score**(70 pts):
* 15 pts: Ability to persist meaningful data in the database and then retrieve it with HTTP requests. 
* 15 pts: Ability to make withdrawals from, deposits to, and transfers between accounts via HTTP requests.
* 10 pts: Proper database schema achieving 3rd normal form. (E.g. Accounts have a proper relationship to their owning user.)
* 10 pts: Login, register, update and logout functionality.  
* 5 pts: Properly layered architectural design. (I.e Controller/Service/DAO) 
* 5 pts: Different user roles with different levels of access implemented correctly. 
* 10 pts: Implementation of 3 stretch goals. (These points will only be counted if all other functionality is present. Additional stretch goals beyond 3 will provide bonus points.)

**Presentation Score**(30 pts):
* 15 pts: Clear, concise and professional communication during the project presentation.
* 10 pts: Ability to communicate clear answers to fully address questions asked about the project. 
* 5 pts: Logical flow to the project presentation. 


## Frequently Asked Questions

1. When is the project due? 

    >A: The project will be showcased on the final Thursday of the batch. The version of the project you last pushed to github before the presentations begin will be the version that is evaluated.


2. Can I have an extension? 
    >A: No. While you are encouraged to continue to work on your projects past the date of the project showcase for your own learning benefit, extensions can not be accommodated as they are already on the final day of actual training.

3. Is there a code freeze? 
    >A: It is recommended that you institute your own code freeze at least a day before the project showcase. However, this is a recommendation only; it will not be enforced.

4. What happens if I break my project that was mostly working right before the showcase? 
    >A: As you should have been regularly pushing code to your repository you should be able to roll back to previously working version. If you have not regularly pushed your code and do not have a working commit to return to you will need to present the state of your application in its current form. 


5. Who will be evaluating the project showcase? 
    >A: Your trainer will be the one providing the full evaluation of your projects. However, the QC team will also be present to ask questions about your project and consult with your trainer. 

6. Are we allowed to collaborate with others on our projects? 
    >A: Collaboration is encouraged. Hopefully you will work together to solve the problems presented in this project.However, you should still be ultimately designing the project yourself. Straight copy/pasting of another person's code is considered plagiarism. NOTE: Code provided in demos by your trainer is not subject to plagiarism concerns. Feel free to copy/paste and edit such code as necessary to suit your projects. 

7. What is a passing score? 
    >A: 75% is the base expected passing score.

8. If I pass the project am I certain to get invited to the next phase of ROCP? 
    >A: The project is a major component of the determination process for invitations to continue you on the next ROCP phase. However, it is not the only metric used and the final determination will be based on your total performance throughout this phase of ROCP.

9. Are there really no extensions? 
    >A: There are not.


