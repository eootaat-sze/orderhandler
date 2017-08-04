CREATE TABLE IF NOT EXISTS Customer(
  id IDENTITY PRIMARY KEY,
  customerName VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  groupName VARCHAR(255) NOT NULL,
  companyName VARCHAR(255) NOT NULL,
  orderDate TIMESTAMP DEFAULT current_timestamp NOT NULL
);

CREATE TABLE IF NOT EXISTS CustomerOrder(
  id IDENTITY PRIMARY KEY,
  customerName VARCHAR(255) NOT NULL,
  groupName VARCHAR(255) NOT NULL,
  companyName VARCHAR(255) NOT NULL,
  sequence VARCHAR(255) NOT NULL,
  scale DECIMAL NOT NULL,
  purification VARCHAR(255) NOT NULL,
  type VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS LaborUser(
  id IDENTITY PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL
);

-- INSERT INTO Customer(id, customerName, password, email, groupName, companyName)
-- VALUES (0, 'testUser', 'aaa', 'a@b.com', 'testGroup','testCompany');

INSERT INTO CustomerOrder
VALUES (0, 'aaa', 'a', 'a', 'asdas12', 1.23, 'valami', 'DNS');
INSERT INTO CustomerOrder
VALUES (1, 'aaa', 'a', 'a', 'asdas12', 1.23, 'valami', 'DNS');
INSERT INTO CustomerOrder
VALUES (2, 'aaa', 'a', 'a', 'asdas12', 1.23, 'valami', 'DNS');
INSERT INTO LaborUser(id, name, email, password)
VALUES (0, 'dante', 'test@test.com', 'dante')