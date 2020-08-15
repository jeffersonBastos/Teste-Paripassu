package dao.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dao.AluguelDAO;
import dao.FilmeDAO;
import entidades.Aluguel;
import entidades.Filme;

public class AluguelDAOImpl implements AluguelDAO{

	@Override
	public void insert(Connection conn, Aluguel aluguel) throws Exception {

        Integer idAluguel = this.getNextId(conn);

        PreparedStatement myStmt = conn.prepareStatement("INSERT INTO en_aluguel(id_aluguel,id_cliente,data_aluguel,valor) VALUES(?,?,?,?)");
              
        myStmt.setInt(1, idAluguel);
        myStmt.setInt(2, aluguel.getCliente().getIdCliente());
        myStmt.setDate(3, new Date(aluguel.getDataAluguel().getTime()));
        myStmt.setFloat(4, aluguel.getValor());

        myStmt.executeUpdate();
        myStmt.close();
        conn.commit();
        
        aluguel.setIdAluguel(idAluguel);
        
        PreparedStatement myRe = conn.prepareStatement("insert into re_aluguel_filme (id_filme, id_aluguel) values (?, ?)");
        List<Filme> idFilme = aluguel.getFilmes();
        myRe.setInt(1, idFilme.get(0).getIdFilme());
        myRe.setInt(2, aluguel.getIdAluguel());
        myRe.executeUpdate();
        myRe.close();
        conn.commit();
        
        System.out.println("Aluguel inserido com sucesso!");
	}

	
	@Override
	public void edit(Connection conn, Aluguel aluguel) throws Exception {
		
		PreparedStatement myStmt = conn.prepareStatement("UPDATE en_aluguel SET data_aluguel = ?, valor = ? WHERE id_aluguel = ?;");
		
		myStmt.setDate(1, new Date(aluguel.getDataAluguel().getTime()));
		myStmt.setFloat(2, aluguel.getValor());
		myStmt.setInt(3, aluguel.getIdAluguel());
		
		myStmt.executeUpdate();
		conn.commit();
		
		System.out.println("Aluguel atualizado!");
		
	}

	@Override
	public void delete(Connection conn, Aluguel aluguel) throws Exception {
		
		PreparedStatement myStmt = conn.prepareStatement("DELETE FROM re_aluguel_filme WHERE id_aluguel=?;DELETE FROM en_aluguel WHERE id_aluguel=?");
		
		myStmt.setInt(1, aluguel.getIdAluguel());
		myStmt.setInt(2, aluguel.getIdAluguel());
		
		myStmt.execute();
		myStmt.close();
		conn.commit();
		
		System.out.println("Aluguel deletado!");

	}
	
    @Override
    public Aluguel find(Connection conn, Integer idAluguel) throws Exception {
        
    	PreparedStatement myStmt = conn.prepareStatement("select * from en_aluguel where id_aluguel = ?");

        myStmt.setInt(1, idAluguel);

        ResultSet myRs = myStmt.executeQuery();
        
        if (!myRs.next()) {
            return null;
        }
        FilmeDAOImpl filmeDAO = new FilmeDAOImpl ();
        
        List<Filme> listaFilmes = new ArrayList<Filme>();
        listaFilmes.addAll(filmeDAO.list(conn));
       

        Integer id_Aluguel = myRs.getInt("id_aluguel");
        Integer id_Cliente = myRs.getInt("id_cliente");
        Date dataAluguel = myRs.getDate("data_aluguel");
        Float valor = myRs.getFloat("valor");
        
		Aluguel aluguel = new Aluguel(id_Aluguel, listaFilmes,
				new ClienteDAOImpl().find(conn, id_Cliente), dataAluguel, valor);
        
		
		return aluguel;
		
    }

	@Override
	public Collection<Aluguel> list(Connection conn) throws Exception {
		
		PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM en_aluguel");
		
		ResultSet rs = myStmt.executeQuery();
		
		FilmeDAO filme = new FilmeDAOImpl ();
		
		
		Collection<Aluguel> listaAlugueis = new ArrayList<>();
		
        while (rs.next()) {
            Integer idAluguel = rs.getInt("id_aluguel");
            Date data = rs.getDate("data_aluguel");
            Float valor = rs.getFloat("valor");
            
            listaAlugueis.add(new Aluguel(idAluguel,(List<Filme>) filme.list(conn),new ClienteDAOImpl().find(conn, rs.getInt("id_cliente")), data, valor));
        }
		return listaAlugueis;
	}
	
	@Override
	public Integer getNextId(Connection conn) throws Exception {

		PreparedStatement myStmt = conn.prepareStatement("select nextval('seq_en_aluguel')");
        ResultSet rs = myStmt.executeQuery();
        rs.next();
        conn.commit();
        return rs.getInt(1);
	}
	
}
