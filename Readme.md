 Order Service (Ecommerce Microservices)

The **Order Service** manages **order creation, order retrieval, and payment status tracking** in the Ecommerce Microservices system.

It communicates with **Product Service** and **Payment Service** using **Kafka (event-driven communication)** to create order summaries and handle asynchronous payment results.

---

#  Tech Stack

* **Spring Boot**
* **Kafka (Producer & Consumer)**
* **Microservices Architecture**
* **REST APIs**
* **API Gateway Integration**
* **MySQL (Persistence)**
* **Maven**
* **Lombok**

---

#  Features Implemented

###  **Create Order (Order Summary)**

* Accepts product IDs from frontend
* Publishes order request to Kafka
* Waits for product service to process items

### **Asynchronous Payment Result Handling**

* Receives payment result from Payment Service via Kafka
* Temporarily caches payment responses
* Client polls using `requestId`

###  **User Order History**

* Fetches all orders for a specific user
* Secured using user identity headers

---

#  API Endpoints

All endpoints are exposed through the **API Gateway** under the `/Order` path.

---

##  Create Order Summary

**POST** `/Order/Order-Summary`

Initiates order creation by sending product IDs to Product Service via Kafka.

###  Headers

```
X-EMAIL: user@example.com
X-UserId: 101
```

###  Request Body

```json
{
  "items": [
    {
      "id": 1,
      "quantity": 5
    }
    
  ],
  "requestId": "abc123xyz"
 

}

```

###  Response

```json
{
  "requestId": "abc123xyz"
}
```

---

##  Get Payment Result

**GET** `/Order/pay-result/{requestId}`

Fetches payment result asynchronously from Payment Service using `requestId`.

###  Path Variable

```
requestId = abc123xyz
```


```

###  Error Scenario

* Throws error if Payment Service does not respond within retry window

---

##  Get User Orders

**GET** `/Order/GetOrder`

Returns all orders placed by the authenticated user.

###  Header

```
X-UserId: 101
```

###  Response

```json
[
  {
    "orderId": "ORD_123",
    "totalAmount": 2499,
    "status": "SUCCESS"
  }
]
```

---

#  Kafka Communication Flow

###  Produced Events

* **ProductRequestEvent**

  * Contains product IDs, user ID, email, request ID
  * Sent to Product Service for price & item validation

###  Consumed Events

* **Product Response Event**

  * Receives enriched product details
* **Payment Response Event**

  * Receives payment success/failure response
  * Cached temporarily for polling via REST API

---

#  Order Processing Flow

```

```

---

#  Future Enhancements (To Be Implemented)

###  Order Cancellation

Allow users to cancel orders before shipment.

###  Order Status Tracking

Real-time order status updates.

###  Retry & Dead Letter Queue

Handle Kafka failures more gracefully.

###  Admin Order Dashboard

View and manage all orders.

---

##  Author

**Pranav Sharma**
Spring Boot | Kafka | Microservices | Backend Engineering

---
