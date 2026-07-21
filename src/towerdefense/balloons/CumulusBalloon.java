package towerdefense.balloons;

import towerdefense.game.GameConstants;
import towerdefense.path.Path;

/**
 * Representate a "CumulusBalloon" in Bloons Tower Defense
 * Can be considered as the standard balloon
 */
public class CumulusBalloon extends Balloon {

    /** The default life point(s) of this type of balloon */
    private static final double DEFAULT_LIFE = GameConstants.CUMULUS_LIFE;
    /** The default speed of this type of balloon */
    private static final double DEFAULT_SPEED = GameConstants.CUMULUS_SPEED;

    /**
     * Construct a CumulusBallon following a path / trajectory
     * 
     * @param trajectory the path the balloon will follow
     */
    public CumulusBalloon(Path trajectory) {
        super(trajectory);
    }

    /**
     * The CumulusBalloon has <strong>1 hit point.</strong>
     * 
     * @see Balloon#getDefaultLife()
     */
    @Override
    public double getDefaultLife() {
        return DEFAULT_LIFE;
    }

    /**
     * The CumulusBalloon has a <strong> speed of 1.</strong>
     * 
     * @see Balloon#getDefaultSpeed()
     */
    @Override
    public double getDefaultSpeed() {
        return DEFAULT_SPEED;
    }
}
