package br.com.mecaniqa.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conexao {
    private Conexao() {
    }

//Tem que adicionar variável nos dados sensíveis, usuário e senha!!!
    public static Connection getConnection() 
    throws SQLException {
        
        String url = "jdbc:mysql://localhost:3306/mecaniqa";
        
        Dotenv dotenv = Dotenv.configure().load();

        String usuario = dotenv.get("DB_USER");
        String senha = dotenv.get("DB_PASSWORD");

        if (url == null || usuario == null || senha == null) {
            throw new SQLException(
                "Configure a URL, o usuário e a senha do banco");
        }

        return DriverManager.getConnection(url, usuario, senha);
    }
}
