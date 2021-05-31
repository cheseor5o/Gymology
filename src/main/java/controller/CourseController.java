package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.Course;
import model.Instance;
import util.Controllers;

import java.net.URL;
import java.util.*;

import static util.OperateJsonFile.readJsonFile;

/**
 * @author Cheese
 * @date 2021/4/26 23:11
 */
public class CourseController extends AbstractController {
    

    @FXML private TextField courseSearchTxt;
    @FXML private Button courseSearchBtn;
    @FXML private Button courseUploadBtn;
    @FXML private ChoiceBox<String> courseSortCBox;
    @FXML public GridPane courseGridPane;


    private final String[] courseSort = util.OperateJsonFile.strToArray(util.OperateJsonFile.readJsonFile(
            Objects.requireNonNull(CourseController.class.getClassLoader().getResource("data/courseSort.json")).getPath()));

    public Boolean auth(){
        if (!Controllers.get(LoginController.class).hasLogging()){
            Controllers.setCenter(LoginController.class);
            return false;
        }
        return true;
    }

    @Override
    @FXML
    public void scene() {
        Instance instance = Controllers.get(LoginController.class).getUser();
        courseUploadBtn.setVisible(instance != null && instance.getIdentity() == Instance.Identity.Manager);
        super.scene();
    }


    private JSONObject loadCourseJson(String sort){
        String str = readJsonFile("src/main/resources/data/course.json");
        JSONObject jsonObj = JSON.parseObject(str);
        JSONObject jsonSubObj = new JSONObject();
        if ("ALL" .equals(sort)){
            return jsonObj;
        }else if ("Keywords".equals(sort)){
            for (Map.Entry<String, Object> entry: jsonObj.entrySet()){
                if (jsonObj.getJSONObject(entry.getKey()).get("name").toString().contains(courseSearchTxt.getText())){
                    jsonSubObj.put(entry.getKey(),entry.getValue());
                }
            }
            return jsonSubObj;
        }else{
            for (Map.Entry<String, Object> entry: jsonObj.entrySet()){
                if (sort.equals(jsonObj.getJSONObject(entry.getKey()).get("sort"))){
                    jsonSubObj.put(entry.getKey(),entry.getValue());
                }
            }
            return jsonSubObj;
        }
    }
    private List<Course> setCoursesData(String sort) {
        Course course;
        List<Course> courses = new ArrayList<>();
        JSONObject courseJsonObj = loadCourseJson(sort);

        for (Map.Entry<String, Object> entry: courseJsonObj.entrySet()){
            course = new Course();
            course.setId(entry.getKey());
            course.setName((String) courseJsonObj.getJSONObject(entry.getKey()).get("name"));
            course.setPicture((String) courseJsonObj.getJSONObject(entry.getKey()).get("picLocation"));
            course.setLocation((String) courseJsonObj.getJSONObject(entry.getKey()).get("videoLocation"));
            course.setSort((String) courseJsonObj.getJSONObject(entry.getKey()).get("sort"));
            course.setTime((String) courseJsonObj.getJSONObject(entry.getKey()).get("time"));
            course.setVip((Boolean) courseJsonObj.getJSONObject(entry.getKey()).get("vip"));
            courses.add(course);
        }
        return courses;
    }
    public void setCourseScene(String sort) throws Exception {
        List<Course> courses = new ArrayList<>(setCoursesData(sort));
        int column = 0;
        int row = 1;
        for (Course course : courses) {
            FXMLLoader courseItemLoader = new FXMLLoader();
            courseItemLoader.setLocation(getClass().getClassLoader().getResource("fxml/courseItem.fxml"));
            Pane courseItemPane = courseItemLoader.load();
            CourseItemController courseItemController = courseItemLoader.getController();
            courseItemController.setCourseData(course);
            if (column == 3) {
                column = 0;
                row++;
            }
            courseGridPane.add(courseItemPane, column++, row);
            GridPane.setMargin(courseItemPane, new Insets(10));
        }



    }
    public void selectCourseSort(ActionEvent event) {
        courseGridPane.getChildren().clear();
        try {
            setCourseScene(courseSortCBox.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void searchCourse() throws Exception {
        courseGridPane.getChildren().clear();
        setCourseScene("Keywords");
    }
    public void uploadCourse(){
        CourseEditController courseEditController = Controllers.get(CourseEditController.class);

        courseEditController.unLoad();
        courseEditController.setFlag(false);
        Controllers.setCenter(CourseEditController.class);
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            courseSortCBox.getItems().addAll(courseSort);
            courseSortCBox.setValue("ALL");
            courseSortCBox.setOnAction(this::selectCourseSort);
            setCourseScene("ALL");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
