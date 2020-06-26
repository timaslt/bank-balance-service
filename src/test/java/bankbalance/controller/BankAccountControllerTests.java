package bankbalance.controller;

import bankbalance.service.BankAccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Tests BankAccountController class. */
@ExtendWith(SpringExtension.class)
@WebMvcTest(BankAccountController.class)
public class BankAccountControllerTests {

  @Autowired private MockMvc mvc;

  @MockBean private BankAccountService bankAccountService;

  /** Test importBankStatement method creating post requests. */
  @Test
  public void importBankStatementTest() {
    // Importing valid csv file
    String csvFileContent =
        "1,2020-06-23 13:43:33,3,,419.99,EUR\n"
            + "3,2020-03-21 09:02:33,4,For food,22.50,EUR\n"
            + "4,2020-06-24 13:43:33,1,,32.00,EUR\n"
            + "2,2020-06-25 13:43:33,3,Payment for the car,3000.00,EUR";
    MockMultipartFile csvFile =
        new MockMultipartFile("file", "test1.csv", "text/csv", csvFileContent.getBytes());
    when(bankAccountService.saveStatementsFromCsv(csvFile)).thenReturn("Success");
    try {
      mvc.perform(multipart("/api/import").file(csvFile))
          .andExpect(status().isOk())
          .andExpect(content().string("Success"));
      verify(bankAccountService, times(1)).saveStatementsFromCsv(csvFile);
    } catch (Exception e) {
      e.printStackTrace();
      fail("importBankStatementTest: error making bank statement import as csv file.");
    }
    // Importing file with invalid file type
    MockMultipartFile txtFile =
        new MockMultipartFile("file", "test2.txt", "text/plain", csvFileContent.getBytes());
    try {
      mvc.perform(multipart("/api/import").file(txtFile))
          .andExpect(status().isOk())
          .andExpect(content().string("Input error: wrong file type."));
      verify(bankAccountService, times(0)).saveStatementsFromCsv(txtFile);
    } catch (Exception e) {
      e.printStackTrace();
      fail("importBankStatementTest: error making invalid file type import (as txt file).");
    }
    // Importing empty file
    MockMultipartFile emptyFile =
        new MockMultipartFile("file", "test3.csv", "text/csv", "".getBytes());
    try {
      mvc.perform(multipart("/api/import").file(emptyFile))
          .andExpect(status().isOk())
          .andExpect(content().string("Wrong input: file is empty."));
      verify(bankAccountService, times(0)).saveStatementsFromCsv(emptyFile);
    } catch (Exception e) {
      e.printStackTrace();
      fail("importBankStatementTest: error making invalid file type import (as txt file).");
    }
  }

  /** Test exportBankStatement method creating get requests. */
  @Test
  public void exportBankStatementTest() {
    String csvFileContent =
        "1,2020-06-23 13:43:33,3,,419.99,EUR\n"
            + "3,2020-03-21 09:02:33,4,For food,22.50,EUR\n"
            + "4,2020-06-24 13:43:33,1,,32.00,EUR\n"
            + "2,2020-06-25 13:43:33,3,Payment for the car,3000.00,EUR";
    MockMultipartFile csvFile =
        new MockMultipartFile("file", "test1.csv", "text/csv", csvFileContent.getBytes());
    when(bankAccountService.getCsvFromStatements(null, null)).thenReturn(csvFileContent);
    try {
      mvc.perform(get("/api/export"))
          .andExpect(status().isOk())
          .andExpect(content().contentType("text/csv;charset=UTF-8"))
          .andExpect(content().string(csvFileContent));
      verify(bankAccountService, times(1)).getCsvFromStatements(null, null);
    } catch (Exception e) {
      e.printStackTrace();
      fail("exportBankStatementTest: error making bank statement export as csv file.");
    }
    // When specifying the dates.
    when(bankAccountService.getCsvFromStatements("2020-06-22", "2020-06-24"))
        .thenReturn(csvFileContent);
    try {
      mvc.perform(get("/api/export").param("dateFrom", "2020-06-22").param("dateTo", "2020-06-24"))
          .andExpect(status().isOk())
          .andExpect(content().contentType("text/csv;charset=UTF-8"))
          .andExpect(content().string(csvFileContent));
      verify(bankAccountService, times(1)).getCsvFromStatements("2020-06-22", "2020-06-24");
    } catch (Exception e) {
      e.printStackTrace();
      fail(
          "exportBankStatementTest: error making bank statement export when specifying the dates.");
    }
  }
}
