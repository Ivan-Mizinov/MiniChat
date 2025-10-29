package io.synergy.minichat.dto;

public class UserDto {
    private NameDto name;
    private String gender;

    public UserDto() {
    }

    public UserDto(String gender, NameDto name) {
        this.gender = gender;
        this.name = name;
    }

    public NameDto getName() {
        return name;
    }

    public void setName(NameDto name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", name.getFirst(), name.getLast(), gender);
    }
}