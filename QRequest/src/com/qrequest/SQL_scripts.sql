-- Create Users table
CREATE TABLE `Users` (
  `Username` varchar(10) NOT NULL DEFAULT '',
  `Password` varchar(40) DEFAULT NULL,
  `IsAdmin` int(11) DEFAULT NULL,
  PRIMARY KEY (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Get account from credentials
SELECT * FROM Users WHERE Username = 'username' AND Password = SHA1('password');

-- Get account from credentials with saved hashed password
SELECT * FROM Users WHERE Username = 'username' AND Password = 'password';

-- Create account (the zero means user not admin by default)
INSERT INTO Users VALUES('username', SHA1('password'), 0);

-- Check if account exists
SELECT COUNT(1) AS UserExists FROM Users WHERE UPPER(Username) = UPPER('username');

-- Get all users whose usernames contain the keyword
SELECT * FROM Users WHERE Username LIKE 'username%';




-- Create Questions table
CREATE TABLE `Questions` (
  `Title` varchar(200) DEFAULT NULL,
  `Description` text,
  `Author` varchar(10) DEFAULT NULL,
  `Id` varchar(36) NOT NULL DEFAULT '',
  `TimePosted` datetime DEFAULT NULL,
  `Tag` varchar(255) DEFAULT NULL,
  `IsPinned` int(11) DEFAULT '-1',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Create a question
INSERT INTO Questions VALUES('title', 'desc', 'author', 'id', 'tag_name');

-- Refresh a question object to get new description/tag
SELECT * FROM Questions WHERE Id = 'id';

-- Get all questions
SELECT * FROM Questions ORDER BY IsPinned DESC, TimePosted DESC;

-- Get filtered questions
SELECT * FROM Questions WHERE (Title LIKE '%title%' OR Description LIKE '%description%') ORDER BY Title DESC;

-- Toggle a question's pinned state
UPDATE Questions SET IsPinned = IsPinned * -1 WHERE Id = 'id';

-- Edit question
UPDATE Questions SET Description = 'description', Tag = 'tag_name' WHERE Id = 'id';

-- Delete question
DELETE FROM Questions WHERE Id = 'id';

-- Mark a question as solved with an answer
UPDATE Questions SET SolvedAnswerId = 'answerId' WHERE Id = 'id';

-- Remove an question's marked-as-solved answer
UPDATE Questions SET SolvedAnswerId = NULL WHERE Id = 'id';




-- Create Answers table
CREATE TABLE `Answers` (
  `Answer` text,
  `Answerer` varchar(10) DEFAULT NULL,
  `Id` varchar(36) NOT NULL DEFAULT '',
  `QuestionId` varchar(36) DEFAULT NULL,
  `TimePosted` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Post answer
INSERT INTO Answers VALUES('answerText', 'author', 'id', 'questionId', CURRENT_TIMESTAMP);

-- Get all answers
SELECT * FROM Answers WHERE QuestionId = 'questionId' ORDER BY IFNULL((SELECT SUM(Vote) FROM Votes WHERE PostId = Answers.Id), 0) DESC, TimePosted DESC;

-- Get number of answers for a question
SELECT COUNT(QuestionId) AS AnswerCount FROM Answers WHERE QuestionId = 'questionId';

-- Edit answer
UPDATE Answers SET Answer = 'description' WHERE Id = 'id';

-- Delete all answers to a question
DELETE FROM Answers WHERE QuestionId = 'questionId';

-- Delete an answer
DELETE FROM Answers Where Id = 'id';




-- Create Votes table
CREATE TABLE `Votes` (
  `Vote` int(11) DEFAULT NULL,
  `PostId` varchar(36) NOT NULL DEFAULT '',
  `Voter` varchar(10) NOT NULL DEFAULT '',
  PRIMARY KEY (`PostId`,`Voter`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Get the vote count on a post
SELECT SUM(Vote) AS Votes FROM Votes WHERE PostId = 'postId';

-- Get the current user's vote on a post
SELECT Vote FROM Votes WHERE PostId = 'postId' AND Voter = 'username';

-- Delete a vote on a post
DELETE FROM Votes Where PostID = 'postId' AND Voter = 'username';

-- Add/change a vote on a post
INSERT INTO Votes VALUES(voteValue, 'postId', 'username') ON DUPLICATE KEY UPDATE Vote = voteValue;




-- Create Reports table
CREATE TABLE `Reports` (
  `ReportType` varchar(200) DEFAULT NULL,
  `ReportDesc` text,
  `TimeReported` datetime DEFAULT NULL,
  `Reporter` varchar(10) DEFAULT NULL,
  `PostId` varchar(36) DEFAULT NULL,
  `ReportId` varchar(36) NOT NULL DEFAULT '',
  PRIMARY KEY (`ReportId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Create a report
INSERT INTO Reports VALUES('reportType', 'description', CURRENT_TIMESTAMP, 'reporter_username', 'postId', 'id');




-- Create Messages table
CREATE TABLE `Messages` (
  `Sender` varchar(10) DEFAULT NULL,
  `Receiver` varchar(10) DEFAULT NULL,
  `Body` text,
  `TimePosted` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Create a message
INSERT INTO Messages VALUES('sender', 'recipient', 'text', CURRENT_TIMESTAMP);

-- Get all messages between two people
SELECT * FROM Messages WHERE ((Receiver = 'username1' AND Sender = 'username2') OR (Receiver = 'username2' AND Sender = 'username1'));