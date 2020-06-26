package bankbalance.service;

import org.springframework.web.multipart.MultipartFile;

/** Interface for storing and exporting bank statements. */
public interface BankAccountService {

  /**
   * Stores new bank statements.
   *
   * @param csvFile multipart file that holds statement data
   * @return string status
   */
  String saveStatementsFromCsv(MultipartFile csvFile);

  /**
   * Builds a string consisting of all bank statements during the period in csv file format.
   *
   * @param dateFrom string represents optional date in format yyyy-MM-dd
   * @param dateTo string represents optional date in format yyyy-MM-dd
   * @return string as csv file content
   */
  String getCsvFromStatements(String dateFrom, String dateTo);
}
