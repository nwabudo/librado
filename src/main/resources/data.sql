insert into tbl_property (property_code, property_value)
values
('user.book.limit', '2');

insert into tbl_book
    (created_at, updated_at, author_name, bookisbncode, book_image_url, book_title, quantity)
values
    (now(), now(), 'Emmanuel Nwabudo', 'ISBN345872JA', '', 'Building APIs: Know How', 10),
    (now(), now(), 'Ismet Baruah', 'ISBN3ERT6783TA', '', 'Effective Human Resourcing', 10),
    (now(), now(), 'Oluwaseun Popoola', 'ISBN4523T72JT', '', 'Versioning APIs: Know How', 10),
    (now(), now(), 'John Nwabudo', 'ISBN56O3O22JA', '', 'Selling APIs: Perks of the Trade', 10),
    (now(), now(), 'Vincent Nwabudo', 'ISBN7649827TA', '', 'Understanding CyberSecurity', 10),
    (now(), now(), 'Daniel Nwabudo', 'ISBN2309872JT', '', 'Trade with Expert Signals: Know How', 10),
    (now(), now(), 'Abraham Nwabudo', 'ISBN3438092JO', '', 'Data Analysis for Beginners', 10);
