package spireMapOverhaul.zones.characterinfluence;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.patches.ZonePatches;
import spireMapOverhaul.zoneInterfaces.ModifiedEventRateZone;
import spireMapOverhaul.zoneInterfaces.OnTravelZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zoneInterfaces.ShopModifyingZone;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class CharacterInfluenceZone extends AbstractZone implements RewardModifyingZone, ShopModifyingZone, OnTravelZone, ModifiedEventRateZone {

    public static final String ID = "CharacterInfluence";

    public CardGroup commonPool;
    public CardGroup uncommonPool;
    public CardGroup rarePool;

    public AbstractPlayer classInfluence;

    public CharacterInfluenceZone() {
        super(ID, Icons.MONSTER, Icons.EVENT, Icons.SHOP);
        this.width = 2;
        this.height = 3;
    }

    public CharacterInfluenceZone(AbstractPlayer p) {
        this();
        this.classInfluence = p;
    }

    public static AbstractPlayer getCurrentZoneCharacter() {
        AbstractZone currentZone = ZonePatches.currentZone();

        if (!(currentZone instanceof CharacterInfluenceZone) ||
                ((CharacterInfluenceZone) currentZone).classInfluence == null)
            return null;

        return ((CharacterInfluenceZone) currentZone).classInfluence;
    }

    @Override
    public void mapGenDone(ArrayList<ArrayList<MapRoomNode>> map) {
        super.mapGenDone(map);
        do {
            this.classInfluence = CardCrawlGame.characterManager.getRandomCharacter(AbstractDungeon.mapRng);
        } while (this.classInfluence.chosenClass == AbstractDungeon.player.chosenClass && CardCrawlGame.characterManager.getAllCharacters().size() != 1);
        this.name = TEXT[2] + this.classInfluence.title;
        updateDescription();
    }

    @Override
    public AbstractZone copy() {
        return new CharacterInfluenceZone(this.classInfluence);
    }

    @Override
    public Color getColor() {
        if (this.classInfluence == null) return Color.GRAY;
        return this.classInfluence.getCardRenderColor();
    }

    public void initPools() {
        commonPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
        uncommonPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
        rarePool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);

        ArrayList<AbstractCard> tmpCardList = new ArrayList<>();
        this.classInfluence.getCardPool(tmpCardList);

        for (AbstractCard c : tmpCardList) {
            switch (c.rarity) {
                case COMMON:
                    commonPool.addToTop(c);
                    continue;
                case UNCOMMON:
                    uncommonPool.addToTop(c);
                    continue;
                case RARE:
                    rarePool.addToTop(c);
                    continue;
                case CURSE:
            }
        }

        //Now, we should have gotten our pools.

    }

    public AbstractCard getCard(AbstractCard.CardRarity rarity, AbstractCard.CardType type) {
        if (commonPool == null || uncommonPool == null || rarePool == null) initPools();
        switch (rarity) {
            case UNCOMMON:
                return type != null ? uncommonPool.getRandomCard(type, true) : uncommonPool.getRandomCard(true);
            case RARE:
                return type != null ? rarePool.getRandomCard(type, true) : rarePool.getRandomCard(true);
            case COMMON:
            default:
                return type != null ? commonPool.getRandomCard(type, true) : commonPool.getRandomCard(true);
        }
    }

    public void replaceCards(ArrayList<AbstractCard> cardList) {
        ArrayList<AbstractCard> newCards = new ArrayList<>();
        for (AbstractCard card : cardList) {
            boolean dupe;
            do {
                dupe = false;
                AbstractCard cardToAdd = getCard(card.rarity, card.type);
                for (AbstractCard newCard : newCards) {
                    if (cardToAdd.cardID.equals(newCard.cardID)) {
                        dupe = true;
                        break;
                    }
                }
                if (!dupe) {
                    if (card.upgraded) cardToAdd.upgrade();
                    newCards.add(cardToAdd);
                }
            } while (dupe);
        }
        cardList.clear();
        cardList.addAll(newCards);
    }

    @Override
    public void modifyRewardCards(ArrayList<AbstractCard> rewardCards) {
        replaceCards(rewardCards);
    }

    @Override
    public void postCreateShopCards(ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
        replaceCards(coloredCards);
    }

    @Override
    public float zoneSpecificEventRate() {
        return 1;
    }

}
