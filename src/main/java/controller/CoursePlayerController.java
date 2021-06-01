package controller;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import model.Course;
import util.Controllers;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class CoursePlayerController extends AbstractController {
    
    private MediaPlayer mediaPlayer;
    private double volumeValue;
    private Duration duration ;

    @FXML private MediaView courseMediaView;
    @FXML private Button volumeButton;
    @FXML private Button functionButton;
    @FXML private Slider processSlider;
    @FXML private Slider volumeSlider;
    @FXML private Label timeLabel;


    private final String playIcon  = Objects.requireNonNull(getClass().getResource("/img/play.png")).toString();
    private final String pauseIcon  = Objects.requireNonNull(getClass().getResource("/img/pause.png")).toString();
    private final String volumeOnIcon  = Objects.requireNonNull(getClass().getResource("/img/volume_on.png")).toString();
    private final String volumeOffIcon  = Objects.requireNonNull(getClass().getResource("/img/volume_off.png")).toString();
    
    public void coursePlayerScene(){
        Controllers.setCenter(this.getClass());
    }
    public void mediaPlayerOnLoad(Course course){
        if (!"null".equals(course.getLocation())) {
            String url = Objects.requireNonNull(getClass().getResource(course.getLocation())).toString();
            Media media = new Media(url);
            mediaPlayer = new MediaPlayer(media);
            courseMediaView.setMediaPlayer(mediaPlayer);


            mediaPlayer.setOnReady(() -> {
                duration = mediaPlayer.getMedia().getDuration();
                updateValues();
            });
            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> updateValues());
            operateVolumeSlider();
            operateProcessSlider();
            setIcon(functionButton, playIcon);
            setIcon(volumeButton, volumeOnIcon);

            volumeButton.setDisable(false);
            functionButton.setDisable(false);
            processSlider.setDisable(false);
            volumeSlider.setDisable(false);
            timeLabel.setVisible(true);
            courseMediaView.setVisible(true);
        }else {

            volumeButton.setDisable(true);
            functionButton.setDisable(true);
            processSlider.setDisable(true);
            volumeSlider.setDisable(true);
            timeLabel.setText("00:00 / 00:00");
            timeLabel.setDisable(true);
            courseMediaView.setVisible(false);

        }


    }
    public void operateProcessSlider(){
        processSlider.valueProperty().addListener((observable, oldValue, newValue) -> {

            if(processSlider.isValueChanging()){
                mediaPlayer.seek(duration.multiply(processSlider.getValue()/100.0));
                updateValues();
            }
        });
    }
    public void operateVideoButton(){
        MediaPlayer.Status status = mediaPlayer.getStatus();
        if(status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.READY || status == MediaPlayer.Status.STOPPED){
            mediaPlayer.play();
            setIcon(functionButton,pauseIcon);
        }else{
            mediaPlayer.pause();
            setIcon(functionButton,playIcon);
        }

    }
    public void operateVolumeButton(){
        volumeValue = mediaPlayer.getVolume();
        if(mediaPlayer.getVolume()>0){
            volumeSlider.setValue(0);
            mediaPlayer.setVolume(0);
            setIcon(volumeButton,volumeOffIcon);
        }else{
            volumeValue = 0.5;
            mediaPlayer.setVolume(volumeValue);
            volumeSlider.setValue(volumeValue * 100);
            setIcon(volumeButton,volumeOnIcon);
        }
    }
    public void operateVolumeSlider(){
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double value = newValue.doubleValue()/100;
            mediaPlayer.setVolume(value);
            if (value==0){
                setIcon(volumeButton,volumeOffIcon);
            }else {
                setIcon(volumeButton,volumeOnIcon);
            }

        });

    }
    protected void updateValues(){
        if(processSlider != null && timeLabel!=null){
            Platform.runLater(() -> {
                Duration currentTime = mediaPlayer.getCurrentTime();
                timeLabel.setText(processFormatTime(currentTime,duration));
                if(!processSlider.isDisabled() && duration.greaterThan(Duration.ZERO) && !processSlider.isValueChanging()){
                    processSlider.setValue(currentTime.toMillis()/duration.toMillis() * 100);
                }
                
            });
        }
    }
    protected String processFormatTime(Duration elapsed, Duration duration){
        int intElapsed = (int)Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        int elapsedMinutes = (intElapsed - elapsedHours *60 *60)/ 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 - elapsedMinutes * 60;
        if(duration.greaterThan(Duration.ZERO)){
            int intDuration = (int)Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            int durationMinutes = (intDuration - durationHours *60 * 60) / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60 - durationMinutes * 60;

            if(durationHours > 0){
                return String.format("%02d:%02d:%02d / %02d:%02d:%02d",elapsedHours,elapsedMinutes,elapsedSeconds,durationHours,durationMinutes,durationSeconds);
            }else{
                return String.format("%02d:%02d / %02d:%02d",elapsedMinutes,elapsedSeconds,durationMinutes,durationSeconds);
            }
        }else{
            if(elapsedHours > 0){
                return String.format("%02d:%02d:%02d / %02d:%02d:%02d",elapsedHours,elapsedMinutes,elapsedSeconds);
            }else{
                return String.format("%02d:%02d / %02d:%02d",elapsedMinutes,elapsedSeconds);
            }
        }
    }
    private void setIcon(Button button, String path){
        Image icon = new Image(path);
        ImageView imageView = new ImageView(icon);
        imageView.setFitWidth(25);
        imageView.setFitHeight((int)(25 * icon.getHeight() / icon.getWidth()));
        button.setGraphic(imageView);


        ColorAdjust colorAdjust = new ColorAdjust();
        button.setOnMousePressed(event ->  {
            colorAdjust.setBrightness(0.5);
            button.setEffect(colorAdjust);
        });
        button.setOnMouseReleased(event -> {
            colorAdjust.setBrightness(0);
            button.setEffect(colorAdjust);
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

}
