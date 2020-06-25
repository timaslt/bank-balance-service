package bankbalance.controller;

import bankbalance.service.BankStatementService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Tests BankStatementController class. */
@ExtendWith(SpringExtension.class)
@WebMvcTest(BankStatementController.class)
public class BankStatementControllerTests {

  @Autowired private MockMvc mvc;

  @MockBean private BankStatementService bankStatementService;

  /** Test importBankStatement method creating post requests. */
  @Test
  public void importBankStatementTest() {
    // Importing valid csv file
    String csvFileContent =
        "1,2020-06-23 13:43:33,3,,419.99,EUR\n"
            + "3,2020-03-21 09:02:33,4,For food,22.50,EUR\n"
            + "4,2020-06-23 13:43:33,1,,32.00,EUR\n"
            + "2,2020-06-23 13:43:33,3,Payment for the car,3000.00,EUR";
    MockMultipartFile csvFile =
        new MockMultipartFile("file", "test1.csv", "text/csv", csvFileContent.getBytes());
    when(bankStatementService.saveStatementsFromCsv(csvFile)).thenReturn("Success");
    try {
      mvc.perform(multipart("/api/import").file(csvFile))
          .andExpect(status().isOk())
          .andExpect(content().string("Success"));
      verify(bankStatementService, times(1)).saveStatementsFromCsv(csvFile);
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
      verify(bankStatementService, times(0)).saveStatementsFromCsv(txtFile);
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
      verify(bankStatementService, times(0)).saveStatementsFromCsv(emptyFile);
    } catch (Exception e) {
      e.printStackTrace();
      fail("importBankStatementTest: error making invalid file type import (as txt file).");
    }
  }
}
