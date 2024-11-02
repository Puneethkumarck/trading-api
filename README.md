[![Quality gate](https://sonarcloud.io/api/project_badges/quality_gate?project=Puneethkumarck_trading-api)](https://sonarcloud.io/summary/new_code?id=Puneethkumarck_trading-api)
[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-white.svg)](https://sonarcloud.io/summary/new_code?id=Puneethkumarck_trading-api)

[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Puneethkumarck_trading-api&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=Puneethkumarck_trading-api)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Puneethkumarck_trading-api&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=Puneethkumarck_trading-api)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Puneethkumarck_trading-api&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=Puneethkumarck_trading-api)


# Trading API

A Java-based cryptocurrency trading API that provides order book management, limit order processing, and trade history tracking capabilities.

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [API Documentation](#api-documentation)
- [Component Diagrams](#component-diagrams)
- [Sequence Diagrams](#sequence-diagrams)
- [Class Diagrams](#class-diagrams)
- [Setup and Configuration](#setup-and-configuration)
- [Code Quality](#code-quality)

## Overview

The Trading API is a Spring Boot application that implements a cryptocurrency trading platform with support for:
- Order book management
- Limit order processing
- Trade history tracking

## Features

### 1. Order Book Management
- Maintains buy (bid) and sell (ask) orders
- Aggregated view of orders at each price level

### 2. Limit Order Processing
- Support for BUY and SELL orders
- Partial and full order fills

### 3. Trade History
- Real-time trade execution recording
- Historical trade lookup
- Trade aggregation by currency pair
- Pagination support

### 4. Additional Features
- Idempotent order submission
- Concurrency handling
- Input validation
- Error handling with appropriate HTTP status codes
- API documentation with OpenAPI/Swagger

## Technology Stack

- **Framework**: Spring Boot 3.3.4
- **Language**: Java 21
- **Build Tool**: Gradle
- **Testing**: JUnit 5, Mockito
- **Documentation**: OpenAPI/Swagger
- **Code Quality**:
    - SonarCloud
    - JaCoCo for code coverage
    - Spotless for code formatting
- **Libraries**:
    - MapStruct for object mapping
    - Jackson for JSON processing
    - Lombok for boilerplate reduction
    - Spring Validation

## Architecture

The application follows a hexagonal (ports and adapters) architecture pattern with the following layers:

1. **API Layer** (Application)
    - Controllers
    - DTOs
    - Mappers
    - Exception Handlers

2. **Domain Layer**
    - Order Book Logic
    - Limit Order Processing
    - Trade Management
    - Domain Models

3. **Infrastructure Layer**
    - Repositories
    - Service Adapters
    - Entity Classes

### Component Diagram

```mermaid
graph LR
%% Styling
    classDef applicationBlock fill:#e1f5fe,stroke:#01579b,stroke-width:2px
    classDef domainBlock fill:#f3e5f5,stroke:#4a148c,stroke-width:2px
    classDef infrastructureBlock fill:#e8f5e9,stroke:#1b5e20,stroke-width:2px
    classDef controller fill:#bbdefb,stroke:#1976d2,stroke-width:1px
    classDef service fill:#e1bee7,stroke:#7b1fa2,stroke-width:1px
    classDef repository fill:#c8e6c9,stroke:#388e3c,stroke-width:1px

%% Application Layer Block
    subgraph ApplicationLayer[" Application Layer "]
        direction TB
        subgraph Controllers[" Controllers "]
            LC[LimitOrderController]
            OBC[OrderBookController]
            THC[TradeHistoryController]
        end

        subgraph Mappers[" Mappers "]
            LODM[LimitOrderDtoMapper]
            OBDM[OrderBookDtoMapper]
            THDM[TradeHistoryDtoMapper]
        end
    end

%% Domain Layer Block
    subgraph DomainLayer[" Domain Layer "]
        direction TB
        subgraph CoreServices[" Core Services "]
            LOCH[LimitOrderCommandHandler]
            OP[OrderProcessor]
            OBQH[OrderBookQueryHandler]
            THQH[TradeHistoryQueryHandler]
        end

        subgraph DomainModel[" Domain Model "]
            OB[OrderBook]
            LO[LimitOrder]
            TH[Trade]
        end

        subgraph Validators[" Validators "]
            OBIV[OrderBookIdemPotencyValidator]
        end
    end

%% Infrastructure Layer Block
    subgraph InfrastructureLayer[" Infrastructure Layer "]
        direction TB
        subgraph Adaptors[" Repository Adaptors "]
            OBA[OrderBookRepositoryAdaptor]
            LOA[LimitOrderRepositoryAdaptor]
            THA[TradeHistoryRepositoryAdaptor]
        end

        subgraph Storage[" Storage "]
            IMO[InMemoryOrderRepository]
            IMOB[InMemoryOrderBookRepository]
            IMT[InMemoryTradeRepository]
        end
    end

%% Relationships - Application to Domain
    LC -->|"Maps DTO"| LODM
    LODM -->|"Creates Command"| LOCH
    OBC -->|"Queries"| OBQH
    THC -->|"Queries"| THQH

%% Relationships - Domain Internal
    LOCH -->|"Uses"| OP
    LOCH -->|"Validates"| OBIV
    OP -->|"Manages"| OB
    OBQH -->|"Reads"| OB
    THQH -->|"Reads"| TH

%% Relationships - Domain to Infrastructure
    OP -->|"Persists"| OBA
    OP -->|"Records"| THA
    OBIV -->|"Checks"| LOA

%% Relationships - Infrastructure Internal
    OBA -->|"Stores"| IMOB
    LOA -->|"Stores"| IMO
    THA -->|"Stores"| IMT

%% Apply styles
    class ApplicationLayer applicationBlock
    class DomainLayer domainBlock
    class InfrastructureLayer infrastructureBlock

    class LC,OBC,THC controller
    class LOCH,OP,OBQH,THQH service
    class IMO,IMOB,IMT repository

%% Styling for subgraphs
    style Controllers fill:#e3f2fd,stroke:#1565c0
    style Mappers fill:#e3f2fd,stroke:#1565c0
    style CoreServices fill:#f3e5f5,stroke:#6a1b9a
    style DomainModel fill:#f3e5f5,stroke:#6a1b9a
    style Validators fill:#f3e5f5,stroke:#6a1b9a
    style Adaptors fill:#e8f5e9,stroke:#2e7d32
    style Storage fill:#e8f5e9,stroke:#2e7d32

%% Layer Labels
    style ApplicationLayer fill:none,stroke:#01579b,stroke-width:4px
    style DomainLayer fill:none,stroke:#4a148c,stroke-width:4px
    style InfrastructureLayer fill:none,stroke:#1b5e20,stroke-width:4px
```


### Class Diagrams

#### Order Book Classes

```mermaid
classDiagram
    class OrderBook {
        +String currencyPair
        +TreeMap<BigDecimal, OrderBookLevel> asks
        +TreeMap<BigDecimal, OrderBookLevel> bids
        +Instant lastChange
        +Long sequenceNumber
    }

    class OrderBookLevel {
        +OrderBookSide side
        +BigDecimal quantity
        +BigDecimal price
        +String currencyPair
        +int orderCount
    }

    class OrderBookRepository {
        <<interface>>
        +findByCurrencyPair(String): Optional<OrderBook>
        +save(OrderBook): Optional<OrderBook>
    }

    OrderBook --> OrderBookLevel
    OrderBookRepository --> OrderBook
```

#### Limit Order Classes

```mermaid
classDiagram
    class LimitOrderCommand {
        +LimitOrder limitOrder
        +String customerOrderId
    }

    class LimitOrder {
        +OrderBookSide side
        +BigDecimal quantity
        +BigDecimal price
        +String currencyPair
        +OrderStatus status
    }

    class OrderProcessor {
        <<abstract>>
        #processOrder()
        #matchOrders()
        #placeOrder()
    }

    class BuyOrderProcessor {
        +getMatchingSide()
        +getPlacementSide()
        +canMatch()
    }

    class SellOrderProcessor {
        +getMatchingSide()
        +getPlacementSide()
        +canMatch()
    }

    LimitOrderCommand --> LimitOrder
    OrderProcessor <|-- BuyOrderProcessor
    OrderProcessor <|-- SellOrderProcessor
```

### Sequence Diagrams

#### Place Limit Order

```mermaid
sequenceDiagram
    participant C as Client
    participant LC as LimitOrderController
    participant LH as LimitOrderCommandHandler
    participant IV as IdemPotencyValidator
    participant OP as OrderProcessor
    participant OR as OrderRepository
    participant TR as TradeRepository

    C->>LC: POST /api/v1/orders
    LC->>LH: handle(command)
    LH->>IV: validate(command)
    IV->>OR: findByOrderId()
    IV->>OR: save(command)
    LH->>OP: processOrder()
    
    alt Order Matches
        OP->>TR: save(trade)
    else No Match
        OP->>OR: save(orderBook)
    end
    
    LC-->>C: OrderResponse
```

#### Get Order Book

```mermaid
sequenceDiagram
    participant C as Client
    participant OC as OrderBookController
    participant OH as OrderBookQueryHandler
    participant OR as OrderBookRepository
    
    C->>OC: GET /api/v1/orders/{pair}
    OC->>OH: getOrderBook(pair)
    OH->>OR: findByCurrencyPair(pair)
    
    alt Order Book Found
        OR-->>OH: OrderBook
        OH-->>OC: OrderBook
        OC-->>C: OrderBookResponse
    else Not Found
        OR-->>OH: Empty
        OH-->>OC: OrderBookNotFoundException
        OC-->>C: 404 Not Found
    end
```

#### Get Trade History

```mermaid
sequenceDiagram
    participant C as Client
    participant TC as TradeHistoryController
    participant TH as TradeHistoryQueryHandler
    participant TR as TradeHistoryRepository
    
    C->>TC: GET /api/v1/trades/{pair}/history
    TC->>TH: getTradeHistory(pair, limit)
    TH->>TR: findRecentTradesByCurrencyPair(pair, limit)
    TR-->>TH: List<Trade>
    TH-->>TC: List<Trade>
    TC-->>C: List<TradeHistoryResponse>
```

## Setup and Configuration

### Prerequisites
- Java 21
- Gradle 8.x

### Build and Run
```bash
# Build the project
./gradlew clean build
```

```bash
# Run tests
./gradlew test
```

```bash
# Run the application
./gradlew bootRun
```

### API Documentation
The API documentation is available at : [swagger](http://localhost:8080/swagger-ui.html)

### Code Style
The project uses Spotless with Eclipse formatter for consistent code style. Format the code using:
```bash
./gradlew spotlessApply
```