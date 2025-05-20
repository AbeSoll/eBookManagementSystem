
PROJECT NAME: E-BOOK MANAGEMENT SYSTEM (ORDERING E-BOOK)

IDE - NETBEANS 8.2 version
SERVER - GlassFish 4.1.1 version
LANGUAGE - JAVA, JAVASCRIPT, CSS
DATABSE - Java DB Derby (need to create database manually)

-There are 2 interfaces which is Admin and user
-Some buttons are clickable only for hyperlink to another page html

====
GROUP NAME : BOOKWORM
====

GROUP MEMBERS:
1. AHMAD SOLEHIN BIN ASMADI (2023479352)
2. NURALYSHA MARDHIAH BINTI MUHAMAD FATHULMUNIR (2023601294)
3. NURSHAZWANI ELLYZA BINTI MOHD NOR (2023867482)
4. MUHAMMAD HAFIZUDDIN BIN RAEMEE (2023874148)

====
USER INTERFACES
====
1. index
2. profile
3. changePassword
4. confirmOrder
5. contact
6. myCart
7. oldBook
8. newBook
9. orderList
10. orderView
11. shippingAddress
12. viewBook

This page will take part if you click login button 

13. contact_2
14. index_2
15. newBook_2
16. oldBook_2
17. shippingAddress_2
18. viewBook_2


====
ADMIN INTERFACES
====
1. adminAddBook
2. adminAllBook
3. adminEditBook
4. adminLogin
5. adminOrderBook
6. adminOrderView
7. adminPanel


====
CREATE DATABASE (Java DB Derby Format)
====

CREATE TABLE "admin" (
    "userName" varchar(25) NOT NULL,
    "password" varchar(100) NOT NULL
);


CREATE TABLE "book" (
  "bookId" INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  "bookName" VARCHAR(100) NOT NULL,
  "authorName" VARCHAR(100) NOT NULL,
  "price" INT NOT NULL,
  "bookCategory" VARCHAR(10) NOT NULL,
  "available" INT NOT NULL,
  "photo" VARCHAR(50) NOT NULL,
  PRIMARY KEY ("bookId")
);


REATE TABLE "forgot" (
  "email" VARCHAR(50) NOT NULL,
  "otp" INT NOT NULL
);


CREATE TABLE "userverifications" (
  "email" VARCHAR(50) NOT NULL,
  "token" INT NOT NULL
);


CREATE TABLE "useraccount" (
  "id" INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  "name" VARCHAR(30) NOT NULL,
  "email" VARCHAR(50) NOT NULL,
  "password" VARCHAR(100) NOT NULL,
  "active" SMALLINT NOT NULL,
  PRIMARY KEY ("id")
);


CREATE TABLE "orderlist" (
  "orderId" INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  "userId" INT NOT NULL,
  "time" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "price" INT NOT NULL,
  "paymentMethod" VARCHAR(10) NOT NULL,
  "status" VARCHAR(10) NOT NULL DEFAULT 'No',
  "name" VARCHAR(30),
  "phone" VARCHAR(20),
  "address1" VARCHAR(30),
  "address2" VARCHAR(30),
  "landmark" VARCHAR(30),
  "city" VARCHAR(30),
  "pincode" VARCHAR(20),
  PRIMARY KEY ("orderId"),
  FOREIGN KEY ("userId") REFERENCES "useraccount" ("id") ON DELETE CASCADE
);


CREATE TABLE "cart" (
  "id" INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  "userId" INT NOT NULL,
  "bookId" INT NOT NULL,
  "quantity" INT NOT NULL DEFAULT 1,
  PRIMARY KEY ("id"),
  FOREIGN KEY ("bookId") REFERENCES "book" ("bookId") ON DELETE CASCADE,
  FOREIGN KEY ("userId") REFERENCES "useraccount" ("id") ON DELETE CASCADE
);


CREATE TABLE "shipping" (
  "id" INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  "userId" INT NOT NULL,
  "name" VARCHAR(30),
  "phone" VARCHAR(20),
  "address1" VARCHAR(30),
  "address2" VARCHAR(30),
  "landmark" VARCHAR(30),
  "city" VARCHAR(30),
  "pincode" VARCHAR(20),
  PRIMARY KEY ("id"),
  FOREIGN KEY ("userId") REFERENCES "useraccount" ("id") ON DELETE CASCADE
);


CREATE TABLE "ordercart" (
  "id" INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  "orderId" INT NOT NULL,
  "bookName" VARCHAR(100) NOT NULL,
  "authorName" VARCHAR(100) NOT NULL,
  "quantity" INT NOT NULL,
  "price" INT NOT NULL,
  PRIMARY KEY ("id"),
  FOREIGN KEY ("orderId") REFERENCES "orderlist" ("orderId") ON DELETE CASCADE
);
