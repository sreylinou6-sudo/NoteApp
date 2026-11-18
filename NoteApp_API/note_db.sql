-- Create Database: note_app 
CREATE DATABASE note_app;
USE note_app;

-- Create Table: notes
CREATE TABLE notes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP
);
-- Create table: categories
CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
--  add category_id to notes table 
ALTER TABLE notes
ADD COLUMN category_id INT NULL AFTER id;

-- create the relationship 
ALTER TABLE notes
ADD CONSTRAINT fk_notes_categories
FOREIGN KEY (category_id)
REFERENCES categories(id)
ON DELETE SET NULL
ON UPDATE CASCADE;