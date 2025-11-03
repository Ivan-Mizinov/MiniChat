package io.synergy.minichat.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContactDto {
    private Long id;
    private String lastName;
    private String firstName;
    private String middleName;
    private String phone;

    public ContactDto() {
    }

    public ContactDto(Long id, String lastName, String firstName, String middleName, String phone) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return lastName + " " + firstName + " " + middleName + ", " + phone;
    }
}
