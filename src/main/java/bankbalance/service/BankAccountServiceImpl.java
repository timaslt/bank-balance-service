package bankbalance.service;

import bankbalance.model.BankStatement;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

/** Stores and exports bank statements. */
@Service
public class BankAccountServiceImpl implements BankAccountService {

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
    return "Success";
  }
}
