package cz.zcu.kiv.ti.sp.gui;

import cz.zcu.kiv.ti.sp.automaton.Automaton;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GUI extends Application {

	private static Application GUI;

	public static void start() {
		launch();
	}

	private Stage primaryStage;

	@Override
	public void start(Stage stage) throws Exception {
		GUI = this;

		this.primaryStage = stage;

		Scene scene = MainView.getInstance().setup();

		this.primaryStage.setScene(scene);
		this.primaryStage.setTitle("Simulace køižovatk");
		this.primaryStage.setResizable(true);

		// Image icon = new
		// Image(getClass().getResource("/gui/img/icon.png").toExternalForm());
		// stage.getIcons().add(icon);

		this.primaryStage.show();

		this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});

		simulate();
	}

	public static void simulate() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Automaton.getInstance().run();
					}
				});
			}
		}).start();
	}

}