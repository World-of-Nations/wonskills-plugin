package fr.holo795.wonskill.jobs.skills;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Skill {

    int id;
    String name;
    String description;
    List<String> effects = new ArrayList<>();
    List<String> permissions_add = new ArrayList<>();
    List<String> permissions_remove = new ArrayList<>();
    int cost;
}
