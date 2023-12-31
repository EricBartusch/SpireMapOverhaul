package spireMapOverhaul.zones.storm;

import basemod.BaseMod;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
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
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zoneInterfaces.ShopModifyingZone;
import spireMapOverhaul.zones.invasion.InvasionUtil;
import spireMapOverhaul.zones.invasion.cards.HandOfTheAbyss;
import spireMapOverhaul.zones.invasion.monsters.*;
import spireMapOverhaul.zones.storm.cardmods.DampModifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static spireMapOverhaul.util.Wiz.atb;
import static spireMapOverhaul.zones.storm.StormUtil.cardValidToMakeDamp;
import static spireMapOverhaul.zones.storm.StormUtil.countValidCardsInHandToMakeDamp;

public class StormZone extends AbstractZone implements CombatModifyingZone, RewardModifyingZone {
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

    @Override
    public void atTurnStartPostDraw() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            atb(new AbstractGameAction() {
                @Override
                public void update() {
                    int validCards = countValidCardsInHandToMakeDamp();
                    if(validCards > 0) {
                        AbstractCard card = AbstractDungeon.player.hand.getRandomCard(AbstractDungeon.cardRandomRng);
                        while (!cardValidToMakeDamp(card)) { //Get random cards until you get one you can make damp
                            card = AbstractDungeon.player.hand.getRandomCard(AbstractDungeon.cardRandomRng);
                        }
                        CardModifierManager.addModifier(card, new DampModifier());
                        card.flash();
                    }
                    isDone = true;
                }
            });
        }
    }
}
