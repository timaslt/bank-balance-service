package bankbalance.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Stores and exports bank statements.
 */
public class BankStatementServiceImpl implements BankStatementService {

    @Override
    public String saveStatementsFromCsv(MultipartFile csvFile) {
        return null;
    }
}
