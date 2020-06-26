package bankbalance.model;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Represents single bank statement. */
public class BankStatement {

  @CsvBindByPosition(position = 0, required = true)
  private String accountNumber;

  @CsvBindByPosition(position = 1, required = true)
  @CsvDate("yyyy-MM-dd HH:mm:ss")
  private LocalDateTime date;

  @CsvBindByPosition(position = 2, required = true)
  private String beneficiaryAccountNumber;

  @CsvBindByPosition(position = 3, required = false)
  private String comment;

  @CsvBindByPosition(position = 4, required = true)
  private double amount;

  @CsvBindByPosition(position = 5, required = true)
  private String currency;

  public BankStatement() {}

  public BankStatement(
      String accountNumber,
      LocalDateTime date,
      String beneficiaryAccountNumber,
      String comment,
      double amount,
      String currency) {
    this.accountNumber = accountNumber;
    this.date = date;
    this.beneficiaryAccountNumber = beneficiaryAccountNumber;
    this.comment = comment;
    this.amount = amount;
    this.currency = currency;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public String getBeneficiaryAccountNumber() {
    return beneficiaryAccountNumber;
  }

  public void setBeneficiaryAccountNumber(String beneficiaryAccountNumber) {
    this.beneficiaryAccountNumber = beneficiaryAccountNumber;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  @Override
  public String toString() {
    return accountNumber
        + ','
        + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        + ','
        + beneficiaryAccountNumber
        + ','
        + comment
        + ','
        + amount
        + ','
        + currency;
  }
}
