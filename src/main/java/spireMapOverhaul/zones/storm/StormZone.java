package spireMapOverhaul.zones.storm;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zoneInterfaces.ShopModifyingZone;
import spireMapOverhaul.zones.invasion.InvasionUtil;
import spireMapOverhaul.zones.invasion.cards.HandOfTheAbyss;
import spireMapOverhaul.zones.invasion.monsters.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StormZone extends AbstractZone implements EncounterModifyingZone, RewardModifyingZone, ShopModifyingZone {
    public static final String ID = "Storm";

    public StormZone() {
        super(ID, Icons.MONSTER, Icons.ELITE);
        this.width = 3;
        this.height = 4;
    }

    @Override
    public AbstractZone copy() {
        return new StormZone();
    }

    @Override
    public Color getColor() {
        return Color.WHITE.cpy();
    }


    @Override
    public void modifyReward(RewardItem rewardItem) {

    }

    @Override
    public void modifyRewards(ArrayList<RewardItem> rewards) {

    }
}
