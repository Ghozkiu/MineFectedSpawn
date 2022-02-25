package ghozkiu.minefectedspawn.utils;

public enum Permissions {
    USER("Permissions.spawnpoint-user"),
    STAFF("Permissions.spawnpoint-staff");
    private final String type;

    Permissions(String paramString1) {
        this.type = paramString1;
    }

    public String get() {
        return this.type;
    }
}