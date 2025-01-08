import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Pipe {
	private int x;
	private int y;
	ImageView topPipe;
	ImageView bottomPipe;
	private Pane pane;
	boolean isPassed = false;
	
	public Pipe(ImageView topPipe, ImageView bottomPipe, Pane pane) {
		this.pane = pane;
        this.topPipe = topPipe;
        this.bottomPipe = bottomPipe;
	}
	
	public double getX() {
		return topPipe.getX();
	}
	
	public void setX(int newX) {
		topPipe.setX(newX);
		bottomPipe.setX(newX);
	}
	
	public double getY(ImageView pipe) {
		return pipe.getY();
	}
	
	public void setY(int newY) {
		topPipe.setY(newY);
		bottomPipe.setY(newY);
	}
	
	public ImageView getTopPipe() {
		return topPipe;
	}
	
	public ImageView getBottomPipe() {
		return bottomPipe;
	}
	
	public void move(double distance) {
		topPipe.setX(topPipe.getX()+distance);
		bottomPipe.setX(bottomPipe.getX()+distance);
	}
	

}