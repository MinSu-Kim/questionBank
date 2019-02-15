-- questionbank
DROP SCHEMA IF EXISTS questionbank;

-- questionbank
CREATE SCHEMA questionbank;

-- ȸ��
CREATE TABLE questionbank.customer (
	customer_code CHAR(4)     NOT NULL COMMENT 'ȸ���ڵ�', -- ȸ���ڵ�
	customer_name VARCHAR(20) NOT NULL COMMENT '�̸�', -- �̸�
	id            VARCHAR(20) NOT NULL COMMENT '���̵�', -- ���̵�
	password      VARCHAR(20) NOT NULL COMMENT '��й�ȣ', -- ��й�ȣ
	email         VARCHAR(40) NOT NULL COMMENT '�̸���', -- �̸���
	employee      TINYINT(4)  NOT NULL COMMENT '�����ڿ���' -- �����ڿ���
)
COMMENT 'ȸ��';

-- ȸ��
ALTER TABLE questionbank.customer
	ADD CONSTRAINT
		PRIMARY KEY (
			customer_code -- ȸ���ڵ�
		);

-- ����(1)
CREATE TABLE questionbank.question (
	question_code  CHAR(10)     NOT NULL COMMENT '�����ڵ�', -- �����ڵ�
	question_title TEXT         NOT NULL COMMENT '����', -- ����
	choice1        VARCHAR(255) NOT NULL COMMENT '����1', -- ����1
	choice2        VARCHAR(255) NOT NULL COMMENT '����2', -- ����2
	choice3        VARCHAR(255) NOT NULL COMMENT '����3', -- ����3
	choice4        VARCHAR(255) NOT NULL COMMENT '����4', -- ����4
	correct        TINYINT      NOT NULL COMMENT '����', -- ����
	state          VARCHAR(20)  NOT NULL COMMENT '����/��������', -- ����/��������
	correct_rate   INT(11)      NULL     COMMENT '�����', -- �����
	picture        TEXT         NULL     COMMENT '�׸�/����', -- �׸�/����
	subject        CHAR(1)      NULL     COMMENT '����', -- ����
	year           INTEGER      NULL     COMMENT '��������', -- ��������
	round          TINYINT      NULL     COMMENT 'ȸ��' -- ȸ��
)
COMMENT '����(1)';

-- ����(1)
ALTER TABLE questionbank.question
	ADD CONSTRAINT
		PRIMARY KEY (
			question_code -- �����ڵ�
		);

-- ���ǻ��� ���
CREATE TABLE questionbank.reply (
	reply_code INT(11)       NOT NULL COMMENT '����ڵ�', -- ����ڵ�
	board      INT(11)       NOT NULL COMMENT '�Խñ��ڵ�', -- �Խñ��ڵ�
	replytext  VARCHAR(1000) NOT NULL COMMENT '��۳���', -- ��۳���
	regdate    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '�Է³�¥', -- �Է³�¥
	moddate    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '������¥', -- ������¥
	writer     CHAR(4)       NOT NULL COMMENT '�ۼ���(ȸ���ڵ�)' -- �ۼ���(ȸ���ڵ�)
)
COMMENT '���ǻ��� ���';

-- ���ǻ��� ���
ALTER TABLE questionbank.reply
	ADD CONSTRAINT
		PRIMARY KEY (
			reply_code -- ����ڵ�
		);

-- ���� ���
CREATE TABLE questionbank.resulttest (
	resultTest_code INT(11)  NOT NULL COMMENT '��������ڵ�', -- ��������ڵ�
	customer        CHAR(4)  NOT NULL COMMENT 'ȸ���ڵ�', -- ȸ���ڵ�
	answer          INT(11)  NULL     COMMENT '�Է��� ��(100)', -- �Է��� ��(1)
	correct         INT(11)  NULL     COMMENT '��(100)', -- ��(1)
	spendTime       INT(11)  NULL     COMMENT '�ɸ� �ð�', -- �ѹ����� �ɸ� �ð�
	pass            TINYINT  NOT NULL COMMENT '�հݿ���(60��)', -- ���俩��
	question_code   CHAR(10) NULL     COMMENT '�����ڵ�' -- �����ڵ�
)
COMMENT '���� ���';

-- ���� ���
ALTER TABLE questionbank.resulttest
	ADD CONSTRAINT
		PRIMARY KEY (
			resultTest_code -- ��������ڵ�
		);

-- ����(5���� 20����)
CREATE TABLE questionbank.subject (
	subject_code CHAR(1)     NOT NULL COMMENT '����', -- �����ڵ�
	subject_name VARCHAR(20) NOT NULL COMMENT '�����̸�' -- �����̸�
)
COMMENT '����(5���� 20����)';

-- ����(5���� 20����)
ALTER TABLE questionbank.subject
	ADD CONSTRAINT
		PRIMARY KEY (
			subject_code -- �����ڵ�
		);

-- ���ǻ���
CREATE TABLE questionbank.board (
	board_code  INT(11)      NOT NULL COMMENT '�Խñ��ڵ�', -- �Խñ��ڵ�
	board_title VARCHAR(200) NOT NULL COMMENT '����', -- ����
	content     TEXT         NOT NULL COMMENT '����', -- ����
	regdate     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '�Խó�¥', -- �Խó�¥
	moddate     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '������¥', -- ������¥
	viewcnt     INT(11)      NOT NULL DEFAULT 0 COMMENT '��ȸ��', -- ��ȸ��
	writer      CHAR(4)      NOT NULL COMMENT 'ȸ���ڵ�(�ۼ���)' -- ȸ���ڵ�(�ۼ���)
)
COMMENT '���ǻ���';

-- ���ǻ���
ALTER TABLE questionbank.board
	ADD CONSTRAINT
		PRIMARY KEY (
			board_code -- �Խñ��ڵ�
		);

-- ���ǻ��� ���
ALTER TABLE questionbank.reply
	ADD CONSTRAINT FK_board_TO_reply -- FK_board_TO_reply
		FOREIGN KEY (
			board -- �Խñ��ڵ�
		)
		REFERENCES questionbank.board ( -- ���ǻ���
			board_code -- �Խñ��ڵ�
		),
	ADD INDEX FK_board_TO_reply (
		board -- �Խñ��ڵ�
	);

-- ���ǻ��� ���
ALTER TABLE questionbank.reply
	ADD CONSTRAINT FK_customer_TO_reply -- FK_customer_TO_reply
		FOREIGN KEY (
			writer -- �ۼ���(ȸ���ڵ�)
		)
		REFERENCES questionbank.customer ( -- ȸ��
			customer_code -- ȸ���ڵ�
		),
	ADD INDEX FK_customer_TO_reply (
		writer -- �ۼ���(ȸ���ڵ�)
	);

-- ���� ���
ALTER TABLE questionbank.resulttest
	ADD CONSTRAINT FK_customer_TO_resultTest -- FK_customer_TO_resultTest
		FOREIGN KEY (
			customer -- ȸ���ڵ�
		)
		REFERENCES questionbank.customer ( -- ȸ��
			customer_code -- ȸ���ڵ�
		),
	ADD INDEX FK_customer_TO_resultTest (
		customer -- ȸ���ڵ�
	);

-- ���ǻ���
ALTER TABLE questionbank.board
	ADD CONSTRAINT FK_customer_TO_board -- FK_customer_TO_board
		FOREIGN KEY (
			writer -- ȸ���ڵ�(�ۼ���)
		)
		REFERENCES questionbank.customer ( -- ȸ��
			customer_code -- ȸ���ڵ�
		),
	ADD INDEX FK_customer_TO_board (
		writer -- ȸ���ڵ�(�ۼ���)
	);

-- ����(1)
ALTER TABLE questionbank.question
	ADD CONSTRAINT FK_subject_TO_question -- ����(5���� 20����) -> ����(1)
		FOREIGN KEY (
			subject -- ����
		)
		REFERENCES questionbank.subject ( -- ����(5���� 20����)
			subject_code -- �����ڵ�
		);

-- ���� ���
ALTER TABLE questionbank.resulttest
	ADD CONSTRAINT FK_question_TO_resulttest -- ����(1) -> ���� ���
		FOREIGN KEY (
			question_code -- �����ڵ�
		)
		REFERENCES questionbank.question ( -- ����(1)
			question_code -- �����ڵ�
		);