package dao;

import models.Room;
import org.jfree.data.jdbc.JDBCPieDataset;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO implements InterfaceDao.RoomDao {

    private String url;
    private String username;
    private String password;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    /**
     * constructeur
     * @param url adresse connexion serveur sql
     * @param username nom de l'utilisateur
     * @param password mot de passe
     */
    public RoomDAO(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }


    /**
     * fonction pour obternir toutes les salles de la BDD
     * @return List <Room></Room>
     */
    @Override
    public List<Room> getAllRoom() {
        List<Room> list = new ArrayList<>();
        try {
            this.connection=DriverManager.getConnection(this.url,this.username,this.password);
            this.preparedStatement = this.connection.prepareStatement("SELECT * from room");
            this.resultSet = this.preparedStatement.executeQuery();

            while(resultSet.next()){
                String name = resultSet.getString("name");
                Long id = resultSet.getLong("id");
                int capacity = resultSet.getInt("capacity");
                Long idSite = resultSet.getLong("id_site");
                list.add(new Room(id,capacity,idSite,name));
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * ajoute une salle a la BDD
     * @param room room a ajouter avec nom et id
     */
    @Override
    public void createRoom(Room room) {
        try {
            this.connection = DriverManager.getConnection(url, username, password);

            if (room.getID() != null) {
                System.out.println("This course already exists.");

            } else {
                this.preparedStatement = this.connection.prepareStatement
                        ("insert into room (name,capacity,id_site) values (?,?,?)");

                this.preparedStatement.setString(1, room.getName());
                this.preparedStatement.setInt(2,room.getCapacity());
                this.preparedStatement.setLong(3,room.getId_site());
                this.preparedStatement.execute();
            }

            System.out.println(room.getName() + " saved into the database");

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("unable to save the product");
        }

    }

    /**
     * affiche la salle selon son nom
     * @param name nom de la salle dans la BDD
     * @return room  selon la string saisie
     */
    @Override
    public Room readRoomByName(String name) {
        try {
            this.connection= DriverManager.getConnection(this.url,this.username,this.password);
            this.preparedStatement= this.connection.prepareStatement
                    ("select * from room where name = ?");
            this.preparedStatement.setString(1,name);
            this.resultSet = this.preparedStatement.executeQuery();

            Room room =new Room();


            while(this.resultSet.next()){

                room.setID(this.resultSet.getLong("id"));
                room.setName(this.resultSet.getString("name"));
                room.setCapacity(this.resultSet.getInt("capacity"));
                room.setId_site(this.resultSet.getLong("id_site"));

            }
            this.resultSet.close();
            this.preparedStatement.close();
            this.connection.close();
            return room;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * affiche la salle selon son id
     * @param id id de la salle que l'on cherche
     * @return room selon l'id
     */
    @Override
    public Room readRoomByID(Long id) {
        try {
            this.connection = DriverManager.getConnection(url, username, password);
            this.preparedStatement = this.connection.prepareStatement
                    ("select * from room where id = ?");
            this.preparedStatement.setLong(1, id);

            this.resultSet = this.preparedStatement.executeQuery();

            Room room = new Room();

            while (this.resultSet.next()) {
                room.setID(this.resultSet.getLong("id"));
                room.setName(this.resultSet.getString("name"));
                room.setCapacity(this.resultSet.getInt("capacity"));
                room.setId_site(this.resultSet.getLong("id_site"));
            }
            this.resultSet.close();
            this.preparedStatement.close();
            this.connection.close();
            return room;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * met a jour une salle dans la BDD
     * @param room room a mettre a jour
     */
    @Override
    public void updateRoom(Room room) {
        try {
            this.connection = DriverManager.getConnection(url, username, password);

            if (room.getID() != null) {
                this.preparedStatement = this.connection.prepareStatement(
                        "update room set name= ? set capacity= ? set id_site=? where id = ? ");
                this.preparedStatement.setString(1, room.getName());
                this.preparedStatement.setInt(2,room.getCapacity());
                this.preparedStatement.setLong(3,room.getId_site());
                this.preparedStatement.setLong(4, room.getID());
                this.preparedStatement.execute();

            } else {
                this.preparedStatement = this.connection.prepareStatement
                        ("insert into room (name,capacity,id_site) values (?,?,?,?)  ;");
                this.preparedStatement.setString(1, room.getName());
                this.preparedStatement.setInt(2,room.getCapacity());
                this.preparedStatement.setLong(3,room.getId_site());
                this.preparedStatement.setLong(4, room.getID());
                this.preparedStatement.execute();
            }
            System.out.println(room.getName() + " saved into the database");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("unable to save the site");
        }

    }

    /**
     * supprime une salle de la BDD
     * @param room salle a supprimer
     */
    @Override
    public void deleteRoom(Room room) {
        try {
            connection = DriverManager.getConnection(url, username, password);

            preparedStatement = connection.prepareStatement("DELETE from room where (name) = ? ");
            preparedStatement.setString(1, room.getName());

            this.preparedStatement.execute();
            System.out.println("La salle a bien été supprimée.");

            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * affiche une camembert de la repartition des salles selon le site
     * @return JDBCPieDataset
     */
    @Override
    public JDBCPieDataset readDataNumberPerSite() {
        JDBCPieDataset data = null;

        try
        {
            this.connection = DriverManager.getConnection(this.url, this.username, this.password);
            data = new JDBCPieDataset(this.connection);
            String sql = "SELECT s.name, Count(id_site) FROM room r INNER JOIN site s WHERE r.id_site = s.id GROUP BY id_site";
            data.executeQuery(sql);
            this.connection.close();
        }
        catch (SQLException e) {
            System.err.print("SQLException: ");
            System.err.println(e.getMessage());
        }
        catch (Exception e) {
            System.err.print("Exception: ");
            System.err.println(e.getMessage());
        }

        return data;
    }
    /**
     * affiche une camembert de la capacite des salles selon le site
     * @return JDBCPieDataset
     */
    @Override
    public JDBCPieDataset readDataCapacityPerSite() {
        JDBCPieDataset data = null;

        try
        {
            this.connection = DriverManager.getConnection(this.url, this.username, this.password);
            data = new JDBCPieDataset(this.connection);
            String sql = "SELECT s.name, SUM(capacity) FROM room r INNER JOIN site s WHERE r.id_site = s.id GROUP BY id_site";
            data.executeQuery(sql);
            this.connection.close();
        }
        catch (SQLException e) {
            System.err.print("SQLException: ");
            System.err.println(e.getMessage());
        }
        catch (Exception e) {
            System.err.print("Exception: ");
            System.err.println(e.getMessage());
        }

        return data;
    }
}
