package bankbalance.database;

import bankbalance.model.BankStatement;

import java.time.LocalDateTime;
import java.util.List;

public interface BankAccountDao {

  void insertBankStatements(List<BankStatement> bankStatements);

  List<BankStatement> getBankStatements(LocalDateTime dateFrom, LocalDateTime dateTo);
}
