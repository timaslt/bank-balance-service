# Bank balance service
This project was built as a Spring Boot Maven app that uses PostgreSQL database and is deployed in Heroku [(https://bankbalanceservice.herokuapp.com/)](https://bankbalanceservice.herokuapp.com/). I used Intellij IDE.

While understanding the task of this challenge I made the following assumptions: 
* endpoint import and export single CSV file(easily changeable)
* CSV files have no headers
* no user interface is needed only the endpoints itself
## Running the code
There are two ways to run the code:
1. By clicking on [(https://bankbalanceservice.herokuapp.com/)](https://bankbalanceservice.herokuapp.com/) and waking the deployed app on Heroku.
2. Cloning the repository and running it as a Spring Boot app on IntelliJ or other IDE.

Then using Postman or another tool to create HTTP requests we can use 3 endpoints:
1. https://<span></span>bankbalanceservice.herokuapp.com/api/import, https://<span></span>bankbalanceservice.herokuapp.com/api/export and https://<span></span>bankbalanceservice.herokuapp.com/api/balance with Heroku app
2. localhost:8080/api/import, localhost:8080/api/export and localhost:8080/api/balance when running the code on localhost

* When importing CSV file make the request with key "file".
* Export has optional string parameters dateFrom and dateTo in yyyy-MM-dd format.
* Balance has mandatory string accountNumber parameter and two optional dateFrom and dateTo parameters.

Solely for more convenient  usage I created TESTER class and added test.csv file to the project.
To reset the database request mapping /reset (simply visiting localhost:8080/reset or https://<span></span>bankbalanceservice.herokuapp.com/reset will do). Initial database has 5 bank accounts with account numbers 1, 2, 3, 4, 5. test.csv for import can be found in resources folder.
## Future improvments
Due to lack of time I haven't manage to do or would like to improve the project by:
* Implementing transactions in different currencies
* Write JUnit tests for balance endpoint.
* Create util for reading and writing to CSV files.
* Change bank_account database table to account_balance which would store balance after each transaction instead of storing current balance and calculating negative deltas to find the balance of particular date.
