package fr.twitmund.keaver.utils;

public enum DbError {

    //IllegalStateException,
    //IllegalArgumentException,
    //SQLException,
    InternalServerError("InternalServerError"),
    DuplicateKey("DuplicateKeyException"),
    Success("Success");

    private final String str;
    DbError(String str) {
        this.str = str;
    }

    public String getValue() {
        return str;
    }


    }
