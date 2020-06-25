package bankbalance.service;

import bankbalance.model.Statement;
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
public class BankStatementServiceImpl implements BankStatementService {

  @Override
  public String saveStatementsFromCsv(MultipartFile csvFile) {
    String result = "";
    List<Statement> bankStatements;
    try {
      Reader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream()));
      bankStatements = new CsvToBeanBuilder(reader).withType(Statement.class).build().parse();
    } catch (IOException e) {
      e.printStackTrace();
      return "Failed to read from the file.";
    }
    return "Success";
  }
}
