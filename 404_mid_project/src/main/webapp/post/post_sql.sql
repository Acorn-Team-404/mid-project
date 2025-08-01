CREATE TABLE comments(
	comment_num NUMBER PRIMARY KEY, -- 댓글 번호
	comment_writer VARCHAR2(20) NOT NULL, -- 작성자
	comment_content VARCHAR2(100) NOT NULL, -- 내용
	comment_targetWriter VARCHAR2(20) NOT NULL, -- 누구에게 작성했는지
	comment_groupNum NUMBER NOT NULL, -- 댓글의 그룹번호
	comment_parentNum NUMBER NOT NULL, -- 부모가 되는 원글의 번호
	comment_deleted CHAR(3) DEFAULT 'no', -- 댓글을 삭제했는지
	comment_createdAt DATE DEFAULT SYSDATE -- 댓글 작성일
);

CREATE SEQUENCE comments_seq;
