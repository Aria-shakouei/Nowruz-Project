package project.nowruz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class User extends Account{
    public String userID;
    public List<Artist> followedArtist;

    public User(String name , int age , String email , String username , String password){
        super(name , age , email , username, password);
        this.userID = username;
        this.followedArtist = new ArrayList<>();
    }

    public static Map<Object, Object> getFollowedArtists() {
        return Map.of();
    }

    public String getUserID(){
        return userID;
    }

    public void setUserID(String userID){
        this.userID = userID;
    }

    public String getRule(){
        return "User";
    }

    public List<Artist> getFollowedArtist(){
        return followedArtist;
    }

    public void setFollowedArtist(List<Artist> followedArtist){
        this.followedArtist = followedArtist;
    }

    public void followArtist(Artist artist){
        if(!followedArtist.contains(artist)){
            followedArtist.add(artist);
        }
    }

    public void unfollowArtist(Artist artist){
        followedArtist.remove(artist);
    }
}
