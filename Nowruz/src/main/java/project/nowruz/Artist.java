package project.nowruz;

import java.util.ArrayList;
import java.util.List;

public class Artist extends Account{
    public String userID;
    public String Biography;
    public List<Album> albums;
    public List<String> socialLinks;

    public Artist(String name , int age , String email , String username , String password , String Biography) {
        super(name, age, email, username, password);
        this.userID = username;
        this.Biography = Biography;
        this.albums = new ArrayList<>();
        this.socialLinks = new ArrayList<>();
    }

    public String getUserID() {
        return userID;
    }

    public String getBiography() {
        return Biography;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void addAlbum(Album album) {
        albums.add(album);
    }

    public List<String> getSocialLinks() {
        return socialLinks;
    }

    public void addSocialLink(String Link) {
        socialLinks.add(Link);
    }

    public void setBiography(String text) {
        this.Biography = text;
    }
}
