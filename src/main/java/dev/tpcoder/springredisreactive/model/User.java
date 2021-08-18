package dev.tpcoder.springredisreactive.model;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
}
