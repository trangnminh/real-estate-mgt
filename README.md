# real-estate-mgt

## Changelogs
### 4/12/2021 (Trang)
- Changed folder structure from class-based to function-based
> It is not necessary to create a Service for each Entity. A Service can wire multiple Repositories. This reduces the number of Services and makes cascading (for example delete all dependent Rentals when a User is deleted) easier. "Important" entities (User, House, Rental) have their own Service classes.
- Added 5 rows of mock data for all classes, relationships included
- Wrote parsers to parse date and time from strings
- Wrote a parser to deserialize field "rental" (actually an ID string) in `payment.json` into nested Rental objects
- Fixed infinite recursion when using GET requests for Rental and Payment due to them referencing each other
- Added manual cascading when deleting User and House: all dependent Deposits, Meetings, and Rentals will be deleted
- Added orphan removal when deleting Rental: all dependent Payments will be deleted

### 08/12/2021 (Phuong)
- Added upload images using S3 bucket
- Change type of attribute `description` in class House
- when you need to run along with S3 bucket, please ask me about the secret key and access key
- When you need front end for fetching image API for posting houses, drop me text, and I will send you 
- put this line in `application.properties`:
`spring.servlet.multipart.max-file-size=100MB`
`spring.servlet.multipart.max-request-size=100MB`
> In `Postman` for POST request: choose `form-data`:
`key` for uploading image is `files`, then select type `file` in the same field. It would automatically change the value to `select files` (you can select one or multiple file, but the type is always image). Then put another field like `name`, the default type of `key` would be `text`.


## Important note:
- Before publishing to GitHub, please run the command line in terminal: `git rm -r --cached src/main/resources/application.properties` and put `src/main/resources/application.properties` in `.gitignore` --> otherwise, people would know my Amazon key and can take advantage of it

## Resources
### Lucid Chart
https://lucid.app/lucidchart/8ef05006-9512-4bab-966d-4d2ce853648d/edit?viewport_loc=-284%2C-7%2C2368%2C1080%2COIuwwrBRowku&invitationId=inv_b1fe5bc2-9d99-4deb-97a5-252d99acfae7
