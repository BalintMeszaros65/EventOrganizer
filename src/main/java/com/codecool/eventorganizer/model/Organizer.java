package com.codecool.eventorganizer.model;

import com.codecool.eventorganizer.utility.BasicInfoValidation;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Organizer extends AppUser implements RecommendationWeightable{
    public Organizer(@Email(groups = BasicInfoValidation.class) String email,
                     @NotBlank(groups = BasicInfoValidation.class) String password,
                     @NotBlank(groups = BasicInfoValidation.class) String firstName,
                     @NotBlank(groups = BasicInfoValidation.class) String lastName, @NotEmpty List<String> roles) {
        super(null, email, password, firstName, lastName, roles, false);
    }

    @Override
    public int calculateWeight() {
        return 14;
    }
}
