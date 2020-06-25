package bankbalance.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Stores and exports bank statements.
 */
@Service
public class BankStatementServiceImpl implements BankStatementService {

    @Override
    public String saveStatementsFromCsv(MultipartFile csvFile) {
        return "Success";
    }
}
