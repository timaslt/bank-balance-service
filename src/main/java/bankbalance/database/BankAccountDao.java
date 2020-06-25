package bankbalance.database;

import bankbalance.model.BankStatement;

import java.util.List;

public interface BankAccountDao {

  void insertBankStatement(List<BankStatement> bankStatements);
}
