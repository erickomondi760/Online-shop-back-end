# 🛒 Online Shop Back-End

![Build Status](https://img.shields.io/github/actions/workflow/status/erickomondi760/Online-shop-back-end/maven.yml?branch=main)
![License](https://img.shields.io/github/license/erickomondi760/Online-shop-back-end)
![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-green)
![Docker](https://img.shields.io/badge/Docker-ready-blue)
![Coverage](https://img.shields.io/codecov/c/github/erickomondi760/Online-shop-back-end)


A robust **Java Spring Boot** back-end service for an online shopping platform. This project provides APIs for managing products, users, authentication, and orders, designed with scalability and maintainability in mind.

---

## 📌 Features
- **User Management**: Registration, login, and secure authentication (JWT).
- **Product Management**: CRUD operations for products.
- **Order Management**: Create and track customer orders.
- **Authentication & Authorization**: Role-based access control.
- **Database Integration**: Supports relational databases (MySQL/PostgreSQL).
- **RESTful APIs**: Well-structured endpoints for front-end integration.
- **Maven Build System**: Easy dependency management and build automation.

---

## 🏗️ Tech Stack
| Layer              | Technology |
|--------------------|------------|
| Language           | Java (JDK 17+) |
| Framework          | Spring Boot |
| Build Tool         | Maven |
| Database           | MySQL / PostgreSQL |
| Authentication     | JWT |
| Containerization   | Docker (optional) |

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL/PostgreSQL running locally or remotely
- Git

### Installation
```bash
# Clone the repository
git clone https://github.com/erickomondi760/Online-shop-back-end.git

# Navigate into the project
cd Online-shop-back-end

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

---

## ⚙️ Configuration
Update `application.properties` (or `application.yml`) with your database credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/online_shop
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
jwt.secret=your_secret_key
```

---

## 📡 API Endpoints

### Authentication
- `POST /api/auth/register` → Register new user
- `POST /api/auth/login` → Login and receive JWT

### Products
- `GET /api/products` → List all products
- `POST /api/products` → Add new product
- `PUT /api/products/{id}` → Update product
- `DELETE /api/products/{id}` → Delete product

### Orders
- `POST /api/orders` → Create order
- `GET /api/orders/{id}` → Get order details

---

## 🧪 Testing
Run unit and integration tests:
```bash
mvn test
```

---

## 📦 Deployment
- **Docker**: Build and run containerized version
```bash
docker build -t online-shop-backend .
docker run -p 8080:8080 online-shop-backend
```
- **Cloud Ready**: Can be deployed on AWS, Azure, or GCP with minimal configuration.

---

## 📖 Documentation
- API documentation can be generated with **Swagger/OpenAPI**.
- Access Swagger UI at: `http://localhost:8080/swagger-ui.html` (if enabled).

---

## 🤝 Contributing
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit changes (`git commit -m 'Add new feature'`)
4. Push to branch (`git push origin feature/your-feature`)
5. Open a Pull Request

---

## 📜 License
This project is licensed under the MIT License – see the LICENSE file for details.

---

## 👨‍💻 Author
**Erick Omondi**  
Software Developer | Java & Spring Boot Specialist

---

