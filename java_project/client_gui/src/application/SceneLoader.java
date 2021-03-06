package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class SceneLoader {

	public final static String MAIN_PATH = "/view/Main.fxml";
	public final static String JOIN_PATH = "/view/Join.fxml";
	public final static String USER_MAIN_PATH = "/view/UserMain.fxml";
	public final static String M_SCHEDULE_PATH = "/view/MonthSchedule.fxml";
	public final static String UPDATE_SCHE_PATH = "/view/UpdateSchedule.fxml";

	public final static String SEARCH_PATH = "/view/SearchIcon.fxml";
	public final static String F_LIST_PATH = "/view/FriendList.fxml";

	public final static String M_SCHE_DETAIL_PATH = "/view/MonthDetail.fxml";
	public final static String NEW_SCHE_PATH = "/view/NewSchedule.fxml";

	public final static String UPDATE_PATH = "/view/UpdateMember.fxml";

	public SceneLoader() {
//		join = (AnchorPane) load("../view/Join.fxml");
//		userMain = (BorderPane) load("../view/UserMain.fxml");
	}

	public Object load(String name) {
		Parent p = null;
		try {
			p = FXMLLoader.load(getClass().getResource(name));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}
}
