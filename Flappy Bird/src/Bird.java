import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Bird {
	private ImageView birdView;
	private Image birdImage;
	int BIRD_WIDTH = 50;
	int BIRD_HEIGHT = 40;

	public Bird(Pane pane) {
		birdImage = new Image("flappybird.png");
        birdView = new ImageView(birdImage);
        
        getBirdView().setFitWidth(BIRD_WIDTH);
        getBirdView().setFitHeight(BIRD_HEIGHT);
        
        getBirdView().setX(160); // Initial X position
        getBirdView().setY(300); // Initial Y position
        pane.getChildren().add(getBirdView());
	}
	

	public ImageView getBirdView() {
		return birdView;
	}
	
	public double getX() {
		return getBirdView().getX();
	}
	
	public double getY() {
		return getBirdView().getY();
	}
		

}