package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Course;
import model.Customer;
import model.Instance;
import org.apache.commons.io.FileUtils;
import util.Controllers;
import util.Databases;
import util.OperateJsonFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Cheese
 * @since 2021/4/26 23:11
 */
public class CourseEditController extends AbstractController {
    
    private Boolean flag;
    private Course oldCourse;

    @FXML
    private Pane courseEditPane;
    @FXML
    private ChoiceBox<String> courseEditSortChoiceBox;
    @FXML
    private CheckBox courseEditVipCheckBox;
    @FXML
    private Button courseEditPicBtn;
    @FXML
    private Button courseEditVideoBtn;
    @FXML
    private TextField courseEditNameTxt;
    @FXML
    private Button courseSaveBtn;
    @FXML
    private Button courseDeleteBtn;
    @FXML
    private Label videoLocationLabel;
    @FXML
    private Label picLocationLabel;

    private final String[] courseSort = util.OperateJsonFile.strToArray(util.OperateJsonFile.readJsonFile(
            Objects.requireNonNull(CourseController.class.getClassLoader().getResource("data/courseSort.json")).getPath()));

    /**
     * 添加喜爱的课
     * @param course 课
     */
    public void addFavouriteCourse(String course){
        Customer customer = obtainCustomer();
        if (customer != null){
            customer.addFavouriteCourse(course);
        }
    }

    /**
     * 移除喜爱的课
     * @param course 课
     */
    public void removeFavouriteCourse(String course){
        Customer customer = obtainCustomer();
        if (customer != null){
            customer.removeFavouriteCourse(course);
        }
    }

    /**
     * 添加喜爱的教练
     * @param coach 课
     */
    public void addFavouriteCoach(String coach){
        Customer customer = obtainCustomer();
        if (customer != null){
            customer.addFavouriteCoach(coach);
        }
    }
    
    /**
     * 移除喜爱的教练
     * @param coach 课
     */
    public void removeFavouriteCoach(String coach){
        Customer customer = obtainCustomer();
        if (customer != null){
            customer.removeFavouriteCoach(coach);
        }
    }

    /**
     * 查看是否登录，若登录返回相应customer
     * @return 已登录：customer；未登录：null，并跳转至登录界面
     */
    private Customer obtainCustomer(){
        LoginController loginController = Controllers.get(LoginController.class);
        if (loginController.hasLogging()) {
            Instance instance = loginController.getInstance();
            return Databases.getDatabase(Customer.class).get(instance.getEmail());
        }
        Controllers.setCenter(loginController.getClass(),false);
        return null;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
        courseDeleteBtn.setDisable(!flag);
    }

    public void unLoad() {
        courseEditNameTxt.setText("");
        courseEditVipCheckBox.setSelected(false);
        videoLocationLabel.setText("(video location)");
        picLocationLabel.setText("(picture location)");
    }

    public void onLoad(@org.jetbrains.annotations.NotNull Course course) {
        oldCourse = course;
        courseEditNameTxt.setText(course.getName());
        picLocationLabel.setText(course.getPicture());
        videoLocationLabel.setText(course.getLocation());
        courseEditVipCheckBox.setSelected(course.getVip());
        courseEditSortChoiceBox.setValue(course.getSort());
    }

    public void loadSort() {
        courseEditSortChoiceBox.getItems().addAll(courseSort);
        courseEditSortChoiceBox.getItems().remove(0);
    }

    public void chooseVideoFile() {
        Stage stage = (Stage) courseEditPane.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose MP4 File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MP4 files (*.mp4)", "*.mp4");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            videoLocationLabel.setText(file.toString());
        }
    }

    public void choosePictureFile() {
        Stage stage = (Stage) courseEditPane.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"),
                new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg")
        );

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            picLocationLabel.setText(file.toString());
        }
    }

    public void save() throws Exception {
        //编辑模式
        if (flag) {
            oldCourse.setName(courseEditNameTxt.getText());
            oldCourse.setSort(courseEditSortChoiceBox.getValue());
            oldCourse.setVip(courseEditVipCheckBox.isSelected());
            if (!picLocationLabel.getText().equals(oldCourse.getPicture())) {
                setCoursePicture(oldCourse, "src/main/resources/img/course/", "target/classes/img/course/");
            }
            if (!videoLocationLabel.getText().equals(oldCourse.getLocation())) {
                setCourseLocation(oldCourse, "src/main/resources/video/", "target/classes/video/");
                oldCourse.setTime(null);
                // TODO 这里要对接一个计算视频时长的方法

            }
            OperateJsonFile.operateCourseJsonFile(oldCourse, "save");
        }
        //上传模式
        else {
            Course course = new Course();
            course.setId(OperateJsonFile.getLastCourseId());
            course.setName(courseEditNameTxt.getText());
            course.setVip(courseEditVipCheckBox.isSelected());
            course.setSort(courseEditSortChoiceBox.getValue());

            if (!"".equals(picLocationLabel.getText())) {
                setCoursePicture(course, "src/main/resources/img/course/", "target/classes/img/course/");
            } else {
                course.setPicture("null");
            }
            if (!"".equals(videoLocationLabel.getText())) {
                setCourseLocation(course, "src/main/resources/video/", "target/classes/video/");
                // TODO 这里要对接一个计算视频时长的方法

            } else {
                course.setLocation("null");
            }
            course.setTime("null");

            OperateJsonFile.operateCourseJsonFile(course, "save");

        }
        CourseController courseController = Controllers.get(CourseController.class);
        courseController.courseGridPane.getChildren().clear();
        courseController.setCourseScene("ALL");
        courseController.scene();
    }

    private void setCoursePicture(Course course, String... files) throws IOException {
        String extension = picLocationLabel.getText().substring(picLocationLabel.getText().lastIndexOf("."));
        for (String file : files) {
            FileUtils.copyFile(new File(picLocationLabel.getText()), new File(file + course.getId() + extension));
        }
        course.setPicture("/img/course/" + course.getId() + extension);
    }

    private void setCourseLocation(Course course, String... files) throws IOException {
        String extension = videoLocationLabel.getText().substring(videoLocationLabel.getText().lastIndexOf("."));
        for (String file : files) {
            FileUtils.copyFile(new File(videoLocationLabel.getText()), new File(file + course.getId() + extension));
        }
        course.setLocation("/video/" + course.getId() + extension);
    }

    public void delete() throws Exception {
        OperateJsonFile.operateCourseJsonFile(oldCourse, "delete");
        CourseController courseController = Controllers.get(CourseController.class);
        courseController.courseGridPane.getChildren().clear();
        courseController.setCourseScene("ALL");
        courseController.scene();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadSort();
    }
}
