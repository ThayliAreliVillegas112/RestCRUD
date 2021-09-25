package mx.edu.utez.controller;

import mx.edu.utez.database.ConecctionMysql;
import mx.edu.utez.model.Employee;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Path("/employee")
public class Service {

    Connection con;
    PreparedStatement pstm;
    ResultSet rs;
    Statement statement;

    /*@GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Employee> getEmployees(){ ***METODO ESTATICO
        List<Employee> employees = new ArrayList<>();

        Employee employee = new Employee();
        employee.setEmployeeNumber(1);
        employee.setFirstName("Luis");
        employees.add(employee);

        employee = new Employee();
        employee.setEmployeeNumber(2);
        employee.setFirstName("Alejandro");
        employees.add(employee);
        return employees;
    }*/

    public List<Employee> findAll(){ //VISUALIZAR A TODOS LOS EMPLEADOS
        List<Employee> listEmployee = new ArrayList<>();
        try{
            con = ConecctionMysql.getConnection();
            String query = "  SELECT employees.employeeNumber, employees.lastName, employees.firstName, employees.extension, employees.email, employees.officeCode, employees.reportsTo, employees.jobTitle FROM employees;";
            statement = con.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()){
                Employee employee = new Employee();
                employee.setEmployeeNumber(rs.getInt("employeeNumber"));
                employee.setLastName(rs.getString("lastName"));
                employee.setFirstName(rs.getString("firstName"));
                employee.setExtension(rs.getString("extension"));
                employee.setEmail(rs.getString("email"));
                employee.setOfficeCode(rs.getInt("officeCode"));
                employee.setReportsTo(rs.getInt("reportsTo"));
                employee.setJobTitle(rs.getString("jobTitle"));
                listEmployee.add(employee);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }finally {
            closeConnection();
        }
        return listEmployee;
    }

    @GET
    @Path("/{id}") //BUSQUEDA POR ID
    @Produces(MediaType.APPLICATION_JSON)
    public Employee getEmployee(@PathParam("id") int employeeNumber) {
        Employee employee = new Employee();
        try {
            con = ConectionMysql.getConnection();
            String query = "SELECT employees.employeeNumber, employees.firstName, employees.lastName, employees.extension, employees.email, employees.officeCode, employees.reportsTo, employees.jobTitle FROM employees WHERE employees.employeeNumber = ?";
            pstm = con.prepareStatement(query);
            pstm.setInt(1, employeeNumber);
            rs = pstm.executeQuery();
            if (rs.next()) {
                employee.setEmployeeNumber(rs.getInt("employeeNumber"));
                employee.setFirstName(rs.getString("firstName"));
                employee.setLastName(rs.getString("lastName"));
                employee.setExtension(rs.getString("extension"));
                employee.setEmail(rs.getString("email"));
                employee.setOfficeCode(rs.getInt("officeCode"));
                employee.setReportsTo(rs.getInt("reportsTo"));
                employee.setJobTitle(rs.getString("jobTitle"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }finally
            closeConnection();
        }
        return employee;
    }

    @POST
    @Path("/{id}/{name}/{lastname}/{extension}/{email}/{code}/{report}/{job}")    // INSERTAR EMPLEADO
    @Produces(MediaType.APPLICATION_JSON)
    public boolean createEmployee(@PathParam("id") int employeeNumber, @PathParam("name") String firstName, @PathParam("lastname") String lastName, @PathParam("extension") String extension, @PathParam("email") String email, @PathParam("code") int code, @PathParam("report") int report, @PathParam("job") String job){
        boolean state = false;
        try{
            con = ConnectionMySQL.getConnection();
            String query = "Insert into employees(employees.employeeNumber, employees.firstName, employees.lastName, employees.extension, employees.email, employees.officeCode, employees.reportsTo, employees.jobTitle) \n" +
                            "values(?, ?, ?, ?, ?, ?, ?, ?);";
            pstm = con.prepareStatement(query);
            pstm.setInt(1, employeeNumber);
            pstm.setString(2, firstName);
            pstm.setString(3, lastName);
            pstm.setString(4, extension);
            pstm.setString(5, email);
            pstm.setInt(6, code);
            pstm.setInt(7, report);
            pstm.setString(8, job);
            state = pstm.executeUpdate() ==1;
        }catch(SQLException ex){
            ex.printStackTrace();
        }finally{
            closeConnection();
        }
        return state;
    }

    @PUT
    @Path("/{id}/{name}/{lastname}/{extension}/{email}/{code}/{report}/{job}") //ACTUALIZAR EMPLEADO
    @Produces(MediaType.APPLICATION_JSON)
    public boolean updateEmployee(@PathParam("id") int employeeNumber, @PathParam("name") String firstName, @PathParam("lastname") String lastName, @PathParam("extension") String extension, @PathParam("email") String email, @PathParam("code") int code, @PathParam("report") int report, @PathParam("job") String job){
        boolean state = false;
        try{
            con = ConnectionMySQL.getConnection();
            String query = "Update employees set employees.firstName = ?, employees.lastName = ?, employees.extension = ?, employees.email = ?, employees.officeCode = ?, employees.reportsTo = ?, employees.jobTitle = ? WHERE employees.employeeNumber = ?;";
            pstm = con.prepareStatement(query);
            pstm.setString(1, firstName);
            pstm.setString(2, lastName);
            pstm.setString(3, extension);
            pstm.setString(4, email);
            pstm.setInt(5, code);
            pstm.setInt(6, report);
            pstm.setString(7, job);
            pstm.setInt(8, employeeNumber);
            state = pstm.executeUpdate() ==1;
        }catch(SQLException ex){
            ex.printStackTrace();
        }finally{
            closeConnection();
        }
        return state;
    }

    @DELETE
    @Path("/{idD}")   //ELIMINAR EMPLEADO
    @Produces(MediaType.APPLICATION_JSON)
    public boolean deleteEmployee(@PathParam("idD") int idD){ 
        boolean state = false;
        try{
            con = ConnectionMySQL.getConnection();
            String queryP = "Delete from employees where employeeNumber = ?;";
            pstm = con.prepareStatement(queryP);
            pstm.setInt(1, idD);
            state = pstm.executeUpdate() == 1;
        }catch(SQLException ex){
            ex.printStackTrace();
        }finally{
            closeConnection();
        }
        return state;
    }
      
    /*@GET   ***BUSQUEDA POR ID ESTATICO
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Employee getEmployee(@PathParam("id") int employeeNumber){
        Employee employee = new Employee();
        if(employeeNumber != 0)
            employee.setFirstName("Alejandro");
        return employee;
    }*/

    public void closeConnection(){  
        try{
            if(con != null){
                con.close();
            }
            if(pstm != null){
                pstm.close();
            }
            if(rs != null){
                rs.close();
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
}
