INSERT INTO questionbank.subject(subject_code, subject_name)VALUES
('A', '���ڰ��ⱸ��'),
('D', '�����ͺ��̽�'),
('C', '������ ���'),
('O', '�ü��'),
('S', '����Ʈ���� ����');

INSERT INTO questionbank.customer
(customer_code, customer_name, id, password, email, employee)
values
('C001', 'ȫ�浿', 'honggil', '11112222', 'hong@test.com', 0),
('C002', '�����', 'suzy94', '22221111', 'suzy94@test.co.kr', 0),
('C003', '�󸮻�', 'lalalalisa', '22223333', 'lisa@test.com', 0),
('E001', '�켱��', 'E001', '22221111', 'mia94@test.com', 1),
('E002', '������', 'E002', '22221111', 'emp02@test.co.kr', 1),
('E003', '������', 'E003', '22223333', 'e003@test.com', 1);