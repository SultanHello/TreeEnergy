# Managing Categories

This is a Telegram bot that helps users create, see, and delete categories. The bot can also work with Excel files to download the category tree.

## Technologies used
- **Java**
- **Spring Boot**
- **Docker** (for containerization)
- **Maven** (for building the project)

## Main functionality
- **/viewTree** — Show category tree.  
- **/addElement <element name>** — Add new element. If no parent, it is root.  
- **/addElement <parent element> <child element>** — Add child to parent. If parent not found, show message.  
- **/removeElement <element name>** — Remove element and children. If not found, show message.  
- **/help** — Show commands and what they do.  
- **/download** — Get Excel file with category tree. 
 

## Run
-Run the command to build the project:
```bash
./mvnw clean package -DskipTests
``` 

- First, create a `.env` file and put the settings in it:
```bash
SPRING_DATASOURCE_URL=YOUR_URL
SPRING_DATASOURCE_USERNAME=YOUR_USERNAME
SPRING_DATASOURCE_PASSWORD=YOUR_PASSWORD
TG_BOT_NAME=YOUR_BOT_NAME
TG_BOT_TOKEN=YOUR_BOT_TOKEN
```
-to run
```bash
docker-compose --env-file .env up --build
```
**Stop the application**
- in console write
```bash
  docker compose down
```