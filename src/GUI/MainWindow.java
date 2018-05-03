package GUI;

import business.SaveFileBusiness;
import domain.ImageObject;
import domain.MosaicObject;
import java.awt.geom.AffineTransform;
import data.SaveFileData;
import domain.PartsImage;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.crypto.spec.GCMParameterSpec;
import javax.imageio.ImageIO;

/**
 *
 * @author hansel
 */
public class MainWindow extends Application {

    //atributos
    private final int WIDTH = 1300;
    private final int HEIGHT = 700;
    private ScrollPane scrollPaneImage, scrollPaneMosaic;
    private Pane pane;
    private Scene scene;
    private Canvas canvasImage, canvasMosaic;
    private Label labelPx, labelRows, labelColumns;
    private Button btnSelectImage, btnDeleteMosaic, saveNewImage, btDrawDefaultGrid, btDrawGrid;
    private TextField txtPS, txtRows, txtColumns;
    int column = 0, row = 0;
    int columnsMosaic = 0, rowsMosaic = 0;
    int filas = 0, columnas = 0;
    private int pixelSize = 0;
    private int i, j, k, l;
    private int partsImageWidth, partsImageHeight;
    private Object image;
    private Button btnDeleteImage;
    int x1, y1, posX, posY;
    private ContextMenu contextMenu;
    private MenuItem miDelete, miRotateLeft, miRotateRight, miVerticalFlip, miHorizontalFlip;
    private MenuBar mbMenu;
    private Menu menuFile;
    private MenuItem miSaveProject, miOpenProject, miNewProject;
    private BorderPane root;
    private ImageView imageView;
    private PixelReader pr;
    private SnapshotParameters snapshot;

    private PartsImage[][] matrizImage;
    private PartsImage[][] matrizMosaic;
    private BufferedImage bufferedImage;
//    private boolean rotateAccess, eraserAccess = false;

    @Override
    public void start(Stage primaryStage) {
        //se inicializan los componentes de la parte grafica
//        FileChooser fileChooser = new FileChooser();
        this.root = new BorderPane();
        this.pane = new Pane();
        this.scene = new Scene(this.pane, WIDTH, HEIGHT);
        this.scrollPaneImage = new ScrollPane();
        this.scrollPaneMosaic = new ScrollPane();
        this.canvasImage = new Canvas(400, 600);
        this.canvasMosaic = new Canvas(650, 600);
        this.scrollPaneImage.setContent(this.canvasImage);
        this.scrollPaneMosaic.setContent(this.canvasMosaic);
        this.scrollPaneImage.setPrefSize(510, 600);
        this.scrollPaneMosaic.setPrefSize(650, 600);
        this.scrollPaneMosaic.relocate(600, 30);
        this.scrollPaneImage.relocate(50, 30);
        this.scrollPaneImage.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.scrollPaneImage.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.scrollPaneMosaic.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.scrollPaneMosaic.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        this.labelPx = new Label("Pixel size:");
        this.labelRows = new Label("Row:");
        this.labelColumns = new Label("Columns:");
        this.txtPS = new TextField();
        this.txtRows = new TextField();
        this.txtColumns = new TextField();
        this.btnSelectImage = new Button("Select an Image");
        this.btnDeleteImage = new Button("Delete Image");
        this.btnDeleteMosaic = new Button("Delete Mosaic");
        this.saveNewImage = new Button("Save Image");
        this.btDrawDefaultGrid = new Button("Draw default Mosaic");
        this.btDrawGrid = new Button("Draw Mosaic");
        saveNewImage.relocate(1174, 640);
        btnDeleteMosaic.relocate(481, 640);
        btnDeleteImage.relocate(350, 640);
        labelPx.relocate(50, 645);
        btnSelectImage.relocate(230, 640);
        txtPS.relocate(102, 640);
        txtPS.setPrefSize(100, 26);
        btDrawDefaultGrid.relocate(600, 640);
        labelRows.relocate(830, 645);
        txtRows.relocate(860, 640);
        txtRows.setPrefSize(50, 26);
        labelColumns.relocate(948, 645);
        txtColumns.relocate(1000, 640);
        txtColumns.setPrefSize(50, 26);
        btDrawGrid.relocate(1070, 640);
        scene.setFill(Color.LIGHTBLUE);

        pane.setBackground(Background.EMPTY);

        this.contextMenu = new ContextMenu();
        this.miDelete = new MenuItem("Delete");
        this.miRotateLeft = new MenuItem("Rotate to left");
        this.miRotateRight = new MenuItem("Rotate to right");
        this.miVerticalFlip = new MenuItem("Vertical flip");
        this.miHorizontalFlip = new MenuItem("Horizontal flip");
        contextMenu.getItems().addAll(miDelete, new SeparatorMenuItem(), miRotateRight, miRotateLeft,
                new SeparatorMenuItem(), miHorizontalFlip, miVerticalFlip);

        mbMenu = new MenuBar();
        mbMenu.setMinWidth(1320);
        menuFile = new Menu("File");
        mbMenu.getMenus().add(menuFile);
        this.miSaveProject = new MenuItem("save Project");
        this.miOpenProject = new MenuItem("Open Project");
        this.miNewProject = new MenuItem("New Project");
        menuFile.getItems().addAll(miSaveProject, miOpenProject, miNewProject);
        this.root.setTop(mbMenu);

        GraphicsContext graCoMosaic = this.canvasMosaic.getGraphicsContext2D();
        GraphicsContext graCoImage = this.canvasImage.getGraphicsContext2D();
//        GraphicsContext graCoImage = this.canvasMosaic.getGraphicsContext2D();

        this.canvasImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                selectAnImage((int) event.getX(), (int) event.getY());
                System.out.println(event.getX() + ", " + event.getY());
            } // handle
        }
        );

        canvasMosaic.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//            int k, l;

            @Override
            public void handle(MouseEvent event) {
                if (matrizMosaic != null) {
                    if (event.getButton() == MouseButton.SECONDARY) {
                        x1 = (int) event.getX();
                        y1 = (int) event.getY();
                        contextMenu.show(canvasMosaic, event.getScreenX(), event.getScreenY());
                        if (event.getButton() == MouseButton.PRIMARY) {
                            contextMenu.setImpl_showRelativeToWindow(false);
                        }
                    } else if (event.getButton() == MouseButton.PRIMARY) {
                        pasteImageOnMosaic(graCoMosaic, (int) event.getX(), (int) event.getY());
                    }
                }
            }
        });

        btnSelectImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GraphicsContext graCo = canvasImage.getGraphicsContext2D();
                selectImage(primaryStage, graCo, canvasImage);
            } // handle
        }
        );

        btnDeleteImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                graCoMosaic.clearRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
                matrizImage = null;
            } // handle
        }
        );

        btnDeleteMosaic.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                graCoMosaic.clearRect(0, 0, canvasMosaic.getWidth(), canvasMosaic.getHeight());
                matrizMosaic = null;
            } // handle
        }
        );

        btDrawDefaultGrid.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (matrizImage != null) {
                    rowsMosaic = row;
                    columnsMosaic = column;
                    drawGrid(graCoMosaic, canvasMosaic, row, column);
                } else {
                    System.err.println("no se pinta lo que no existe");
                }
            }
        }
        );

        btDrawGrid.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (matrizImage != null) {
                    if (!txtRows.getText().equals("") && !txtColumns.getText().equals("")) {
                        rowsMosaic = Integer.parseInt(txtRows.getText());
                        columnsMosaic = Integer.parseInt(txtColumns.getText());
                        drawGrid(graCoMosaic, canvasMosaic, rowsMosaic, columnsMosaic);
                        txtRows.setText("");
                        txtColumns.setText("");
                    } else {
                        System.err.println("Error in print Grind");
                    }
                } else {
                    System.err.println("no se pinta grid si no hay imagen");
                }
            }
        }
        );

        saveNewImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (matrizMosaic != null) {
                    exportImage(primaryStage, graCoMosaic);
                } else {
                    System.err.println("no se puede guardar vacio");
                }
            }
        });

        miDelete.setOnAction(new EventHandler<ActionEvent>() {
            int k, l;
            Image imagen = null;

            @Override
            public void handle(ActionEvent event) {
                for (int x = 0; x < columnsMosaic; x++) {
                    for (int y = 0; y < MainWindow.this.rowsMosaic; y++) {
                        if (matrizMosaic[x][y].chunkClicked(x1, y1)) {
                            k = x;
                            l = y;
                            break;
                        }
                    }
                }
                matrizMosaic[k][l].setiBytes(matrizImage[i][j].getiBytes());
                graCoMosaic.clearRect(matrizMosaic[k][l].getPosix() * pixelSize + 2, matrizMosaic[k][l].getPosiy() * pixelSize + 2, pixelSize - 3, pixelSize - 3);
                matrizMosaic[k][l].setiBytes(null);
                drawGridSave(graCoMosaic, canvasMosaic, rowsMosaic, columnsMosaic);
            }

        });

        miRotateRight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

        miRotateLeft.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

        miVerticalFlip.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

        miHorizontalFlip.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

        miSaveProject.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    safeProyect(primaryStage);
                } catch (IOException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        miOpenProject.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                reinit(primaryStage, canvasImage, graCoImage, graCoMosaic, canvasMosaic);
            }
        });

        miNewProject.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newProyect(canvasImage, graCoImage, graCoMosaic, canvasMosaic);
                txtRows.setText("");
                txtColumns.setText("");
                btDrawDefaultGrid.setDisable(false);
                btDrawGrid.setDisable(false);
                btnDeleteMosaic.setDisable(false);
            }
        });

        this.pane.getChildren().add(this.scrollPaneImage);
        this.pane.getChildren().add(this.scrollPaneMosaic);
        this.pane.getChildren().add(this.btnSelectImage);
        this.pane.getChildren().add(this.btnDeleteMosaic);
        this.pane.getChildren().add(this.txtPS);
        this.pane.getChildren().add(this.txtRows);
        this.pane.getChildren().add(this.txtColumns);
        this.pane.getChildren().add(this.labelPx);
        this.pane.getChildren().add(this.labelColumns);
        this.pane.getChildren().add(this.labelRows);
        this.pane.getChildren().add(this.btDrawDefaultGrid);
        this.pane.getChildren().add(this.btDrawGrid);
        this.pane.getChildren().add(this.btnDeleteImage);
        this.pane.getChildren().add(this.saveNewImage);
        this.pane.getChildren().add(this.root);
        primaryStage.setTitle("Mosaic Maker");
        primaryStage.setScene(this.scene);
        primaryStage.resizableProperty().set(false);
        primaryStage.show();
    } // start

    public void selectImage(Stage primaryStage, GraphicsContext graCoImage, Canvas canvasImage) {
        FileChooser fileChooserSelect = new FileChooser();
        if (!txtPS.getText().equals("")) {
            txtPS.setEditable(false);
            FileChooser.ExtensionFilter chooser = new FileChooser.ExtensionFilter("Image Extends", "*.png", "*.jpg", "*.jpeg");
            fileChooserSelect.getExtensionFilters().add(chooser);
            File selectedDirectory = fileChooserSelect.showOpenDialog(primaryStage);
            if (selectedDirectory != null) {
                try {
                    bufferedImage = ImageIO.read(selectedDirectory);
                    canvasImage.setHeight(bufferedImage.getHeight());
                    canvasImage.setWidth(bufferedImage.getWidth());
                    imageParts(graCoImage, canvasImage);
                } catch (IOException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            System.err.println("Ingrese pixel size");
        }
    }

    public void selectAnImage(int posX, int posY) {
        if (matrizImage == null) {
            System.out.println("Ingrese una imagen primero");
        } else if (matrizImage != null) {
            for (int x = 0; x < column; x++) {
                for (int y = 0; y < row; y++) {
                    if (matrizImage[x][y].chunkClicked(posX, posY)) {
                        i = x;
                        j = y;
                        break;
                    } else {
                        System.err.println("ERROR" + column + row);
                    }
                }
            }
        }
    }

    public void pasteImageOnMosaic(GraphicsContext graCoMosaic, int posX, int posY) {
        for (int x = 0; x < column; x++) {
            for (int y = 0; y < MainWindow.this.row; y++) {
                if (matrizMosaic[x][y].chunkClicked(posX, posY)) {
                    k = x;
                    l = y;
                    break;
                }
            }
        }
        try {
            matrizMosaic[k][l].setiBytes(matrizImage[i][j].getiBytes());
            matrizMosaic[k][l].draw(graCoMosaic);
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public byte[] imageToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    } // imageToBytes

    public void imageParts(GraphicsContext graCoIma, Canvas canvasImage) {
        // determina el ancho y la altura
        pixelSize = Integer.parseInt(txtPS.getText());
        // determina el ancho y la altura
        this.partsImageWidth = (int) this.bufferedImage.getWidth();
        this.partsImageHeight = (int) this.bufferedImage.getHeight();
        //define el las filas y las columnas de la matriz
        row = partsImageHeight / pixelSize;
        column = partsImageWidth / pixelSize;
        filas = 0;
        columnas = 0;
        System.err.println(column + ", " + row);
//        PixelReader pixelReader = image.getPixelReader();
        matrizImage = new ImageObject[column][row];
        //ciclos para ir cortando la imagen con el tamaño de los pixeles
        for (int x = 0; x < this.column; x++) {
            for (int y = 0; y < this.row; y++) {
                //Initialize the image array with image parts
                if (pixelSize < partsImageWidth) {
                    if (pixelSize < partsImageHeight) {
                        //corta la imagen en partes
                        try {
                            BufferedImage aux = bufferedImage.getSubimage((x * this.pixelSize), (y * this.pixelSize), this.pixelSize, this.pixelSize);
                            //va guardando las partes de la imagen en una matriz de objetos y la va pintando a la vez
                            this.matrizImage[x][y] = new ImageObject(imageToBytes(aux), x, y, pixelSize);
                            matrizImage[x][y].draw(graCoIma);
                        } catch (IOException ex) {
                            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        //se le resta los pixeles de la parte recortada
                        partsImageHeight -= pixelSize;
                        filas++;
                        System.err.println("filas" + filas);
                    } else {
                        System.err.println("error en fila");
                    }
                } else {
                    System.err.println("error en columna");
                }
                // draws the image parts
            } // for y
            partsImageWidth -= pixelSize;
            partsImageHeight = (int) this.bufferedImage.getHeight();
            columnas++;
            System.err.println("columnas" + columnas);
        } // for x
    }

    //inicializa el grid
    public void initMosiacPartsImage(int row, int column) {
        //inicializa el grid con el tamaño de la matriz de objetos donde estan las partes de imagenes
        this.matrizMosaic = new MosaicObject[column][row];
        for (int x = 0; x < column; x++) {
            for (int y = 0; y < row; y++) {
                this.matrizMosaic[x][y] = new MosaicObject(new byte[0], x, y, this.pixelSize);
            }
        }
    }

    //pintar el grid
    public void drawGrid(GraphicsContext gc, Canvas canvasMosaic, int row, int column) {
        //se inicia el grid
        initMosiacPartsImage(row, column);
        canvasMosaic.setHeight(row * pixelSize);
        canvasMosaic.setWidth(column * pixelSize);
        for (int x = 0; x <= row; x++) {
            gc.strokeLine(0, x * pixelSize, column * pixelSize, x * pixelSize); // rows
        }
        for (int y = 0; y <= column; y++) {
            gc.strokeLine(y * pixelSize, 0, y * pixelSize, pixelSize * row); // cols
        }
    } // drawGrid

    public void drawGridSave(GraphicsContext gc, Canvas canvasMosaic, int row, int column) {
        //se inicia el grid
//        initMosiacPartsImage(row, column);
        canvasMosaic.setHeight(row * pixelSize);
        canvasMosaic.setWidth(column * pixelSize);
        for (int x = 0; x <= row; x++) {
            gc.strokeLine(0, x * pixelSize, column * pixelSize, x * pixelSize); // rows
        }
        for (int y = 0; y <= column; y++) {
            gc.strokeLine(y * pixelSize, 0, y * pixelSize, pixelSize * row); // cols
        }
    }

    public void newProyect(Canvas canvasImage, GraphicsContext graCoImage, GraphicsContext graCoMosaic, Canvas canvasMosaic) {
        txtPS.setEditable(true);
        txtPS.setText("");
        this.matrizMosaic = null;
        this.matrizImage = null;
        this.pixelSize = 0;
        this.filas = 0;
        this.column = 0;
        this.columnas = 0;
        this.row = 0;
        this.image = null;
        this.bufferedImage = null;
        graCoImage.clearRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
        graCoMosaic.clearRect(0, 0, canvasMosaic.getWidth(), canvasMosaic.getHeight());
    }

    public void exportImage(Stage primaryStage, GraphicsContext graCoMosaic) {
        try {
            graCoMosaic.clearRect(0, 0, canvasMosaic.getWidth(), canvasMosaic.getHeight());
            graCoMosaic.clearRect(0, 0, column * pixelSize, row * pixelSize);
            repaintImage(graCoMosaic, row, column);
            WritableImage wim = new WritableImage((int) Math.round(canvasMosaic.getWidth()), (int) Math.round(canvasMosaic.getHeight()));
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setFill(Color.TRANSPARENT);
            canvasMosaic.snapshot(snapshotParameters, wim);
            FileChooser fileChooserExport = new FileChooser();
            FileChooser.ExtensionFilter chooser = new FileChooser.ExtensionFilter("PNG", "*.png");
            fileChooserExport.getExtensionFilters().add(chooser);
//        fileChooserExport.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
//        setExtFilters(fileChooser);
            File file = fileChooserExport.showSaveDialog(primaryStage);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
            } catch (IOException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
            drawGridSave(graCoMosaic, canvasMosaic, rowsMosaic, columnsMosaic);
            repaintImage(graCoMosaic, row, column);
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    
    public void repaintImage(GraphicsContext graCoMosaic, int row, int column) throws IOException {
        for (int x = 0; x < columnsMosaic; x++) {
            for (int y = 0; y < rowsMosaic; y++) {
                try {
                    if (matrizMosaic[x][y].getiBytes().length != 0) {
                        matrizMosaic[x][y].draw(graCoMosaic);
                        System.err.println("se pinto");
                    } else {
                        System.err.println("error al pintar");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            } // for y
        } // for x
    }

    /*
    en el metodo lo que se intenta es que por cada parte de la imagen
    se vaya partiendo un pedazo con la misma funcion que el imageParts 
    pero esta vez por pixel e ir guardando en la una posicion diferente..
    como decir "la primera fila se guarde en la primera columna"
     */
    public void rotateMosaic(MosaicObject mosaicImageVec[][], GraphicsContext graCoMosaic, int posX, int posY) {
//        int pxs = 1;
//        int c = pixelSize * pxs;
//        int r = pixelSize * pxs;
//        int height = pixelSize;
//        int width = pixelSize;
////        PixelReader pr = mosaicImageVec[x][y].getPixelReader();
//        if (this.mosaicImageVec != null) {
//            for (int x = 0; x < column; x++) {
//                for (int y = 0; y < row; y++) {
//                    if (partsImageVec[x][y].pressMouse(posX, posY)) {
//                    if (mosaicImageVec[x][y] != null) {
//                        PixelReader pr = mosaicImageVec[x][y].getImage().getPixelReader();
//                        for (int i = 0; i < c; i++) {
//                            for (int j = 0; j < r; j++) {
//                                WritableImage writableImage = new WritableImage(pr, (x * pxs), (y * pxs), pxs, pxs);
//                                Image aux = (Image) writableImage;
//                                //va guardando las partes de la imagen en una matriz de objetos y la va pintando a la vez
//                                partsImageVec[i][j] = partsImageVec[j][i];
//                                partsImageVec[j][i].paintImage(graCoMosaic);
//                                height-= pxs;
//                            }
//                            width-=pxs;
//                        }
//                    }
//                    }
//                }
//            } // for y
//        } // for x
    }

    //guarda la informacion del mosaicObject en .dat falta de hacer
    public void safeProyect(Stage primaryStage) throws IOException {
        if (pixelSize != 0 && row != 0 && column != 0) {
//                setExtFilters(fileChooser);
//                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DAT", "*.dat"));
            FileChooser fileChooserSafe = new FileChooser();
            FileChooser.ExtensionFilter chooser = new FileChooser.ExtensionFilter("DAT", "*.dat");
            fileChooserSafe.getExtensionFilters().add(chooser);
//                fileChooser.getSelectedExtensionFilter();
            File file = fileChooserSafe.showSaveDialog(primaryStage);
            if (file != null) {
                new SaveFileBusiness().saveProject(matrizImage, matrizMosaic, file);
            } else {
                System.err.println("no se guardo nada");
            }
        }
    }

    public void openProject(Stage primaryStage, GraphicsContext graCoMosaic, Canvas canvasMosaic) {
        FileChooser fileChooserOpen = new FileChooser();
        FileChooser.ExtensionFilter chooser = new FileChooser.ExtensionFilter("SAV", "*.sav");
        fileChooserOpen.getExtensionFilters().add(chooser);
//        setExtFilters(fileChooser);
        File selectedDirectory = fileChooserOpen.showOpenDialog(primaryStage);
        if (selectedDirectory != null) {
        }
    }

    public void reinit(Stage primaryStage, Canvas canvasImage, GraphicsContext graCoImage, GraphicsContext graCoMosaic, Canvas canvasMosaic) {
        FileChooser fileChooserOpen = new FileChooser();
        FileChooser.ExtensionFilter chooser = new FileChooser.ExtensionFilter("DAT", "*.dat");
        fileChooserOpen.getExtensionFilters().add(chooser);
        File file = fileChooserOpen.showOpenDialog(primaryStage);
        try {
            if (file.exists()) {
                List<PartsImage[][]> partsImageses = new SaveFileBusiness().recover(file);
                if (partsImageses.get(0) != null) {
                    this.matrizImage = partsImageses.get(0);
                    this.pixelSize = this.matrizImage[0][0].getPixelSize();
                    this.row = this.matrizImage.length;
                    this.columnas = this.matrizImage[0].length;
                    canvasImage.setHeight((this.row) * this.pixelSize + ((this.row + 1) * 5));
                    canvasImage.setWidth((this.column) * this.pixelSize + ((this.column + 1) * 5));
                    for (int x = 0; x < this.column; x++) {
                        for (int y = 0; y < this.row; y++) {
                            if (matrizImage[x][y].getiBytes().length != 0) {
                                this.matrizImage[x][y].draw(graCoImage);
                            } else {
                                System.err.println("error al pintar");
                            }
                        } // for y
                    } // for x
                } // if (list.get(0) != null)
                if (partsImageses.get(1) != null) {

                    this.matrizMosaic = partsImageses.get(1);
                    this.row = this.matrizMosaic.length;
                    this.column = this.matrizMosaic[0].length;
                    canvasMosaic.setHeight(this.row * this.pixelSize);
                    canvasMosaic.setWidth(this.column * this.pixelSize);
                    drawGrid(graCoMosaic, canvasMosaic, column, row);
                    repaintImage(graCoMosaic, column, row);
                } // if (list.get(1) != null)
            } // if (new File("save.dat").exists())
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } // try-catch
        txtPS.setText(String.valueOf(pixelSize));
        txtPS.setEditable(false);
        txtColumns.setEditable(false);
        txtRows.setEditable(false);
        btDrawDefaultGrid.setDisable(true);
        btDrawGrid.setDisable(true);
        btnDeleteMosaic.setDisable(true);
    } // reinit

    public static void main(String[] args) {
        launch(args);
    } // main
} // fin de la clase

