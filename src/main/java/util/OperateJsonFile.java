package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import model.Course;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;


/**
 * @author Cheese
 */
public class OperateJsonFile {

    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8);
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void operateCourseJsonFile(Course course,String commend) throws IOException {
        File file = new File("src/main/resources/data/course.json");
        String str = readJsonFile("src/main/resources/data/course.json");
        JSONObject jsonObj = JSON.parseObject(str);
        BufferedWriter writer = null;
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false), "UTF-8"));


        if (commend == "save") {
            JSONObject jsonSubObj = new JSONObject();
            jsonSubObj.put("name", course.getName());
            jsonSubObj.put("picLocation", course.getPicture());
            jsonSubObj.put("videoLocation", course.getLocation());
            jsonSubObj.put("sort", course.getSort());
            jsonSubObj.put("time", course.getTime());
            jsonSubObj.put("vip", course.getVip());
            jsonObj.put(course.getId(), jsonSubObj);
            System.out.println(jsonObj);
        }
        else if (commend == "delete"){
            jsonObj.remove(course.getId());
        }
        writer.write(jsonObj.toString());
        writer.close();
    }


    public static String[] strToArray(String jsonStr){
        JSONArray jsonArray = com.alibaba.fastjson.JSON.parseArray(jsonStr);
        String[] array =new String[jsonArray.size()];
        String[] strings = jsonArray.toArray(array);
        return strings;
    }

    public static String getLastCourseId(){
        String courseId = "";
        //String str = readJsonFile(Objects.requireNonNull(CourseController.class.getClassLoader().getResource("data/course.json")).getPath());


        String str = readJsonFile("src/main/resources/data/course.json");
        JSONObject jsonObj = JSON.parseObject(str);

        for (Map.Entry<String, Object> entry: jsonObj.entrySet()){ courseId = entry.getKey(); }
        courseId = String.valueOf(Integer.parseInt(courseId)+1);

        return courseId;

    }




}
