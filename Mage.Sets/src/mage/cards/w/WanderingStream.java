
package mage.cards.w;

import java.util.UUID;
import mage.abilities.dynamicvalue.MultipliedValue;
import mage.abilities.dynamicvalue.common.DomainValue;
import mage.abilities.effects.Effect;
import mage.abilities.effects.common.GainLifeEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;

/**
 *
 * @author LoneFox
 */
public final class WanderingStream extends CardImpl {

    public WanderingStream(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.SORCERY},"{2}{G}");

        // Domain - You gain 2 life for each basic land type among lands you control.
        Effect effect = new GainLifeEffect(new MultipliedValue(new DomainValue(), 2));
        effect.setText("Domain - You gain 2 life for each basic land type among lands you control");
        this.getSpellAbility().addEffect(effect);
     }

    public WanderingStream(final WanderingStream card) {
        super(card);
    }

    @Override
    public WanderingStream copy() {
        return new WanderingStream(this);
    }
}
