package com.st0x0ef.wonskill.jobs.actions;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
