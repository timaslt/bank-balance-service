package bankbalance.controller;

import bankbalance.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/** Handles bank statement imports and exports via Rest API. */
@RequestMapping("/api")
@Controller
public class BankAccountController {

  private final BankAccountService bankAccountService;

  @Autowired
  public BankAccountController(BankAccountService bankAccountService) {
    this.bankAccountService = bankAccountService;
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
    return bankAccountService.saveStatementsFromCsv(csvFile);
  }

  /**
   * Handles get request by returning csv file with bank statements from during the specified period
   * or from the start till now.
   *
   * @param dateFrom string represents optional date in format yyyy-MM-dd
   * @param dateTo string represents optional date in format yyyy-MM-dd
   * @return csv file
   */
  @GetMapping(value = "/export", produces = "text/csv")
  @ResponseBody
  public String exportBankStatement(
      @RequestParam(required = false) String dateFrom,
      @RequestParam(required = false) String dateTo) {
    return bankAccountService.getCsvFromStatements(dateFrom, dateTo);
  }

  /**
   * Handles get request for calculating bank account balance by returning txt file.
   *
   * @param accountNumber string bank account id
   * @param dateFrom string represents optional date in format yyyy-MM-dd
   * @param dateTo string represents optional date in format yyyy-MM-dd
   * @return txt file
   */
  @GetMapping(value = "/balance", produces = "text/plain")
  @ResponseBody
  public String calculateAccountBalance(
          @RequestParam(required = true) String accountNumber,
          @RequestParam(required = false) String dateFrom,
          @RequestParam(required = false) String dateTo) {
    return bankAccountService.getAccountBalance(accountNumber, dateFrom, dateTo);
  }
}
