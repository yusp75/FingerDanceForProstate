package com.yushealth.prostate.fingerdanceforprostate;

public class MusicFileData {

    private String name;
    private boolean inPlaying;

    public MusicFileData(String name) {
        this.name = name;
        this.inPlaying = false;
    }

    public String getName() {
        return name;
    }

    public boolean isInPlaying() {
        return inPlaying;
    }

    public void setInPlaying(boolean played) {
        this.inPlaying = played;
    }

}
