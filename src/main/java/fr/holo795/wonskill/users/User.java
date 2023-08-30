package fr.holo795.wonskill.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.holo795.wonskill.jobs.actions.ActionData;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class User {

    String uuid;
    int jobID;
    int point = 0;
    int usedPoint = 0;
    int xp = 0;
    List<Integer> unlockedSkills = new ArrayList<>();
    List<ActionData> actionData = new ArrayList<>();

    public ActionData getActionData(int id) {
        for (ActionData actionData : actionData) {
            if (actionData.getId() == id) {
                return actionData;
            }
        }
        return null;
    }

}
