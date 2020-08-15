import dao.AluguelDAO;
import dao.ClienteDAO;
import dao.FilmeDAO;
import dao.jdbc.ClienteDAOImpl;
import dao.jdbc.FilmeDAOImpl;
import dao.jdbc.AluguelDAOImpl;
import entidades.Aluguel;
import entidades.Cliente;
import entidades.Filme;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;




public class Main {

    public static void main(String[] args) {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/001", "postgres", "123");
            conn.setAutoCommit(false);
            System.out.println("SISTEMA SELECAO ESTAGIO PARIPASSU - JEFFERSON BASTOS");
            //Demonstrar o funcionamento aqui
            ClienteDAO clienteDAO = new ClienteDAOImpl();
            FilmeDAO filmeDAO = new FilmeDAOImpl();
            AluguelDAO aluguelDAO = new AluguelDAOImpl();
            
            Boolean funcionamento = true;
			Scanner scanner = new Scanner(System.in);
            
            while(funcionamento == true) {
            	System.out.println("INICIO:");
            	System.out.println("DIGITE:");
            	System.out.println("1- Cadastrar aluguel");
            	System.out.println("2- Excluir aluguel");
            	System.out.println("3- Editar aluguel");
            	System.out.println("4- Procurar aluguel");
            	System.out.println("5- Listar alugueis");
            	System.out.println("6- Cadastrar filme");
            	System.out.println("7- Excluir filme");
            	System.out.println("8- Editar filme");
            	System.out.println("9- Procurar filme");
            	System.out.println("10- Listar filme");
            	System.out.println("0- Sair");
            	int opcao = scanner.nextInt();
            	
            	switch (opcao) {
            	//Cadastro aluguel
            	case 1:
            		try {
	            		System.out.println("Cadastro Aluguel");
	            		System.out.println("Digite o id do cliente: ");
						Integer idCliente = scanner.nextInt();
						Cliente cliente = clienteDAO.find(conn, idCliente);
						
						Date dataAluguel = new Date(System.currentTimeMillis());
						System.out.println("Data do aluguel:");
						System.out.println(dataAluguel);
						
						List<Filme> filmesAluguel = new ArrayList<>();
						try {
							System.out.println("Digite o id de um filme ja cadastrado");
							Integer idFilme = scanner.nextInt();
							Filme filme = filmeDAO.find(conn, idFilme);
							filmesAluguel.add(filme);
							System.out.println("filme encontrado:");
							System.out.println(filme.getNome());
						}catch (Exception e) {
							System.out.println("erro ao procurar filme");
							break;
						}
						System.out.println("Digite o valor total do aluguel: ");
						Float valor = scanner.nextFloat();
	
						Integer nextId = aluguelDAO.getNextId(conn);
	
						Aluguel aluguel = new Aluguel(nextId, filmesAluguel, cliente, dataAluguel, valor);
	
						aluguelDAO.insert(conn, aluguel);
	
											
            		}catch (Exception e) {
            			System.out.println(e);
						System.out.println("Erro ao cadastrar aluguel");
					}
					break;
				// Exclui Aluguel	
				case 2:
					try {
						System.out.println("Excluir aluguel");
						System.out.println("Digite o id do aluguel:");
						Integer idAluguelEX = scanner.nextInt();
						aluguelDAO.delete(conn, aluguelDAO.find(conn, idAluguelEX));
					}catch (Exception e) {
						System.out.println("Numero incorreto");
					}	
					break;
				// Edita aluguel	
				case 3:
					try {
						System.out.println("Edita aluguel");
						System.out.println("Digite o id do aluguel:");
						Integer idAluguelED = scanner.nextInt();
						aluguelDAO.edit(conn, aluguelDAO.find(conn, idAluguelED));
					}catch (Exception e) {
						System.out.println("Numero incorreto");
					}	
					break;
				//Procura Aluguel	
				case 4:
					try {
						System.out.println("Procura aluguel");
						System.out.println("Digite o id do aluguel:");
						Integer idAluguelPR = scanner.nextInt();
						Aluguel aluguel4 = aluguelDAO.find(conn, idAluguelPR);
						System.out.println("valor: " + aluguel4.getValor());
						System.out.println("Cliente: " + aluguel4.getCliente().getNome());
						System.out.println("Data: " + aluguel4.getDataAluguel());
						System.out.println("Filmes: ");
						int i = 1;
						for(Filme filme : aluguel4.getFilmes() ) {
							System.out.println(i +"- " + filme.getNome());
							i++;
						}
					} catch (Exception e) {
						System.out.println("Numero incorreto");
					}
					
					break;
				//lista aluguel
            	case 5:
					try {
						System.out.println("Lista de alugueis:");
						for (Aluguel A : aluguelDAO.list(conn) ) {
							System.out.println("ID: " + A.getIdAluguel());
							System.out.println("Data: " + A.getDataAluguel());
							System.out.println("-----------------------------");
						}

					}catch (Exception e) {
						System.out.println("Erro ao listar alugueis");
					}
					break;
				//Cadastro filme	
            	case 6:
            		try {
	            		System.out.println("Cadastro filme");
						Integer idFilme = filmeDAO.getNextId(conn);
	
						System.out.println("Digite a data- DD/MM/YYYY:");
						String dataFilmeString = scanner.next();
						
						SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
						Date dataFilme = formato.parse(dataFilmeString);
	
						System.out.println("Digite o nome do filme:");
						scanner.nextLine();
						String nomeFilme = scanner.nextLine();
	
						System.out.println("Digite a descricao:");
						
						String descricaoFilme = scanner.nextLine();
								
						Filme novoFilme = new Filme(idFilme, dataFilme, nomeFilme, descricaoFilme);
						filmeDAO.insert(conn, novoFilme);
            		}catch (Exception e) {
            			System.out.println(e);
						System.out.println("erro ao cadastrar filme");
					}	
            		break;
				// Exclui filme	
				case 7:
					try {
						System.out.println("Excluir filme");
						System.out.println("Digite o id do filme:");
						filmeDAO.delete(conn, scanner.nextInt());
						System.out.println("Fime Excluido");
					} catch (Exception e) {
						System.out.println("erro ao excluir filme");
					}
					
					break;
				// Edita filme	
				case 8:
					try {
						System.out.println("Editar filme");
						System.out.println("Digite o id do filme:");
						Integer idFilme = scanner.nextInt();
						filmeDAO.edit(conn, filmeDAO.find(conn, idFilme));
						
					} catch (Exception e) {
						System.out.println("Erro ao editar filme");
					}
					
					break;
				//Procura filme	
				case 9:
					try {
						System.out.println("Procurar filme");
						System.out.println("Digite o id do filme:");
						Integer idFilme = scanner.nextInt();
						Filme filme9 = filmeDAO.find(conn, idFilme);
						
						System.out.println("Id " + filme9.getIdFilme());
						System.out.println("Nome: " + filme9.getNome());
						System.out.println("Descrição: " + filme9.getDescricao());
						System.out.println("Data de lançamento: " + filme9.getDataLancamento());
					}catch (Exception e) {
						System.out.println("Erro ao procurar filme");
					}	
					break;
				//lista filme
				case 10:
					try {
						System.out.println("Lista de filmes:");

						for (Filme F : filmeDAO.list(conn) ) {
							System.out.println("ID: " + F.getIdFilme());
							System.out.println("Data: " + F.getDataLancamento());
							System.out.println("Data: " + F.getNome());
							System.out.println("Data: " + F.getDescricao());
							System.out.println("-----------------------------");
						}
					} catch (Exception e) {
						System.out.println("Erro ao listar filmes");
					}
					break;
				//Sair	
				case 0:
					funcionamento = false;
					scanner.close();
				default:
					System.out.println("Numero invalido");
					break;
				}
            	
            	
            
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Fim do teste.");
    }
}