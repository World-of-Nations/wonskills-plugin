package fr.holo795.wonskill.jobs.actions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Action {

    int id;
    String type;
    List<String> item = new ArrayList<>();
    List<String> block = new ArrayList<>();
    List<String> entity = new ArrayList<>();
    int amount;
    int xp;
}
