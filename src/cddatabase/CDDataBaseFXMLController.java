/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cddatabase;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import java.util.Date;
import javafx.scene.control.cell.PropertyValueFactory;
/**
 *
 * @author kevinbudd
 */
public class CDDataBaseFXMLController implements Initializable {
    
    @FXML
    private Button btnConnect;
    @FXML
    private TextField txtConnection;
    @FXML
    private TextField txtUserName;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtArtistName;
    @FXML
    private TextField txtArtistGenre;
    @FXML
    private TextField txtAlbum;
    @FXML
    private TextField txtAlbumConsoleArtist;
    @FXML
    private TextField txtReleaseDate;
    private TableView<String> tableviewArtist;
    @FXML
    private TableView<String> tableviewAlbum;
    @FXML
    private TableView<String> tableviewBorrower;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtPhone;
    
    private ObservableList artistData = FXCollections.observableArrayList();
    private ObservableList albumData = FXCollections.observableArrayList();
    private ObservableList borrowerData = FXCollections.observableArrayList();
    private ObservableList borrowListData = FXCollections.observableArrayList();
    
    private ObservableList artistNamesData = FXCollections.observableArrayList();
    private ObservableList albumNamesData = FXCollections.observableArrayList();
    private ObservableList borrowerNameData = FXCollections.observableArrayList();
    
    private ObservableList albumHistoryData = FXCollections.observableArrayList();
    private ObservableList borrowerHistoryData = FXCollections.observableArrayList();
    private ObservableList artistDataClass = FXCollections.observableArrayList();
    private ObservableList borrowListDataClass = FXCollections.observableArrayList();
    
    private static Connection connection;
    private String connectionString;
    private String username;
    private String password;
    @FXML
    private Button btnArtist;
    @FXML
    private Button btnAddNewArtist;
    @FXML
    private Button btnAlbum;
    @FXML
    private Button btnAddNewAlbum;
    @FXML
    private Button btnBorrower;
    @FXML
    private Button btnAddNewBorrower;
    @FXML
    private Tab mnuBorrowList;
    @FXML
    private ListView<String> lstArtist;
    
    private String selectedArtist;
    private String selectedAlbum;
    private String selectedBorrower;
    private String selectedBorrowListItem;
    
    @FXML
    private ListView<String> lstAlbums;
    @FXML
    private Button btnBorrow;
    private TableView<String> tableviewBorrowList;
    @FXML
    private ListView<String> lstBorrowerName;
    @FXML
    private Button btnReturn;
    @FXML
    private TableView<String> tableviewAlbumHistory;
    @FXML
    private TableView<String> tableviewBorrowerHistory;
    
    private Date date;
    private Date dueDate;
    private SimpleDateFormat sdf;
    @FXML
    private TableView<Artist> tableviewArtistClass;
    
    private BorrowList selectedBorrowListClass;
    private Artist selectedArtistClass;
    @FXML
    private TableView<BorrowList> tableviewBorrowListClass;
    @FXML
    private TableColumn<BorrowList, String> colBLID;
    @FXML
    private TableColumn<BorrowList, String> colBLAlbumName;
    @FXML
    private TableColumn<BorrowList, String> colBLBorrowerName;
    @FXML
    private TableColumn<BorrowList, String> colBLBorrowDate;
    @FXML
    private TableColumn<BorrowList, String> colBLDueDate;
    @FXML
    private TableColumn<Artist, String> colARTID;
    @FXML
    private TableColumn<Artist, String> colARTArtist;
    @FXML
    private TableColumn<Artist, String> colARTGenre;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        date = new Date();
        String DATE_FORMAT = "yyyy-MM-dd";
        sdf = new SimpleDateFormat(DATE_FORMAT);
        System.out.println(sdf.format(date));
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        dueDate = new Date(System.currentTimeMillis() + (14 * DAY_IN_MS));
        System.out.println(sdf.format(dueDate));
        
        colARTID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colARTArtist.setCellValueFactory(new PropertyValueFactory<>("name"));
        colARTGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        
        colBLID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colBLAlbumName.setCellValueFactory(new PropertyValueFactory<>("albumName"));
        colBLBorrowerName.setCellValueFactory(new PropertyValueFactory<>("borrowerName"));
        colBLBorrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        colBLDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CDDataBaseFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        lstArtist.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            
            selectedArtist = newValue;
            System.out.println(selectedArtist);
            
            try {
                populateAlbumListview(selectedArtist);
            } catch (SQLException ex) {
                Logger.getLogger(CDDataBaseFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        });
        
        lstAlbums.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            
            selectedAlbum = newValue;
            
            System.out.println(selectedAlbum);
            
            albumHistoryData.removeAll(albumHistoryData);
            
            String SQL = "select AlbumName, BorrowerName, BorrowDate, DueDate from BorrowListHistory where AlbumName = '" + selectedAlbum + "';";

            buildData(SQL, albumHistoryData, tableviewAlbumHistory);
            
            
            
        });
        
        lstBorrowerName.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            
            selectedBorrower = newValue;
            
            System.out.println(selectedBorrower);
            
            borrowerHistoryData.removeAll(borrowerHistoryData);
            
            String SQL = "select AlbumName, BorrowerName, BorrowDate, DueDate from BorrowListHistory where BorrowerName = '" + selectedBorrower + "';";

            buildData(SQL, borrowerHistoryData, tableviewBorrowerHistory);
            
        });
        
        tableviewArtistClass.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            
            selectedArtistClass = newSelection;
            
            System.out.println(selectedArtistClass.getName() + " " + selectedArtistClass.getGenre());
            
        });
        
        tableviewBorrowListClass.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            
            selectedBorrowListClass = newSelection;
            
            //System.out.println(selectedBorrowListClass.getAlbumName());
            
        });
            
        
    }
     
     // Class creating method (for artist) to populate Tableview from Database //
    public void buildArtistDataClass(){        
   
    try{      
        String SQL = "Select * from Artist";
        
        Statement statement;
        
        statement = connection.createStatement();
        
        ResultSet rs = statement.executeQuery(SQL);  
        
        while(rs.next()){
            Artist artist = new Artist();
            artist.setId(rs.getString("id"));                       
            artist.setName(rs.getString("name"));
            artist.setGenre((rs.getString("genre")));
            
            artistDataClass.add(artist);                  
        }
        tableviewArtistClass.setItems(artistDataClass);
    }
    catch(Exception e){
          e.printStackTrace();
          System.out.println("Error on Building Data");            
    }
}
    
    public void buildBorrowListDataClass(){        
   
        borrowListDataClass.removeAll(borrowListDataClass);
       
        
    try{      
        String SQL = "Select * from BorrowList";
        
        Statement statement;
        
        statement = connection.createStatement();
        
        ResultSet rs = statement.executeQuery(SQL);  
        
        while(rs.next()){
            BorrowList borrowList = new BorrowList();
            borrowList.setId(rs.getString("id")); 
            borrowList.setAlbumName(rs.getString("albumName"));
            borrowList.setBorrowerName(rs.getString("borrowerName"));
            borrowList.setBorrowDate(rs.getString("borrowDate"));
            borrowList.setDueDate(rs.getString("dueDate"));
           
            
            borrowListDataClass.add(borrowList);                  
        }
        tableviewBorrowListClass.setItems(borrowListDataClass);
    }
    catch(Exception e){
          e.printStackTrace();
          System.out.println("Error on Building Data");            
    }
}
    
    
    public void populateAlbumListview(String artist) throws SQLException{
        
        albumNamesData.removeAll(albumNamesData);
        
        Statement statement;

        statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("select AlbumName from Album where Artist = '" + artist 
                + "' and Status = 'in hand';");
        
        while (rs.next()) {
            albumNamesData.add(rs.getString("AlbumName"));
        }
        
        lstAlbums.setItems(albumNamesData);
        
    }
    
    public void buildData(String sql, ObservableList data, TableView<String> tableview) {
          
        try{
            
            ResultSet rs = connection.createStatement().executeQuery(sql);
            
            tableview.getColumns().clear();
            
            // Add Table Columns
            for(int i = 0 ; i < rs.getMetaData().getColumnCount(); i++){
                
                final int j = i; 
                
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                        return new SimpleStringProperty(param.getValue().get(j).toString());                        
                    }                    
                });
                tableview.getColumns().addAll(col); 
                
            }

            // Add data to observableList
            while(rs.next()){
                
                ObservableList<String> row = FXCollections.observableArrayList();
                
                for(int i = 1 ; i <= rs.getMetaData().getColumnCount(); i++){
                    
                    row.add(rs.getString(i));
                }
                
                data.add(row);

            }

            // add data to the tableview
            tableview.setItems(data);
            
          }catch (Exception ex){
                          
          }
      }

    @FXML
    private void btnConnect_Click(ActionEvent event) throws SQLException {
        
        if("Connect".equals(btnConnect.getText()))
        {
            connectionString = txtConnection.getText();
            username = txtUserName.getText();
            password = txtPassword.getText();

            try{
            connection = DriverManager.getConnection(connectionString , username, password);
            
            btnConnect.setText("Disconnect");
            } 
            catch (Exception ex) {
                System.out.println(ex);
            }

        }
        else
        {
            connection.close();
            btnConnect.setText("Connect");
        }
        
    }
    
    @FXML
    private void btnArtist_Click(ActionEvent event) throws SQLException {
        
        buildArtistDataClass();
        

        }

    @FXML
    private void btnAlbum_Click(ActionEvent event) throws SQLException {
        
        albumData.removeAll(albumData);
        
        buildData("select * from Album", albumData, tableviewAlbum);
}

    @FXML
    private void btnBorrower_Click(ActionEvent event) throws SQLException {
        
        borrowerData.removeAll(borrowerData);
        
        buildData("select * from Borrower", borrowerData, tableviewBorrower);
    }


    @FXML
    private void btnAddNewArtist_Click(ActionEvent event) throws SQLException {
        
        // Create a statement
        Statement statement = connection.createStatement();

        // Execute an insert statement
        statement.execute("INSERT INTO Artist(id, Name, Genre) VALUES (NULL, '" + txtArtistName.getText() 
            + "', '" + txtArtistGenre.getText() + "');");
        
        // Refresh tableview with new data
        artistDataClass.removeAll(artistDataClass);
        buildArtistDataClass();
        
        
    }

    @FXML
    private void btnAddNewAlbum_Click(ActionEvent event) throws SQLException {
        
        // Create a statement
        Statement statement = connection.createStatement();
        
        // Execute an insert statement
        statement.execute("INSERT INTO Album(id, AlbumName, Artist, ReleaseDate, Status) VALUES (NULL, '" +
                txtAlbum.getText() + "', '" + txtAlbumConsoleArtist.getText() + "', '" + txtReleaseDate.getText() + "', DEFAULT);");
        
        artistNamesData.removeAll(artistNamesData);
        
        Statement st;

        st = connection.createStatement();

        ResultSet rs = st.executeQuery("select Name from Artist");
        
        while (rs.next()) {
            artistNamesData.add(rs.getString("Name"));
        }
        
        if (!artistNamesData.contains(txtAlbumConsoleArtist.getText()))
        {
            Statement state = connection.createStatement();
            state.execute("INSERT INTO Artist(id, Name, Genre) VALUES (NULL, '" + txtAlbumConsoleArtist.getText() 
            + "', DEFAULT);");
        }
        
        // Refresh tableview
        albumData.removeAll(albumData);
        String SQL = "select * from Album";
        buildData(SQL, albumData, tableviewAlbum);
        
    }

    @FXML
    private void btnAddNewBorrower_Click(ActionEvent event) throws SQLException {
        
        // Create a statement
        Statement statement = connection.createStatement();
        
        // Execute an insert statement
        statement.execute("INSERT INTO Borrower(id, Name, Address, Phone) VALUES (NULL, '" +
                txtName.getText() + "', '" + txtAddress.getText() + "', '" + txtPhone.getText() + "');");
        
        // Refresh tableview
        borrowerData.removeAll(borrowerData);
        String SQL = "select * from Borrower";
        buildData(SQL, borrowerData, tableviewBorrower);
    }

    @FXML
    private void mnuBorrowList_Clicked(Event event) throws SQLException {
        
        buildBorrowListDataClass();
        
        artistNamesData.removeAll(artistNamesData);
        
        Statement st;

        st = connection.createStatement();

        ResultSet rs = st.executeQuery("select Name from Artist");
        
        while (rs.next()) {
            artistNamesData.add(rs.getString("Name"));
        }
        
        lstArtist.setItems(artistNamesData);
        
        borrowerNameData.removeAll(borrowerNameData);
        
        Statement state;
        
        state = connection.createStatement();
        
        ResultSet results = state.executeQuery("select Name from Borrower");
        
        while (results.next()) {
            borrowerNameData.add(results.getString("Name"));
        }
        
        lstBorrowerName.setItems(borrowerNameData);
    }

    @FXML
    private void btnBorrow_Click(ActionEvent event) throws SQLException {
        
        // Create a statement
        Statement statement = connection.createStatement();

        // Execute an insert statement
        statement.execute("INSERT INTO BorrowList(id, AlbumName, BorrowerName, BorrowDate, DueDate) VALUES" +
                "(NULL, '" + selectedAlbum  + "', '" + selectedBorrower + "', '" + sdf.format(date) +
                "', '" + sdf.format(dueDate) + "');");
        
        
        // Execute an insert statement
        statement.execute("INSERT INTO BorrowListHistory(id, AlbumName, BorrowerName, BorrowDate, DueDate) VALUES" +
                "(NULL, '" + selectedAlbum  + "', '" + selectedBorrower + "', '" + sdf.format(date) +
                "', '" + sdf.format(dueDate) + "');");
        
        
        buildBorrowListDataClass();
        
        // Update Album table 
        Statement state = connection.createStatement();
        
        // Execute an update Statement
        state.execute("update Album set Status = 'Borrowed' where AlbumName = '" + selectedAlbum + "';");
        
        
    }

    @FXML
    private void btnReturn_Click(ActionEvent event) throws SQLException {
        
        // Update Album table 
        Statement statement = connection.createStatement();
        
        // Execute an update Statement
        statement.execute("update Album set Status = 'In Hand' where AlbumName = '" + selectedBorrowListClass.getAlbumName() + "';");
        
        statement.clearBatch();
        
        statement.execute("delete from BorrowList where AlbumName = '" + selectedBorrowListClass.getAlbumName() + "';");
        
        buildBorrowListDataClass();
    }
    
}
