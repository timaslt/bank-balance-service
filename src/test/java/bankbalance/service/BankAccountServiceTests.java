package bankbalance.service;

import bankbalance.database.BankAccountDao;
import bankbalance.model.BankStatement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

/** Tests BankAccountService class. */
@ExtendWith(SpringExtension.class)
public class BankAccountServiceTests {

  @Mock BankAccountDao databaseMock;

  @InjectMocks private static BankAccountServiceImpl bankAccountService;

  /**
   * Test saveStatementsFromCsv method using mock multipart files. Checking that all mandatory
   * information is inside csv file.
   */
  @Test
  public void saveStatementsFromCsvTest() {
    // Using CSV that has a record for every mandatory data.
    String validContent =
        "1,2020-06-23 13:43:33,3,,419.99,EUR\n"
            + "3,2020-03-21 09:02:33,4,For food,22.50,EUR\n"
            + "4,2020-06-24 13:43:33,1,,32.00,EUR\n"
            + "2,2020-06-25 13:43:33,3,Payment for the car,3000.00,EUR";
    MultipartFile csvFile =
        new MockMultipartFile("file", "test1.csv", "text/csv", validContent.getBytes());
    assertEquals(
        "Success",
        bankAccountService.saveStatementsFromCsv(csvFile),
        "saveStatementsFromCsvTest: valid csv file was not saved.");
    verify(databaseMock, times(1)).insertBankStatements(anyList());

    // Using CSV that does not have mandatory field (account number in 4th row).
    String invalidContent =
        "1,2020-06-23 13:43:33,3,,419.99,EUR\n"
            + "3,2020-03-21 09:02:33,4,For food,22.50,EUR\n"
            + "4,2020-06-24 13:43:33,1,,32.00,EUR\n"
            + ",2020-06-25 13:43:33,3,Payment for the car,3000.00,EUR";
    csvFile = new MockMultipartFile("file", "test1.csv", "text/csv", invalidContent.getBytes());
    assertEquals(
        "Failed to read from the file.",
        bankAccountService.saveStatementsFromCsv(csvFile),
        "saveStatementsFromCsvTest: wrong output while saving invalid csv file");
  }

  /**
   * Test getCsvFromStatements method so that it generates csv file content from list of bank statements.
   */
  @Test
  public void getCsvFromStatementsTest() {
    String csvFileContent =
        "1,2020-06-23 13:43:33,3,,419.99,EUR\n"
            + "3,2020-03-21 09:02:33,4,For food,22.5,EUR\n"
            + "4,2020-06-24 13:43:33,1,,32.0,EUR\n"
            + "2,2020-06-25 13:43:33,3,Payment for the car,3000.0,EUR\n";
    List<BankStatement> statements = new ArrayList<>();
    statements.add(new BankStatement("1", LocalDateTime.of(2020,06,23,13,43,33), "3", "", 419.99, "EUR"));
    statements.add(new BankStatement("3", LocalDateTime.of(2020,03,21,9,02,33), "4", "For food", 22.50, "EUR"));
    statements.add(new BankStatement("4", LocalDateTime.of(2020,06,24,13,43,33), "1", "", 32.00, "EUR"));
    statements.add(new BankStatement("2", LocalDateTime.of(2020,06,25,13,43,33), "3", "Payment for the car", 3000.00, "EUR"));
    LocalDateTime localDateTime = LocalDateTime.now();
    when(databaseMock.getBankStatements(any(), any())).thenReturn(statements);
    assertEquals(
            csvFileContent,
            bankAccountService.getCsvFromStatements("2020-06-22", "2020-06-24"),
            "getCsvFromStatements: valid csv file was not generated.");
    verify(databaseMock, times(1)).getBankStatements(any(), any());
  }
}
