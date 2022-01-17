package com.bot.bottelegrama.domain;

public class BotUser {
    private Long id;
    private String username;
    private Position position;
    private String kurs;
    private String year;
    private String group;
    private String yourKurs;

    public String getYourKurs() {
        return yourKurs;
    }

    public void setYourKurs(String yourKurs) {
        this.yourKurs = yourKurs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getKurs() {
        return kurs;
    }

    public void setKurs(String kurs) {
        this.kurs = kurs;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
