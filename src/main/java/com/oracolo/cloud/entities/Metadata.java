package com.oracolo.cloud.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.Instant;

@Embeddable
public class Metadata {

    @Column(name = "insert_date")
    private Instant insertDate;

    @Column(name = "update_date")
    private Instant updateDate;

    public Instant getInsertDate() {
        return insertDate;
    }

    public Metadata setInsertDate(Instant insertDate) {
        this.insertDate = insertDate;
        return this;
    }

    public Instant getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "insertDate=" + insertDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
