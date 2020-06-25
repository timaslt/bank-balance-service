package bankbalance.controller;

import bankbalance.service.BankStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Handles bank statement imports and exports via Rest API.
 */
@RequestMapping("/api")
@Controller
public class BankStatementController {

  private final BankStatementService bankStatementService;

  @Autowired
  public BankStatementController(BankStatementService bankStatementService) {
    this.bankStatementService = bankStatementService;
  }

  /**
   * Handles bank statement import in csv format.
   *
   * @param csvFile multipart file that contains bank statement data
   * @return string response with request status
   */
  @PostMapping(value = "/import")
  @ResponseBody
  public String importBankStatement(@RequestParam("file") MultipartFile csvFile) {
    if (!csvFile.getContentType().equals("text/csv")) {
      return "Input error: wrong file type.";
    }
    if (csvFile.isEmpty()) {
      return "Wrong input: file is empty.";
    }
    return bankStatementService.saveStatementsFromCsv(csvFile);
  }
}
