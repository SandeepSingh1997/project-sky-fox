package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.ResultSet;
import java.sql.Statement;

public class V1_17__UpdatePasswordHistoryTableSetPlainTextPasswordToHashedPassword extends BaseJavaMigration {

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public void migrate(Context context) throws Exception {
        try (Statement select = context.getConnection().createStatement()) {
            try (ResultSet rows = select.executeQuery("SELECT * FROM password_history")) {
                while (rows.next()) {
                    int userId = rows.getInt(1);
                    String password = rows.getString(2);
                    String hashedPassword = bCryptPasswordEncoder.encode(password);
                    try (Statement update = context.getConnection().createStatement()) {
                        update.execute("UPDATE password_history SET password='" + hashedPassword + "' WHERE user_id=" + userId);
                    }
                }
            }
        }
    }
}