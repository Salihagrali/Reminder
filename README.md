# Reminder 📬  
-A backend project to study and solidify my skills on Spring Security, JWT, Quartz and other areas of backend development.

## 📌 What is Reminder?
-Reminder is a project to set up scheduled mails to remind the user later. It uses Quartz Scheduler to schedule mails and JavaMailSender to actually send mails to the specified email.
This project has custom Spring Security architecture and JWT implementation. To access the secured endpoints, first one should register as a user then should login to 
get the JWT token. This token is required to access the secured endpoints.

## 🧠 What I Learned
-Most valuable lesson I got from this project is actually planning what to do beforehand. Initially I started off without security. Due to my lack of experience in the security, I couldn't connect the dots
to implement a whole secured system. I messed up entities such as UserEntity. Also created unnecessary entities. When I understood what my mistakes are, I was already deep in overall
implementation. Nevertheless, with reading docs and watching tutorials I overcome that problem.


## 🚀 Technologies Used

- Java 21
- Spring Boot
- Spring Security (custom implementation)
- JWT (JSON Web Tokens)
- Quartz Scheduler
- JavaMailSender
- Maven
