package dao;

import database.ConnectionDb;

import model.Animal;

import model.Vache;

import model.Poulet;


import java.sql.Connection;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.sql.Statement;

import java.util.ArrayList;

import java.util.List;



public class AnimalDAOImpl implements AnimalDAO {



    @Override

    public void addVache(Vache vache) {

        String animalSql = "INSERT INTO Animals (farm_id, age, healthStatus, type) VALUES (?, ?, ?, ?)";

        String vacheSql  = "INSERT INTO Vaches (animal_id, milkProduction) VALUES (?, ?)";



        try {

            Connection cnx = ConnectionDb.getConnection();

            cnx.setAutoCommit(false);



            PreparedStatement animalStmt = cnx.prepareStatement(animalSql, Statement.RETURN_GENERATED_KEYS);

            animalStmt.setInt(1, vache.getFarmId());

            animalStmt.setInt(2, vache.getAge());

            animalStmt.setString(3, vache.getHealthStatus());

            animalStmt.setString(4, vache.getType());

            animalStmt.executeUpdate();



            ResultSet keys = animalStmt.getGeneratedKeys();

            if (keys.next()) {

                int animalId = keys.getInt(1);



                PreparedStatement vacheStmt = cnx.prepareStatement(vacheSql);

                vacheStmt.setInt(1, animalId);

                vacheStmt.setDouble(2, vache.getMilkProduction());

                vacheStmt.executeUpdate();



                cnx.commit();

                System.out.println("Vache ajoutée avec succès.");

            } else {

                cnx.rollback();

                System.out.println("Echec d'ajout de la vache.");

            }



            ConnectionDb.closecnx(cnx);

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

    }



    @Override

    public void addPoulet(Poulet poulet) {

        String animalSql = "INSERT INTO Animals (farm_id, age, healthStatus, type) VALUES (?, ?, ?, ?)";

        String pouletSql = "INSERT INTO Poulets (animal_id, eggProduction) VALUES (?, ?)";



        try {

            Connection cnx = ConnectionDb.getConnection();

            cnx.setAutoCommit(false);



            PreparedStatement animalStmt = cnx.prepareStatement(animalSql, Statement.RETURN_GENERATED_KEYS);

            animalStmt.setInt(1, poulet.getFarmId());

            animalStmt.setInt(2, poulet.getAge());

            animalStmt.setString(3, poulet.getHealthStatus());

            animalStmt.setString(4, poulet.getType());

            animalStmt.executeUpdate();



            ResultSet keys = animalStmt.getGeneratedKeys();

            if (keys.next()) {

                int animalId = keys.getInt(1);



                PreparedStatement pouletStmt = cnx.prepareStatement(pouletSql);

                pouletStmt.setInt(1, animalId);

                pouletStmt.setInt(2, poulet.getEggProduction());

                pouletStmt.executeUpdate();



                cnx.commit();

                System.out.println("Poulet ajouté avec succès.");

            } else {

                cnx.rollback();

                System.out.println("Echec d'ajout du poulet.");

            }



            ConnectionDb.closecnx(cnx);

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

    }



    @Override

    public List<Animal> getByFarm(int farmId) {

        List<Animal> animals = new ArrayList<>();

        try {

            Connection cnx = ConnectionDb.getConnection();

            PreparedStatement stmt = cnx.prepareStatement(

                    "SELECT * FROM Animals WHERE farm_id = ?"

            );

            stmt.setInt(1, farmId);

            ResultSet rs = stmt.executeQuery();



            while (rs.next()) {

                Animal animal = new Animal(

                        rs.getInt("id"),

                        rs.getInt("farm_id"),

                        rs.getInt("age"),

                        rs.getString("healthStatus"),

                        rs.getString("type")

                );

                animals.add(animal);

            }



            ConnectionDb.closecnx(cnx);

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

        return animals;

    }



    @Override

    public Animal getById(int id) {

        Animal animal = null;

        try {

            Connection cnx = ConnectionDb.getConnection();

            PreparedStatement stmt = cnx.prepareStatement(

                    "SELECT * FROM Animals WHERE id = ?"

            );

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();



            if (rs.next()) {

                animal = new Animal(

                        rs.getInt("id"),

                        rs.getInt("farm_id"),

                        rs.getInt("age"),

                        rs.getString("healthStatus"),

                        rs.getString("type")

                );

            }



            ConnectionDb.closecnx(cnx);

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

        return animal;

    }



    @Override

    public void delete(int id) {

        try {

            Connection cnx = ConnectionDb.getConnection();

            // Supprime l'animal principal, les tables Vaches/Poulets

            // se nettoient via CASCADE en base

            PreparedStatement stmt = cnx.prepareStatement(

                    "DELETE FROM Animals WHERE id = ?"

            );

            stmt.setInt(1, id);



            int rows = stmt.executeUpdate();

            if (rows > 0) System.out.println("Animal supprimé.");

            else          System.out.println("Echec de suppression.");



            ConnectionDb.closecnx(cnx);

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

    }

}