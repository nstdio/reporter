package com.github.nstdio.reporter.gui.config;

import com.github.nstdio.reporter.gui.Utils;
import com.github.nstdio.reporter.gui.dialog.AlertBuilder;
import com.github.nstdio.reporter.gui.entity.Repo;
import com.github.nstdio.reporter.gui.prefs.Key;
import com.github.nstdio.reporter.gui.prefs.Prefs;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javaslang.control.Try;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.internet.InternetAddress;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static javafx.collections.FXCollections.observableArrayList;

public class ConfigController implements Initializable {
    private final static Logger logger = LoggerFactory.getLogger(ConfigController.class);
    private final Prefs prefs = Prefs.defaultPrefs();
    private final InternetAddress address = new InternetAddress();

    @FXML
    private TableColumn<String, String> emailColumn;
    @FXML
    private TableView<Repo> repoTableView;
    @FXML
    private TextField commiter;
    @FXML
    private TextField from;
    @FXML
    private TableView<String> receiptsTableView;
    @FXML
    private TextField username;


    public static void present(Window owner) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(ConfigController.class.getResource("/view/config.fxml"));
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setTitle("Preference");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(owner);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        repoTableView.setItems(observableArrayList(prefs.getRepos()));
        receiptsTableView.setItems(observableArrayList(prefs.getArray(Key.EMAIL_RECEIPT)));

        emailColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));

        prefs.get(Key.COMMITER).ifPresent(s -> commiter.setText(s));
        prefs.get(Key.EMAIL_USERNAME).ifPresent(s -> username.setText(s));
        prefs.get(Key.EMAIL_FROM).ifPresent(s -> from.setText(s));
    }

    @FXML
    private void close(ActionEvent actionEvent) {
        Optional.of(actionEvent.getSource())
                .map(src -> (Button) src)
                .map(button -> (Stage) button.getScene().getWindow())
                .ifPresent(stage -> {
                    stage.close();
                    logger.info("Closed.");
                });
    }

    @FXML
    private void onSave(ActionEvent actionEvent) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            prefs.put(Key.COMMITER, commiter.getText());
            prefs.put(Key.EMAIL_USERNAME, username.getText());
            prefs.put(Key.EMAIL_FROM, from.getText());
            prefs.put(repoTableView.getItems());
            prefs.putStringList(Key.EMAIL_RECEIPT, receiptsTableView.getItems());

            logger.info("Prefs thread finish its work.");
        });

        close(actionEvent);
    }

    @FXML
    private void onProjectCommit(TableColumn.CellEditEvent<Repo, String> event) {
        event.getRowValue().setProject(event.getNewValue());
    }

    @FXML
    private void onPathEditStart(TableColumn.CellEditEvent<Repo, String> event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Please select git repository.");

        final File dir = directoryChooser.showDialog(event.getTableView().getScene().getWindow());
        Optional<File> gitDir = Utils.validGitDir(dir);

        gitDir.ifPresent(file -> {
            final Repo repo = event.getRowValue();
            repo.setPath(file.getAbsolutePath());

            if (repo.getProject().trim().isEmpty()) {
                repo.setProject(Utils.projectName(file));
            }

            repo.setValid(true);

            event.getTableView().refresh();
        });

        if (!gitDir.isPresent() && dir != null) {
            AlertBuilder.builder()
                    .type(AlertType.ERROR)
                    .title("Invalid git directory.")
                    .header("Cannot find git directory.")
                    .content(String.format("Cannot find git directory in selected path: %s", dir.getAbsolutePath()))
                    .build()
                    .showAndWait();
        }
    }

    @FXML
    private void onReceiptAdd() {
        addItem(receiptsTableView, "");
    }

    @FXML
    private void onReceiptRemove() {
        removeSelected(receiptsTableView);
    }

    @FXML
    private void onAdd() {
        addItem(repoTableView, new Repo("", "", false));
    }

    @FXML
    private void onRemove() {
        removeSelected(repoTableView);
    }

    private <T> void removeSelected(TableView<T> tableView) {
        final T selectedItem = tableView.getSelectionModel().getSelectedItem();
        tableView.getItems().remove(selectedItem);
    }

    private <T> void addItem(TableView<T> tableView, T item) {
        final ObservableList<T> items = tableView.getItems();
        items.add(item);

        tableView.requestFocus();
        tableView.getSelectionModel().select(items.size() - 1);
    }

    @FXML
    public void onEmailCommit(TableColumn.CellEditEvent<String, String> event) {
        Optional.ofNullable(event.getNewValue())
                .filter(StringUtils::isNotBlank)
                .ifPresent(s -> {
                    final String value = Try.of(() -> {
                        address.setAddress(s);
                        address.validate();
                        return event.getNewValue().toLowerCase();
                    }).getOrElse(event::getOldValue);

                    setRowValue(event, value);
                });
    }

    private <T> void setRowValue(TableColumn.CellEditEvent<T, String> event, T s) {
        event.getTableView().getItems().set(event.getTablePosition().getRow(), s);
    }
}
