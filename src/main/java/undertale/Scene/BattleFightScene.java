package undertale.Scene;

import java.util.ArrayList;

import undertale.Enemy.Enemy;
import undertale.Enemy.EnemyManager;
import undertale.Enemy.Titan;
import undertale.GameMain.InputManager;
import undertale.GameObject.ObjectManager;
import undertale.Scene.Rounds.*;
import undertale.Sound.SoundManager;
import undertale.UI.UIManager;

public class BattleFightScene extends Scene {
    private EnemyManager enemyManager;
    private int phase = 0; // 0-2
    private int phaseRound = 0; // 0-2
    private int currentRoundIndex = 0; // 当前回合索引
    private ArrayList<Round> rounds;
    private long roundTime;
    private boolean isSpecial = false;

    public BattleFightScene(ObjectManager objectManager, InputManager inputManager, UIManager uiManager, EnemyManager enemyManager) {
        super(objectManager, inputManager, uiManager);
        this.enemyManager = enemyManager;
        init();
    }

    @Override
    public void init() {
        phase = 0;
        phaseRound = -1;
        initRounds();
        roundTime = 0;
    }

    @Override
    public void onEnter() {
        registerAsObserver();
        roundTime = 0;
        
        if(isSpecial) {
            // 特殊回合：使用索引12的RoundSpecial
            currentRoundIndex = 12;
            isSpecial = false;
        } else {
            phaseRound++;
            if (phaseRound > 2) {
                // 第三阶段后循环重复三个回合，不增加phase
                if (phase < 2) {
                    phase++;
                }
                phaseRound = 0;
            }
            currentRoundIndex = phase * 4 + phaseRound;
        }
        
        initRounds();
        rounds.get(currentRoundIndex).onEnter();
        uiManager.setSelected(-1);
        objectManager.initPlayerPosition();
        objectManager.startPlayerLightExpansion();
        objectManager.allowPlayerMovement(true);
    }

    /**
     * 重置回合状态（用于新游戏开始）
     */
    public void reset() {
        phase = 0;
        phaseRound = -1;
        currentRoundIndex = 0;
        isSpecial = false;
        roundTime = 0;
    }

    private void initRounds() {
        rounds = new ArrayList<>();
        for(int p = 0; p < 3; p++) {
            rounds.add(new RoundSwarm(p + 1, 16000, 1500, uiManager));
            rounds.add(new RoundSnake(p + 1, 17000, 1500, uiManager));
            rounds.add(new RoundFinger(p + 1, 22000, 1500, uiManager));
            rounds.add(new RoundSpecial(p + 1, 17000, 1500, uiManager, enemyManager));
        }
        rounds.add(new RoundSpecial(3, 17000, 1500, uiManager, enemyManager));
    }

    /**
     * 返回当前回合编号（0-based）
     */
    public int getRoundNumber() {
        return currentRoundIndex;
    }

    public void afterUnleash() {
        // 进入下一个阶段
        if (phase < 2) {
            phase = phase + 1;
            isSpecial = true;
        } else {
            // 第三阶段使用unleash触发特殊回合
            isSpecial = true;
        }
    }

    @Override
    public void onExit() {
        unregisterAsObserver();
        objectManager.resetPlayerLight();
        objectManager.allowPlayerMovement(false);
        uiManager.setSelected(0);
        objectManager.clearBullets();
        objectManager.clearCollectables();
        objectManager.clearRipples();
        objectManager.clearTitanSpawnParticles();
    }

    @Override
    public void update(float deltaTime) {
        if(!objectManager.isPlayerAlive()) {
            SceneManager.getInstance().switchScene(SceneEnum.GAME_OVER, true);
            return;
        }
        roundTime += deltaTime * 1000;
        Round currentRound = rounds.get(currentRoundIndex);

        currentRound.moveBattleFrame(deltaTime);
        
        if(roundTime > currentRound.getFrameMoveTime()) {
            currentRound.updateRound(deltaTime);
        }
        if(roundTime >= currentRound.getRoundDuration()) {
            SceneManager.getInstance().shouldSwitch = true;
        }
        objectManager.updateFightScene(deltaTime);
        uiManager.makePlayerInFrame();
        // 持续播放 spawn_attack SE
        if (!SoundManager.getInstance().isSePlaying("spawn_attack")) {
            SoundManager.getInstance().playSE("spawn_attack");
        }
        // 回合结束，处理Titan的weaken状态
        Enemy currentEnemy = enemyManager.getCurrentEnemy();
        if (SceneManager.getInstance().shouldSwitch && currentEnemy instanceof Titan) {
            ((Titan) currentEnemy).endTurn();
        }
        SceneManager.getInstance().switchScene(SceneEnum.BATTLE_MENU);
    }

    @Override
    public void render() {
        enemyManager.render();
        uiManager.renderBattleUI();
        objectManager.renderFightScene();
        Round currentRound = rounds.get(currentRoundIndex);
        currentRound.render();
    }

    @Override
    public SceneEnum getCurrentScene() {
        return SceneEnum.BATTLE_FIGHT;
    }

    public int getPhase() {
        return phase;
    }

    @Override
    public void processInput(boolean[] preKeyStates, boolean[] currKeyStates) {}

    @Override
    protected void registerAsObserver() {
        inputManager.addObserver(objectManager.getPlayer());
    }

    @Override
    protected void unregisterAsObserver() {
        inputManager.removeObserver(objectManager.getPlayer());
    }

    public int getPhaseRound() {
        return phaseRound;
    }

    public enum RoundKind { SWARM, SNAKE, FINGER, UNKNOWN }

    public RoundKind getCurrentRoundKind() {
        if (rounds == null || rounds.size() <= currentRoundIndex || currentRoundIndex < 0) return RoundKind.UNKNOWN;
        Round r = rounds.get(currentRoundIndex);
        if (r instanceof RoundSwarm) return RoundKind.SWARM;
        if (r instanceof RoundSnake) return RoundKind.SNAKE;
        if (r instanceof RoundFinger) return RoundKind.FINGER;
        return RoundKind.UNKNOWN;
    }
}