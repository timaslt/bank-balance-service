package bankbalance.database;

import bankbalance.model.BankStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/** Communicates with database and inserts, updates, selects data from tables. */
@Repository
public class BankAccountDaoImpl implements BankAccountDao {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public BankAccountDaoImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * Inserts bank statements and updates bank account balances in the database.
   *
   * @param bankStatements list of bank statements
   */
  @Override
  public void insertBankStatements(List<BankStatement> bankStatements) {
    bankStatements.forEach(
        statement -> {
          // Insert bank statement
          jdbcTemplate.update(
              "INSERT INTO bank_statement (account_number, operation_date, beneficiary_number, description, amount, currency) VALUES (?,?,?,?,?,?)",
              statement.getAccountNumber(),
              statement.getDate(),
              statement.getBeneficiaryAccountNumber(),
              statement.getComment(),
              statement.getAmount() * 100,
              statement.getCurrency());
          // Update bank account balances
          jdbcTemplate.update(
              "UPDATE bank_account SET balance = balance - ? WHERE account_number = ?",
              statement.getAmount() * 100,
              statement.getAccountNumber());
          jdbcTemplate.update(
              "UPDATE bank_account SET balance = balance + ? WHERE account_number = ?",
              statement.getAmount() * 100,
              statement.getBeneficiaryAccountNumber());
        });
  }

  @Override
  public List<BankStatement> getBankStatements(LocalDateTime dateFrom, LocalDateTime dateTo) {
    List<BankStatement> statements =
        jdbcTemplate.query(
            "SELECT * FROM bank_statement WHERE operation_date >= ? AND operation_date <= ?",
            new Object[] {dateFrom, dateTo},
            (rs, rowNum) ->
                new BankStatement(
                    rs.getString("account_number"),
                    rs.getTimestamp("operation_date").toLocalDateTime(),
                    rs.getString("beneficiary_number"),
                    rs.getString("description"),
                    rs.getBigDecimal("amount").doubleValue() / 100,
                    rs.getString("currency")));
    return statements;
  }
}
