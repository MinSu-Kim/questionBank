-- questionBank
DROP SCHEMA IF EXISTS `questionBank`;

-- questionBank
CREATE SCHEMA `questionBank`;

-- ����(1)
CREATE TABLE `questionBank`.`question` (
	`question_code`  CHAR(10)     NOT NULL COMMENT '�����ڵ�', -- �����ڵ�
	`question_title` TEXT         NOT NULL COMMENT '����', -- ����
	`choice1`        VARCHAR(255) NOT NULL COMMENT '����1', -- ����1
	`choice2`        VARCHAR(255) NOT NULL COMMENT '����2', -- ����2
	`choice3`        VARCHAR(255) NOT NULL COMMENT '����3', -- ����3
	`choice4`        VARCHAR(255) NOT NULL COMMENT '����4', -- ����4
	`correct`        INT(1)       NOT NULL COMMENT '����', -- ����
	`state`          VARCHAR(20)  NOT NULL COMMENT '����/��������', -- ��������
	`correct_rate`   INT          NULL     COMMENT '�����', -- �����
	`picture`        TEXT         NULL     COMMENT '�׸�/����' -- �׸�/����
)
COMMENT '����(1)';

-- ����(1)
ALTER TABLE `questionBank`.`question`
	ADD CONSTRAINT `PK_question` -- ����(1) �⺻Ű
		PRIMARY KEY (
			`question_code` -- �����ڵ�
		);

-- ����(5���� 20����)
CREATE TABLE `questionBank`.`subject` (
	`question_code` CHAR(10)    NOT NULL COMMENT '�����ڵ�', -- �����ڵ�
	`test`          CHAR(4)     NOT NULL COMMENT '�����ڵ�', -- �����ڵ�
	`subject_code`  CHAR(1)     NOT NULL COMMENT '����', -- ����
	`subject_name`  VARCHAR(20) NOT NULL COMMENT '�����̸�' -- �����̸�
)
COMMENT '����(5���� 20����)';

-- ����(5���� 20����)
ALTER TABLE `questionBank`.`subject`
	ADD CONSTRAINT `PK_subject` -- ����(5���� 20����) �⺻Ű
		PRIMARY KEY (
			`question_code`, -- �����ڵ�
			`test`           -- �����ڵ�
		);

-- ��������(100����)
CREATE TABLE `questionBank`.`test` (
	`test_code` CHAR(4) NOT NULL COMMENT '�����ڵ�', -- �����ڵ�
	`year`      INT(4)  NOT NULL COMMENT '��������', -- ��������
	`round`     INT     NOT NULL COMMENT 'ȸ��', -- ȸ��
	`test_time` INT     NULL     COMMENT '�ð�(150��)' -- �ð�(150��)
)
COMMENT '��������(100����)';

-- ��������(100����)
ALTER TABLE `questionBank`.`test`
	ADD CONSTRAINT `PK_test` -- ��������(100����) �⺻Ű
		PRIMARY KEY (
			`test_code` -- �����ڵ�
		);

-- ȸ��
CREATE TABLE `questionBank`.`customer` (
	`customer_code` CHAR(4)     NOT NULL COMMENT 'ȸ���ڵ�', -- ȸ���ڵ�
	`customer_name` VARCHAR(20) NOT NULL COMMENT '�̸�', -- �̸�
	`id`            VARCHAR(20) NOT NULL COMMENT '���̵�', -- ���̵�
	`password`      VARCHAR(20) NOT NULL COMMENT '��й�ȣ', -- ��й�ȣ
	`email`         VARCHAR(40) NOT NULL COMMENT '�̸���', -- �̸���
	`employee`      TINYINT     NOT NULL COMMENT '�����ڿ���' -- �����ڿ���
)
COMMENT 'ȸ��';

-- ȸ��
ALTER TABLE `questionBank`.`customer`
	ADD CONSTRAINT `PK_customer` -- ȸ�� �⺻Ű
		PRIMARY KEY (
			`customer_code` -- ȸ���ڵ�
		);

-- ����(20����) ���ý���
CREATE TABLE `questionBank`.`unitTest` (
	`unitTest_code` INT      NOT NULL COMMENT '���񺰽��� �ڵ�', -- ���񺰽��� �ڵ�
	`answer`        INT(1)   NULL     COMMENT '�Է��� ��(20��)', -- �Է��� ��(20��)
	`correct`       TINYINT  NULL     COMMENT '����(20��)', -- ����(20��)
	`customer`      CHAR(4)  NOT NULL COMMENT 'ȸ���ڵ�', -- ȸ���ڵ�
	`question_code` CHAR(10) NULL     COMMENT '�����ڵ�', -- �����ڵ�
	`test`          CHAR(4)  NULL     COMMENT '�����ڵ�' -- �����ڵ�
)
COMMENT '����(20����) ���ý���';

-- ����(20����) ���ý���
ALTER TABLE `questionBank`.`unitTest`
	ADD CONSTRAINT `PK_unitTest` -- ����(20����) ���ý��� �⺻Ű
		PRIMARY KEY (
			`unitTest_code` -- ���񺰽��� �ڵ�
		);

ALTER TABLE `questionBank`.`unitTest`
	MODIFY COLUMN `unitTest_code` INT NOT NULL AUTO_INCREMENT COMMENT '���񺰽��� �ڵ�';

-- ���� ���
CREATE TABLE `questionBank`.`resultTest` (
	`resultTest_code` INT     NOT NULL COMMENT '��������ڵ�', -- ��������ڵ�
	`customer`        CHAR(4) NOT NULL COMMENT 'ȸ���ڵ�', -- ȸ���ڵ�
	`answer`          INT     NULL     COMMENT '�Է��� ��(100)', -- �Է��� ��(100)
	`correct`         INT     NULL     COMMENT '��(100)', -- ��(100)
	`spendTime`       INT     NULL     COMMENT '�ɸ� �ð�', -- �ɸ� �ð�
	`score`           INT     NOT NULL COMMENT '���輺��', -- ���輺��
	`pass`            TINYINT NOT NULL COMMENT '�հݿ���(60��)', -- �հݿ���(60��)
	`test`            CHAR(4) NOT NULL COMMENT '�����ڵ�' -- �����ڵ�
)
COMMENT '���� ���';

-- ���� ���
ALTER TABLE `questionBank`.`resultTest`
	ADD CONSTRAINT `PK_resultTest` -- ���� ��� �⺻Ű
		PRIMARY KEY (
			`resultTest_code` -- ��������ڵ�
		);

ALTER TABLE `questionBank`.`resultTest`
	MODIFY COLUMN `resultTest_code` INT NOT NULL AUTO_INCREMENT COMMENT '��������ڵ�';

-- ���ǻ���
CREATE TABLE `questionBank`.`board` (
	`board_code`  INT          NOT NULL COMMENT '�Խñ��ڵ�', -- �Խñ��ڵ�
	`board_title` VARCHAR(200) NOT NULL COMMENT '����', -- ����
	`content`     TEXT         NOT NULL COMMENT '����', -- ����
	`regdate`     TIMESTAMP    NOT NULL DEFAULT now() COMMENT '�Խó�¥', -- �Խó�¥
	`moddate`     TIMESTAMP    NOT NULL DEFAULT now() COMMENT '������¥', -- ������¥
	`viewcnt`     INT          NOT NULL DEFAULT 0 COMMENT '��ȸ��', -- ��ȸ��
	`writer`      CHAR(4)      NOT NULL COMMENT 'ȸ���ڵ�(�ۼ���)' -- ȸ���ڵ�(�ۼ���)
)
COMMENT '���ǻ���';

-- ���ǻ���
ALTER TABLE `questionBank`.`board`
	ADD CONSTRAINT `PK_board` -- ���ǻ��� �⺻Ű
		PRIMARY KEY (
			`board_code` -- �Խñ��ڵ�
		);

ALTER TABLE `questionBank`.`board`
	MODIFY COLUMN `board_code` INT NOT NULL AUTO_INCREMENT COMMENT '�Խñ��ڵ�';

-- ���ǻ��� ���
CREATE TABLE `questionBank`.`reply` (
	`reply_code` INT           NOT NULL COMMENT '����ڵ�', -- ����ڵ�
	`board`      INT           NOT NULL COMMENT '�Խñ��ڵ�', -- �Խñ��ڵ�
	`replytext`  VARCHAR(1000) NOT NULL COMMENT '��۳���', -- ��۳���
	`regdate`    TIMESTAMP     NOT NULL DEFAULT now() COMMENT '�Է³�¥', -- �Է³�¥
	`moddate`    TIMESTAMP     NOT NULL DEFAULT now() COMMENT '������¥', -- ������¥
	`writer`     CHAR(4)       NOT NULL COMMENT '�ۼ���(ȸ���ڵ�)' -- �ۼ���(ȸ���ڵ�)
)
COMMENT '���ǻ��� ���';

-- ���ǻ��� ���
ALTER TABLE `questionBank`.`reply`
	ADD CONSTRAINT `PK_reply` -- ���ǻ��� ��� �⺻Ű
		PRIMARY KEY (
			`reply_code` -- ����ڵ�
		);

ALTER TABLE `questionBank`.`reply`
	MODIFY COLUMN `reply_code` INT NOT NULL AUTO_INCREMENT COMMENT '����ڵ�';

-- ����(5���� 20����)
ALTER TABLE `questionBank`.`subject`
	ADD CONSTRAINT `FK_test_TO_subject` -- ��������(100����) -> ����(5���� 20����)
		FOREIGN KEY (
			`test` -- �����ڵ�
		)
		REFERENCES `questionBank`.`test` ( -- ��������(100����)
			`test_code` -- �����ڵ�
		);

-- ����(5���� 20����)
ALTER TABLE `questionBank`.`subject`
	ADD CONSTRAINT `FK_question_TO_subject` -- ����(1) -> ����(5���� 20����)
		FOREIGN KEY (
			`question_code` -- �����ڵ�
		)
		REFERENCES `questionBank`.`question` ( -- ����(1)
			`question_code` -- �����ڵ�
		);

-- ����(20����) ���ý���
ALTER TABLE `questionBank`.`unitTest`
	ADD CONSTRAINT `FK_customer_TO_unitTest` -- ȸ�� -> ����(20����) ���ý���
		FOREIGN KEY (
			`customer` -- ȸ���ڵ�
		)
		REFERENCES `questionBank`.`customer` ( -- ȸ��
			`customer_code` -- ȸ���ڵ�
		);

-- ����(20����) ���ý���
ALTER TABLE `questionBank`.`unitTest`
	ADD CONSTRAINT `FK_subject_TO_unitTest` -- ����(5���� 20����) -> ����(20����) ���ý���
		FOREIGN KEY (
			`question_code`, -- �����ڵ�
			`test`           -- �����ڵ�
		)
		REFERENCES `questionBank`.`subject` ( -- ����(5���� 20����)
			`question_code`, -- �����ڵ�
			`test`           -- �����ڵ�
		);

-- ���� ���
ALTER TABLE `questionBank`.`resultTest`
	ADD CONSTRAINT `FK_customer_TO_resultTest` -- ȸ�� -> ���� ���
		FOREIGN KEY (
			`customer` -- ȸ���ڵ�
		)
		REFERENCES `questionBank`.`customer` ( -- ȸ��
			`customer_code` -- ȸ���ڵ�
		);

-- ���� ���
ALTER TABLE `questionBank`.`resultTest`
	ADD CONSTRAINT `FK_test_TO_resultTest` -- ��������(100����) -> ���� ���
		FOREIGN KEY (
			`test` -- �����ڵ�
		)
		REFERENCES `questionBank`.`test` ( -- ��������(100����)
			`test_code` -- �����ڵ�
		);

-- ���ǻ���
ALTER TABLE `questionBank`.`board`
	ADD CONSTRAINT `FK_customer_TO_board` -- ȸ�� -> ���ǻ���
		FOREIGN KEY (
			`writer` -- ȸ���ڵ�(�ۼ���)
		)
		REFERENCES `questionBank`.`customer` ( -- ȸ��
			`customer_code` -- ȸ���ڵ�
		);

-- ���ǻ��� ���
ALTER TABLE `questionBank`.`reply`
	ADD CONSTRAINT `FK_board_TO_reply` -- ���ǻ��� -> ���ǻ��� ���
		FOREIGN KEY (
			`board` -- �Խñ��ڵ�
		)
		REFERENCES `questionBank`.`board` ( -- ���ǻ���
			`board_code` -- �Խñ��ڵ�
		);

-- ���ǻ��� ���
ALTER TABLE `questionBank`.`reply`
	ADD CONSTRAINT `FK_customer_TO_reply` -- ȸ�� -> ���ǻ��� ���
		FOREIGN KEY (
			`writer` -- �ۼ���(ȸ���ڵ�)
		)
		REFERENCES `questionBank`.`customer` ( -- ȸ��
			`customer_code` -- ȸ���ڵ�
		);