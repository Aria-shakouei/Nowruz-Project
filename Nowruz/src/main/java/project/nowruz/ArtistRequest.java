package project.nowruz;


public class ArtistRequest {
    public String name;
    public int age;
    public String email;
    public String username;
    public String password;
    public String biography;

    public ArtistRequest(String name, int age, String email, String username, String password, String biography) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.username = username;
        this.password = password;
        this.biography = biography;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getBiography() { return biography; }
}