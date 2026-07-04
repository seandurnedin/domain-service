-- Sample catalogue seeded on first startup by DataSeeder (gated by app.data-seed.enabled).
-- ISBNs are plain 13-digit ISBN-13 values (no hyphens) to fit the books.isbn column (length 14).
INSERT INTO books (isbn, title, author, publisher, published_year, genre, total_copies, available_copies, status)
VALUES
    ('9780134685991', 'Effective Java', 'Joshua Bloch', 'Addison-Wesley', 2018, 'Programming', 3, 3, 'IN_STORE'),
    ('9780596007126', 'Head First Design Patterns', 'Eric Freeman', 'O''Reilly', 2004, 'Programming', 2, 2, 'IN_STORE'),
    ('9780307474278', 'The Da Vinci Code', 'Dan Brown', 'Doubleday', 2003, 'Fiction', 4, 4, 'IN_STORE'),
    ('9780451524935', '1984', 'George Orwell', 'Secker & Warburg', 1949, 'Fiction', 5, 5, 'IN_STORE'),
    ('9780062316097', 'Sapiens', 'Yuval Noah Harari', 'Harper', 2015, 'Non-Fiction', 3, 3, 'IN_STORE');
