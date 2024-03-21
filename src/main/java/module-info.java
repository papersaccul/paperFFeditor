module com.papersaccul.paperffeditor {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires atlantafx.base;
    requires java.desktop;

    opens com.papersaccul.paperffeditor to javafx.fxml;
    exports com.papersaccul.paperffeditor;
}
