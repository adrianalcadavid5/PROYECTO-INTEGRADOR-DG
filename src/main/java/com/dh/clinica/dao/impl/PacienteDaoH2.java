package com.dh.clinica.dao.impl;

import com.dh.clinica.dao.IDao;
import com.dh.clinica.db.H2Connection;
import com.dh.clinica.model.Domicilio;
import com.dh.clinica.model.Paciente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PacienteDaoH2 implements IDao<Paciente> {
    public static final Logger logger = LoggerFactory.getLogger(PacienteDaoH2.class);;

    public static final String INSERT = "INSERT INTO PACIENTES VALUES (DEFAULT,?,?,?,?,?)";

    public static final String SELECT_ID = "SELECT * FROM PACIENTES WHERE ID = ?";
    public static final String SELECT_ALL ="SELECT * FROM PACIENTES";
    public static final String UPDATE = "UPDATE PACIENTES SET APELLIDO=?, NOMBRE=?," +
            "DNI=?, FECHA_INGRESO=?, ID_DOMICILIO=? WHERE ID=?";
    public static final String DELETE = "DELETE FROM PACIENTES WHERE ID =? ";

    public DomicilioDaoH2 domicilioDaoH2 = new DomicilioDaoH2();

    @Override
    public Paciente guardar(Paciente paciente) {
        Connection connection = null;
        Paciente pacienteARetornar = null;
        //Necesito traer la direccion porque un paciente tiene una direccion y no puedo hacer una inserccion sin el domicilio
        //si o si lo necesito por que de lo contrario me faltaria el id del domicilio, antes de guardar el paciente debo de guardar el domicilio y obtener el id
        Domicilio domicilioAuxiliar = domicilioDaoH2.guardar(paciente.getDomicilio());

        try {
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, paciente.getApellido());
            preparedStatement.setString(2, paciente.getNombre());
            preparedStatement.setString(3, paciente.getDni());
            preparedStatement.setDate(4, Date.valueOf(paciente.getFechaIngreso()));
            preparedStatement.setInt(5, domicilioAuxiliar.getId());
            preparedStatement.executeUpdate();
            connection.commit();

            //traer la clave primaria
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            //se pone if y no while por que viene 1 sola fila, al buscar por id
            if (resultSet.next()) {
                Integer id = resultSet.getInt(1);
                pacienteARetornar = new Paciente(id, paciente.getApellido(), paciente.getNombre(), paciente.getDni(), paciente.getFechaIngreso(), domicilioAuxiliar);

            }
            logger.info("paciente persistido " + pacienteARetornar);

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.error(ex.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                    logger.error(ex.getMessage());
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return pacienteARetornar;
    }

    @Override
    public Paciente buscarPorId(Integer id) {
        Connection connection = null;
        Paciente pacienteEncontrado = null;

        try {
            connection = H2Connection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                Integer idDB = resultSet.getInt(1);
                String apellido = resultSet.getString(2);
                String nombre = resultSet.getString(3);
                String dni = resultSet.getString(4);
                LocalDate fecha = resultSet.getDate(5).toLocalDate();
                Integer id_domicilio = resultSet.getInt(6);// este campo de id domicilio lo necesitamos por que
                //esta vinculado paciente con un domicilio con una forenkey, por eso creamos primero domicilio y luego paciente
            // con esta infomacion genero el objeto domicilio
                Domicilio domicilio = domicilioDaoH2.buscarPorId(id_domicilio);
                //con todos los datos armo mi paciente
                pacienteEncontrado = new Paciente(idDB,apellido,nombre,dni,fecha,domicilio);

            }
            if (pacienteEncontrado !=null){
                logger.info("paciente encontrado "+ pacienteEncontrado);
            }else logger.info("el paciente no fue encontrado ");



        } catch (Exception e) {    //catch corto, va hasta antes del return
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return pacienteEncontrado;
    }

    @Override
    public List<Paciente> buscarTodos() {
        Connection connection = null;
        List<Paciente> pacientes = new ArrayList<>();
        Paciente pacienteDesdeDB = null;
        try{
            connection = H2Connection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL);
            while (resultSet.next()){
                Integer idDB = resultSet.getInt(1);
                String apellido = resultSet.getString(2);
                String nombre = resultSet.getString(3);
                String dni = resultSet.getString(4);
                LocalDate fechaIngreso = resultSet.getDate(5).toLocalDate();
                Integer id_domicilio = resultSet.getInt(6);
                Domicilio domicilio = domicilioDaoH2.buscarPorId(id_domicilio);
                pacienteDesdeDB = new Paciente(idDB, apellido, nombre, dni, fechaIngreso, domicilio);
                logger.info("paciente" + pacienteDesdeDB);
                pacientes.add(pacienteDesdeDB);
            }

        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return pacientes;

    }


    @Override
    public void modificar(Paciente paciente) {
        Connection connection = null;
        domicilioDaoH2.modificar(paciente.getDomicilio());
        try{
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, paciente.getApellido());
            preparedStatement.setString(2, paciente.getNombre());
            preparedStatement.setString(3, paciente.getDni());
            preparedStatement.setDate(4, Date.valueOf(paciente.getFechaIngreso()));
            preparedStatement.setInt(5, paciente.getDomicilio().getId());
            preparedStatement.setInt(6, paciente.getId());
            preparedStatement.executeUpdate();
            connection.commit();

            logger.info("paciente modificado "+  paciente);

        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.error(ex.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                    logger.error(ex.getMessage());
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void eliminar(Integer id) {
        Connection connection = null;
        Paciente paciente = buscarPorId(id);
        try{
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);
            if (paciente != null) {
                domicilioDaoH2.eliminar(paciente.getDomicilio().getId());
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
                connection.commit();
            }
            logger.info("paciente con id "+id+ " eliminado " );

        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.error(ex.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                    logger.error(ex.getMessage());
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
