package dev.tpcoder.springredisreactive.model;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Item implements Serializable {

    private String id;
    private String name;
    private Long amount;
}
