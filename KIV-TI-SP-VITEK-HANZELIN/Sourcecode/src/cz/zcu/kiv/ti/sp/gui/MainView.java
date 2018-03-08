package cz.zcu.kiv.ti.sp.gui;

import java.io.PrintStream;

import cz.zcu.kiv.ti.sp.automaton.Automaton;
import cz.zcu.kiv.ti.sp.gui.img.ImageReference;
import cz.zcu.kiv.ti.sp.utils.TextConsole;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public final class MainView {

	/** Jedináèek */
	private static MainView instance;

	/** Vyvtvoøená scéna */
	private Scene scene;

	public ImageView view;
	public Image currentImage;

	/**
	 * Konstruktor
	 */
	private MainView() {

	}

	public Scene setup() {
		BorderPane mainPane = new BorderPane();

		currentImage = new Image(ImageReference.getInstance().getClass().getResource("S0.png").toExternalForm());
		view = new ImageView(currentImage);

		mainPane.setTop(this.setupTop());
		mainPane.setLeft(this.setupLeft());
		mainPane.setCenter(this.setupCenter());
		mainPane.setRight(this.setupRight());
		mainPane.setBottom(this.setupBottom());

		mainPane.setStyle("-fx-background-color: #000000;");

		this.scene = new Scene(mainPane, 1000, 600);

		return this.scene;

	}

	private Node setupBottom() {
		try {
			TextArea ta = new TextArea();

			ta.textProperty().addListener(new ChangeListener<Object>() {
				@Override
				public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
					ta.setScrollTop(Double.MAX_VALUE); // this will scroll to the bottom
					// use Double.MIN_VALUE to scroll to the top
				}
			});

			TextConsole console = new TextConsole(ta);
			PrintStream ps = new PrintStream(console, true, "CP1250");
			System.setOut(ps);
			System.setErr(ps);

			return ta;
		} catch (Exception e) {
			return null;
		}
	}

	private Node setupRight() {
		return null;
	}

	private Node setupCenter() {
		return view;
	}

	private Node setupLeft() {
		VBox box = new VBox();

		Button prechod = new Button("Tlaèítka chodce");

		prechod.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Pošle stisk tlaèítka do automatu
				Automaton.getInstance().tick('t');

			}
		});

		Button start = new Button("Zapnout øízení køižovatky");

		start.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Pošle stisk tlaèítka do automatu
				Automaton.getInstance().tick('p');

			}
		});

		Button end = new Button("Vypnout øízení køížovatky");

		end.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Pošle stisk tlaèítka do automatu
				Automaton.getInstance().tick('k');

			}
		});

		prechod.setMaxWidth(Double.MAX_VALUE);
		start.setMaxWidth(Double.MAX_VALUE);
		end.setMaxWidth(Double.MAX_VALUE);

		prechod.setMinHeight(50);
		start.setMinHeight(50);
		end.setMinHeight(50);

		start.setStyle("-fx-background-color: green; -fx-text-fill: white;");
		end.setStyle("-fx-background-color: red; -fx-text-fill: white;");

		box.getChildren().add(start);
		box.getChildren().add(end);
		box.getChildren().add(prechod);

		box.setAlignment(Pos.CENTER);

		return box;
	}

	private Node setupTop() {
		return null;
	}

	public static MainView getInstance() {
		if (MainView.instance == null) {
			MainView.instance = new MainView();

		}
		return MainView.instance;
	}

}