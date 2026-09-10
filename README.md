# 📋 Todo App — Fullstack приложение на Java

Учебный проект, демонстрирующий эволюцию Java-веб-разработки: от чистого `HttpServer` до полноценного Fullstack-приложения на **Spring Boot + React** с **JWT-авторизацией**, **PostgreSQL** и **Docker**.

---

## 🎯 О проекте

**Todo-менеджер** — приложение для управления задачами с регистрацией пользователей. Каждый пользователь видит только свои задачи.

### Функционал

- ✅ Регистрация и логин (JWT)
- ✅ Создание, просмотр, удаление задач
- ✅ Отметка задач как выполненных
- ✅ Фильтрация (все / активные / выполненные)
- ✅ Привязка задач к пользователю
- ✅ Данные сохраняются в PostgreSQL

---

## 🛠️ Технологии

### Backend
- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Security** + **JWT** (jjwt)
- **Spring Data JPA** + **Hibernate**
- **PostgreSQL 15**
- **Maven**
- **JUnit 5** + **Mockito**

### Frontend
- **React 18**
- **Vite**
- **CSS3**

### DevOps
- **Docker** + **Docker Compose**

---

## 📁 Структура проекта

```
todo-app/
├── backend/                          # Spring Boot приложение
│   ├── src/
│   │   ├── main/java/com/todo/
│   │   │   ├── TodoApplication.java
│   │   │   ├── config/               # Security, JWT, CORS
│   │   │   ├── controller/           # REST API
│   │   │   ├── model/                # Сущности (Todo, User)
│   │   │   ├── repository/           # JPA репозитории
│   │   │   └── service/              # Бизнес-логика
│   │   └── main/resources/
│   │       └── application.properties
│   ├── src/test/java/com/todo/       # Тесты
│   ├── Dockerfile
│   └── pom.xml
├── frontend/                         # React приложение
│   ├── src/
│   │   ├── api.js                    # Запросы к бэкенду
│   │   ├── App.jsx
│   │   ├── Login.jsx
│   │   ├── Register.jsx
│   │   ├── TodoList.jsx
│   │   └── TodoForm.jsx
│   ├── Dockerfile
│   └── package.json
├── docker-compose.yml
├── .gitignore
└── README.md
```

---

## 🚀 Быстрый старт

### Вариант 1: Docker (рекомендуется)

```bash
# Клонировать репозиторий
git clone https://github.com/your-username/todo-app.git
cd todo-app

# Запустить всё одной командой
docker-compose up -d --build

# Открыть в браузере
# http://localhost:3000
```

### Вариант 2: Локальный запуск

#### Требования
- Java 17+
- Node.js 20+
- PostgreSQL 15+ (или Docker)
- Maven

#### 1. Запустить PostgreSQL

```bash
docker run --name todo-postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_DB=todoapp \
  -p 5432:5432 \
  -d postgres:15
```

#### 2. Запустить бэкенд

```bash
cd backend
mvn spring-boot:run
```

Бэкенд будет доступен на `http://localhost:8080`.

#### 3. Запустить фронтенд

```bash
cd frontend
npm install
npm run dev
```

Фронтенд будет доступен на `http://localhost:3000`.

---

## 🔌 REST API

### Аутентификация

| Метод | URL | Описание |
|-------|-----|----------|
| `POST` | `/api/auth/register` | Регистрация |
| `POST` | `/api/auth/login` | Логин (возвращает JWT) |

**Пример регистрации:**

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"test","email":"test@mail.com","password":"test123"}'
```

**Пример логина:**

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"test123"}'
```

Ответ:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "username": "test"
}
```

### Задачи (требуют JWT)

| Метод | URL | Описание |
|-------|-----|----------|
| `GET` | `/api/todos` | Все задачи пользователя |
| `GET` | `/api/todos?filter=active` | Активные задачи |
| `GET` | `/api/todos?filter=completed` | Выполненные задачи |
| `POST` | `/api/todos` | Создать задачу |
| `PUT` | `/api/todos/{id}/toggle` | Переключить статус |
| `DELETE` | `/api/todos/{id}` | Удалить задачу |

**Пример запроса с токеном:**

```bash
curl http://localhost:8080/api/todos \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 🧪 Тестирование

```bash
cd backend
mvn test
```

Проект содержит:

- **Unit-тесты** для сервисов (`TodoServiceTest` — Mockito)
- **Интеграционные тесты** для контроллеров (`TodoRestControllerTest` — MockMvc)
- **Тесты репозиториев** (`TodoRepositoryTest` — H2)
- **Тест загрузки контекста** (`TodoApplicationTests`)

---

## 📚 История версий

Проект создавался поэтапно для изучения Java-веб-разработки. Каждая версия — отдельная ветка.

| Версия | Ветка | Технологии | Что изучено |
|--------|-------|-----------|-------------|
| **v1** | `v1-java-http` | Java + HttpServer | Основы HTTP, сокеты |
| **v2** | `v2-servlets` | Servlets + JSP + Tomcat | Java-веб, MVC |
| **v3** | `v3-spring-mvc` | Spring MVC + JSP | DI, контроллеры |
| **v4** | `v4-spring-boot` | Spring Boot + Thymeleaf | Автоконфигурация |
| **v5** | `v5-spring-boot-react` | Spring Boot + React | REST API, SPA |
| **v6** | `v6-database-security` | JWT + H2 + JPA | Безопасность, БД |
| **v7** | `v7-postgres-docker` | PostgreSQL + Docker | Контейнеризация базы данных |
| **v8** | `v8-docker-compose` | Docker Compose | Полная контейнеризация |
| **v9** | `v8-tests` | JUnit + Mockito | Тестирование |

### Переключение между версиями

```bash
git checkout v1-java-http
git checkout v8-tests
```

---

## 🔐 Безопасность

- Пароли хешируются с **BCrypt**
- Аутентификация через **JWT токены**
- Токен хранится в `localStorage` на фронтенде
- Каждый запрос к API проверяется фильтром `JwtRequestFilter`
- **CORS** настроен для `http://localhost:3000`

---

## 🗄️ База данных

### Схема

**Таблица `users`:**
| Колонка | Тип | Описание |
|---------|-----|----------|
| id | BIGINT | Первичный ключ |
| username | VARCHAR(20) | Уникальный логин |
| password | VARCHAR(100) | Хеш пароля (BCrypt) |
| email | VARCHAR(255) | Уникальный email |
| role | VARCHAR(255) | Роль пользователя |

**Таблица `todos`:**
| Колонка | Тип | Описание |
|---------|-----|----------|
| id | BIGINT | Первичный ключ |
| title | VARCHAR(100) | Название задачи |
| description | VARCHAR(500) | Описание |
| completed | BOOLEAN | Статус выполнения |
| created_at | TIMESTAMP | Дата создания |
| updated_at | TIMESTAMP | Дата обновления |
| user_id | BIGINT | Внешний ключ на users |

---

## 🎓 Чему учит проект

- ✅ Основы HTTP и клиент-серверного взаимодействия
- ✅ Java Servlets и JSP
- ✅ Spring MVC, DI, IoC
- ✅ Spring Boot и автоконфигурация
- ✅ REST API и JSON
- ✅ React и работа с API через `fetch`
- ✅ JWT авторизация
- ✅ JPA/Hibernate и работа с БД
- ✅ Docker и контейнеризация
- ✅ Модульное и интеграционное тестирование
