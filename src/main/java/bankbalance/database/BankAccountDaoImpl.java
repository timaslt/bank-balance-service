package bankbalance.database;

import bankbalance.model.BankStatement;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Communicates with database and inserts, updates, selects data from tables.
 */
@Repository
public class BankAccountDaoImpl implements BankAccountDao {

    /**
     * Checks validity of the statements and updates the database.
     * @param bankStatements list of bank statements
     */
    @Override
    public void insertBankStatements(List<BankStatement> bankStatements) {

    }
}
