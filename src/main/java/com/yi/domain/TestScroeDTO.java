package com.yi.domain;

public class TestScroeDTO {
	private String customer;  //�׽�Ʈ�� ȸ��
	private int totalQuestion;//Ǭ ��ü���� ����
	private int correctQuestion;//���� ���� ����
	private int totalSpendTime;//�ɸ� �ѽð�
	
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public int getTotalQuestion() {
		return totalQuestion;
	}
	public void setTotalQuestion(int totalQuestion) {
		this.totalQuestion = totalQuestion;
	}
	public int getCorrectQuestion() {
		return correctQuestion;
	}
	public void setCorrectQuestion(int correctQuestion) {
		this.correctQuestion = correctQuestion;
	}
	public int getTotalSpendTime() {
		return totalSpendTime;
	}
	public void setTotalSpendTime(int totalSpendTime) {
		this.totalSpendTime = totalSpendTime;
	}
	
	@Override
	public String toString() {
		return "TestScroeDTO [customer=" + customer + ", totalQuestion=" + totalQuestion + ", correctQuestion="
				+ correctQuestion + ", totalSpendTime=" + totalSpendTime + "]";
	}
	
	
	
	
}
