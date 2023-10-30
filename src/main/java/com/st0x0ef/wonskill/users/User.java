package com.st0x0ef.wonskill.users;

import com.st0x0ef.wonskill.WonSkill;
import com.st0x0ef.wonskill.jobs.actions.ActionData;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class User {
    @Setter
    String uuid;
    @Setter
    int jobID;
    int point = 0;
    int usedPoint = 0;
    int xp = 0;
    @Setter
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

    public void setPoint(int amount) {
        point = amount;
        WonSkill.db.setSkillPoints(uuid, point);
    }

    public void setUsedPoint(int amount) {
        usedPoint = amount;
        WonSkill.db.setSkillUsedPoints(uuid, usedPoint);
    }

    public void setXp(int amount) {
        xp = amount;
        WonSkill.db.setSkillXp(uuid, xp);
    }
}
