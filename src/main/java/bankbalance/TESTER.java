package bankbalance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TESTER {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public TESTER(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @RequestMapping("/reset")
  public String reset() {
    jdbcTemplate.execute("DELETE FROM bank_statement");
    jdbcTemplate.execute("DELETE FROM bank_account");
    jdbcTemplate.execute(
        "INSERT INTO bank_account (account_number, balance, currency)\n"
            + "VALUES\n"
            + "\t(1, 50000, 'EUR'),\n"
            + "\t(2, 55000, 'EUR'),\n"
            + "\t(3, 60000, 'USD'),\n"
            + "\t(4, 70000, 'USD'),\n"
            + "\t(5, 80000, 'EUR')");
    System.out.println("Successful reset.");
    return "";
  }
}
