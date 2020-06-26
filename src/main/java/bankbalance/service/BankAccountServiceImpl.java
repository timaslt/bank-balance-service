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
}
