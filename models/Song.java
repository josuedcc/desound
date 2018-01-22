package cc.devjo.desound.models;

/**
 * Created by Josue on 01/02/2016.
 */
public class Song {
    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

   /* public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }*/

    public Song(String songName, String idVideo, String urlImgDef, String urlPlay, String urlPlay2) {
        this.songName = songName;
        this.idVideo = idVideo;
        this.urlImgDef = urlImgDef;
        this.urlPlay = urlPlay;
        this.urlPlay2 = urlPlay2;
        // this.stars = stars;
    }

    public String songName;


    public String getUrlPlay() {
        return urlPlay;
    }

    public void setUrlPlay(String urlPlay) {
        this.urlPlay = urlPlay;
    }

    public String urlPlay;

    public String getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(String idVideo) {
        this.idVideo = idVideo;
    }

    public String idVideo;

    public String getUrlPlay2() {
        return urlPlay2;
    }

    public void setUrlPlay2(String urlPlay2) {
        this.urlPlay2 = urlPlay2;
    }

    public String urlPlay2;

    public String getUrlImgDef() {
        return urlImgDef;
    }

    public void setUrlImgDef(String urlImgDef) {
        this.urlImgDef = urlImgDef;
    }

    public String urlImgDef;
    //public int stars;
}