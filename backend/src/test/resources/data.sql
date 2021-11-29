-- Insert into User Table
INSERT INTO tbl_user
(created_at, updated_at, email, first_name, last_name)
SELECT now(), now(), 'nwabudoemmanuel@gmail.com', 'Emmanuel', 'Nwabudo'
WHERE NOT EXISTS (
        SELECT email FROM tbl_user WHERE email = 'nwabudoemmanuel@gmail.com'
    );

-- Insert into Property Table
INSERT INTO tbl_property
(created_at, updated_at, property_code, property_value)
SELECT now(), now(), 'user.book.limit', '2'
WHERE NOT EXISTS (
        SELECT property_code FROM tbl_property WHERE property_code = 'user.book.limit'
    );


-- Insert into Book Table
insert into tbl_book
(created_at, updated_at, author_name, bookisbncode, book_image_url, book_title, quantity)
SELECT now(), now(), 'Emmanuel Nwabudo', 'ISBN345872JA', '', 'Building APIs: Know How', 2
WHERE NOT EXISTS (
        SELECT bookisbncode FROM tbl_book WHERE bookisbncode = 'ISBN345872JA'
    );

insert into tbl_book
(created_at, updated_at, author_name, bookisbncode, book_image_url, book_title, quantity)
select
    now(), now(), 'Ismet Baruah', 'ISBN3ERT6783TA', '', 'Effective Human Resourcing', 1
WHERE NOT EXISTS (
        SELECT bookisbncode FROM tbl_book WHERE bookisbncode = 'ISBN3ERT6783TA'
    );

insert into tbl_book
(created_at, updated_at, author_name, bookisbncode, book_image_url, book_title, quantity)
select
    now(), now(), 'Oluwaseun Popoola', 'ISBN4523T72JT', '', 'Versioning APIs: Know How', 5
WHERE NOT EXISTS (
        SELECT bookisbncode FROM tbl_book WHERE bookisbncode = 'ISBN4523T72JT'
    );


insert into tbl_book
(created_at, updated_at, author_name, bookisbncode, book_image_url, book_title, quantity)
select
    now(), now(), 'John Nwabudo', 'ISBN56O3O22JA', '', 'Selling APIs: Perks of the Trade', 10
WHERE NOT EXISTS (
        SELECT bookisbncode FROM tbl_book WHERE bookisbncode = 'ISBN56O3O22JA'
    );

insert into tbl_book
(created_at, updated_at, author_name, bookisbncode, book_image_url, book_title, quantity)
select
    now(), now(), 'Vincent Nwabudo', 'ISBN7649827TA', '', 'Understanding CyberSecurity', 10
WHERE NOT EXISTS (
        SELECT bookisbncode FROM tbl_book WHERE bookisbncode = 'ISBN7649827TA'
    );

insert into tbl_book
(created_at, updated_at, author_name, bookisbncode, book_image_url, book_title, quantity)
select
    now(), now(), 'Daniel Nwabudo', 'ISBN2309872JT', '', 'Trade with Expert Signals: Know How', 10
WHERE NOT EXISTS (
        SELECT bookisbncode FROM tbl_book WHERE bookisbncode = 'ISBN2309872JT'
    );

insert into tbl_book
(created_at, updated_at, author_name, bookisbncode, book_image_url, book_title, quantity)
select
    now(), now(), 'Abraham Nwabudo', 'ISBN3438092JO', '', 'Data Analysis for Beginners', 10
WHERE NOT EXISTS (
        SELECT bookisbncode FROM tbl_book WHERE bookisbncode = 'ISBN3438092JO'
    );