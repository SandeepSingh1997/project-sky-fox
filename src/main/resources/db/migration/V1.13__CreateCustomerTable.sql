CREATE TABLE CUSTOMER
(
    ID       BIGINT GENERATED BY DEFAULT AS IDENTITY,
    NAME     VARCHAR(50) NOT NULL,
    EMAIL    VARCHAR(50) NOT NULL,
    PHONE_NUMBER  VARCHAR(10) NOT NULL,
    USER_ID BIGINT REFERENCES USERTABLE (ID),
    PRIMARY KEY (ID)
);