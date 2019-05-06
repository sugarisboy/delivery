package dev.muskrat.delivery.core.persons;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "USERS")
public class Person {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    @Getter
    private Long id;

    @Column(name = "first_name", length = 64, nullable = false)
    @Setter
    @Getter
    private String name;

    @Column(name = "second_name", length = 64, nullable = false)
    @Setter
    @Getter
    private String secondName;

    @Column(name = "phone", length = 16, nullable = false)
    @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$")
    private String phone;

    @Column(name = "role", length = 2, nullable = false)
    @Getter
    @Setter
    @Enumerated
    private Role role;

    /*private int getPhone() {
        String phone = this.phone.replaceAll("[\\-\\+]", "");
        try {
            return Integer.valueOf(phone);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return -1;
        }
    }*/
}
