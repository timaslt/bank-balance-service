package bankbalance.service;

import org.springframework.web.multipart.MultipartFile;

/** Interface for storing and exporting bank statements. */
public interface BankStatementService {

  /**
   * Stores new bank statements.
   *
   * @param csvFile multipart file that holds statement data
   * @return string status
   */
  String saveStatementsFromCsv(MultipartFile csvFile);
}
