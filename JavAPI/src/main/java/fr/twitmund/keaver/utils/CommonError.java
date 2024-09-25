package fr.twitmund.keaver.utils;

public enum CommonError {
    InvalidToken("Invalid token");
    private final String str;
    CommonError(String str) {
        this.str = str;
    }

    public String getValue() {
        return str;
    }
}
