# real-estate-mgt

## Note
### **FRONTEND REPOSITORY**
https://github.com/phu0n9/real-estate

### **INSTRUCTION**
- You must start both Kafka and Redis servers to run the application
- Install Redis to test the cache: https://redis.io/topics/quickstart
- Install Kafka to test the message queue and email: https://kafka.apache.org/quickstart
- Get the `application.yaml` and `redisson.yaml` files and put them in `src/resources`
- Get the `.env` file and put it in the root folder (same level as `README.md`)
- Get the zip file and put the `house` folder inside `data` folder

### **HOUSE QUERY FORMAT (POSTMAN)**
- All variables are not required (use defaults if null)
- Sort and order by any House property (name, address etc.)
- Page request starts at 1, change with `pageNo`
- Default page size is 20, change with `pageSize`
- House price is generated within 100K - 300K
- House "type" and "status" properties are generated from below lists
```
const typeList = ["apartment", "serviced", "street"];
const statusList = ["available", "reserved", "rented"];
```
- Cache size is 1000 houses a batch
- Cache lives for 10 minutes (6 minutes if no request)
- When you debug, try flushing the cache first by running `redis-cli flushdb` in Terminal
#### By Price Range
- Search by params `low` and `high` (if not provided, default to 100K and 300K)
- Default sort and order is "price" descending (most expensive first)

> localhost:8080/api/v1/houses/search/byPriceBetween?pageNo=1&pageSize=20&low=199000&high=200000&sortBy=price&orderBy=desc

#### By Query

- Search by params `query` (match name, address and description), default is empty (match all)
- Default sort and order is "name" ascending (alphabetical order)

> localhost:8080/api/v1/houses/search?pageNo=1&query=Sunt ullamco labore&sortBy=name&orderBy=asc

## Changelogs

### 06/01/2022 (Phuong)

- Implement new cleaner Kafka version for serializer
- Add AWS key configuration
- application.yaml and back-end.env are changed, get it through
  here: https://docs.google.com/document/d/1LAGecNNdEr2tOjjm9eEe6AdkPSyLVQckuLSNnFtcTkQ/edit?usp=sharing

### 05/01/2022 (Phuong)

- Fix admin authentication, change .env file
- Added CORS in each controller

### 05/01/2022 (Trang)

- Fix out of bound errors for House pagination

### 02/01/2022 (Phuong)

- Finished Oauth2 using Auth0 and comment out all controllers endpoint
- Go to front-end repository to get access token key
- `Bugs`: Post Users endpoint --> don't test this
- Add separate `POST` and `PUT` requests using `requestBody` except for class `Meeting`

### 27/12/2021 (Trang)

- All emails are now sent from *eeet2582.realestatemgt@gmail.com*
- Configuration (YAML) files are moved to .gitignore, ask backend team
- Reduce HOUSE_BATCH_SIZE to 200, queries feel faster now (cache limit is still 1000)

### 25/12/2021 (Phuong)

- Finish KafKa for booking a meeting
- `Note`: Turn off any anti-virus for sending email

### 23/12/2021 (Trang)

- Finish Redis cache for House (get + pagination)

### 23/12/2021 (Trang)

- Generate 1M rows for House (50K rows x 20 files)
- Basic cache for House (getOne, upsert, delete)
- House objects live 20 minutes after the first time queried
- Add house data to your local machine by:
  * Download the zip file:
    * https://drive.google.com/file/d/1zK0-4ja-Lt4fnUjIQiWPpOWpa6Zz4qfy/view?usp=sharing
  * Extract `/house` folder and put the folder inside `/src/data`

### 21/12/2021 (Phuong)

- House update and tested
- How to use Postman for House
  API: https://docs.google.com/document/d/1xA9813h3P2GNO4l_WNfn-KGI0xK8Q-DQnUJFLb_P2gs/edit?usp=sharing

### 19/12/2021 (Trang)

- All endpoints for Deposit and Meeting work (get all, get one, get by userId or houseId, upsert, delete)
- Add more endpoints for Rental to be searched by userId or houseId

### 17/12/2021 (Phuong)

- Can update image in s3 by delete and upload more into the same folder
- Search/sort/pagination with name, type and price

### 17/12/2021 (Trang)

- All endpoints for Rental and Payment work (get all, get one, get and upsert by rentalId, add, delete, update, sort &
  pagination)
- **Weird POSTMAN issue:** after posting a new payment, getting all payments will show an ID and not the new Payment
  object; querying the new payment by ID and Rental still work as expected

### 16/12/2021 (Trang)

- All endpoints for AppUser work (get all, get one, add, delete, update, search, sort & pagination)
- All dates must use "yyyy-MM-dd" and all times must use "HH:mm"
- Some endpoints in UserController have been deleted, please check them out

### 16/12/2021 (Phuong)

- Added upload images using S3 bucket
- Change type of attribute `description` in class House
- Link to image url: https://realestatemgt.s3.ap-southeast-1.amazonaws.com/dataset/1/1_bathroom.jpg

> where 1 is the row id

- Pagination and sort with house and users, search by name with `house` and search by name/phone/email with `user`

### 4/12/2021 (Trang)

- Changed folder structure from class-based to function-based

> It is not necessary to create a Service for each Entity. A Service can wire multiple Repositories. This reduces the number of Services and makes cascading (for example delete all dependent Rentals when a User is deleted) easier. "Important" entities (User, House, Rental) have their own Service classes.

- Added 5 rows of mock data for all classes, relationships included
- Wrote parsers to parse date and time from strings
- Wrote a parser to deserialize field "rental" (actually an ID string) in `payment.json` into nested Rental objects
- Fixed infinite recursion when using GET requests for Rental and Payment due to them referencing each other
- Added manual cascading when deleting User and House: all dependent Deposits, Meetings, and Rentals will be deleted
- Added orphan removal when deleting Rental: all dependent Payments will be deleted

## Resources

### Lucid Chart

https://lucid.app/lucidchart/8ef05006-9512-4bab-966d-4d2ce853648d/edit?viewport_loc=-284%2C-7%2C2368%2C1080%2COIuwwrBRowku&invitationId=inv_b1fe5bc2-9d99-4deb-97a5-252d99acfae7
