package undertale.Utils;

public enum Difficulty {
    SIMPLE(0.7f, 0.7f, 0.6f, 5.0f, 2.0f, 32),
    NORMAL(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 32),
    HARD(1.0f, 1.0f, 1.25f, 2.0f, 0.7f, 64);

    private final float bulletSpeedMultiplier;
    private final float bulletDamageMultiplier;
    private final float bossHealthMultiplier;
    private final float tpGainMultiplier;
    private final float spawnIntervalMultiplier;
    private final int startingHealth;

    Difficulty(float bulletSpeed, float bulletDamage, float bossHealth, float tpGain, float spawnInterval, int startingHealth) {
        this.bulletSpeedMultiplier = bulletSpeed;
        this.bulletDamageMultiplier = bulletDamage;
        this.bossHealthMultiplier = bossHealth;
        this.tpGainMultiplier = tpGain;
        this.spawnIntervalMultiplier = spawnInterval;
        this.startingHealth = startingHealth;
    }

    public float getBulletSpeedMultiplier() {
        return bulletSpeedMultiplier;
    }

    public float getBulletDamageMultiplier() {
        return bulletDamageMultiplier;
    }

    public float getBossHealthMultiplier() {
        return bossHealthMultiplier;
    }

    public float getTpGainMultiplier() {
        return tpGainMultiplier;
    }

    public float getSpawnIntervalMultiplier() {
        return spawnIntervalMultiplier;
    }

    public int getStartingHealth() {
        return startingHealth;
    }
}
