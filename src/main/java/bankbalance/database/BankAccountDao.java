package bankbalance.database;

import bankbalance.model.BankStatement;

import java.util.List;

public interface BankAccountDao {

  void insertBankStatements(List<BankStatement> bankStatements);
}
