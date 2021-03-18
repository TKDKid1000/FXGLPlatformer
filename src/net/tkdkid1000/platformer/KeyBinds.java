package net.tkdkid1000.platformer;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.getInput;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;

import javafx.scene.input.KeyCode;

public class KeyBinds {

	public void register() {
        
        getInput().addAction(new UserAction("ClearBlock") {
        	@Override
            protected void onActionBegin() {
        		FXGL.getGameWorld().getEntitiesByType(Type.FALLINGMAP).forEach(map -> {
        			map.removeFromWorld();
        		});
            }
        }, KeyCode.C);
        
        getInput().addAction(new UserAction("SpawnBlock") {
        	@Override
            protected void onActionBegin() {
        		new App().spawnFallingBlock();
            }
        }, KeyCode.E);
        
        getInput().addAction(new UserAction("Respawn") {
        	@Override
            protected void onActionBegin() {
        		App.getInstance().player.setPosition(getAppWidth() / 2, getAppHeight() - 400);
            }
        }, KeyCode.R);
        
		UserAction fullscreen = new UserAction("ToggleFullscreen") {
        	@Override
            protected void onActionBegin() {
        		if (FXGL.getPrimaryStage().isFullScreen()) {
        			FXGL.getPrimaryStage().setFullScreen(false);
        		} else {
        			FXGL.getPrimaryStage().setFullScreen(true);
        		}
            }
        };
        
        getInput().addAction(fullscreen, KeyCode.F11);
        
        UserAction moveDown = new UserAction("MovePlayerDown") {
            @Override
            protected void onActionBegin() {
            	Optional<Entity> player = getGameWorld().getEntityByID("player", 1);
            	player.get().setY(player.get().getY()+5);
            }

            @Override
            protected void onAction() {
            	Optional<Entity> player = getGameWorld().getEntityByID("player", 1);
            	player.get().setY(player.get().getY()+5);
            }
        };
        getInput().addAction(moveDown, KeyCode.S);
        UserAction moveUp = new UserAction("MovePlayerUp") {
			@Override
            protected void onActionBegin() {
				EnumSet<Type> mapcore = EnumSet.of(Type.FALLINGMAP, Type.MAP);
				mapcore.forEach(maptype -> {
					FXGL.getGameWorld().getEntitiesByType(maptype).forEach(map -> {
						if (App.getInstance().player.getBottomY() >= map.getY()-1 
								&& App.getInstance().player.getY() <= map.getBottomY()
								&& map.getX() < App.getInstance().player.getRightX() 
								&& map.getRightX() > App.getInstance().player.getX()) {
		            		FXGL.getWorldProperties().setValue("a.jumps", FXGL.getWorldProperties().getInt("a.jumps")+1);
		            		class Jump extends TimerTask {
		            		    
		            			private Entity player;
		            			private int counter = 200;
		            			
								public Jump(Entity player) {
		            				this.player = player;
		            			}

		            		    @Override
		            		    public void run() {
		            		        if (counter > 0) {
		            		        	counter--;
		            		        	player.translateY(-1);
		            		        } else {
		            		            this.cancel();
		            		        }
		            		    }

		            		}
		            		new Timer().scheduleAtFixedRate(new Jump(App.getInstance().player), 1, 5);
		        		}
					});
				});
            }

            @Override
            protected void onAction() {
            	if (App.getInstance().player.getBottomY() >= App.getInstance().map.getBottomY()-101) {
            		class Jump extends TimerTask {
            		    
            			private Entity player;
            			private int counter = 100;
            			
						public Jump(Entity player) {
            				this.player = player;
            			}

            		    @Override
            		    public void run() {
            		        if (counter > 0) {
            		        	counter--;
            		        	player.translateY(-1);
            		        } else {
            		            this.cancel();
            		        }
            		    }

            		}
            		new Timer().scheduleAtFixedRate(new Jump(App.getInstance().player), 1, 5);
        		}
            	
            }
        };
        getInput().addAction(moveUp, KeyCode.W);
        UserAction moveRight = new UserAction("MovePlayerRight") {
            @Override
            protected void onActionBegin() {
            	Optional<Entity> player = getGameWorld().getEntityByID("player", 1);
            	player.get().setX(player.get().getX()+5);
            }

            @Override
            protected void onAction() {
            	Optional<Entity> player = getGameWorld().getEntityByID("player", 1);
            	player.get().setX(player.get().getX()+5);
            }
        };
        getInput().addAction(moveRight, KeyCode.D);
        UserAction moveLeft = new UserAction("MovePlayerLeft") {
            @Override
            protected void onActionBegin() {
            	Optional<Entity> player = getGameWorld().getEntityByID("player", 1);
            	player.get().setX(player.get().getX()-5);
            }

            @Override
            protected void onAction() {
            	Optional<Entity> player = getGameWorld().getEntityByID("player", 1);
            	player.get().setX(player.get().getX()-5);
            }
        };
        getInput().addAction(moveLeft, KeyCode.A);
	}
}
