package com.botcountsomething.telegramspringbot.database;

import jakarta.persistence.Entity;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Entity(name = "tg_data") //привязываемся к существующей таблице
public class User {

    @Id
    private long id;
    private String name;
    private int msg_numb;

    public void setId(Long id) {
        this.id = id;
    }

    @jakarta.persistence.Id
    public Long getId() {
        return id;
    }
}