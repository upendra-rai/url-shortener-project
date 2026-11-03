# 🔗 URL Shortener Service (Distributed Architecture)

Yeh ek high-scale, production-ready **Distributed URL Shortener Service** hai (jaise Bitly ya TinyURL). Is project ko **Spring Boot 3**, **Apache ZooKeeper**, **Redis**, aur **PostgreSQL** ke saath design kiya gaya hai taaki high traffic me bhi ultra-low latency aur zero ID collision mile.

---

## 🚀 Key Features

* **Distributed Unique ID Generator**: Apache ZooKeeper aur Curator use karke cluster me safely range-based numeric IDs generate karta hai (zero collision).
* **Base62 Encoding**: Numeric ID ko compact, clean, URL-safe alphanumeric string (`[0-9a-zA-Z]`) me convert karta hai.
* **Ultra-Fast Redirection (Redis Caching)**: URL redirection requests ke liye Redis caching use karta hai taaki direct database hit na ho aur microseconds me redirect ho.
* **Cache Invalidation**: Jab bhi koi URL update ya delete hota hai, Redis cache automatically evict/invalidate ho jata hai.
* **Security & Auth**: JWT-based stateless authentication, role-based access control, aur custom logout token revocation.
* **Dockerized**: ZooKeeper, Redis, PostgreSQL, aur Spring Boot app ke liye complete `docker-compose.yml` aur multi-stage `Dockerfile`.

---

## 🛠 Tech Stack

| Component | Technology | Role |
| :--- | :--- | :--- |
| **Backend Framework** | Spring Boot 3.2.5 (Java 17+) | REST APIs, Business Logic |
| **Distributed Coordinator** | Apache ZooKeeper 3.9 + Apache Curator | Distributed Lock aur ID Range Allocation |
| **In-Memory Cache** | Redis 7 | Lightning-fast URL Redirection & Caching |
| **Relational Database** | PostgreSQL 15 | Permanent Storage (URLs, Users, Tokens) |
| **Security** | Spring Security + JJWT | JWT Auth, Role management, Logout handler |
| **DevOps / Containers** | Docker & Docker Compose | Containerization & Local Dev environment |

---

## 🧠 System Architecture & Kaise Kaam Karta Hai?

```mermaid
flowchart TD
    Client["Client / Browser"]
    App["Spring Boot App (URL Service)"]
    ZK[("Apache ZooKeeper")]
    Redis[("Redis Cache")]
    DB[("PostgreSQL DB")]

    Client -->|1. POST /v1/short-urls| App
    App -->|Allocate Range e.g. 1-1000| ZK
    App -->|Base62 Encode ID to Code| App
    App -->|Save URL & Code| DB

    Client -->|2. GET /{code}| App
    App -->|Check Cache| Redis
    Redis --Cache Miss--> App
    App -->|Fetch from DB & Populate Cache| DB
    Redis --Cache Hit--> App
    App -->|302 Redirect to Original URL| Client
```

---

## 🔍 Core Mechanisms Deep Dive

### 1. ZooKeeper Range-Based ID Generation (`DistributedIdGenerator`)

#### Problem:
Agar humare paas 5 ya 10 application servers chal rahe hain, aur har server direct Database auto-increment ID use karega, toh database bottleneck ban jayega aur race condition aa sakti hai. Random strings generate karne se hash collisions ke chances hote hain jise bar-bar database check karna padta hai.

#### Solution:
Humne **Apache ZooKeeper** ke through **Range-Based Allocation** implement kiya hai:

1. **ZooKeeper Node**: ZooKeeper me ek centralized counter node hota hai: `/url-shortener/id-generator/counter`.
2. **Distributed Lock**: Jab app ko naye IDs ki zaroorat hoti hai, wo ZooKeeper par distributed mutex lock (`/locks/id-range`) acquire karta hai.
3. **Block Allocation**: App ek block of IDs reserve kar leta hai (default `block-size = 1000`):
   * *Server 1* aata hai -> Range milti hai: `1 se 1000`. ZooKeeper counter ho jata hai `1000`.
   * *Server 2* aata hai -> Range milti hai: `1001 se 2000`. ZooKeeper counter ho jata hai `2000`.
4. **In-Memory Generation**: Server 1 apne allocated block (`1 se 1000`) ko memory me `AtomicLong` se ultra-fast increment karta hai bina database ya ZooKeeper ko touch kiye!
5. Jab 1000 IDs exhaust ho jate hain, server dobara ZooKeeper ke paas jaakar next block (`2001 se 3000`) mangta hai.

**Fayda**: Ultra-low latency, zero collisions, aur database par zero load!

---

### 2. Base62 Encoding (`Base62Encoder`)

ZooKeeper se mila numeric ID (jaise `125135123`) ko compact short code me convert karne ke liye hum **Base62** use karte hain:

* **Characters used (Total 62)**: `0-9`, `A-Z`, `a-z`
* Base64 ke mukable Base62 me `+` aur `/` characters nahi hote, isliye ye **100% URL-Safe** hota hai bina kisi extra encoding ke.

#### Conversion Example:
Numeric ID ko 62 se divide karte hain aur remainders ko map karte hain:
$$\text{ID: } 125135123 \longrightarrow \text{Base62 Code: } \texttt{beX0kaEy}$$

Result: Long URL `https://www.youtube.com/watch?v=...` ban jata hai:
`http://localhost:8080/beX0kaEy`

---

### 3. Redis Caching & High-Performance Redirection

URL Shortener systems **Read-Heavy** hote hain (1 URL create hota hai par millions of times click hota hai).

1. **Cache Read (`@Cacheable(value = "urls", key = "#code")`)**:
   * Jab user `GET /{code}` hit karta hai, Spring Boot pehle **Redis** me key `urls::{code}` search karta hai.
   * **Cache Hit**: Agar Redis me mil gaya, toh request database tak jati hi nahi! Direct Redis se original URL milta hai aur browser ko **302 Found** redirect bhej diya jata hai (< 2ms response).
   * **Cache Miss**: Agar Redis me nahi hai, toh PostgreSQL se query hoti hai aur response automatic Redis me cache ho jata hai.
2. **Cache Eviction**:
   * Agar koi user apna URL **Update** ya **Delete** karta hai, toh `ShortUrlService` turant Redis se us code ka cache evict (delete) kar deta hai, taaki stale/purana URL redirect na ho.

---

## 📋 API Endpoints

### 1. URL Shortener APIs

| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :--- |
| `POST` | `/v1/short-urls` | Naya Short URL generate karna | Yes |
| `GET` | `/v1/short-urls/{id}` | ID se Short URL details dekhna | Yes |
| `GET` | `/v1/short-urls` | Sabhi Short URLs dekhna (Pagination) | Yes |
| `GET` | `/v1/short-urls/user/{userId}` | Specific user ke saare URLs dekhna | Yes |
| `PUT` | `/v1/short-urls/{id}` | Existing URL ko update karna (Cache evict hoga) | Yes |
| `DELETE`| `/v1/short-urls/{id}` | URL delete karna (Cache evict hoga) | Yes |

### 2. Public Redirection & Health

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/{code}` | Original Long URL par 302 Redirect karta hai |
| `GET` | `/health` | Service health check |

### 3. Authentication APIs

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/v1/auth/register` | Naya account create karna |
| `POST` | `/v1/auth/login` | Login karke JWT token receive karna |
| `POST` | `/v1/auth/logout` | Token invalidate karke logout karna |

---

## 💻 Sample Requests & Responses

### Short URL Create Request
```http
POST /v1/short-urls
Content-Type: application/json
Authorization: Bearer <YOUR_JWT_TOKEN>

{
  "originalUrl": "https://www.youtube.com/shorts/A8VMlBdsLMA"
}
```

### Response (`201 Created`):
```json
{
  "id": 1,
  "shortCode": "beX0kaEy",
  "originalUrl": "https://www.youtube.com/shorts/A8VMlBdsLMA",
  "shortUrl": "http://localhost:8080/beX0kaEy",
  "clickCount": 0,
  "expiresAt": "2036-11-04 02:30",
  "createdAt": "04 November 2026 02:30",
  "active": true
}
```

### Browser Redirection
Kisi bhi browser ya terminal me hit karein:
```bash
curl -I http://localhost:8080/beX0kaEy
```
Response:
```http
HTTP/1.1 302 Found
Location: https://www.youtube.com/shorts/A8VMlBdsLMA
```

---

## 🏃 Project Run Karne Ka Tarika

### Step 1: Pre-requisites Start Karein (Docker Compose)
App ko run karne ke liye PostgreSQL, Redis, aur ZooKeeper ki zaroorat hoti hai:

```bash
# Sari dependencies background me start karein
docker compose up -d zookeeper redis postgres
```

Status check karein:
```bash
docker compose ps
```

### Step 2: Spring Boot App Run Karein
Apne terminal ya IDE (IntelliJ / Eclipse / STS) se run karein:

```bash
# Terminal se run karne ke liye:
mvn spring-boot:run
```

---

## 🛠 Useful Debugging & Inspection Commands

### 1. Redis Inspect Karna:
```bash
# Redis CLI open karein
redis-cli

# Sabhi cached keys dekhna
KEYS *

# Kisi short code ka cached URL dekhna
GET "urls::beX0kaEy"

# Live traffic monitor karna (Cache hit dekhne ke liye)
MONITOR
```

### 2. ZooKeeper Health Check:
```bash
# Check if ZooKeeper is healthy
echo ruok | nc localhost 2181
# Output aayega: imok

# ZooKeeper status dekhna
echo stat | nc localhost 2181
```

---

## 📁 Project Directory Structure

```text
url-short-service/
├── docker-compose.yml              # ZooKeeper, Redis, Postgres & App container setup
├── Dockerfile                      # Multi-stage production container build
├── pom.xml                         # Maven dependencies (Curator, Redis, Spring Data JPA, JWT)
├── src/
│   ├── main/
│   │   ├── java/com/upendra/url/service/
│   │   │   ├── config/             # ZooKeeperConfig, RedisConfig, SecurityConfiguration, LogoutService
│   │   │   ├── controllers/        # ShortUrlController, FreeController (Redirect), AuthenticationController
│   │   │   ├── dtos/               # Request & Response DTOs
│   │   │   ├── entities/           # ShortUrl, User, Token entities
│   │   │   ├── repositories/       # UrlRepository, UserRepository, TokenRepository
│   │   │   ├── security/           # DistributedIdGenerator, JwtService, JwtAuthenticationFilter
│   │   │   ├── services/           # ShortUrlService, AuthenticationService
│   │   │   └── util/               # Base62Encoder, QrCodeGeneratorService
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-local.properties
└── README.md
```

