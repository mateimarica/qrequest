-- Create users table (utf8_general_ci means case-insensitive)
CREATE TABLE `users` (
  `username` varchar(10) COLLATE utf8_general_ci NOT NULL,
  `password` varchar(40) NOT NULL,
  `is_admin` int DEFAULT 0,
  PRIMARY KEY (`username`)
);

-- Get account from credentials
SELECT * FROM users WHERE username = 'username' AND password = SHA1('password');

-- Get account from credentials with saved hashed password
SELECT * FROM users WHERE username = 'username' AND password = 'password';

-- Create account (the zero means user not admin by default)
INSERT INTO users VALUES('username', SHA1('password'));

-- Check if account exists [LEGACY]
SELECT COUNT(1) AS user_exists FROM users WHERE UPPER(username) = UPPER('username');

-- Get all users whose usernames contain the keyword
SELECT * FROM users WHERE username LIKE 'username%';




-- Create questions table
CREATE TABLE `questions` (
  `title` varchar(200) NOT NULL,
  `description` text DEFAULT '',
  `author` varchar(10) NOT NULL,
  `id` varchar(36) NOT NULL DEFAULT UUID(),
  `date_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `tag` varchar(255) DEFAULT NULL,
  `is_pinned` int(11) DEFAULT '-1',
  `solved_answer_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
  CONSTRAINT fk_author FOREIGN KEY (author) 
    REFERENCES users(username)
  CONSTRAINT fk_solved_answer_id FOREIGN KEY (solved_answer_id) 
    REFERENCES answers(id)
);

-- Create a question
INSERT INTO questions VALUES('title', 'desc', 'author', 'id', 'tag_name');

-- Refresh a question object to get new description/tag
SELECT * FROM questions WHERE id = 'id';

-- Get all questions
SELECT * FROM questions ORDER BY is_pinned DESC, date_created DESC;

-- Get filtered questions
SELECT * FROM questions WHERE (title LIKE '%title%' OR description LIKE '%description%') ORDER BY title DESC;

-- Toggle a question's pinned state
UPDATE questions SET is_pinned = is_pinned * -1 WHERE id = 'id';

-- Edit question
UPDATE questions SET description = 'description', tag = 'tag_name' WHERE id = 'id';

-- Delete question
DELETE FROM questions WHERE id = 'id';

-- Mark a question as solved with an answer
UPDATE questions SET solved_answer_id = 'answerid' WHERE id = 'id';

-- Remove an question's marked-as-solved answer
UPDATE questions SET solved_answer_id = NULL WHERE id = 'id';




-- Create answers table
CREATE TABLE `answers` (
  `answer` text NOT NULL,
  `answerer` varchar(10) NOT NULL,
  `id` varchar(36) NOT NULL DEFAULT UUID(),
  `question_id` varchar(36) NOT NULL,
  `date_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
  CONSTRAINT fk_question_id FOREIGN KEY (question_id) 
    REFERENCES questions(id)
);

-- Post answer
INSERT INTO answers VALUES('answerText', 'author', 'id', 'questionid', CURRENT_TIMESTAMP);

-- Get all answers
SELECT * FROM answers WHERE question_id = 'questionid' ORDER BY IFNULL((SELECT SUM(vote) FROM votes WHERE post_id = answers.id), 0) DESC, date_created DESC;

-- Get number of answers for a question
SELECT COUNT(question_id) AS answerCount FROM answers WHERE question_id = 'questionid';

-- Edit answer
UPDATE answers SET answer = 'description' WHERE id = 'id';

-- Delete all answers to a question
DELETE FROM answers WHERE question_id = 'questionid';

-- Delete an answer
DELETE FROM answers Where id = 'id';




-- Create votes table
CREATE TABLE `votes` (
  `vote` int NOT NULL,
  `post_id` varchar(36) NOT NULL,
  `voter` varchar(10) NOT NULL,
  PRIMARY KEY (`post_id`,`voter`)
);

-- Get the vote count on a post
SELECT SUM(vote) AS votes FROM votes WHERE post_id = 'postid';

-- Get the current user's vote on a post
SELECT vote FROM votes WHERE post_id = 'postid' AND voter = 'username';

-- Delete a vote on a post
DELETE FROM votes Where PostID = 'postid' AND voter = 'username';

-- Add/change a vote on a post
INSERT INTO votes VALUES(voteValue, 'postid', 'username') ON DUPLICATE KEY UPDATE vote = voteValue;




-- Create reports table
CREATE TABLE `reports` (
  `report_type` varchar(200) NOT NULL,
  `report_desc` text NOT NULL,
  `date_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `reporter` varchar(10) NOT NULL,
  `post_id` varchar(36) NOT NULL,
  `report_id` varchar(36) NOT NULL,
  PRIMARY KEY (`report_id`)
  CONSTRAINT fk_question_id FOREIGN KEY (post_id) 
    REFERENCES questions(id)
  CONSTRAINT fk_answer_id FOREIGN KEY (post_id) 
    REFERENCES answers(id)
  CONSTRAINT fk_reporter FOREIGN KEY (reporter) 
    REFERENCES users(username)
);

-- Create a report
INSERT INTO reports VALUES('reportType', 'description', CURRENT_TIMESTAMP, 'reporter_username', 'postid', 'id');




-- Create messages table
CREATE TABLE `messages` (
  `sender` varchar(10) NOT NULL,
  `receiver` varchar(10) NOT NULL,
  `body` text NOT NULL,
  `date_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
  CONSTRAINT fk_sender FOREIGN KEY (sender) 
    REFERENCES users(username)
  CONSTRAINT fk_receiver FOREIGN KEY (receiver) 
    REFERENCES users(username)
);

-- Create a message
INSERT INTO messages VALUES('sender', 'recipient', 'text', CURRENT_TIMESTAMP);

-- Get all messages between two people
SELECT * FROM messages WHERE ((receiver = 'username1' AND sender = 'username2') OR (receiver = 'username2' AND sender = 'username1'));