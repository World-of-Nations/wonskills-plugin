package fr.holo795.wonskill.jobs.actions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ActionData {

    int id;
    int amount = 0;

}
