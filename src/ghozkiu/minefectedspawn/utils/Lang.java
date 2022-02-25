package ghozkiu.minefectedspawn.utils;

public enum Lang {


    SP_CREATED("Lang.spawnpoint-created"),
    SP_REMOVED("Lang.spawnpoint-removed"),
    PLAYER_TP("Lang.player-teleported"),
    SP_TITLE("Lang.spawnpoint-title"),
    SP_SUBTITLE("Lang.spawnpoint-subtitle"),
    SP_NOT_FOUND("Lang.spawnpoint-not-found"),
    SP_NAME_ERROR("Lang.spawnpoint-name-error"),
    SP_CREATION_ERROR("Lang."),
    SP_REMOVE_ERROR("Lang."),
    SP_ALREADY_EXISTS("Lang.spawnpoint-already-exists"),
    SP_PERMISSION_ERROR("Lang.spawnpoint-no-permission");
    private final String type;

    Lang(String param) {
        this.type = param;
    }

    public String get() {
        return this.type;
    }
}
