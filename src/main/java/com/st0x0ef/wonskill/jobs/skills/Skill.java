package com.st0x0ef.wonskill.jobs.skills;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
