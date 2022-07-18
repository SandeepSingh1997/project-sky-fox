package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.ResultSet;
import java.sql.Statement;

public class V1_17__UpdateUsertableSetPlainTextPasswordToHashedPassword extends BaseJavaMigration {

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public void migrate(Context context) throws Exception {
        try (Statement select = context.getConnection().createStatement()) {
            try (ResultSet rows = select.executeQuery("SELECT * FROM usertable")) {
                while (rows.next()) {
                    int id = rows.getInt(1);
                    String password = rows.getString(3);
                    String hashedPassword = bCryptPasswordEncoder.encode(password);
                    try (Statement update = context.getConnection().createStatement()) {
                        update.execute("UPDATE usertable SET password='" + hashedPassword + "' WHERE id=" + id);
                    }
                }
            }
        }
    }
}
