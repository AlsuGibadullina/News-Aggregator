# News Aggregator with Infinispan

This project is a centralized platform for aggregating news from multiple APIs. It provides users with a personalized news feed based on their preferences and trends.

## Features

- Aggregation of data from various news APIs (e.g., NewsAPI, Bing News Search).
- Real-time news processing: duplicate removal, topic detection, and importance-based sorting.
- Caching of popular queries (e.g., "Top 10 news of the day") using **Infinispan**.
- User personalization: filters by topics, regions, and sources.
- User interaction: commenting, liking news articles.
- Secure user authentication and registration.

## Technology Stack

- **Programming Language**: Java
- **Framework**: Spring Boot (for REST API development and integration with databases and external APIs)
- **Caching**: Infinispan (for storing sessions, popular queries, and other data)
- **Database**: PostgreSQL (for storing users, news, comments, and relationships between entities)
- **API Integration**: RESTful calls to NewsAPI, Bing News Search, and others
- **Testing**: JUnit
- **Version Control**: Git

## Prerequisites

- **Java 17** or higher
- **PostgreSQL 13** or higher
- **Maven 3.8+**
- **Docker** (optional, for containerized deployment)

## Installation

### 1. Clone the repository
```bash
$ git clone <repository-url>
$ cd news-aggregator
```

### 2. Configure the database
1. Create a PostgreSQL database.
2. Update the `application.properties` file with your database credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/news_aggregator
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>
```

### 3. Build and run the application
```bash
$ mvn clean install
$ java -jar target/news-aggregator-0.0.1-SNAPSHOT.jar
```

### 4. Access the application
The application will be available at [http://localhost:8080](http://localhost:8080).

## Usage

### API Endpoints
- **GET** `/api/news` - Retrieve aggregated news.
- **POST** `/api/auth/login` - User login.
- **POST** `/api/auth/register` - User registration.
- **POST** `/api/comments` - Add a comment to a news article.
- **PUT** `/api/news/{id}/like` - Like a news article.

### Additional APIs
Documentation for all endpoints is available at `/swagger-ui`.

## Testing
Run unit tests using Maven:
```bash
$ mvn test
```
