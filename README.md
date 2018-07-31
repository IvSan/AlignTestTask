# Align Test Task
## Requirements
#### Functional Requirements
Write a simple inventory management system. User should be able to:
+ Enter new product (product details are listed below)
+ Find product by name/brand
+ Update/remove product
+ See all leftovers on a separate page (leftovers means product which quantity is less than 5)  
 
Product details:
+ Name
+ Brand
+ Price
+ Quantity

 It should be two users - admin (can create, add, remove) and user with read-only access.  
 
#### Non-functional requirements
+ All application layers should be covered with tests
+ This system should be written using spring boot
+ All data should be accessed using REST API.
+ Application should be compliant with 12factor apps approach (https://12factor.net)
 
#### Bonus Tasks
 Bonus tasks are optional, but shows more advanced expertise. Feel free to implement any number of those or add and describe something by
 yourself.
+ Export search result as an xls file
+ Application should run in docker container.
+ Consider more complex security mechanism like JWT.
+ Provide virtual configs for 2 envs (staging and prod)
+ Add instrumentation to your code where you think it's reasonable (a way to check knowledge about metrics, logging etc.)
 
Publish results on github. Commit history is desired.
 
## Implementation
#### Rest web service
##### Administrator role IS required
+ **POST** on `/product` to add new product.   
Parameters `name`, `price` and `quantity` are required, parameter `brand` is optional.  
Example: `POST /product?name=pensil&price=0.2&quantity=19&brand=BIC`  
Successfully saved product as the response.
 
+ **PUT** on `/product` to update the product.  
Parameter `id` is required, parameters `name`, `brand`, `price` and `quantity` are optional, only ones to update.  
Example: `PUT /product?id=1&quantity=5`  
Successfully updated product as the response.

+ **DELETE** on `/product` to delete the product.  
Parameter `id` is required.  
Example: `DELETE /product?id=1`  
Status 200 OK as the response.

##### Administrator role is NOT required
+ **GET** on `/product` to find and list products.   
Parameters `name` and `brand` are optional. Case INSENSITIVE.  
Examples: `GET /products`, `GET /products?name=pensil&brand=bic`  
Get without parameters is limited with 1000 results.  

+ **GET** on `/leftovers` to find and list all products which quantity is less than 5.   
No parameters needed.  
Example: `GET /leftovers`
