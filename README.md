# real-estate-mgt

## Note

- Refer to the two `.yaml` files in /src/main/resources for config options
- Install Redis to test the cache: https://redis.io/topics/quickstart

## Changelogs

### 27/12/2021 (Trang)

- All emails are now sent from *eeet2582.realestatemgt@gmail.com*
- Configuration (YAML) files are moved to .gitignore, ask backend team
- You must start both Kafka and Redis servers to run the application

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
