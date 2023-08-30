package fr.holo795.wonskill.jobs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.holo795.wonskill.jobs.actions.Action;
import fr.holo795.wonskill.jobs.skills.Skill;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Job {

    int id;
    String name;
    String description;
    String permission;
    List<Skill> skills = new ArrayList<>();
    List<Action> actions = new ArrayList<>();
    List<Integer> exp_to_point = new ArrayList<>();

}
