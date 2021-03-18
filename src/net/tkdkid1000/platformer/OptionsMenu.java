package net.tkdkid1000.platformer;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.core.util.EmptyRunnable;
import com.almasb.fxgl.dsl.FXGL;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class OptionsMenu extends FXGLMenu {

	private static final int SIZE = 150;
	
	private Animation<?> animation;
	
	public OptionsMenu() {
		super(MenuType.GAME_MENU);
		
		getContentRoot().setTranslateX(FXGL.getAppWidth() / 2.0 - SIZE);
        getContentRoot().setTranslateY(FXGL.getAppHeight() / 2.0 - SIZE);
        
        Shape shape = new Circle(SIZE, SIZE, SIZE);
        
        getContentRoot().getChildren().addAll(shape);

        getContentRoot().setScaleX(0);
        getContentRoot().setScaleY(0);
        
        animation = FXGL.animationBuilder()
                .duration(Duration.seconds(0.66))
                .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                .scale(getContentRoot())
                .from(new Point2D(0, 0))
                .to(new Point2D(1, 1))
                .build();
	}
	
	@Override
    public void onCreate() {
        animation.setOnFinished(EmptyRunnable.INSTANCE);
        animation.stop();
        animation.start();
    }

    @Override
    protected void onUpdate(double tpf) {
        animation.onUpdate(tpf);
    }
}
