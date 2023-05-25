package com.example.filemanager;

import com.example.filemanager.appfunc.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class FMController {

    public FMController(){

    }


    Path Root;

    @FXML
    public void setRoot(String path){
        Root = new File(path).toPath();
        lastOpened.add(Root);
        curDir = Root;
        refresh();
    }
    managerMethods methods;

    Path curDir;
    Path selFile;
    @FXML
    Stage mainStage;
    //@FXML
    //Node eventNode;
    @FXML
    Scene scene;
    @FXML
    List<Path> lastOpened = new ArrayList<>();
    List<Path> lastRedo = new ArrayList<>();

    @FXML
    private Button btnBack, btnForward, btnRefresh;
    @FXML
    private Label lblPath;
    @FXML
    private BorderPane VboxRoot;
    @FXML
    private TextField fileMoveLog, txtSearch;
    @FXML
    private TableColumn<tabContainer, File> iconColumn;

    @FXML
    private TableColumn<tabContainer, String> lastModColumn, nameColumn, sizeColumn, typeColumn;

    TableColumn<tabContainer, String> source = new TableColumn<>("Source");
    @FXML
    private TableView<tabContainer> tableView;

    @FXML
    private TreeView<tabContainer> treeView;

    private static final Logger LOGGER = Logger.getLogger(FMMain.class.getName());
    FileHandler fileHandler;
    @FXML
    public void initialize() throws IOException {
        fileHandler = new FileHandler("FileManager.log");

        // Установка форматтера для определения структуры записей в лог-файле
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);

        LOGGER.addHandler(fileHandler);
    }
    @FXML
    void SaveLog(ActionEvent event) {
        fileHandler.close();
    }
    @FXML
    void OnClickAbout(ActionEvent event) throws IOException {
        Stage stageAbout = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(FMMain.class.getResource("views/FM-about-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stageAbout.setTitle("File Manager");
        stageAbout.setScene(scene);
        stageAbout.show();
        stageAbout.setResizable(false);
    }
    @FXML
    void onBack(ActionEvent event) {
        if(!curDir.endsWith(Root)){
            lastRedo.add(curDir);
            if(lastOpened.size() > 1){
                lastOpened.remove(lastOpened.size()-1);
                curDir = lastOpened.get(lastOpened.size()-1);
            }
            refresh();
            btnForward.setDisable(false);
        }
    }

    @FXML
    void onForward(ActionEvent event) {


        if(lastRedo.size() > 0){
            curDir = lastRedo.get(lastRedo.size()-1);
            lastRedo.remove(lastRedo.size()-1);
            refresh();
        }
        /*
        else if(lastOpened.size() == 1){
            curDir = lastOpened.get(0);
            refresh();
            lastOpened.remove(0);
            btnForward.setDisable(true);
        }
        else {
            curDir = lastOpened.get(lastOpened.size()-lastPtr);
            lastPtr--;
            refresh();
            lastOpened.remove(lastOpened.size()-1);
        }

         */
    }

    @FXML
    void onRefresh(ActionEvent event) {
        refresh();
    }

    void refresh(){

        btnBack.setDisable(false);
        TreeItem<tabContainer> treeRoot = new TreeItem<>(new tabContainer(Root.toFile()));
        treeRoot.setExpanded(true);
        treeView.setRoot(treeRoot);


        if (curDir.toString().equals(Root.toString()))
        {
            btnBack.setDisable(true);
            lastOpened.clear();
            lastOpened.add(Root);
        }
        if(!(lastRedo.size() > 0))
            btnForward.setDisable(true);

        if(!lastOpened.get(lastOpened.size()-1).toString().equals(curDir.toString()))
            lastOpened.add(curDir);
        openTable(curDir.toFile(), tableView);
        init();
        addNode(treeView.getRoot(), Root.toFile());

        LOGGER.log(Level.INFO, curDir.toString());
        //openTree(treeRoot, Root);
    }

    void init(){
        tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!Objects.isNull(tableView.getSelectionModel().getSelectedItem())){
                    selFile = Path.of(tableView.getSelectionModel().getSelectedItem().getFile().getPath());
                    if(event.getClickCount() == 2){
                        if(selFile.toFile().isDirectory()){
                            curDir = selFile;
                            lastRedo.clear();
                            refresh();
                        }
                        else {
                            try {
                                System.out.println(curDir.toFile());
                                //Process app = Runtime.getRuntime().exec("xdg-open " + selFile.toFile());
                                //Desktop.getDesktop().open(selFile.toFile());
                                ProcessBuilder processBuilder = new ProcessBuilder("xdg-open", selFile.toFile().toString());
                                Process process = processBuilder.start();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        });

        tableView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ENTER){
                    selFile = Path.of(tableView.getSelectionModel().getSelectedItem().getFile().getPath());
                    if(selFile.toFile().isDirectory()){
                        curDir = selFile;
                        lastRedo.clear();
                        refresh();
                    }
                    else {
                        try {
                            System.out.println(curDir.toFile());
                            //Process app = Runtime.getRuntime().exec("xdg-open " + selFile.toFile());
                            //Desktop.getDesktop().open(selFile.toFile());
                            ProcessBuilder processBuilder = new ProcessBuilder("xdg-open", selFile.toFile().toString());
                            Process process = processBuilder.start();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

            }
        });

        ContextMenuFactory factory = new ContextMenuFactory();

        methods = factory.getMethods();
        methods.setRoot(Root);
        final ContextMenu contextMenu = factory.createInstance(fileMoveLog);
        contextMenu.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                refresh();
            }
        });
        tableView.setContextMenu(contextMenu);
        tableView.setRowFactory(tv -> {
            TableRow<tabContainer> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                File selectedFile;
                if(Objects.isNull(row.getItem()))
                    factory.setFile(curDir.toFile());
                else{
                    selectedFile = row.getItem().getFile();
                    factory.setFile(selectedFile);
                }
                if (event.getButton() == MouseButton.SECONDARY) {
                    contextMenu.show(tableView, event.getScreenX(), event.getScreenY());

                }
            });

            row.setOnDragDetected(event -> {
                if (! row.isEmpty()) {
                    Dragboard db = row.startDragAndDrop(TransferMode.ANY);
                    ClipboardContent content = new ClipboardContent();
                    List<File> files = new ArrayList<>();
                    files.add(row.getItem().getFile());
                    content.putFiles(files);
                    db.setContent(content);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();

                if (db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    event.consume();
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    File source = db.getFiles().get(db.getFiles().size()-1);

                    File dest;
                    System.out.println(row.getIndex());
                    if(!Objects.isNull(row.getItem()))
                        dest = row.getItem().getFile();
                    else dest = curDir.toFile();

                    System.out.println(source + "\n" + dest + "\n" + new File(dest.toString() + "/" + source.getName()));

                    //source.renameTo(new File(dest.toString() + "/" + source.getName()))
                    if (methods.moveFile(source, dest))
                        fileMoveLog.setText("File " + source.getName() + " successfully drag and drop into " + dest.getName());
                    else fileMoveLog.setText("Error in drag and drop");
                    LOGGER.log(Level.INFO, fileMoveLog.getText());
                    event.setDropCompleted(true);
                    event.consume();
                    refresh();

                }
            });
            return row;
        });






        treeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    selFile = treeView.getSelectionModel().getSelectedItem().getValue().getFile().toPath();
                    if(selFile.toFile().isFile()) {
                        try {
                            //Process app = Runtime.getRuntime().exec("xdg-open " + selFile.toFile());
                            // Desktop.getDesktop().open(selFile.toFile());
                            ProcessBuilder processBuilder = new ProcessBuilder("xdg-open", selFile.toFile().toString());
                            Process process = processBuilder.start();
                            LOGGER.log(Level.INFO, "Open " + selFile.toFile().toString());
                            curDir = selFile.getParent();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else curDir = selFile;
                    refresh();
                    lastRedo.clear();
                }
            }
        });
    }

    private void openTable(File selDir, TableView<tabContainer> tableView){

        lblPath.setText(new File("/Root/" + Root.relativize(curDir)).getPath());


        File[] files = selDir.listFiles();
        List<tabContainer> tabs = new ArrayList<>();

        if(!curDir.toString().equals(Root.toString())){
            tabContainer parentDir = new tabContainer(curDir.getParent().toFile());
            parentDir.setName("...");
            tabs.add(parentDir);
        }
        for (File file : Objects.requireNonNull(files)) {
            tabs.add(new tabContainer(file));
        }

        tableView.setItems(FXCollections.observableList(tabs));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        lastModColumn.setCellValueFactory(new PropertyValueFactory<>("LastMod"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("Size"));

        tableView.refresh();
    }

    @FXML
    private void addNode(TreeItem<tabContainer> parentNode, File file) {


            tabContainer container = new tabContainer(file);
            TreeItem<tabContainer> node = new TreeItem<>(container);
            node.setExpanded(true);
            parentNode.getChildren().add(node);

            if (file.isDirectory()) {
                for (File subFile : Objects.requireNonNull(file.listFiles())) {
                    addNode(node, subFile);
                }
            }


    }
    @FXML
    void OnDelete(ActionEvent event) {
        File file = tableView.getSelectionModel().getSelectedItem().getFile();
        methods.deleteFile(file);
    }
    @FXML
    void onRename(ActionEvent event) {
        methods.renameFile(tableView.getSelectionModel().getSelectedItem().getFile());
    }

    @FXML
    public void CreateFolder(ActionEvent event) {
        methods.makeDir(curDir.toFile());
    }

    @FXML
    public void CreateFile(ActionEvent event) {
        methods.createFile(curDir.toFile());
    }

    @FXML
    public void Rename(ActionEvent event) {
        methods.renameFile(selFile.toFile());
    }

    @FXML
    public void DeleteFile(ActionEvent event) {
        methods.deleteFile(selFile.toFile());
    }

    @FXML
    public void PermDelete(ActionEvent event) {
        methods.permDeleteFile(selFile.toFile());
    }
    @FXML
    void PasteAction(ActionEvent event) {
        methods.paste(curDir.toFile());
    }
    @FXML
    void CopyAction(ActionEvent event) {
        methods.copy(selFile.toFile());
    }
    @FXML
    void CutAction(ActionEvent event) {
        methods.cut(selFile.toFile());
    }
    @FXML
    void MountDrive(ActionEvent event) throws IOException {
        Process process = Runtime.getRuntime().exec("lsblk -o MOUNTPOINT");

        File FlashDir = new File(Root + "/FlashDrive");
        if (FlashDir.mkdir())
            fileMoveLog.setText("Folder created");
        else fileMoveLog.setText("Folder was not created");
        LOGGER.log(Level.INFO, fileMoveLog.getText());


        File mountFrom = new File("/dev/");
        final String[] flashdrive = new String[1];
        Files.walk(mountFrom.toPath()).forEach(file ->{
            if(file.toFile().getName().startsWith("sd") && file.toFile().getName().length() == 4 && file.toFile().getName().endsWith("4"))
                flashdrive[0] = file.toString();

        });
        String command = "mount " + flashdrive[0] + " " + FlashDir.toString();
        System.out.println(command);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);

            Process mntProcess = processBuilder.start();
            int exitCode = mntProcess.waitFor();

            if (exitCode == 0) {
                fileMoveLog.setText("Mounted in Root/Flashdrive");
            } else {
                fileMoveLog.setText("Mount error");
            }
            LOGGER.log(Level.INFO, fileMoveLog.getText());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


        
    }
    @FXML
    public void OnActionSearch(ActionEvent event) throws IOException {
        if(!tableView.getColumns().contains(source))
        {
            source.prefWidthProperty().set(10);
            source.setCellValueFactory(new PropertyValueFactory<>("Source"));
            tableView.getColumns().add(source);
        }
        if(!txtSearch.getText().isEmpty())
        {
            List<tabContainer> tabs = new ArrayList<>();
            Files.walk(curDir).forEach(file ->{
                if(file.toFile().getName().startsWith(txtSearch.getText()))
                    tabs.add(new tabContainer(file.toFile()));
            });
            tableView.setItems(FXCollections.observableList(tabs));
        } else {
            tableView.getColumns().remove(source);
            refresh();
        }
    }
}
