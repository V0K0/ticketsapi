# Tickets api
Сервис для создания заявок по оплате билетов.
# Статус заявки
* **URL** 

  /api/travels/:id
  
* **Method**

  `GET`
  
* **URL params**
  
   **Required:** `id=[int]`
   
* **Successful response**

  * **Code:** 200 <br />
    **Content:** 
    
    ```javascript
    { status : "COMPLETED" }
    ```
    
* **Error response**   

    * **Code:** 404 <br />
 
 # Все заявки пользователя
* **URL** 

  /api/users/:id
  
* **Method**

  `GET`
  
* **URL params**
  
   **Required:** `id=[int]`
   
* **Successful response**

  * **Code:** 200 <br />
    **Content:** 
    
    ```json
    [{
        "id": 22,
        "createdAt": "2020-09-01T16:47:38.579+00:00",
        "updatedAt": "2020-09-01T19:35:13.911+00:00",
        "status": "COMPLETED",
        "ticket": {
            "id": 23,
            "createdAt": "2020-09-01T16:47:38.549+00:00",
            "updatedAt": "2020-09-01T16:47:38.550+00:00",
            "userId": 11,
            "departureTime": "2020-09-23T14:35:00.000+00:00",
            "routeNumber": 113
        }
    }]
    ```
    
    
* **Error response**   

    * **Code:** 404 <br />
    
# Создать новую заявку
* **URL** 

  /api/travels
  
* **Method**

  `POST`
  
* **URL params**
  
   **No parameters** 
   
   
* **Request body**

```json
{
	"userId": 1,
	"routeNumber": 111,
	"departureTime":"2020-10-23T09:35:00-05:00"
}
```

* **Successful response**

  * **Code:** 201 <br />
    **Content:** 
    
    ```json
    {
        "request_id": 25
    }
    ```
    
    
* **Error response**   

    * **Code:** 400 <br />
    
# Получить случайный статус
* **URL** 

 api/statuses/random
  
* **Method**

  `GET`
  
* **URL params**
  
   **No parameters** 
   
* **Successful response**

  * **Code:** 200 <br />
    **Content:** 
    
    ```json
    "COMPLETED"
    ```
    
    
