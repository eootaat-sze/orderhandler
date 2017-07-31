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
-- VALUES (0, "testUser", "testUser", "testUser@a.com", "testGroup","testCompany");
INSERT INTO LaborUser(id, name, email, password)
VALUES (0, 'dante', 'test@test.com', 'dante')