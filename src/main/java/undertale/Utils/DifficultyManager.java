package undertale.Utils;

public class DifficultyManager {
    private static DifficultyManager instance;
    private Difficulty currentDifficulty = Difficulty.NORMAL;

    private DifficultyManager() {
        this.currentDifficulty = Difficulty.NORMAL;
    }

    public static DifficultyManager getInstance() {
        if (instance == null) {
            synchronized (DifficultyManager.class) {
                if (instance == null) {
                    instance = new DifficultyManager();
                }
            }
        }
        return instance;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.currentDifficulty = difficulty;
    }

    public Difficulty getDifficulty() {
        return currentDifficulty;
    }

    public float getBulletSpeedMultiplier() {
        return currentDifficulty.getBulletSpeedMultiplier();
    }

    public float getBulletDamageMultiplier() {
        return currentDifficulty.getBulletDamageMultiplier();
    }

    public float getBossHealthMultiplier() {
        return currentDifficulty.getBossHealthMultiplier();
    }

    public float getTpGainMultiplier() {
        return currentDifficulty.getTpGainMultiplier();
    }

    public float getSpawnIntervalMultiplier() {
        return currentDifficulty.getSpawnIntervalMultiplier();
    }

    public int getStartingHealth() {
        return currentDifficulty.getStartingHealth();
    }
}
