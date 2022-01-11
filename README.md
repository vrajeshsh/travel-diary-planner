# Travel-Diary-Planner
When we plan a trip , we want to search for hotels, flights and weather.
And with the ongoing COVID 19 pandemic we also want to look at the COVID 19 statistics of different countries. 
During our trip, we take pictures, videos and we want to store the data in notes, we want all our information to be in one location. 
We have created this application for this purpose.
It will be easy for users to enjoy the trip experience with the help of this application.

Functionality Description:

Login and registration feature using the user's email and password. 
Account creation
Search weather based on location
Search weather forecast based on location 
Photos, videos, voice memos, and other files can be stored on the cloud storage.
A location(longitude and latitude) can be tagged to a particular record (Travel Note).
Search for COVID-19 statistics for a particular country.
Search for a particular record (Travel Note) in the database.

External Apis Being used

https://www.weatherapi.com/
https://rapidapi.com/apidojo/api/hotels4/
https://api.covid19api.com
https://free.currencyconverterapi.com/

Functional Requirements

The user should see a home screen with an automatic image slider with captions suggesting the various features of the application.
The users of the software system shall be authenticated with a username and password. Username shall consist of minimum 6 and maximum 32 alphanumeric characters and which may consist of special characters (_.-@) as well. Passwords shall consist of minimum 8 and maximum 32 characters of which at least 1 must be uppercase letters, at least 1 must be lowercase letters, at least 1 must be numbers, and at least 1 must be special characters (above the keyboard).
The users can register in the application by providing their first name, last name, address, city, selecting their state code from drop down menu, zip code, security question along with an answer and their valid username and password (as specified in the previous requirement).
The user can search for hotels that are currently available in a specific city by entering the correct city name. The search results would specify the hotel name, address and their google maps location. These results can be viewed in list or grid view.
The user can search for current weather conditions for a specific city by entering the city name and also the number of days of weather forecast the user wants to know, for the entered city. These results can be viewed in list or grid view.
The user can search for COVID-19 statistics in the form of a bar chart, based on the selected country from the drop down menu, the selected option type i.e. new cases or deaths, the start date and the appropriate end date.
A logged in user should can manage their account by editing their first name, last name,
address, city, zip code, state, valid email address.
The user shall be able to set their profile picture either by uploading an image or by capturing their photo using their webcam.
The user shall be able to change their password by entering the correct username and answering the security question.
The user shall be able to delete their account and doing so will also delete all the data associated with their account.
The user shall be able to log out using the log out option.
The user shall be able to access their travel notes or records which will have title, note, creation date and the location in the form of google maps link.
The user shall be able to filter the travel notes based on the Title, Note and Date columns according to a specified column search field and a global search field.
The user shall be able to sort the table based on the Title, Note and Date columns.
The user shall be able to create, view, edit, and delete the travel notes.
The user shall be able to manage and access the  files for a selected travel note or record.
The user shall be able to view (if the file format is of type JPG, JPEG, PNG, GIF, HTML, PDF or TXT), upload, download, delete and play a video file (if the file format is of type MP4, OGG or WEBM).
The user shall be able to perform database search on their cloud database travel notes or records with a search string given as input and also the search category (Title, Note or All).
The user shall be able to perform database search on their cloud database travel notes or records with a search date given and get all the records that were created on or after the specified date.
The user shall be able to perform database search on their cloud database travel notes or records with a search date given and get all the records that were created before the specified date.
The user shall be able to perform database search on their cloud database travel notes or records with a search From-date and To-date and get all the records that were created on or between the specified dates.
The currency converter feature helps the user convert easily between currencies at the click of a button.

Non-Functional Requirements


The software system shall be developed using royalty-free software products.
The web-based software system shall be usable with Microsoft Internet Explorer browser version 6.0 or higher, Chrome web browser version 96.0 or higher or Firefox web browser version 94.0 or higher.
The software system shall be executable under Sun Solaris Unix and Microsoft Windows Server 2003 operating systems.
Interoperability among the software components shall be accomplished using the High Level Architecture standard.
Each page which does not use an external API call should load within 5 seconds.
Under normal circumstances, the home page must be fully rendered within 5 seconds, for 90% of users
Under heavy load, the home page must be fully rendered within 8 seconds.
The User Interface should be intuitive and user friendly.
The software application should not be down for more than 10 minutes.
Customers can access their account 96% of the time without failure.
If a major incident happens on the website, the business must take measures to go back to being fully operational within three days.
In the case of unplanned system downtime, all features will be available again after one working day.
The website must follow the service-oriented architecture.

