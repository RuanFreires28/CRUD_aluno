package edu.curso.View;

//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
//import java.util.Calendar;

import edu.curso.Controller.AlunoController;
import edu.curso.Entity.Aluno;

import javafx.application.Application;

import javafx.beans.binding.Bindings;
//import javafx.beans.property.ReadOnlyLongWrapper;
//import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleLongProperty;
//import javafx.beans.property.SimpleStringProperty;

import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

//import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javafx.util.Callback;

public class AlunoBoundary extends Application {
    private TextField txtNome = new TextField();
    private TextField txtRA = new TextField();
   // private TextField txtID = new TextField();
    private DatePicker dtaNascimento = new DatePicker();
    private AlunoController control = new AlunoController();
    private TableView<Aluno> tabela = new TableView<>();
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void bindings() { 
        Bindings.bindBidirectional(txtNome.textProperty(), control.nomeProperty());
        Bindings.bindBidirectional(txtRA.textProperty(), control.raProperty());
        Bindings.bindBidirectional(dtaNascimento.valueProperty(), control.nascimentoProperty());
        tabela.setItems( control.listaProperty() );
        
    }

    @SuppressWarnings("unchecked")
    public void createTable() { 
        
    /*  TableColumn<Aluno, String> col1 = new TableColumn<>("ID");
        col1.setCellValueFactory(
            cell -> new ReadOnlyStringWrapper( Long.toString(cell.getValue().getId())));
    */ 

        TableColumn<Aluno, Long> col1 = new TableColumn<>("Id");
        col1.setCellValueFactory(
            cell -> new SimpleLongProperty( cell.getValue().getId() ).asObject()
        );

        TableColumn<Aluno, String> col2 = new TableColumn<>("Nome");
        col2.setCellValueFactory(
            cell -> new ReadOnlyStringWrapper( cell.getValue().getNome() )
        );

        TableColumn<Aluno, String> col3 = new TableColumn<>("RA");
        col3.setCellValueFactory(
            cell -> new ReadOnlyStringWrapper( cell.getValue().getRa() )
        );

        TableColumn<Aluno, String> col4 = new TableColumn<>("Nascimento");
        col4.setCellValueFactory(
            cell -> { 
                String strDate = dtf.format( cell.getValue().getNascimento() );
                return new ReadOnlyStringWrapper( strDate );
            }
        );        

        
        tabela.getSelectionModel().selectedItemProperty().addListener(
            (obs, antigo, novo) -> control.alunoParaTela(novo)
        );

        TableColumn<Aluno, Void> col5 = new TableColumn<>("apagar");
        col5.setCellFactory(
            new Callback<TableColumn<Aluno,Void>,TableCell<Aluno,Void>>(){

                @Override
                public TableCell<Aluno, Void> call(TableColumn<Aluno, Void> param) {
                    return new TableCell<Aluno, Void>() { 
                        private Button btnApagar = new Button("Apagar");

                        @Override
                        public void updateItem(Void item, boolean empty) { 
                            super.updateItem(item, empty);

                            if (!empty) {
                                btnApagar.setOnAction( 
                                    act ->  {
                                        int linha = getIndex();
                                        Aluno aluno = tabela.getItems().get( linha );
                                        control.apagar(aluno);
                                    }
                                );
                                setGraphic( btnApagar );
                            } else {
                                setGraphic(null);
                            }
                        }
                    };
                }
            }
        );

        tabela.getColumns().addAll( col1, col2, col3, col4, col5 );
        
    }  


    public void start(Stage stage) { 
        BorderPane panePrincipal = new BorderPane();
        GridPane paneForm = new GridPane();
        HBox paneBotoes = new HBox();

        panePrincipal.setTop( paneForm );
        panePrincipal.setCenter( tabela );

        bindings();
        createTable();

        ColumnConstraints coluna1 = new ColumnConstraints();
        coluna1.setPercentWidth(30);

        ColumnConstraints coluna2 = new ColumnConstraints();
        coluna2.setPercentWidth(70);

        paneForm.getColumnConstraints().addAll( coluna1, coluna2 );


        paneForm.add( new Label("nome"), 0, 0);
        paneForm.add( txtNome, 1, 0);
        

        paneForm.add( new Label("RA: "), 0, 1);
        txtRA.setPromptText("Digite o RA (10 dígitos numéricos)");
        paneForm.add( txtRA, 1, 1);

        paneForm.add( new Label("Nascimento: "), 0, 2);
        paneForm.add( dtaNascimento, 1, 2);

        paneForm.add( paneBotoes, 0, 3, 2, 1);

        Button btnSalvar = new Button("Salvar");
        Button btnPesquisar = new Button("Pesquisar");

        btnSalvar.setOnAction( evento -> {
            try {
            control.verificaRa();
            control.criarAluno();
            new Alert( AlertType.INFORMATION, 
                "Aluno salvo com sucesso", ButtonType.OK).show();
            } catch (IllegalArgumentException e) {
                 new Alert( AlertType.ERROR, 
                e.getMessage(), ButtonType.OK).show();   
            }
        } );

        btnPesquisar.setOnAction( evento -> {
            try {
                control.verificaRa();
                try {
                    control.pesquisarRA();
                } catch (Exception e) {
                    new Alert( AlertType.ERROR, 
                    e.getMessage(), ButtonType.OK).show();    
                }
            } catch (IllegalArgumentException e) {
                 new Alert( AlertType.ERROR, 
                e.getMessage(), ButtonType.OK).show();   
            }
            
         });

        paneBotoes.getChildren().addAll(btnSalvar, btnPesquisar);

        Scene scn = new Scene( panePrincipal, 800, 600);
        stage.setScene(scn);
        stage.setTitle("Registro de Alunos");
        stage.show();
    }

    public static void main(String[] args) {
       Application.launch(AlunoBoundary.class, args);
    }
    
}
