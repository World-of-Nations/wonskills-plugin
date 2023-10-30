package com.st0x0ef.wonskill.jobs;

import com.st0x0ef.wonskill.jobs.actions.Action;
import com.st0x0ef.wonskill.jobs.skills.Skill;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
