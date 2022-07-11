package com.booking.passwordHistory.repository;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "password_history")
public class PasswordHistory {

    @EmbeddedId
    @NotNull(message = "User id and password must be provided")
    private PasswordHistoryPK passwordHistoryPK;

    @NotNull(message = "Created at must be provided")
    private Timestamp createdAt;

    public PasswordHistory() {
    }

    public PasswordHistory(PasswordHistoryPK passwordHistoryPK, Timestamp createdAt) {
        this.passwordHistoryPK = passwordHistoryPK;
        this.createdAt = createdAt;
    }

    public PasswordHistoryPK getPasswordHistoryPK() {
        return passwordHistoryPK;
    }

    public void setPasswordHistoryPK(PasswordHistoryPK passwordHistoryPK) {
        this.passwordHistoryPK = passwordHistoryPK;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
