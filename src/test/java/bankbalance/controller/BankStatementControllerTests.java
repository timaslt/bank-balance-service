package bankbalance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Tests BankStatementController class. */
@ExtendWith(SpringExtension.class)
@WebMvcTest(BankStatementController.class)
public class BankStatementControllerTests {

  @Autowired private MockMvc mvc;

  @Autowired ObjectMapper objectMapper;

  @Test
  public void importBankStatementTest() {
//    String csvFile =
//        "acountId,date,beneficiaryId,comment,amount,currency\n"
//            + "1,2020-06-23 13:43:33,3,,419.99,EUR\n"
//            + "3,2020-03-21 9:2:33,4,For food,22.50,EUR\n"
//            + "4,2020-06-23 13:43:33,1,,32.00,EUR\n"
//            + "2,2020-06-23 13:43:33,3,Payment for the car,3000.00,EUR";
//    try {
//      mvc.perform(
//              post("/api/import")
//                  .contentType("text/csv")
//                  .content(objectMapper.writeValueAsString(csvFile)))
//          .andExpect(status().isOk());
//    } catch (Exception e) {
//      e.printStackTrace();
//      fail("importBankStatementTest: error making bank statement import as csv file.");
//    }
  }
}
