package co.nxtgrid;

import org.joda.time.DateTime;

public class RequestData {
  private double amount;
  private long powerLimit;
  private String decoderKey;
  private int randomNumber;
  private DateTime issueDate;
  private String type;

    public String getType() {
      return this.type;
    }

    public void setType(String type) {
      this.type = type;
    }
    
    public DateTime getIssueDate() {
      return issueDate;
    }

    public void setIssueDate(String issueDate) {
      this.issueDate = DateTime.parse(issueDate);
    }

    public int getRandomNumber() {
      return randomNumber;
    }

    public void setRandomNumber(int randomNumber) {
      this.randomNumber = randomNumber;
    }

    public double getAmount() {
      return amount;
    }

    public void setAmount(double amount) {
      this.amount = amount;
    }

    public String getDecoderKey() {
        return decoderKey;
    }

    public void setDecoderKey(String decoderKey) {
        this.decoderKey = decoderKey;
    } 

    public long getPowerLimit() {
      return powerLimit;
    }

    public void setPowerLimit(long powerLimit) {
      this.powerLimit = powerLimit;
    }
}