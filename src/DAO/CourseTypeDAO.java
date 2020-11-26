package DAO;
import InterfaceDao.CourseTypeDao;
import Models.Course;
import Models.CourseType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CourseTypeDAO implements CourseTypeDao {

    private String url;
    private String username;
    private String password;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public CourseTypeDAO(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;

    }


    @Override
    public List<CourseType> getAllCourseType() {
        List<CourseType> list = new ArrayList<>();

        try {
            this.connection=DriverManager.getConnection(this.url,this.username,this.password);
            preparedStatement = connection.prepareStatement("SELECT * from type_course");
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                String name = resultSet.getString("name");
                Long id = resultSet.getLong("id");
                list.add(new CourseType(id, CourseType.Type.valueOf(name)));
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

    @Override
    public void createCoursDao(CourseType courseType) {
        try {
            connection = DriverManager.getConnection(url, username, password);

            if (courseType.getID() != null) {
                System.out.println("This course already exists.");

            } else {
                preparedStatement = connection.prepareStatement
                        ("insert into type_course (name) values (?)");

                preparedStatement.setString(1, courseType.getType());

                preparedStatement.execute();
            }


            System.out.println(courseType.getType() + " saved into the database");

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("unable to save the product");
        }


    }

    @Override
    public CourseType readCourseTypeByName(String name) {
        try {
            this.connection= DriverManager.getConnection(this.url,this.username,this.password);
            this.preparedStatement= this.connection.prepareStatement
                    ("select * from type_course where name = ?");
            this.preparedStatement.setString(1,name);
            this.resultSet = this.preparedStatement.executeQuery();

            CourseType courseType=new CourseType();

            while(this.resultSet.next()){

                courseType.setID(this.resultSet.getLong("id"));
                courseType.setType(this.resultSet.getString("name"));

            }

            this.resultSet.close();
            this.preparedStatement.close();
            this.connection.close();
            return courseType;


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


    }

    @Override
    public CourseType readCourseTypeById(Long Id) {
        try {
            this.connection = DriverManager.getConnection(url, username, password);
            this.preparedStatement = this.connection.prepareStatement
                    ("select * from type_course where id = ?");
            this.preparedStatement.setLong(1, Id);

            this.resultSet = this.preparedStatement.executeQuery();

            CourseType courseType = new CourseType();

            while (this.resultSet.next()) {
                courseType.setID(this.resultSet.getLong("id"));
                courseType.setType(this.resultSet.getString("name"));
            }
            this.resultSet.close();
            this.preparedStatement.close();
            this.connection.close();
            return courseType;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateCourseTypeDao(CourseType courseType) {
        try {
            this.connection = DriverManager.getConnection(url, username, password);

            if (courseType.getID() != null) {
                this.preparedStatement = this.connection.prepareStatement(
                        "update type_course set name= ? where id = ? ");
                this.preparedStatement.setString(1, courseType.getType());
                this.preparedStatement.setLong(2, courseType.getID());
                this.preparedStatement.execute();

            } else {
                this.preparedStatement = this.connection.prepareStatement
                        ("insert into type_course (name) values (?)  ;");

                this.preparedStatement.setString(1, courseType.getType());

                this.preparedStatement.execute();
            }

            System.out.println(courseType.getType() + " saved into the database");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("unable to save the product");
        }

    }

    @Override
    public void deleteCourseTypeDao(CourseType courseType) {
        try {
            connection = DriverManager.getConnection(url, username, password);

            preparedStatement = connection.prepareStatement("DELETE from type_course where (name) = ? ");
            preparedStatement.setString(1, courseType.getType());

            this.preparedStatement.execute();
            System.out.println("La matière a bien été supprimée.");

            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}