package edu.curso.Controller;

import java.time.LocalDate;

import edu.curso.Entity.Aluno;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



public class AlunoController{

    private ObservableList<Aluno> lista = FXCollections.observableArrayList();

    private StringProperty nome = new SimpleStringProperty("");
    private LongProperty id = new SimpleLongProperty(0);
    private StringProperty ra = new SimpleStringProperty("");
    private ObjectProperty<LocalDate> nascimento = 
            new SimpleObjectProperty<>(LocalDate.now());

    public ObservableList<Aluno> listaProperty() {
        return lista;
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public LongProperty idProperty() {
    
        return id;
    }

    public StringProperty raProperty() {
        return ra;
    }

    public ObjectProperty<LocalDate> nascimentoProperty() {
        return nascimento;
    }

   public void verificaRa() throws IllegalArgumentException {

    if(ra.length().get() != 10 || !ra.getValue().matches("\\d{0,10}"))
        throw new IllegalArgumentException("RA invalido");
   }

    public void alunoParaTela( Aluno aluno ) { 
        if (aluno != null) { 
            nome.set( aluno.getNome() );
            ra.set( aluno.getRa() );
            id.set( aluno.getId() );
            nascimento.set( aluno.getNascimento() );
        }
    }

    public void apagar(Aluno aluno){
        lista.remove(aluno);
        atualizaIdAll();
    }

    private void atualizaIdAll(){

        for (Aluno a: lista){
            id.set(lista.indexOf(a));
        }
    }

    public void criarAluno() {
        
        Aluno aluno = new Aluno();

        aluno.setId(lista.size());
        aluno.setNascimento(nascimento.get());
        aluno.setNome(nome.get());
        aluno.setRa(ra.get());

        lista.add(aluno);
        limpar();
    }


    public void pesquisarRA() throws Exception{

        boolean existe = false;

        for (Aluno a: lista){
            if(a.getRa().equals(ra.getValue()))
            {
                nome.set(a.getNome());
                nascimento.set(a.getNascimento());
                ra.set(a.getRa());
                existe = true;
            }    
        } 
        if (!existe){
            throw new Exception("Valor não encontrado"); 
        }

    }

    public void limpar(){
        nome.set("");
        ra.set("");
        nascimento.set(LocalDate.now());
    }
}