package com.example.eval1;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HelloApplication extends Application {
    Stage window;
    Scene home, display;
    AlertBox alertBox = new AlertBox();
    String downLoadedDatas;

    @Override
    public void start(Stage stage) throws IOException {
        window = stage;
        loadDisplay(getDisplayDatas("Lyon"));

        loadHome();
        displayScene(home);
    }

    public static void main(String[] args) throws IOException { launch(); }

    public void displayScene(Scene scene) {
        window.setScene(scene);
        window.show();
    }

    public void loadHome() {
        //composants scene2
        Button button1 = new Button("Consulter les données (Api)");
        Button button3 = new Button("Mettre à jour la météo");
        Button button4 = new Button("Supprimer une proposition");
        Button button5 = new Button("Bonus");

        button1.setOnAction(e -> {
            window.setScene(display);
        });

        //setup scene2
        StackPane layout = new StackPane();
        layout.getChildren().add(button1);

        home = new Scene(layout, 600, 300);
        window.setTitle("Hello");
    }

    public void loadDisplay(WeatherDatas.Root datas) throws IOException {
        //setup scene2
        GridPane gridPane = new GridPane();

        gridPane.setMinSize(400, 200);

        //Setting the padding
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        //Setting the vertical and horizontal gaps between the columns
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        //Setting the Grid alignment
        gridPane.setAlignment(Pos.CENTER);

        //composants scene2
        Button button1 = new Button("Back");
        Text text1 = new Text("City : " + datas.request.query);
        Text text2 = new Text("Time : " + datas.current.observation_time);
        Text text3 = new Text("Temp : " + datas.current.temperature + " °c");
        ImageView image = new ImageView(datas.current.weather_icons.get(0));

        TextField input = new TextField("Enter a city");
        Button button = new Button("Search datas");

        Button download = new Button("Download datas");

        gridPane.add(text1, 0, 0);
        gridPane.add(text2, 1, 0);
        gridPane.add(text3, 1, 1);
        gridPane.add(image, 0, 2);
        gridPane.add(download, 1, 2);

        Integer i = 0;
        for (String data : datas.current.weather_descriptions) {
            Text desc = new Text(data);
            gridPane.add(desc, 0, 4+i);
            i++;
        }
        gridPane.add(input, 0, 6+i);
        gridPane.add(button, 1, 6+i);
        gridPane.add(button1, 0, 7+i);

        button.setOnAction(e -> {
            try {
                loadDisplay(getDisplayDatas(input.getText()));
                displayScene(display);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        download.setOnAction(e -> {
            try {
                downloadDatas(datas);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }
        });

        button1.setOnAction(e -> {
            displayScene(home);
        });

        display = new Scene(gridPane, 600, 300);
        window.setTitle(datas.request.query);
    }

    public String getBrutDatas(String city) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://api.weatherstack.com/current?access_key=aecf1c3fabfacda254bc9b259c622228&query=" + city))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        downLoadedDatas = response.body().toString();

        return response.body();
    }

    public WeatherDatas.Root getDisplayDatas(String city) throws JsonProcessingException {

        ObjectMapper om = new ObjectMapper();
        WeatherDatas.Root root = om.readValue(getBrutDatas(city), WeatherDatas.Root.class);

        return root;
    }

    public void downloadDatas(WeatherDatas.Root datas) throws FileNotFoundException, UnsupportedEncodingException {
        try {
            File myObj = new File("./files/meteo-" + datas.location.name.replace(" ", "_") + ".json");
            if (myObj.createNewFile()) {
                alertBox.display("File created !", myObj.getName() + " was created !");

                try {
                    FileWriter myWriter = new FileWriter(myObj.getAbsolutePath());
                    myWriter.write(downLoadedDatas);
                    myWriter.close();
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

   /* public void loadUpdate() {
        //composants scene2
        TextField name1 = new TextField("First name");
        TextField name2 = new TextField("name");
        TextField city = new TextField("City");
        TextField temp = new TextField("temperature");
        TextField city = new TextField("City");


        //setup scene2
        StackPane layout = new StackPane();
        layout.getChildren().add(button1);

        home = new Scene(layout, 600, 300);
        window.setTitle("Hello");
    }
    */
}