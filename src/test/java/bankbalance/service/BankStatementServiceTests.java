package bankbalance.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Tests BankStatementService class. */
@ExtendWith(SpringExtension.class)
public class BankStatementServiceTests {

  private static BankStatementServiceImpl bankStatementService;

  @BeforeAll
  public static void setUp() {
    bankStatementService = new BankStatementServiceImpl();
  }

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
            + "4,2020-06-23 13:43:33,1,,32.00,EUR\n"
            + "2,2020-06-23 13:43:33,3,Payment for the car,3000.00,EUR";
    MultipartFile csvFile =
        new MockMultipartFile("file", "test1.csv", "text/csv", validContent.getBytes());
    assertEquals(
        "Success",
        bankStatementService.saveStatementsFromCsv(csvFile),
        "saveStatementsFromCsvTest: valid csv file was not saved.");

    // Using CSV that does not have mandatory field (account number in 4th row).
    String invalidContent =
        "1,2020-06-23 13:43:33,3,,419.99,EUR\n"
            + "3,2020-03-21 09:02:33,4,For food,22.50,EUR\n"
            + "4,2020-06-23 13:43:33,1,,32.00,EUR\n"
            + ",2020-06-23 13:43:33,3,Payment for the car,3000.00,EUR";
    csvFile = new MockMultipartFile("file", "test1.csv", "text/csv", invalidContent.getBytes());
    assertEquals(
        "Failed to read from the file.",
        bankStatementService.saveStatementsFromCsv(csvFile),
        "saveStatementsFromCsvTest: wrong output while saving invalid csv file");
  }
}
