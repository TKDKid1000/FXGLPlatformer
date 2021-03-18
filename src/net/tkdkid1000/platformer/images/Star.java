package net.tkdkid1000.platformer.images;

import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;

public class Star {

	public static Shape get() {
		Path path = new Path();
		MoveTo moveto = new MoveTo(100, 70);
		LineTo line1 = new LineTo(320, 160);
		LineTo line2 = new LineTo(130,230);
		LineTo line3 = new LineTo(230,50);
		LineTo line4 = new LineTo(270, 250);
		LineTo line5 = new LineTo(110, 70);
		path.getElements().add(moveto);
		path.getElements().addAll(line1, line2, line3, line4, line5);
		return path;
	}
}
