# 🚀 Social Network - Server Side
🔹 **Production Version**  
[Click here to access the live production version](https://social-network-client-k8fp.onrender.com/login)

🔹 **Client Side**  
[Click here to go client side](https://github.com/yakov152005/social-network-client)


---

## 🎯 Overview
The backend of **Social Network** is developed using **Java & Spring Boot**, offering a **secure, scalable, and optimized REST API**.  
The architecture follows a **layered design**, ensuring clear separation of concerns:  
📌 **Entities** → **Repositories** → **Services** → **Controllers** → **DTOs** → **Responses**

This backend provides **authentication, caching, real-time updates, email & SMS verification**, and **scheduled jobs for maintenance**. It is fully **Dockerized and deployed with CI/CD using GitHub Actions and Render**.

---

## 🏗 Tech Stack
🛠 **Backend Framework:** Spring Boot, Java  
🔒 **Security:** Spring Security, JWT (JSON Web Tokens), Password Hashing (SHA-256 + Salt)  
📡 **Real-Time Updates:** SSE (Server-Sent Events)  
📦 **Database:** MySQL (optimized queries & indexes using JPA)  
📨 **Email & SMS Verification:** Twilio, Mailgun  & Custom api

📸 **Cloud Storage:** Cloudinary for media uploads  
⚡ **Caching:** **Spring Boot Caching** for optimized performance  
🗄 **Scheduled Jobs:** CRON Job (session tracking, expired token cleanup, remove stories after 24 hours ago, sends an email to users who have not logged in for over a month)  
📋 **Environment Configuration:** Dotenv for local & AppConfig managing variables for production.   
🐳 **Containerization & Deployment:** Docker + Render + GitHub Actions (CI/CD)

---

## 🔄 System Flow

### **1️⃣ Frontend to Backend Communication**
- **The client (React) sends requests using Axios** to RESTful API endpoints.
- **Backend controllers handle the request**, then delegate to services.
- **Services process the business logic** and interact with repositories.
- **Repositories execute JPA queries** on the MySQL database.
- **Caching optimizes responses**, and SSE provides **real-time updates**.
- **All operations are containerized in Docker** for scalable deployment.

### **2️⃣ Authentication & Security**
- **User Registration:**
    - Strong password validation & uniqueness check (email/phone).
    - If valid, **an email is sent with account details**.
    - **Password is hashed & salted (SHA-256)** before storage.

- **Login Process:**
    - User enters credentials → **JWT is generated**.
    - **2FA SMS verification** is sent to the phone.
    - On success, session starts & **user gains access**.

- **Password Reset:**
    - User requests reset → **Receives email with a unique token**.
    - User enters token & new password → **New hashed & salted password is saved**.
    - Confirmation email is sent.

### **3️⃣ Data Storage & Optimization**
- MySQL database with **efficient JPA queries and indexes**.
- User content (posts, comments, media) stored with Cloudinary integration.
- **Spring Boot Caching** improves response times and reduces redundant queries.

### **4️⃣ Real-Time Notifications & Messaging**
- **SSE (Server-Sent Events)** used for real-time notifications/messages/comments/stories/online friends.
- Users get **instant alerts for likes, follows, and comments**.

### **5️⃣ Automated Jobs & Expiration Handling**
- **CRON Jobs** run monthly to check for inactive users & send reminders.
- **CRON Jobs** run 24 hours ago to remove stories after 24 hours ago.
- **Token expiration handling** ensures security by removing stale sessions.

### **6️⃣ Deployment & Scaling**
- **Dockerized environment** ensures consistency across deployments.
- **CI/CD pipeline (GitHub Actions) automates deployment** to Render.
- Environment variables managed securely with AppConfig and `.env` files.

---

## 📂 Project Structure

```plaintext
Social-Network-Server/
├── 📂 .github
│     ├── workflow
│         ├── deploy.yml # CI/CD GitHub Actions for auto-deployment
├── 📂 src/main/java/org.server.socialnetworkserver
│    ├── 📂 config          # Security, JWT, Caching & Environment Management
│    ├── 📂 controllers     # API endpoints for handling requests
│    ├── 📂 dtos            # Data transfer objects for request/response
│    ├── 📂 entities        # Database entities (User, Post, Comment, etc.)
│    ├── 📂 jobs            # Scheduled tasks (CronJobs)
│    ├── 📂 repositories    # JPA repositories for database interaction
│    ├── 📂 responses       # Custom API responses
│    ├── 📂 services        # Business logic layer
│    ├── 📂 test            # Unit & integration tests
│    ├── 📂 utils           # Helper functions, JWT token & password generators & utilities & Api sms/email/gpt
│    └── SocialNetworkServerApplication  # Main application with enabled annotations  
├── 📂 resources             # Application properties settings  
│    
├── Dockerfile              # Docker container setup  
├── .env                    # Environment variable template for local use  
├── pom.xml                 # Mvn dependencies
└── README.md               # Project documentation  
```

---

## Entity-Relationship Diagram (ERD)

![ERD](https://i.imgur.com/oGsvnZv.png)

---

## 🔄 Installation & Setup
```bash
# Clone repository
git clone https://github.com/your-repo/social-network-server.git
cd social-network-server

# Build and run with Maven
mvn clean install
mvn spring-boot:run

#DotEnv Example
DB_URL_LOCAL=yourLocalDb
DB_HOST_LOCAL=yourHost
DB_USERNAME_LOCAL=yourUsername
DB_PASSWORD_LOCAL=yourPassword
DB_NAME_LOCAL=yourDBname
SMS_TOKEN=You Need SMS Token or Cancel it
SENDER_EMAIL=yourMail
SENDER_PASSWORD=yourPassAppMail
URL_CLIENT_PC=http://localhost:3000
URL_TEST=http://localhost:8080/social-network/slow-endpoint

# Running with Docker
docker build -t social-network-server .
docker run -p 8080:8080 social-network-server
```

---

## 📡 API Communication

### **User Registration (Secure)**
```java
@PostMapping("/add-user")
public ValidationResponse addUser(@RequestBody User user) {
    return userService.addUser(user);
}
```

### **Login with 2FA SMS Verification**
```java
 @PostMapping("/login-user")
public LoginResponse loginUser(@RequestBody Map<String, String> loginDetails) {
    return userService.loginUser(loginDetails);
}

@PostMapping("/verify-code")
public Map<String, String> verifyCode(@RequestBody Map<String, String> verificationDetails) {
    return userService.verifyCode(verificationDetails);
}
```

### **Password Reset Request**
```java
 @GetMapping("/reset-password/{email}&{username}")
public BasicResponse resetPasswordForThisUser(@PathVariable String email, @PathVariable String username) {
    return userService.resetPasswordForThisUser(email,username);
}

@PostMapping("/confirm-reset-password")
public BasicResponse confirmResetPassword(@RequestParam String token){
    return userService.confirmPasswordReset(token);
}
```

---

## 🔐 Security Features

✅ **JWT Authentication with Expiration Handling**  
✅ **Two-Factor Authentication (2FA) via SMS**  
✅ **Password Hashing (SHA-256 + Salt)**  
✅ **Rate Limiting to Prevent Abuse**  
✅ **Spring Boot Caching for Optimized Performance**  
✅ **CORS & Helmet for API Security**

---

## 📡 Real-Time Features

📡 **SSE for Instant Updates**
- Notifications for **likes, comments, follows, messages, online friends, stories**.
- Real-time updates without excessive polling.

📢 **Automated Email & SMS Alerts**
- **Registration confirmation via email**.
- **Password reset email with unique token**.

---

## 📅 Automated Jobs (CRON Jobs)

🕒 **Runs once per month**
- Sends **reminder emails** to inactive users.

🕒 **Runs once per 24 hours**
- Checks for **expired stories more than 24 hours have passed** and removes them.
  
🕒 **Runs once per day**
- Checks for **expired authentication tokens** and removes them.

---

## 🛠 Deployment & CI/CD

🚀 **Dockerized for scalable deployment**  
🔄 **CI/CD with GitHub Actions**  
🌍 **Hosted on Render (auto-deployment on commit)**

---

## 📩 Contact & Contribute

💡 Contributions are welcome via **Pull Requests**.  
Feel free to reach out via email: 📧 yakovbenhemo5@gmail.com


🚀 **Built for a seamless and secure social networking experience!** 🌍  





