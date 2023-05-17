ALTER TABLE review DROP FOREIGN KEY review_ibfk_1;
ALTER TABLE review DROP FOREIGN KEY review_ibfk_2;

ALTER TABLE review DROP INDEX reviewee_id;
ALTER TABLE review DROP INDEX reviewer_id;