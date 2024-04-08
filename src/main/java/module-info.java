module fr.kinan.saad.iaapplication {
    requires javafx.controls;
    requires javafx.fxml;


    opens fr.kinan.saad.iaapplication to javafx.fxml;
    exports fr.kinan.saad.iaapplication;
}