package undertale.Scene;

import undertale.GameMain.InputManager;
import undertale.GameMain.Game;
import undertale.GameObject.ObjectManager;
import undertale.Sound.SoundManager;
import undertale.Texture.FontManager;
import undertale.UI.UIManager;
import undertale.Utils.Difficulty;
import undertale.Utils.DifficultyManager;
import static org.lwjgl.glfw.GLFW.*;

public class DifficultySelectScene extends Scene {
    private int selectedIndex = 1;
    private static final int OPTIONS_COUNT = 3;
    private static final String[] DIFFICULTY_NAMES = {"SIMPLE", "NORMAL", "HARD"};
    private static final String[] DIFFICULTY_DESCRIPTIONS = {
        "* Reduced bullets.",
        "* Standard challenge.",
        "* Increased speed."
    };
    private SoundManager soundManager;
    private FontManager fontManager;

    public DifficultySelectScene(ObjectManager objectManager, InputManager inputManager, UIManager uiManager) {
        super(objectManager, inputManager, uiManager);
        this.soundManager = SoundManager.getInstance();
        this.fontManager = FontManager.getInstance();
        init();
    }

    @Override
    public void init() {
        selectedIndex = 1;
    }

    @Override
    public void onEnter() {
        registerAsObserver();
        selectedIndex = 1;
        soundManager.playMusic("titan_battle");
    }

    @Override
    public void onExit() {
        unregisterAsObserver();
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void render() {
        fontManager.drawText("* Choose your difficulty.", 100, 550, 1.0f, 1.0f, 1.0f, 1.0f);

        float baseY = 450;
        float spacing = 80;

        for (int i = 0; i < OPTIONS_COUNT; i++) {
            float y = baseY - i * spacing;
            if (i == selectedIndex) {
                fontManager.drawText(">", 70, y, 1.0f, 1.0f, 1.0f, 1.0f);
                fontManager.drawText(DIFFICULTY_NAMES[i], 100, y, 1.0f, 1.0f, 0.0f, 1.0f);
            } else {
                fontManager.drawText(DIFFICULTY_NAMES[i], 100, y, 1.0f, 1.0f, 1.0f, 1.0f);
            }
        }

        fontManager.drawText(DIFFICULTY_DESCRIPTIONS[selectedIndex], 100, baseY - OPTIONS_COUNT * spacing - 30, 1.0f, 1.0f, 1.0f, 1.0f);

        fontManager.drawText("* Press Z to confirm.", 100, 100, 1.0f, 1.0f, 1.0f, 1.0f);
        fontManager.drawText("* Press X to go back.", 100, 70, 1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public SceneEnum getCurrentScene() {
        return SceneEnum.DIFFICULTY_SELECT;
    }

    @Override
    public void processInput(boolean[] preKeyStates, boolean[] currKeyStates) {
        if (currKeyStates[GLFW_KEY_UP] && !preKeyStates[GLFW_KEY_UP]) {
            soundManager.playSE("menu_move");
            selectedIndex = (selectedIndex + 1) % OPTIONS_COUNT;
        }
        if (currKeyStates[GLFW_KEY_DOWN] && !preKeyStates[GLFW_KEY_DOWN]) {
            soundManager.playSE("menu_move");
            selectedIndex = (selectedIndex - 1 + OPTIONS_COUNT) % OPTIONS_COUNT;
        }
        if (currKeyStates[GLFW_KEY_Z] && !preKeyStates[GLFW_KEY_Z]) {
            soundManager.playSE("confirm");
            Difficulty[] difficulties = {Difficulty.SIMPLE, Difficulty.NORMAL, Difficulty.HARD};
            DifficultyManager.getInstance().setDifficulty(difficulties[selectedIndex]);
            // 选择难度后重置玩家和敌人状态
            Game.getPlayer().resetPlayer();
            Game.getObjectManager().getEnemyManager().resetEnemies();
            SceneManager.getInstance().switchScene(SceneEnum.BATTLE_MENU, true);
        }
        if (currKeyStates[GLFW_KEY_X] && !preKeyStates[GLFW_KEY_X]) {
            soundManager.playSE("menu_move");
            SceneManager.getInstance().switchScene(SceneEnum.START_MENU, true);
        }
    }
}
