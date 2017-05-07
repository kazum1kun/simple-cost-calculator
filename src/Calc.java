import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.function.UnaryOperator;

/**
 * Main GUI class for the calculator
 *
 * @author Xuanli Lin
 * @version alpha-0.0.1
 */
public class Calc extends Application {
    // Input labels and fields
    private TextField heightField = new TextField();
    private Label heightLabel = new Label("高");

    private TextField lossField = new TextField();
    private Label lossLabel = new Label("损耗");

    private TextField laceWidthField = new TextField();
    private Label laceWidthLabel = new Label("花距");

    private TextField multiplicityField = new TextField();
    private Label multiplicityLabel = new Label("幅数");

    private TextField widthField = new TextField();
    private Label widthLabel = new Label("宽");

    private TextField ratioField = new TextField();
    private Label ratioLabel = new Label("比例");

    private TextField doorWidthField = new TextField();
    private Label doorWidthLabel = new Label("布料门幅");

    private TextField meterNumberField = new TextField();
    private Label meterNumberLabel = new Label("开米数");
    // Default numerical format for the input text
    private DecimalFormat format = new DecimalFormat("#.#");
    private UnaryOperator<TextFormatter.Change> textFormat = change -> {
        if (change.getControlNewText().isEmpty()) {
            return change;
        }

        ParsePosition parsePosition = new ParsePosition(0);
        Object object = format.parse(change.getControlNewText(), parsePosition);

        if (object == null || parsePosition.getIndex() < change.getControlNewText().length()) {
            return null;
        } else {
            return change;
        }
    };

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Main scene of the app
        Scene scene = new Scene(new Group());
        primaryStage.setTitle("开米数计算器");
        primaryStage.setWidth(280);
        primaryStage.setHeight(390);

        // App name
        Label appNameLabel = new Label("开米数计算器");
        appNameLabel.setFont(new Font("Microsoft YaHei", 20));

        configureLabel(heightLabel);
        configureTextField(heightField);
        HBox heightBox = new HBox(heightLabel, heightField);

        configureLabel(lossLabel);
        configureTextField(lossField);
        HBox lossBox = new HBox(lossLabel, lossField);

        configureLabel(laceWidthLabel);
        configureTextField(laceWidthField);
        HBox laceWidthBox = new HBox(laceWidthLabel, laceWidthField);

        configureLabel(multiplicityLabel);
        configureTextField(multiplicityField);
        HBox multiplicityBox = new HBox(multiplicityLabel, multiplicityField);

        configureLabel(widthLabel);
        configureTextField(widthField);
        HBox widthBox = new HBox(widthLabel, widthField);

        configureLabel(ratioLabel);
        configureTextField(ratioField);
        HBox ratioBox = new HBox(ratioLabel, ratioField);

        configureLabel(doorWidthLabel);
        configureTextField(doorWidthField);
        HBox doorWidthBox = new HBox(doorWidthLabel, doorWidthField);

        configureLabel(meterNumberLabel);
        configureTextField(meterNumberField);
        meterNumberField.setEditable(false);
        HBox meterNumberBox = new HBox(meterNumberLabel, meterNumberField);

        Separator meterNumberSeparator = new Separator();

        // A VBox to house various labels and fields
        VBox mainBox = new VBox();
        mainBox.setSpacing(10);
        mainBox.setPadding(new Insets(10, 20, 10, 20));
        mainBox.getChildren().addAll(appNameLabel, widthBox, heightBox, lossBox, ratioBox,
                doorWidthBox, laceWidthBox, multiplicityBox, meterNumberSeparator, meterNumberBox);

        ((Group) scene.getRoot()).getChildren().addAll(mainBox);
        // Show the app
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Format Labels
    private void configureLabel(Label label) {
        label.setPrefWidth(75);
        label.setFont(new Font("Microsoft YaHei", 13));
    }

    // Format TextFields
    private void configureTextField(TextField textField) {
        textField.setTextFormatter(new TextFormatter(textFormat));

        // Set up listener for fields
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isReadyForMultiplicityCalc()) {
                double width = Double.parseDouble(widthField.getText().trim());
                double ratio = Double.parseDouble(ratioField.getText().trim());
                double doorWidth = Double.parseDouble(doorWidthField.getText().trim());
                double multiplicity = width * ratio / doorWidth;
                if (multiplicity % 1 >= 0.3)
                    multiplicity = Math.ceil(multiplicity);
                else
                    multiplicity = Math.floor(multiplicity);
                int multiplicityInt = (int) multiplicity;
                multiplicityField.setText(multiplicityInt + "");
            }

            if (isReadyForMeterCalc()) {
                double height = Double.parseDouble(heightField.getText().trim());
                double loss = Double.parseDouble(lossField.getText().trim());
                double laceWidth = Double.parseDouble(laceWidthField.getText().trim());
                double multiplicity = Double.parseDouble(multiplicityField.getText().trim());
                double meterNumber = Math.ceil((height + loss) / laceWidth) * multiplicity;
                int meterNumberInt = (int) meterNumber;
                meterNumberField.setText(meterNumberInt + "");
            }
        });
    }

    // Whether multiplicity is ready to be calculated
    private boolean isReadyForMultiplicityCalc() {
        return !widthField.getText().trim().isEmpty() &&
                !ratioField.getText().trim().isEmpty() &&
                !doorWidthField.getText().trim().isEmpty();
    }

    // Whether all necessary info was inputted
    private boolean isReadyForMeterCalc() {
        return !multiplicityField.getText().trim().isEmpty() &&
                !heightField.getText().trim().isEmpty() &&
                !lossField.getText().trim().isEmpty() &&
                !laceWidthField.getText().trim().isEmpty();
    }
}
