package net.tkdkid1000.platformer;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.texture.Texture;

import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

import java.util.Arrays;
import java.util.EnumSet;

import com.almasb.fxgl.achievement.Achievement;
import com.almasb.fxgl.achievement.AchievementEvent;

public class App extends GameApplication {

	public Entity map;
	public Entity player;
	public static Text text;
	private static App instance;
	
	public static App getInstance() {
		return instance;
	}
	
	@Override
	protected void initSettings(GameSettings settings) {
		settings.setWidth(1200);
		settings.setHeight(600);
		settings.setTitle("Platformer");
		settings.setVersion("0.0.1-DEV-SNAPSHOT");
		settings.setFullScreenAllowed(true);
		settings.setFullScreenFromStart(false);
		settings.setAppIcon("armi.png");
		settings.setApplicationMode(ApplicationMode.DEVELOPER);
		settings.setMainMenuEnabled(false);
		settings.setCloseConfirmation(true);
		settings.setSceneFactory(new SceneFactory() {
			@Override
			public FXGLMenu newGameMenu() {
				return new MainMenu();
			}
			@Override
			public FXGLMenu newMainMenu() {
				return new OptionsMenu();
			}
		});
		settings.setProfilingEnabled(true);
		settings.setIntroEnabled(false);
		settings.setClickFeedbackEnabled(true);
		settings.setManualResizeEnabled(false);
		settings.setCredits(Arrays.asList(new String[] {"TKDKid1000", "github.com/TKDKid1000"}));
	}
	
	@Override
	protected void initGame() {
		FXGL.getGameScene().setBackgroundColor(Color.RED);
		FXGL.getWorldProperties().setValue("a.jumps", 0);
		FXGL.getAchievementService().registerAchievement$fxgl_achievement(new Achievement("Jumpman", "Jump 100 times.", "a.jumps", 100));
		FXGL.getWorldProperties().setValue("a.leavebox", false);
		FXGL.getAchievementService().registerAchievement$fxgl_achievement(new Achievement("Out of the box", "Leave the play area.", "a.leavebox", true));
		instance = this;
		getGameScene().getRoot().setCursor(Cursor.DEFAULT);
		spawnPlayer();
		spawnMap();
		text = FXGL.addText("tracker", 300, 550);
		//run(() -> spawnFallingBlock(), Duration.seconds(10));
		run(() -> {
			getGameWorld().getEntitiesByType(Type.PLAYER).forEach(player -> {
				if (player.getBottomY() < map.getBottomY()) {
					player.setY(player.getY()+1);
				}
			});
		}, Duration.millis(1));
		
		loopBGM("bgm.mp3");
		new KeyBinds().register();
		FXGL.getEventBus().addEventHandler(AchievementEvent.ACHIEVED, e -> {
			Achievement achievement = e.getAchievement();
			FXGL.getNotificationService().pushNotification("Achievement Get! " + achievement.getName());
			FXGL.getNotificationService().pushNotification(achievement.getDescription());
		});
	}
	
	@Override
	protected void initPhysics() {
		EnumSet<Type> mapcore = EnumSet.of(Type.FALLINGMAP, Type.MAP);
		mapcore.forEach(maptype -> {
			onCollision(Type.PLAYER, maptype, (player, map) -> {
				while (player.getBottomY() < map.getY() && player.getRightX() > map.getX()) {
					player.translateX(-1);
				}
				while (player.getBottomY() < map.getY() && player.getX() < map.getRightX()) {
					player.translateX(1);
				}
				while (player.getBottomY() > map.getY() && player.getY() < map.getBottomY()) {
					player.translateY(-1);
				}
//				while (player.getY() < map.getBottomY()) {
//					player.translateY(1);
//				}
			});
		});
		onCollisionBegin(Type.PLAYER, Type.MAP, (player, map) -> {
			player.translateY(-1);
		});
	}
	
	@Override
	protected void onUpdate(double tpf) {
		getGameWorld().getEntitiesByType(Type.PLAYER).forEach(player -> {
			if (FXGL.getWorldProperties().getBoolean("a.leavebox")) {
				while (player.getRightX() > getAppWidth()) {
					player.translateX(-1);
				}
				while (player.getX() < 0) {
					player.translateX(1);
				}
			} else {
				if (player.getX() > getAppWidth() || player.getRightX() < 0) {
					FXGL.getWorldProperties().setValue("a.leavebox", true);
				}
			}
		});
		getGameWorld().getEntitiesByType(Type.FALLINGMAP).forEach(falling -> falling.translateY(50 * tpf));
	}

	private void spawnPlayer() {
		Texture texture = FXGL.getAssetLoader().loadTexture("player.png", 64, 128);
		this.player = entityBuilder()
				.type(Type.PLAYER)
				.at(100, getAppHeight())
				.viewWithBBox(texture)
				.collidable()
				.buildAndAttach();
		player.addComponent(new IDComponent("player", 1));
	}
	
	private void spawnMap() {
		this.map = entityBuilder()
				.at(0, getAppHeight()-100)
				.viewWithBBox(new Rectangle(getAppWidth(), 100, Color.BLACK))
				.type(Type.MAP)
				.collidable()
				.buildAndAttach();
		map.addComponent(new IDComponent("map", 2));
		entityBuilder()
		.at(300, 400)
		.viewWithBBox(new Rectangle(50, 100, Color.BLACK))
		.type(Type.MAP)
		.collidable()
		.buildAndAttach();
		entityBuilder()
		.at(500, 300)
		.viewWithBBox(new Rectangle(50, 200, Color.BLUE))
		.type(Type.MAP)
		.collidable()
		.buildAndAttach();
		entityBuilder()
		.at(700, 200)
		.viewWithBBox(new Rectangle(50, 300, Color.GREEN))
		.type(Type.MAP)
		.collidable()
		.buildAndAttach();
		entityBuilder()
		.at(900, 100)
		.viewWithBBox(new Rectangle(50, 400, Color.YELLOW))
		.type(Type.MAP)
		.collidable()
		.buildAndAttach();
	}

	public void spawnFallingBlock() {
		Texture texture = FXGL.getAssetLoader().loadTexture("armi.png", 128, 128);
		entityBuilder()
				.type(Type.FALLINGMAP)
				.at(FXGLMath.random(0, getAppWidth() - 64), 0)
				.viewWithBBox(texture)
				.collidable()
				.buildAndAttach();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
