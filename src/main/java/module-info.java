module com.papersaccul.paperffeditor {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.papersaccul.paperffeditor to javafx.fxml;
    exports com.papersaccul.paperffeditor;
}
