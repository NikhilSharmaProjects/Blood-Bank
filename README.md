# Blood Bank Management System

Hey! So this is my college project for managing blood bank operations digitally. The idea is to make it easier for blood banks to track donors, manage blood inventory, and help people who need blood find it quickly.

## What Does This Project Do?

Think of it as a mini website for a blood bank. There are three types of users:

1. **Admin** - The person managing the blood bank (like the boss)
2. **Donors** - People who want to donate blood
3. **Users/Receivers** - People who need blood for patients

Each type of user can log in and do different things based on their role.

## Main Features

### For Admin:
- Add new blood donors to the system
- View all registered donors
- Update blood stock (like when new blood comes in)
- See reports of who received blood and when
- Check how many units of each blood type are available

### For Donors:
- Sign up and create an account
- Log in to see current blood requirements
- View available blood stock

### For Users (People who need blood):
- Create an account
- Request blood for a patient
- Search for donors by blood group
- See if blood is available before requesting

## How It's Built

I used a bunch of different technologies to build this:

### The Front-End (What You See):
- **HTML** - The basic structure of all the pages
- **CSS** - Makes everything look nice with colors, fonts, and layouts
- **JavaScript** - Adds interactive features (like showing alerts and validating forms)

### The Back-End (What Happens Behind the Scenes):
This part handles all the logic and data processing. Don't worry if this sounds complicated - basically:
- **Java Servlets** - These are like workers that process requests (login, add donor, etc.)
- **JSP** - Helps display data on web pages dynamically
- **MySQL Database** - Stores all the information (like a big digital filing cabinet)

### Build Tool:
- **Maven** - Think of it like a helper that automatically downloads all the tools and libraries we need

## Project Structure Explained

Here's how the files are organized (simplified version):

```
Blood Bank/
├── src/main/
│   ├── java/            (All the behind-the-scenes code)
│   │   ├── servlet/     (Handles things like login, adding donors, etc.)
│   │   └── util/        (Database connection stuff)
│   └── webapp/          (The actual web pages you see)
│       ├── css/         (Styling files)
│       ├── js/          (JavaScript for interactions)
│       └── *.html       (All the web pages)
├── database_schema.sql  (Sets up the database tables)
└── pom.xml             (Maven configuration - lists what tools we need)
```

## What You Need to Run This

Before you can run this project, you need to install a few things on your computer:

1. **Java JDK 8+** - The programming language runtime
2. **Apache Tomcat 9+** - A server that runs Java web applications
3. **MySQL 8+** - The database to store all information
4. **Maven** - Helps build and run the project

## Setting It Up (Step by Step)

### Step 1: Set Up the Database

First, you need to create the database where all the information will be stored.

1. Open MySQL (you can use MySQL Workbench or command line)
2. Run this command in your MySQL:
   ```sql
   source path/to/database_schema.sql
   ```
   (Replace `path/to/` with the actual folder where your project is)

This will:
- Create a database called `blood_bank`
- Create 5 tables (admin, donor, users, blood_stock, blood_issue)
- Add some test data so you can try logging in

3. Update the database password:
   - Open the file: `src/main/java/util/DBConnection.java`
   - Find the line that says `PASSWORD = "your_mysql_password"`
   - Change it to your actual MySQL password

### Step 2: Build the Project

Open a terminal/command prompt in your project folder and run:
```bash
mvn clean install
```

This creates a `.war` file (like a packaged version of your website) in the `target` folder.

### Step 3: Deploy to Tomcat

1. Find the `blood-bank.war` file in the `target` folder
2. Copy it to your Tomcat's `webapps` folder
   - Usually located at `C:\Program Files\Apache Tomcat\webapps\` on Windows
   - Or `/usr/local/tomcat/webapps/` on Mac/Linux
3. Start Tomcat:
   - Windows: Run `startup.bat` from Tomcat's `bin` folder
   - Mac/Linux: Run `./startup.sh` from Tomcat's `bin` folder

### Step 4: Open in Browser

Open your web browser and go to:
```
http://localhost:8080/blood-bank/
```

## Test Login Credentials

I've added some test accounts so you can try it out:

**Admin Login:**
- Username: `admin`
- Password: `admin123`

**Test Donor:**
- Phone: `9876543210`
- Password: `donor123`

**Test User:**
- Username: `user1`
- Password: `user123`

## Database Tables Explained (Simple Version)

The database has 5 main tables:

1. **admin** - Stores admin username and password
2. **donor** - All donor information (name, age, blood group, contact, etc.)
3. **users** - People who need blood (receivers)
4. **blood_stock** - How many units of each blood type are available
5. **blood_issue** - History of all blood requests and who got what

## How the Pages Work Together

1. **index.html** - The home page where you choose your role
2. **Login pages** - Different login pages for admin, donor, and user
3. **Dashboard pages** - After logging in, you see your personalized dashboard
4. **Management pages** - For admin to manage donors and stock

## Common Issues You Might Face

### "Can't connect to database"
- Make sure MySQL is running
- Check if your password in `DBConnection.java` is correct
- Make sure you created the database using `database_schema.sql`

### "Port 8080 already in use"
- Another program is using port 8080
- Either close that program or change Tomcat's port in `server.xml`

### "Page not found (404)"
- Make sure Tomcat is running
- Check if the `.war` file is in Tomcat's `webapps` folder
- Wait a few seconds for Tomcat to unpack the war file

## What I Learned Making This

This was my first real project combining frontend and backend, and I learned:
- How to connect a website to a database
- How sessions work (keeping users logged in)
- How to validate forms with JavaScript
- Basic security (like preventing unauthorized access)
- How different roles can have different permissions

## Future Improvements (If I Had More Time)

- Add email notifications when blood is low
- Allow donors to schedule appointments
- Add a map showing nearby blood banks
- Better security with password encryption
- Mobile-responsive design improvements

## Blood Groups Supported

The system handles all 8 major blood types:
- A+ and A-
- B+ and B-
- AB+ and AB-
- O+ and O-

## Technologies Used (Summary)

| Layer | Technology |
|-------|------------|
| Frontend | HTML, CSS, JavaScript |
| Backend | Java Servlets |
| Database | MySQL |
| Server | Apache Tomcat |
| Build Tool | Maven |

## A Quick Note

This project was built for learning purposes and to help understand how full-stack web applications work. It covers the basics of user authentication, database operations, and session management.

---

If you have any questions about how something works or need help running it, feel free to ask!
