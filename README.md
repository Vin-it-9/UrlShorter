# URL Shortener

A modern, high-performance URL shortening service built with Quarkus and Tailwind CSS. This application allows users to create shortened URLs with customizable expiration dates and provides detailed analytics for each shortened link.

## Features

- ✂️ Convert long URLs into short, manageable links
- 📊 Track clicks and view detailed statistics for each URL
- ⏱️ Set custom expiration dates (1-365 days)
- 🔄 Easy copy-to-clipboard functionality
- 📱 Responsive design for all devices
- 🚀 High-performance backend with Quarkus
- 🎨 Modern UI with Tailwind CSS and animations

## Technology Stack

- **Backend**: [Quarkus](https://quarkus.io/) - Supersonic Subatomic Java framework
- **Frontend**: HTML5, [Tailwind CSS](https://tailwindcss.com/), JavaScript
- **Templating**: [Qute](https://quarkus.io/guides/qute)
- **Database**: JPA/Hibernate with your choice of database
- **Build Tool**: Maven
- **Animations**: AOS (Animate On Scroll)

## Prerequisites

- Java 21
- Maven 3.8.1+
- Your preferred database (PostgreSQL, MySQL, etc.)

## Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/Vin-it-9/UrlShorter.git
   cd url-shortener
   ```

2. **Configure the database connection**
   Edit `src/main/resources/application.properties` with your database credentials:
   ```properties
   quarkus.datasource.db-kind=postgresql
   quarkus.datasource.username=your_username
   quarkus.datasource.password=your_password
   quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/your_database
   
   # Connection pool configuration
   quarkus.datasource.jdbc.max-size=3
   quarkus.datasource.jdbc.min-size=1
   ```

3. **Run in development mode**
   ```bash
   ./mvnw compile quarkus:dev
   ```

4. **Access the application**
   Open [http://localhost:8080](http://localhost:8080) in your browser

## Configuration

### Important Configuration Properties

| Property | Description | Default |
|----------|-------------|---------|
| `url.shortener.domain` | Base domain for shortened URLs | `http://localhost:8080/` |
| `quarkus.datasource.jdbc.max-size` | Maximum DB connection pool size | `3` |
| `quarkus.hibernate-orm.database.generation` | Database schema generation strategy | `update` |

### Production Configuration

For production environments, update the `url.shortener.domain` property to your actual domain:

```properties
%prod.url.shortener.domain=https://your-domain.com/
```

## API Endpoints

### Shorten URL
- **URL**: `/shorten`
- **Method**: `POST`
- **Content-Type**: `application/json`
- **Request Body**:
  ```json
  {
    "url": "https://example.com/your/very/long/url",
    "expirationDays": 30
  }
  ```
- **Success Response**:
  ```json
  {
    "shortenedUrl": "https://your-domain.com/abc123"
  }
  ```

### Redirect to Original URL
- **URL**: `/{shortCode}`
- **Method**: `GET`
- **Response**: Redirects to the original URL

### Get URL Statistics
- **URL**: `/stats/{shortCode}`
- **Method**: `GET`
- **Response**:
  ```json
  {
    "shortCode": "abc123",
    "originalUrl": "https://example.com/your/very/long/url",
    "createdAt": "2025-03-25T09:58:03.361547",
    "expiresAt": "2025-04-24T09:58:03.361547",
    "accessCount": 4,
    "lastAccessedAt": "2025-03-25T04:43:43.22213"
  }
  ```

## Usage

1. **Shorten a URL**: 
   - Enter the URL you want to shorten
   - Set the expiration time using the slider (1-365 days)
   - Click "Shorten URL"

2. **View Statistics**:
   - For any shortened URL, click "View Statistics" to see detailed analytics
   - Or navigate to `/stats/view/{shortCode}` directly

## Screenshots

![Home Page]([https://i.imgur.com/example-home.png](https://github.com/user-attachments/assets/f5b3e6b3-8d50-4a1b-85d4-45cefa2fe334))

*The URL shortener homepage*

![Statistics Page]([https://i.imgur.com/example-stats.png](https://github.com/user-attachments/assets/a6527b08-df3e-427c-952b-9273502272e5
))
*The statistics view for a shortened URL*

## Building for Production

```bash
./mvnw package -Pproduction
```

The application will be packaged as an uber-jar in `target/quarkus-app/`.

## Running in Production

```bash
java -jar target/quarkus-app/quarkus-run.jar
```


Developed with ❤️ and ☕
* Date: 2025-03-25
* Version: 1.0.0
