package router;

import java.io.IOException;
import java.util.ArrayList;

import application.Connector;
import application.Main;
import application.SceneLoader;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import network_dto.NetworkData;
import service.MemberService;
import service.ScheduleService;
import vo.Member;
import vo.Schedule;

public class MainRouter {
	NetworkData<?> data;

	public static Stage stage;

	public static MemberService memberService = new MemberService();
	public static ScheduleService scheduleService = new ScheduleService();

	public NetworkData<?> route(NetworkData<?> data) {
		String action = data.getAction();
		Object value = data.getV();

		if (Main.loginMember == null) {
			Member m = null;
			if (value instanceof Member) {
				m = (Member) value;
			}
			switch (action) {
			case "member/login":
				login(m);
				break;
			case "member/join":
				join(m);
				break;
			case "member/find":
				find(m);
				break;
			}
		} else {
			String actionClass = action.split("/")[0];
			switch (actionClass) {
			case "member":
				memberRoute(data);
				break;
			case "schedule":
				scheduleRoute(data);
				break;
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private void scheduleRoute(NetworkData<?> data) {
		String action = data.getAction().split("/")[1];
		int result = 0;
		BorderPane borderPane;
		AnchorPane monthCal;
		switch (action) {
		case "find":
			ArrayList<Schedule> scheList = (ArrayList<Schedule>) data.getV();
			scheduleService.getAllSchedule(scheList);
			borderPane = (BorderPane) ScheduleService.border;
			Platform.runLater(() -> {
				stage.setScene(borderPane.getScene());
			});
			break;
		case "save":
			result = (Integer) data.getV();
			if (result == 1) {
				Connector.send(new NetworkData<Member>("schedule/find", Main.loginMember));
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("?????? ?????? ??????");
				alert.setHeaderText("?????? ????????? ??????????????????.");
				alert.show();
			}
			break;
		case "findWeek":
			ArrayList<Schedule> scheWeekList = (ArrayList<Schedule>) data.getV();
			scheduleService.getWeekSchedule(scheWeekList);
			borderPane = (BorderPane) ScheduleService.border;
			Platform.runLater(() -> {
				stage.setScene(borderPane.getScene());
			});
			break;

		case "findByNo":
			Schedule schedule = (Schedule) data.getV();
			scheduleService.getDetailSchedule(schedule);
			break;
		case "update":
			result = (Integer) data.getV();
			if (result == 1) {
//				Connector.send(new NetworkData<Member>("schedule/find", Main.loginMember));
				monthCal = (AnchorPane) Main.sceneLoader.load(SceneLoader.M_SCHEDULE_PATH);
				borderPane = (BorderPane) ScheduleService.border;
				ScheduleService.setCalendar(monthCal);
				Platform.runLater(() -> {
					borderPane.setCenter(monthCal);
				});
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("?????? ?????? ??????");
				alert.setHeaderText("?????? ????????? ??????????????????.");
				alert.show();
			}
			break;
		case "delete":
			result = (Integer) data.getV();
			if (result == 1) {
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("?????? ??????");
					alert.setHeaderText("?????? ?????? ??????.");
					alert.show();
					alert.setOnCloseRequest(ev -> {
						AnchorPane exmonthCal = (AnchorPane) Main.sceneLoader.load(SceneLoader.M_SCHEDULE_PATH);
						BorderPane exborderPane = (BorderPane) ScheduleService.border;
						ScheduleService.setCalendar(exmonthCal);
						exborderPane.setCenter(exmonthCal);
					});
				});

			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("?????? ?????? ??????");
				alert.setHeaderText("?????? ????????? ??????????????????.");
				alert.show();
			}
			break;
		}

	}

	@SuppressWarnings("unchecked")
	private void memberRoute(NetworkData<?> data) {
		String action = data.getAction().split("/")[1];
		switch (action) {
		case "frdList":
			ArrayList<Member> frdList = (ArrayList<Member>) data.getV();
			memberService.frdList(frdList);
			break;

		case "update":
			if (data.getV() != null) {
				Member member = (Member) data.getV();
				memberService.update(member);
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("?????? ?????? ?????? ??????");
				alert.setHeaderText("????????? ??????????????????.");
				alert.show();
			}
			break;
		case "delete":
			memberService.delete(data);
			break;

		case "findId":
			ArrayList<Member> findID = (ArrayList<Member>) data.getV();
			memberService.findId(findID);
			break;

//		case "mbList":
//			ArrayList<Member> MbList = (ArrayList<Member>) data.getV();
//			memberService.mbList(MbList);
//			break;

		case "alert":
			ArrayList<Object> alertList = (ArrayList<Object>) data.getV();
			memberService.getAlert(alertList);
			break;
		case "frdAdd":
			int result = (Integer) data.getV();
			Platform.runLater(() -> {
				if (result == 1) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("?????? ??????");
					alert.setHeaderText("?????? ?????? ??????!");
					alert.setContentText("?????? ????????? ?????????????????????.");
					alert.show();
				} else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("?????? ??????");
					alert.setHeaderText("?????? ?????? ??????!");
					alert.setContentText("?????? ????????? ?????????????????????.");
					alert.show();
				}
			});
			break;
		case "frdDelete":
			int result2 = (Integer) data.getV();
			Platform.runLater(() -> {
				if (result2 == 1) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("?????? ??????");
					alert.setHeaderText("?????? ?????? ??????!");
					alert.setContentText("?????? ????????? ?????????????????????.");
					alert.show();
					Platform.runLater(() -> {
						Connector.send(new NetworkData<Member>("member/frdList", Main.loginMember));
					});
				} else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("?????? ??????");
					alert.setHeaderText("?????? ?????? ??????!");
					alert.setContentText("?????? ??????????????? ?????????????????????.");
					alert.show();
				}
			});
			break;
		}
	}

	private void login(Member member) {
		if (member != null) {
			Main.loginMember = member;
			try {
				BorderPane userMain = FXMLLoader.load(getClass().getResource("/view/UserMain.fxml"));
				Platform.runLater(() -> {
					stage.setScene(new Scene(userMain));
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("????????? ??????");
				alert.setHeaderText("?????? ????????? ???????????????.");
				alert.show();
			});
		}
	}

	private void join(Member m) {
		if (m == null) {
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("??????????????? ???????????????????????????.");
				alert.show();
			});
		} else {
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("?????? ???????????? ???????????????.");
				alert.show();
			});
		}
	}

	private void find(Member member) {
		if (member == null) {
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("??????????????? ??????????????????.");
				alert.show();
			});

		} else {
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("????????? ??????????????????.");
				alert.show();
			});

		}

	}
}
