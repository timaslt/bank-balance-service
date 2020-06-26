package bankbalance.service;

import bankbalance.database.BankAccountDao;
import bankbalance.model.BankStatement;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** Stores and exports bank statements. */
@Service
public class BankAccountServiceImpl implements BankAccountService {

  private final BankAccountDao bankAccountDao;

  @Autowired
  public BankAccountServiceImpl(BankAccountDao bankAccountDao) {
    this.bankAccountDao = bankAccountDao;
  }

  /**
   * Stores new bank statements into database.
   *
   * @param csvFile multipart file that holds statement data
   * @return string status
   */
  @Override
  public String saveStatementsFromCsv(MultipartFile csvFile) {
    String result = "";
    List<BankStatement> bankStatements;
    try {
      Reader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream()));
      bankStatements = new CsvToBeanBuilder(reader).withType(BankStatement.class).build().parse();
    } catch (IOException e) {
      e.printStackTrace();
      return "Failed to extract input stream from the file.";
    } catch (RuntimeException e) {
      e.printStackTrace();
      return "Failed to read from the file.";
    }
    bankAccountDao.insertBankStatements(bankStatements);
    return "Success";
  }

  /**
   * Extracts bank statements from database and returns it as a csv file content.
   *
   * @param dateFrom string represents optional date in format yyyy-MM-dd
   * @param dateTo string represents optional date in format yyyy-MM-dd
   * @return string that consists of csv content
   */
  @Override
  public String getCsvFromStatements(String dateFrom, String dateTo) {
    LocalDateTime dateTimeFrom;
    LocalDateTime dateTimeTo;
    if (dateFrom == null || dateFrom.isEmpty()) {
      dateTimeFrom = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
    } else {
      dateTimeFrom =
          LocalDateTime.parse(
              dateFrom + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    if (dateTo == null || dateTo.isEmpty()) {
      dateTimeTo = LocalDateTime.now();
    } else {
      dateTimeTo =
          LocalDateTime.parse(
              dateTo + " 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    List<BankStatement> statements = bankAccountDao.getBankStatements(dateTimeFrom, dateTimeTo);
    String csvFileAsString = "";
    for (BankStatement st : statements) {
      csvFileAsString += st + "\n";
    }
    return csvFileAsString;
  }

  /**
   *
   * @param accountNumber string account id
   * @param dateFrom string represents optional date in format yyyy-MM-dd
   * @param dateTo string represents optional date in format yyyy-MM-dd
   * @return
   */
  @Override
  public String getAccountBalance(String accountNumber, String dateFrom, String dateTo) {
    String result = "";
    LocalDateTime dateTimeFrom;
    LocalDateTime dateTimeTo;
    if (dateFrom == null || dateFrom.isEmpty()) {
      dateTimeFrom = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
    } else {
      dateTimeFrom =
          LocalDateTime.parse(
              dateFrom + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    if (dateTo == null || dateTo.isEmpty()) {
      dateTimeTo = LocalDateTime.now();
    } else {
      dateTimeTo =
          LocalDateTime.parse(
              dateTo + " 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    // Calculate balance using negative deltas, since in the database is current account balance.
    int balance = (int) (bankAccountDao.getAccountBalance(accountNumber));
    System.out.println(balance);
    List<BankStatement> statementsAfterDateTo =
        bankAccountDao.getAccountBankStatements(accountNumber, dateTimeTo, LocalDateTime.now());
    // Calculating balance at dateTo date.
    for (BankStatement statement : statementsAfterDateTo) {
      if (statement.getAccountNumber().equals(accountNumber)) {
        balance += (int) (statement.getAmount() * 100);
      } else {
        balance -= (int) (statement.getAmount() * 100);
      }
    }
    List<BankStatement> statementsForThePeriod =
        bankAccountDao.getAccountBankStatements(accountNumber, dateTimeFrom, dateTimeTo);
    result +=
        dateTimeTo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            + " balance "
            + ((double) (balance / 100))
            + "\n";
    for (BankStatement st : statementsForThePeriod) {
      if (st.getAccountNumber().equals(accountNumber)) {
        balance += (int) (st.getAmount() * 100);
        result +=
            st.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                + " transaction -"
                + st.getAmount()
                + " "
                + st.getCurrency()
                + " balance "
                + ((double) (balance / 100))
                + "\n";
      } else {
        balance -= (int) (st.getAmount() * 100);
        result +=
            st.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                + " transaction +"
                + st.getAmount()
                + " "
                + st.getCurrency()
                + " balance "
                + ((double) (balance / 100))
                + "\n";
      }
    }
    return result;
  }
}
