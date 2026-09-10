# E-Commerce Backend

A modular monolithic e-commerce backend,.

This repository is the **minimal starting point** — enough for everyone to clone, connect to a
database, and run tests, and nothing else. There is no User, Product, or Cart logic in here.
The guiding rule for everything in this repo, and everything added to it later, is:

> Introduce a technology or abstraction when we encounter the real problem it solves —
> not because it's common in industry.

---

## 1. Technology (what's actually used right now)

- Java 17
- Spring Boot 3.3.x
- Maven (with wrapper — no local Maven install required)
- Spring Web, Spring Data JPA, Hibernate
- MySQL 8 (connected via env vars — see §6)
- Spring Boot Validation
- JUnit 5, Mockito, Spring Boot Test
- Lombok

That's the whole list. No Docker, no CI/CD, no Flyway/Liquibase, no Spring Security/JWT, no Redis,
no Kafka, no microservices. Not because those are bad tools — because none of them solve a problem
this project has yet. See §8 for what will trigger adding each one.

---

## 2. Architecture (future — not implemented yet)

Every feature will eventually live in its own top-level package, structured the same way:

```
com.example.ecommerce
│
├── user/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── entity/
│   └── dto/
│
├── product/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── entity/
│   └── dto/
│
├── cart/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── entity/
│   └── dto/
│
├── common/
│   ├── exception/
│   └── response/
│
└── EcommerceApplication.java
```

Later, the same pattern extends to `order/`, `category/`, `inventory/`, `coupon/`, `wishlist/`,
`payment/`, `auth/`, `role/`.

**None of `user/`, `product/`, `cart/`, or anything else above `common/` exists in this repository
yet.** They're documented here so every developer builds the same shape when they start, but
nothing is scaffolded — no empty packages, no placeholder classes. Git doesn't track empty
directories anyway, and a placeholder `User.java` or `ProductController.java` would just be dead
code someone has to remember to delete.

### What each layer is responsible for

| Layer | Responsibility |
|---|---|
| `controller` | Accepts HTTP requests, validates input shape, delegates to `service`, returns a response. No business logic. |
| `service` | Business logic and rules for that feature. Talks to `repository` and to other features' services when it needs their data. |
| `repository` | Spring Data JPA interfaces — persistence only, no business rules. |
| `entity` | JPA-mapped domain objects for that feature. |
| `dto` | Request/response shapes the feature's controller exposes. Feature-local, not global — see below. |

DTOs stay inside their own feature (`product/dto/`, not a shared `dto/` package) so that opening
one feature's folder shows everything about it, and two developers working on different features
never collide in the same package.

---

## 3. What's actually in this repository

```
ecommerce-backend/
├── .mvn/wrapper/
├── src/
│   ├── main/
│   │   ├── java/com/example/ecommerce/
│   │   │   ├── EcommerceApplication.java
│   │   │   └── common/
│   │   │       ├── exception/GlobalExceptionHandler.java
│   │   │       └── response/ApiErrorResponse.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/example/ecommerce/EcommerceApplicationTests.java
├── .gitignore
├── mvnw / mvnw.cmd
├── pom.xml
└── README.md
```

### `common/` — the only shared infrastructure

`GlobalExceptionHandler` and `ApiErrorResponse` exist because **every** future controller needs
consistent error handling, not because "every project has one." They produce this shape for any
unhandled or validation exception:

```json
{
  "timestamp": "2026-08-22T10:15:30Z",
  "status": 400,
  "error": "Bad Request",
  "message": "quantity: must be positive",
  "path": "/api/products"
}
```

**What does not belong in `common/`:** `ProductNotFoundException`, `CartNotFoundException`,
`InsufficientStockException`, `OrderNotFoundException` — these encode one feature's business
rules and get created by whoever implements that feature, inside that feature's own package, when
it's actually needed. Don't pre-create them here.

---

## 4. Feature ownership

```
Developer 1 → user      (User, Role, Authentication — later)
Developer 2 → product   (Category, Product, Inventory — later)
Developer 3 → cart      (Cart, CartItem — later)
```

Ownership expands as the team takes on `order`, `coupon`, `wishlist`, `payment`. To start a
feature, create `<feature>/{controller,service,repository,entity,dto}` under
`com.example.ecommerce`, matching the shape in §2 — nothing else in the repo needs to change to
do that.

---

## 5. Testing strategy

**Unit tests** — one class, dependencies mocked with Mockito. This is what lets a developer test
their feature before a feature it depends on is finished:

```
CartService
  ↓
mock ProductService / ProductRepository   (Developer 2's code — may not exist yet)
mock UserRepository                        (Developer 1's code — may not exist yet)
```

Developer 3 can write and pass this test against agreed-upon interfaces, without Product or User
being implemented at all.

**Integration tests** — real Spring components together, e.g. `UserService → UserRepository →
MySQL`, verifying they actually work as wired, not just in isolation.

**API/system tests** — a full HTTP request through `Controller → Service → Repository →
Database`, verifying the whole path end to end.

**What's here now:** one test, `EcommerceApplicationTests#contextLoads`, which proves the Spring
application context starts successfully. Because there's no in-memory database configured, this
test requires `DB_URL`/`DB_USERNAME`/`DB_PASSWORD` to point at a real, reachable MySQL instance —
that's intentional at this stage: the point of this test is proving the app can actually connect
to a real database, not working around not having one. Feature-level unit, integration, and API
tests get added by whoever implements that feature.

---

## 6. Development setup

1. Clone the repository.
2. Have a MySQL instance available (create a database for the app to connect to).
3. Set `DB_URL`, e.g. `jdbc:mysql://localhost:3306/ecommerce`.
4. Set `DB_USERNAME`.
5. Set `DB_PASSWORD`.
6. Run the application: `./mvnw spring-boot:run`
7. Run tests: `./mvnw clean test`

No `.env` file or Docker setup is provided — see §8 for when that gets added.

---

## 7. Git

This repository intentionally does not include a `.git` directory, branches, or CI/CD — only a
`.gitignore`. Initialize Git yourselves however the team wants to structure branches and PRs; that
process isn't dictated by this starting point.

---

## 8. When to introduce what (roadmap, not implemented)

| Problem we'll eventually hit | Likely solution |
|---|---|
| Schema needs to evolve safely once real data exists | Flyway |
| "Works on my machine" / inconsistent local setup | Docker |
| Need to authenticate and authorize users | Spring Security + JWT |
| Something gets read far more than it changes and it's slow | Redis (caching) |
| Work needs to happen async / decoupled from the request | A messaging technology |
| PRs need automated build/test verification | GitHub Actions |
| Concurrent stock updates cause inconsistency | A locking/transaction strategy |

None of these are needed yet. Add them when the corresponding problem is real, not preemptively —
and expect that decision to be made deliberately by the team, in review, not slipped in solo.

---

## 9. Full domain roadmap (not implemented)

Users, Roles, Authentication, Products, Categories, Cart, CartItems, Orders, OrderItems, Coupons,
Wishlist, Payment simulation, Inventory, plus pagination/filtering/sorting on list endpoints.
All future work — none of it exists in this repository yet.
