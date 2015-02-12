package com.test.pai;

public class CurrResult
{
	private double currPai;

	private double difference;

	private double totalPai;

	public CurrResult()
	{
	}

	public CurrResult(double currPai, double difference, double totalPai)
	{
		super();
		this.currPai = currPai;
		this.difference = difference;
		this.totalPai = totalPai;
	}

	public double getCurrPai()
	{
		return currPai;
	}

	public void setCurrPai(double currPai)
	{
		this.currPai = currPai;
	}

	public double getDifference()
	{
		return difference;
	}

	public void setDifference(double difference)
	{
		this.difference = difference;
	}

	public double getTotalPai()
	{
		return totalPai;
	}

	public void setTotalPai(double totalPai)
	{
		this.totalPai = totalPai;
	}

	@Override
	public String toString()
	{
		return "CurrResult [currPai=" + currPai + ", difference=" + difference + ", totalPai=" + totalPai + "]";
	}

}
