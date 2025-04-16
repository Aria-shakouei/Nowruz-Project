package project.nowruz;

import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Song {
    public int songId;
    public String title;
    public String genre;
    public String album;
    public Artist artist;
    public int duration;
    public Date releaseYear;
    public int likeCount;
    public byte[] audioData;
    public String audioSourceType;
    public String originalFileName;
    public byte[] coverImageData;
    public Image coverImage;
    public String lyrics;
    public List<Comment> comments;

    public Song(int songId, String title, String genre, String album,Artist artist,
                int duration, Date releaseYear, int likeCount) {
        this.songId = songId;
        this.title = title;
        this.genre = genre;
        this.album = album;
        this.artist = artist;
        this.duration = duration;
        this.releaseYear = releaseYear;
        this.likeCount = likeCount;
        this.comments = new ArrayList<>();
    }

    // Getters and setters
    public int getSongId() { return songId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getAlbum() { return album; }
    public void setAlbum(String album) { this.album = album; }
    public Artist getArtist() { return artist; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public Date getReleaseYear() { return releaseYear; }
    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }

    public boolean hasAudio() {
        return audioData != null && audioData.length > 0;
    }

    public byte[] getAudioData() { return audioData; }
    public void setAudioData(byte[] audioData) { this.audioData = audioData; }

    public String getAudioSourceType() { return audioSourceType; }
    public void setAudioSourceType(String audioSourceType) { this.audioSourceType = audioSourceType; }

    public String getOriginalFileName() { return originalFileName; }
    public void setOriginalFileName(String originalFileName) { this.originalFileName = originalFileName; }

    public String getLyrics() { return lyrics; }
    public void setLyrics(String lyrics) { this.lyrics = lyrics; }

    public List<Comment> getComments() { return comments; }
    public void addComment(Comment comment) { comments.add(comment); }

    public byte[] getCoverImageData() { return coverImageData; }
    public void setCoverImageData(byte[] coverImageData) {
        this.coverImageData = coverImageData;
        if (coverImageData != null) {
            this.coverImage = new Image(new java.io.ByteArrayInputStream(coverImageData));
        }
    }

    public Image getCoverImage() { return coverImage; }
}