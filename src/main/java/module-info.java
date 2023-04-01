module com.quixo.projet_quixo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;

    requires org.controlsfx.controls;

    opens com.quixo.projet_quixo to javafx.fxml;
    exports com.quixo.projet_quixo;

    exports com.quixo.projet_quixo.controller;
    opens com.quixo.projet_quixo.controller to javafx.fxml;
}